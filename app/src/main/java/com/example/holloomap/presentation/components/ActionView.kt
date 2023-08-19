package com.example.holloomap.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.holloomap.util.UiText
import com.example.holloomap.R

@Composable
fun ActionView(
    title: UiText,
    saveDestination: () -> Unit,
    getAllDes: () -> Unit,
    shouldShowAllDes:Boolean,
    isDestinationAdded: Boolean
) {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        if(shouldShowAllDes){
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {

                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = getAllDes,

                    ) {
                    Text(text = "Show All Des")
                }
            }
        }

        if (isDestinationAdded) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {

                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = saveDestination,

                    ) {
                    Text(text = context.getString(R.string.save))
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(100.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .padding(16.dp)
                .background(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colors.background
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                text = title.asString(context = context),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}