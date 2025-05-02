package com.ndhu.moneymanagementapp.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.R
import com.ndhu.moneymanagementapp.components.SearchBox

// to store repeatable/repetitive/common composables function design

// Fonts
val syncopateFont = FontFamily(
    Font(R.font.syncopate_regular, FontWeight.Normal),
    Font(R.font.syncopate_bold, FontWeight.Bold)
)
val kronaOneFont = FontFamily(
    Font(R.font.krona_one_regular, FontWeight.Normal)
)
val sensationLightFont = FontFamily(
    Font(R.font.sensation_light, FontWeight.Light)
)

val surannaFont = FontFamily(
    Font(R.font.suranna_regular, FontWeight.Normal),
)

val alexandriaFont = FontFamily(
    Font(R.font.alexandria_semibold, FontWeight.SemiBold)
)

// Colors
val green = Color(0xFF009669)
val darkGrey = Color(0xFF9D9D9D)

// Background Image used in most pages
@Composable
fun BackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.background_dark),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        contentScale = ContentScale.Crop
    )
}

// "WE SPLIT" Title Text
@Composable
fun TitleText(){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "WE",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontFamily = kronaOneFont,
            fontSize = 12.sp,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "SPLIT",
            color = Color(0xFF009669), // Green color
            style = MaterialTheme.typography.titleLarge,
            fontFamily = kronaOneFont,
            fontSize = 12.sp,
        )
    }
}
/*
@Composable
fun ContentTopBar(navController: NavController, topBarText: String){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.width(10.dp))

        Column (
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Text(topBarText, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            SearchBox()
        }

    }
}
 */


@Composable
fun ContentTopBar(navController: NavController, topBarText: String) {
    /*
    Content Top Bar
    */
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(55.dp))

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
            }
        }

        Spacer(modifier = Modifier.width(5.dp))

        Column(
            modifier = Modifier.weight(10f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = topBarText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = surannaFont
            )
            SearchBox()
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.width(40.dp))
        }
    }
}

/*
@Composable
fun ContentCard(){
    // White Box
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(0.8f)
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(Color(0xFFF0F0F0))
            .padding(start = 24.dp, top = 48.dp, bottom = 48.dp, end = 24.dp)
    ) {

    }

}


 */