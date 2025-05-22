package com.ndhu.moneymanagementapp.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.ndhu.moneymanagementapp.components.SearchBoxDefault
import com.ndhu.moneymanagementapp.models.ProfilePicture
import com.ndhu.moneymanagementapp.models.Store

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
val dangerRed = Color(0xFF961400)
val primaryGreen = Color(0xFF009669) // Main brand color
val darkGreyContainer = Color(0xFF9D9D9D) // mostly used for dark grey container

// May be used in the future (need to refactor)
// val softWhite = Color(0xFFF9F9F9), // old dialog container color
// val whiteSmoke = Color(0xFFF5F5F5) // new dialog container color
// val cancelGray = Color(0xFF666666) // Cancel outlinedButtonColors in CustomFormDialog

val correctTransparentGreen = Color(0xAA4CAF50) // green transparent for selected user card
val overlayColor = Color(0x33009669) // 20% transparent green (Hex ARGB: 33 = ~20% opacity)

// for profile picture
val profilePictures = listOf(
    ProfilePicture(1, R.drawable.defaultavatarimage),
    ProfilePicture(2, R.drawable.black_panther),
    ProfilePicture(3, R.drawable.bull),

    ProfilePicture(4, R.drawable.racoon),
    ProfilePicture(5, R.drawable.cat),
    ProfilePicture(6, R.drawable.gato_mafia),

    ProfilePicture(7, R.drawable.fox),
    ProfilePicture(8, R.drawable.crocodile),
    ProfilePicture(9, R.drawable.dog),

    ProfilePicture(10, R.drawable.snake),
    ProfilePicture(11, R.drawable.tiger),
    ProfilePicture(12, R.drawable.penguin),
)

// maxPictureRand variable must follows the number of template profile picture available
var minPictureRand = 2
var maxPictureRand = 12
var profilePictureRandomizer = (minPictureRand..maxPictureRand).random()

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
fun ContentTopBar(navController: NavController, topBarText: String, searchBox: @Composable () -> Unit = { SearchBoxDefault() }) {
    /*
    Content Top Bar
    */
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(30.dp))
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
            searchBox()
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


@Composable
fun ItemContentTopBar(navController: NavController, store: Store?, searchBox: @Composable () -> Unit = { SearchBoxDefault() }) {
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
        // Left
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(30.dp))
            }

            Spacer(modifier = Modifier.height(65.dp))
        }

        Spacer(modifier = Modifier.width(15.dp))

        // Middle
        Column(
            modifier = Modifier.weight(10f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Row(

            ){

                Spacer(modifier = Modifier.width(20.dp))

                Card (modifier = Modifier
                    .fillMaxWidth()
                    // .background(Color(0xFFD9D9D9))
                    .clickable { /* TODO: editable? */ },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFD9D9D9) // correct way to set Card background
                    )


                ) {

                    Column(
                        // modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        // to make suranna font stick close to each other (normally they spaced out far)
                        verticalArrangement = Arrangement.spacedBy((-23).dp) // try -2 to -6

                    ){
                        Text(
                            text = store?.name ?: "Unknown Store",
                            fontSize = 30.sp,
                            fontFamily = surannaFont,
                            //lineHeight = 10.sp
                            //fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = store?.address ?: "Unknown Store",
                            fontSize = 15.sp,
                            fontFamily = surannaFont,
                            //lineHeight = 10.sp
                            //fontWeight = FontWeight.Bold
                        )


                    }


                }

            }


            searchBox()
        }

        // Right
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.width(40.dp))
        }
    }
}



@Composable
fun SplitItemContentTopBar(navController: NavController, topBarText: String) {
    /*
    Content Top Bar
    */
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

//            IconButton(onClick = { navController.popBackStack() }) {
//                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(30.dp))
//            }
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
            // SearchBox()
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.width(40.dp))

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.Close, contentDescription = "Cancel", modifier = Modifier.size(30.dp))
            }
        }
    }
}


/*
@Composable
fun ContentCard( content: @Composable ColumnScope.() -> Unit){
    // White Box
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(Color(0xFFD9D9D9))
            .padding(10.dp)
    ) {

    }

}

 */
