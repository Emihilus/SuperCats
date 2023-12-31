package emis.dsw.supercats.screens

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider.getUriForFile
import coil.compose.AsyncImage
import coil.request.ImageRequest
import emis.dsw.supercats.R
import emis.dsw.supercats.viewmodel.AppUiState
import emis.dsw.supercats.viewmodel.AppViewModel
import java.io.File


@Composable
fun SavedCatsScreen(
    modifier: Modifier = Modifier,
    appUiState: AppUiState,
    viewModel: AppViewModel,
) {

    val context = LocalContext.current;

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        LazyColumn() {
            items(appUiState.savedCats.size) { index ->
//                var color = if (currentIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier.clickable(
//                    onClick = { setLocation(index) }
//                )
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .background(Color.Black),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = appUiState.savedCats[index].name)
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(appUiState.savedCats[index].absolutePath)
                                .build(),
                            contentDescription = "icon",
                            contentScale = ContentScale.Inside,
                        )
                        Row()
                        {
                            Button(
                                onClick = {


                                    val file = getFilePathInPictures(appUiState.savedCats[index].name)
                                    val contentUri: Uri = getUriForFile(
                                        context,
                                        "${context.packageName}.provider",
                                        file
                                    )
                                    val sendIntent: Intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(
                                            Intent.EXTRA_STREAM,
                                            contentUri
                                        )
                                        type = "image/*"
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    }

                                    val shareIntent = Intent.createChooser(sendIntent, null)
                                    context.startActivity(shareIntent)
                                },
                                modifier = modifier.padding(6.dp)
                            ) {
//                                Text(
//                                    text = "Udostępnij",
//                                )
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
//                                    tint = Color.Cyan
                                )
                            }
                            Button(
                                onClick = {
                                    val file = getFilePathInPictures(appUiState.savedCats[index].name)

                                    if (file.exists()) {
                                        val deleted = file.delete()

                                        if (deleted) {
                                            // Plik został pomyślnie usunięty
                                            Toast.makeText(context, "Kot został usunięty", Toast.LENGTH_SHORT).show()
                                            viewModel.loadSavedCats()
                                        } else {
                                            // Błąd podczas usuwania pliku
                                            Toast.makeText(context, "Błąd podczas usuwania kota", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        // Plik nie istnieje
                                        Toast.makeText(context, "Kot nie istnieje", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = modifier.padding(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
//                                    tint = Color.Cyan
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}

fun getFilePathInPictures(fileName: String): File {
    val picturesDirectory =
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "SuperCats")

    return File(picturesDirectory, fileName)
}