package emis.dsw.supercats.viewmodel

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

data class AppUiState(
    val cats: MutableList<CatObj> = mutableListOf(),
    val savedCats: List<File> = emptyList()
)


class AppViewModel(private val weatherRepo: CatApiRepo = CatApiRepo()) :
    ViewModel() {


    // Expose screen UI state
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun getRandomCats() {
        viewModelScope.launch {
            val response = weatherRepo.getRndCatsCall()
            val jsonObject = JSONArray(response.bodyAsText());
//            println(response.bodyAsText())
            val cots: MutableList<CatObj> = mutableListOf()

            for (i in 0 until jsonObject.length()) {
                cots.add(
                    CatObj(
                        name = jsonObject.getJSONObject(i).getString("id"),
                        url = jsonObject.getJSONObject(i).getString("url")
                    )
                )
            }

            _uiState.update { currentState: AppUiState ->
                currentState.copy(
                    cats = cots,
                )
            }
        }
    }

    fun loadSavedCats() {
        viewModelScope.launch {
            val picturesDirectory =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "SuperCats")

            if (picturesDirectory.exists() && picturesDirectory.isDirectory) {
                _uiState.update { currentState: AppUiState ->
                    currentState.copy(
                        savedCats = picturesDirectory.listFiles()?.toList()?.reversed() ?: emptyList<File>(),
                    )
                }
            }
        }
    }

    fun saveCatImage(fileUrl: String) {
        GlobalScope.launch { downloadFile(fileUrl = fileUrl) }
    }

    private fun downloadFile(fileUrl: String): Boolean {
        return try {
            // Utwórz URL i połącz się z serwerem
            val url = URL(fileUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            // Pobierz InputStream
            val inputStream: InputStream = connection.inputStream

            // Sprawdź, czy folder istnieje, a jeśli nie, utwórz go
            val picturesDirectory =
                File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "SuperCats"
                )
            if (!picturesDirectory.exists()) {
                picturesDirectory.mkdir()
            }

            // Utwórz plik docelowy
            val fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1)
            val outputFile = File(picturesDirectory, fileName)

            // Zapisz plik
            val outputStream = FileOutputStream(outputFile)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            // Zamknij strumienie
            inputStream.close()
            outputStream.close()

            loadSavedCats()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}