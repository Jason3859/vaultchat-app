package dev.jason.app.compose.vaultchat.core.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    LoadingIndicator(
        modifier = Modifier
            .size(200.dp)
            .then(modifier)
    )
}