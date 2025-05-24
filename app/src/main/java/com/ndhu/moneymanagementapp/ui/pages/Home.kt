package com.ndhu.moneymanagementapp.ui.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.InsertChartOutlined
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndhu.moneymanagementapp.R
import kotlinx.coroutines.runBlocking
import com.ndhu.moneymanagementapp.controllers.UserController
import com.ndhu.moneymanagementapp.enums.AppScreens
import com.ndhu.moneymanagementapp.models.User

@Composable
fun Home(context: Context, navController: NavController) {
    val loginUser = runBlocking {
        UserController().getOnboardUser(context = context)?.username ?: "{FAIL-LOGIN}"
    }
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

    val coroutineScope = rememberCoroutineScope()

    var user: User? = null

//    LaunchedEffect(Unit) {
//        coroutineScope.launch {
//            user = UserController().getOnboardUser(context)
//        }
//    }

    runBlocking {
        user = UserController().getOnboardUser(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background_dark),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // "WE SPLIT"
            Row(
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

                // TODO: need dynamic spacing (maybe via applying weights?)
                Spacer(modifier = Modifier.width(160.dp))

                Button(
                    onClick = { navController.navigate(route = AppScreens.DebtListingScreen.name) },
                    colors = ButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent,
                    )
                    ) {

                    Row(
                        // modifier = Modifier.fillMaxHeight()
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Chart",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontFamily = kronaOneFont
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(Icons.Default.InsertChartOutlined,
                            contentDescription = "Debt Listing Screen",
                            modifier = Modifier.size(30.dp),
                            tint = Color.White
                        )
                    }


                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Profile Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .weight(0.2f)
                    .clickable(onClick = {
                        navController.navigate(AppScreens.AccountScreen.name)
                    })
            ) {

                Log.d("ProfilePic", user?.username ?: "null")

                var profilePic = profilePictures.firstOrNull(){ it.id == (user?.profilePictureId ?: 1) }

                if (profilePic != null) {
                    Image(
                        // painter = painterResource(id = R.drawable.profile_picture),
                        painter = painterResource(id = profilePic.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(0.2f)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(0.8f)
                ) {
                    Text(
                        text = "Hello",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = sensationLightFont,
                        fontSize = 12.sp
                    )
                    Text(
                        text = loginUser,
                        color = Color.hsv(43f, 1f, 1f),
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = syncopateFont,
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(78.dp))

            // Greyish color Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(Color(0xFFD9D9D9))
                    .padding(start = 24.dp, top = 48.dp, bottom = 48.dp, end = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeButton(
                        iconResId = R.drawable.icon_split_bill,
                        backgroundResId = R.drawable.button_background,
                        text = "Split Bill",
                        textFontSize = 24,
                        onClick = { navigateToSplitBillScreen(navController) },
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxHeight()
                    )
                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        HomeButton(
                            iconResId = R.drawable.icon_history,
                            backgroundResId = R.drawable.bill_history,
                            text = "Bill History",
                            textFontSize = 16,
                            onClick = { navigateToBillHistoryScreen(navController) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                        HomeButton(
                            iconResId = R.drawable.icon_contact,
                            backgroundResId = R.drawable.contact,
                            text = "Contact",
                            textFontSize = 20,
                            onClick = { navigateToContactScreen(navController) },
                            modifier = Modifier
                                .weight(2f)
                                .fillMaxWidth()
                        )

                        // Temporary Button
//                        Button(onClick = { navController.navigate(route = AppScreens.DebtListingScreen.name) }) {
//                            Text(text = "Graph (Temp)")
//                        }
                    }
                }
            }
        }
    }
}


@Composable
fun HomeButton(
    iconResId: Int,
    backgroundResId: Int,
    text: String,
    textFontSize: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val surannaFont = FontFamily(
        Font(R.font.suranna_regular, FontWeight.Normal),
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFB2DFDB))
            .clickable { onClick() }
    ) {
        // Background for button
        Image(
            painter = painterResource(id = backgroundResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(
                color = Color(0xFF009669), // Light color tint
                blendMode = BlendMode.Multiply
            ),
            modifier = Modifier
                .matchParentSize()
                .alpha(0.8f)
        )

        // Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = textFontSize.sp,
                fontFamily = surannaFont,
                modifier = Modifier.align(Alignment.TopStart)
            )

            Image(
                painter = painterResource(id = iconResId),
                contentDescription = text,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

fun navigateToSplitBillScreen(navController: NavController) {
    navController.navigate(route = AppScreens.ListStoresScreen.name)
}

fun navigateToBillHistoryScreen(navController: NavController) {
    navController.navigate(route = AppScreens.BillHistoryScreen.name)
}

fun navigateToContactScreen(navController: NavController) {
    navController.navigate(route = AppScreens.ContactScreen.name)
}
