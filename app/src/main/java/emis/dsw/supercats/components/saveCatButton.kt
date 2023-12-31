package emis.dsw.supercats.components

import android.Manifest
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SaveCat(
    createFolder: (String) -> Unit,
    saveCatImage: () -> Unit,
) {

    val context = LocalContext.current;
    val writePermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    if (writePermissionState.status.isGranted) {
        Button(onClick = {
            createFolder("SuperCats")
            saveCatImage()
            Toast.makeText(context, "Kot został zapisany!", Toast.LENGTH_SHORT).show()
        }) {
            Text("Zapisz Kota")
        }
    } else {
        Button(onClick = { writePermissionState.launchPermissionRequest() }) {
            Text("Zapisz Kota")
        }
    }
}