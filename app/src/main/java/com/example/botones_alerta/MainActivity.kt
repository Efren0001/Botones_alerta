package com.example.botones_alerta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.botones_alerta.ui.theme.Botones_alertaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Botones_alertaTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var message by remember { mutableStateOf("Presiona un botón para una acción") }
    var dialogState by remember { mutableStateOf<DialogType?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            ButtonWithoutIcon("Botón 1: Confirmación", Color(0xFF4CAF50)) {
                dialogState = DialogType.Confirmation
            }
            ButtonWithoutIcon("Botón 2: Eliminación", Color(0xFFF44336)) {
                dialogState = DialogType.Deletion
            }
            ButtonWithoutIcon("Botón 3: Información", Color(0xFF2196F3)) {
                dialogState = DialogType.Information
            }
            ButtonWithoutIcon("Botón 4: Autenticación", Color(0xFFFF9800)) {
                dialogState = DialogType.Authentication
            }
            ButtonWithoutIcon("Botón 5: Error Crítico", Color(0xFF9C27B0)) {
                dialogState = DialogType.CriticalError
            }
        }
    }

    dialogState?.let {
        AlertDialogComponent(
            dialogType = it,
            onDismiss = { dialogState = null },
            onAction = { actionMessage -> message = actionMessage }
        )
    }
}

@Composable
fun ButtonWithoutIcon(
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun AlertDialogComponent(
    dialogType: DialogType,
    onDismiss: () -> Unit,
    onAction: (String) -> Unit
) {
    val (title, message, positiveButton, negativeButton, onPositiveAction) = when (dialogType) {
        DialogType.Confirmation -> DialogData(
            title = "Confirmación de Acción",
            message = "¿Estás seguro de que deseas continuar con esta acción?",
            positiveButton = "Sí",
            negativeButton = "No",
            onPositiveAction = { onAction("Acción Confirmada") }
        )
        DialogType.Deletion -> DialogData(
            title = "Eliminar Elemento",
            message = "Esta acción es irreversible. ¿Deseas eliminar este elemento?",
            positiveButton = "Eliminar",
            negativeButton = "Cancelar",
            onPositiveAction = { onAction("Elemento Eliminado") }
        )
        DialogType.Information -> DialogData(
            title = "Aviso Importante",
            message = "Recuerda que los cambios realizados no se pueden deshacer.",
            positiveButton = "Entendido",
            negativeButton = null,
            onPositiveAction = { onDismiss() }
        )
        DialogType.Authentication -> DialogData(
            title = "Requiere Autenticación",
            message = "Para continuar, necesitas autenticarte de nuevo.",
            positiveButton = "Autenticar",
            negativeButton = "Cancelar",
            onPositiveAction = { onAction("Usuario Autenticado") }
        )
        DialogType.CriticalError -> DialogData(
            title = "Error Crítico",
            message = "Se ha producido un error crítico. ¿Deseas intentar nuevamente?",
            positiveButton = "Reintentar",
            negativeButton = "Cancelar",
            onPositiveAction = { onAction("Intento de Reintento") }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onPositiveAction()
                onDismiss()
            }) { Text(positiveButton) }
        },
        dismissButton = {
            negativeButton?.let {
                TextButton(onClick = onDismiss) { Text(it) }
            }
        },
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = { Text(message) },
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.background
    )
}

data class DialogData(
    val title: String,
    val message: String,
    val positiveButton: String,
    val negativeButton: String?,
    val onPositiveAction: () -> Unit
)

enum class DialogType {
    Confirmation,
    Deletion,
    Information,
    Authentication,
    CriticalError
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    Botones_alertaTheme {
        MainScreen()
    }
}
