package com.nocturne.store.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nocturne.store.data.remote.model.CategoryItem

@Composable
fun CategoryChips(
    categories: List<CategoryItem>,
    selected: String?,
    onSelect: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier            = modifier,
        contentPadding      = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected  = selected == null,
                onClick   = { onSelect(null) },
                label     = { Text("All", fontWeight = FontWeight.Medium) },
                shape     = RoundedCornerShape(50)
            )
        }
        items(categories) { cat ->
            FilterChip(
                selected  = selected == cat.slug,
                onClick   = { onSelect(cat.slug) },
                label     = { Text(cat.name, fontWeight = FontWeight.Medium) },
                shape     = RoundedCornerShape(50)
            )
        }
    }
}
