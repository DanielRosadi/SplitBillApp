package com.ndhu.moneymanagementapp.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// still in experiment


@Composable
fun LazyColumnDesignTemplate(/* storeController: StoreController, data: SnapshotStateList<Store>, viewModel: SharedViewModel, */ navController: NavController, lazyColumnFunc: ()-> Unit, bottomPage: @Composable () -> Unit) {
    // var showDialogAddStore by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        // Background Image
        BackgroundImage()

        // Full Page
        Column(
            modifier = Modifier
                .fillMaxWidth()
            //.fillMaxHeight()
            // .padding(paddingValues)
            //.padding(10.dp)
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            TitleText()

            Spacer(modifier = Modifier.height(30.dp))

            // White Box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color(0xFFD9D9D9))
                    .padding(10.dp)
            )
            {

                // Content Top Bar
                // TODO: search func
                ContentTopBar(navController = navController, topBarText = "List of Store",)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(7f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(darkGrey)

                ){
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentPadding = PaddingValues(5.dp)
                    ) {

                        lazyColumnFunc() // for items and lazycolumnfunction
                        /*

                        // items(data) { item ->
                        items(data) { item ->
                            ListDataStore(storeController, item, navController, data, viewModel)
                        }
                        */

                    }

                }

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        //.fillMaxHeight()
                        .weight(1f)
                        .padding(2.dp)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // we no longer need delete item, keep it for possible future change
                    /*
                Button(onClick = {navController.navigate(route = AppScreens.DeleteStoresScreen.name)}, modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .weight(1f)) {
                    Text(text = "Delete (Temp)")
                }
                 */

                    // bottom UI here

                    bottomPage() // for buttons

                    /*

                    Button(
                        onClick = { showDialogAddStore = true },
                        modifier = Modifier
                            //.height(60.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()

                        //.weight(1f)
                        ,
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonColors(
                            containerColor = green,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White
                        )
                    ) {
                        Text(text = "Add Store")
                    }
                    */
                    /*

                    if (showDialogAddStore) {
                        PopUpAddStore(
                            storeController = storeController,
                            onDismiss = { showDialogAddStore = false },
                            data,
                            viewModel
                        )
                    }
                    */

                }
            }
        }
    }
}
