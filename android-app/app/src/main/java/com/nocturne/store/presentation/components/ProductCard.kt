package com.nocturne.store.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nocturne.store.core.util.CurrencyFormatter
import com.nocturne.store.data.remote.model.Product

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Thumbnail
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                AsyncImage(
                    model               = product.thumbnail,
                    contentDescription  = product.title,
                    contentScale        = ContentScale.Crop,
                    modifier            = Modifier.fillMaxSize()
                )
                // Discount badge
                if (product.discountPercentage >= 1.0) {
                    Surface(
                        modifier  = Modifier.padding(8.dp).align(Alignment.TopStart),
                        shape     = RoundedCornerShape(6.dp),
                        color     = MaterialTheme.colorScheme.primary,
                        tonalElevation = 0.dp
                    ) {
                        Text(
                            text  = "-${product.discountPercentage.toInt()}%",
                            color = Color.White,
                            fontSize   = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier   = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Info
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text       = product.title,
                    style      = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines   = 2,
                    overflow   = TextOverflow.Ellipsis,
                    color      = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Rating row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector        = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint               = Color(0xFFF59E0B),
                        modifier           = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text  = "%.1f".format(product.rating),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Price row
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text       = CurrencyFormatter.format(product.discountedPrice),
                        style      = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color      = MaterialTheme.colorScheme.primary
                    )
                    if (product.discountPercentage >= 1.0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text           = CurrencyFormatter.format(product.price),
                            style          = MaterialTheme.typography.labelSmall,
                            color          = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }
        }
    }
}
