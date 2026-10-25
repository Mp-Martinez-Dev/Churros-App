import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.churros_app.ui.components.BordeGlassGradient

@Composable
fun DialogoGlassContainer(
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(28.dp)

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(Color(0xFF121620)) // Fondo oscuro Glasscorp
                .border(
                    width = 1.5.dp,
                    brush = BordeGlassGradient, // Tu degradado RGB neón
                    shape = forma
                ),
            color = Color(0xFF121620),
            shape = forma,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                content = content
            )
        }
    }
}