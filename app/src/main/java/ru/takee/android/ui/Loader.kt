package ru.takee.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.takee.android.R
import ru.takee.android.ui.theme.Accent_20

@Preview(widthDp = 400, heightDp = 850, showBackground = true, backgroundColor = 0xFFFFFFFF )
@Composable
fun LoaderPreview(){
    Loader()
}

@Composable
fun Loader() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Accent_20)
        .clickable {  }
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.2f)
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                .align(Alignment.Center)
        ) {
            Text(
                text = stringResource(id = R.string.wait_wile_analysing),
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.Center),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}