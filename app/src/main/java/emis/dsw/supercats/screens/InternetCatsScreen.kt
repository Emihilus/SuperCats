package emis.dsw.supercats.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import emis.dsw.supercats.viewmodel.AppViewModel
import emis.dsw.supercats.viewmodel.CatObj
import emis.dsw.supercats.components.SaveCat

@Composable
fun InternetCatsScreen(
    modifier: Modifier = Modifier,
    cats: MutableList<CatObj>,
    getRndCats: () -> Unit,
    createFolder: (String) -> Unit,
    saveCatImage: (String) -> Unit,
    viewModel: AppViewModel,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    )
    {
//        Button(onClick = {
//            println("Locations ");
//            getRndCats()
//        }) {
//            Text(
//                text = "Losuj KOTY",
//            )
//        }

        LazyColumn() {
            items(cats.size) { index ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(cats[index].url)
                            .size(Size.ORIGINAL) // Set the target size to load the image at.
                            .build()
                    )
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .background(Color.Black),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = "img",
                            modifier = Modifier.height(200.dp)
                        )
                    }
                    SaveCat(
                        createFolder = createFolder,
                        saveCatImage = { ->
                            saveCatImage(cats[index].url)}
                    )
                }

            }
        }
    }
}