package ru.takee.android.ui.pet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.takee.android.R
import ru.takee.android.ui.theme.Accent

@Preview(widthDp = 400, heightDp = 850, showBackground = true, backgroundColor = 0xFFFFFFFF )
@Composable
fun PetPhotoPlaceholderPreview(){
    PetPhotoPlaceholder()
}

@Composable
fun PetPhotoPlaceholder() {
    val stroke = Stroke(width = 12f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 15f), 0f)
    )
    ConstraintLayout(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .drawBehind {
                drawRoundRect(
                    color = Accent,
                    style = stroke,
                    cornerRadius = CornerRadius(32.dp.toPx())
                )
            }
    ){
        val (text, image) = createRefs()
        val topGuideline = createGuidelineFromTop(0.2f)
        val bottomGuideline = createGuidelineFromBottom(0.16f)
        Image(
            modifier = Modifier
                .fillMaxWidth(0.32f)
                .aspectRatio(0.89f)
                .constrainAs(image){
                    top.linkTo(topGuideline)
                    start.linkTo(parent.start, margin = 12.dp)
                    end.linkTo(parent.end)
                },
            painter = painterResource(id = R.drawable.ic_pet_pick_hoto),
            contentDescription = null
        )
        Text(
            modifier = Modifier.constrainAs(text){
                bottom.linkTo(bottomGuideline)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            text = stringResource(id = R.string.pick_from_gallery)
        )
    }
}