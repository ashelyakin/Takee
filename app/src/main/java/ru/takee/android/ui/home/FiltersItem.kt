package ru.takee.android.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.takee.android.R
import ru.takee.android.ui.theme.Accent
import ru.takee.android.ui.theme.Grey

@Preview
@Composable
fun FiltersItemPreview(){
    FiltersItem(Triple(1, "Фильтр", R.drawable.ic_filter_all), false){}
}

@Preview
@Composable
fun FiltersItemPreview1(){
    FiltersItem(Triple(1, "Фильтр", R.drawable.ic_filter_all), true){}
}

@Composable
fun FiltersItem(model: Triple<Int, String, Int>, isSelected: Boolean, onClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .height(52.dp)
            .wrapContentWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ){
                onClick(model.first)
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Accent else Color.White
        ),
        border = if (isSelected) null else BorderStroke(1.dp, Grey),
        shape = RoundedCornerShape(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Image(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxHeight(0.6f)
                    .align(Alignment.CenterVertically),
                painter = painterResource(id = model.third),
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .padding(top = 14.dp, bottom = 14.dp, end = 24.dp)
                    .align(Alignment.CenterVertically),
                text = model.second,
                color = if (isSelected) Color.White else Grey
            )
        }

    }
}