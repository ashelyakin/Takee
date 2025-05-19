package ru.takee.android.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import ru.takee.android.ui.pet.PetPhotoPlaceholder

@Composable
fun PhotoFrame(imgPath: String?, pickPhotoCallback: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(0.94f)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                pickPhotoCallback()
            },
        shape = RoundedCornerShape(32.dp)
    ) {
        if (imgPath != null){
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                model = imgPath,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        } else{
            PetPhotoPlaceholder()
        }
    }
}