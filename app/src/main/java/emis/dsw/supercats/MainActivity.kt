package emis.dsw.supercats

import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import emis.dsw.supercats.theme.JetpackWeatherTheme
import emis.dsw.supercats.viewmodel.AppViewModel
import java.io.File

class MainActivity : ComponentActivity() {
    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel.getRandomCats()
        appViewModel.loadSavedCats()
        setContent {
            JetpackWeatherTheme {
                val uiState by appViewModel.uiState.collectAsStateWithLifecycle()
                App(
                    viewModel = appViewModel,
                    appUiState = uiState,
                    getRndCats = {
                        appViewModel.getRandomCats()
                    },
                    saveCatImage = { fileUrl: String ->
                        appViewModel.saveCatImage(fileUrl)
                    },
                    createFolder = { folderName: String ->
                        val externalStorageState = Environment.getExternalStorageState()

                        if (Environment.MEDIA_MOUNTED == externalStorageState) {
                            val picturesDirectory =
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            val folder = File(picturesDirectory, folderName)

                            if (!folder.exists()) {
                                folder.mkdir()
                            }
                        }
                    }
                )
            }
        }
    }

}