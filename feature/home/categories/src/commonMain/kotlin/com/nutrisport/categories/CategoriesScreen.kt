package com.nutrisport.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nutrisport.categories.component.CategoryCard
import com.nutrisport.shared.Surface
import com.nutrisport.shared.domain.ProductCategory

@Composable
fun CategoriesScreen(
    onClick: (ProductCategory) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = Surface)
            .padding(all = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProductCategory.entries.forEach { productCategory ->
            CategoryCard(
                productCategory = productCategory,
                onClick = {
                        onClick(productCategory)
                }
            )
        }
    }
}