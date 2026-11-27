package com.nocturne.store.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PaginationBar(
    currentPage: Int,
    totalPages: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (totalPages <= 1) return
    Row(
        modifier            = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment   = Alignment.CenterVertically
    ) {
        OutlinedIconButton(
            onClick  = onPrev,
            enabled  = currentPage > 0,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Previous",
                modifier           = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        val start = maxOf(0, currentPage - 2)
        val end   = minOf(totalPages - 1, currentPage + 2)
        for (page in start..end) {
            val isActive = page == currentPage
            if (isActive) {
                Button(
                    onClick  = {},
                    modifier = Modifier.padding(horizontal = 2.dp).size(36.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text       = "${page + 1}",
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                OutlinedButton(
                    onClick  = {},
                    modifier = Modifier.padding(horizontal = 2.dp).size(36.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("${page + 1}")
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedIconButton(
            onClick  = onNext,
            enabled  = currentPage < totalPages - 1,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector        = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next",
                modifier           = Modifier.size(16.dp)
            )
        }
    }
}
