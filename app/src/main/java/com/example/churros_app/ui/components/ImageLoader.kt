import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

@Composable
fun ImagenGifGlass(
    gifResId: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Configuramos el decodificador de GIF según la versión de Android
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    val painter = rememberAsyncImagePainter(
        model = gifResId,
        imageLoader = imageLoader
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
    )
}