package com.alexruskovski.falcon.ui.details

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.request.ImageResult
import com.alexruskovski.falcon.R
import com.alexruskovski.falcon.model.Launch
import com.alexruskovski.falcon.ui.components.FullScreenLoading
import com.alexruskovski.falcon.ui.components.InsetAwareTopAppBar
import com.alexruskovski.falcon.ui.components.LoadingContent
import com.alexruskovski.falcon.ui.state.UiState
import com.alexruskovski.falcon.util.produceUiState

/**
 * Created by Alexander Ruskovski on 14/08/2021
 */

@ExperimentalAnimationApi
@Composable
fun DetailsScreen(
    launchId: String?,
    detailsScreenViewModel: DetailsScreenViewModel,
    navController: NavHostController
) {

    if (launchId == null) {
        NoLaunchIdPassed {
            navController.popBackStack()
        }
        return
    }

    val (postUiState, refreshPost) = produceUiState(detailsScreenViewModel) {
        getSingleLaunchDetails(launchId)
    }

    val scaffoldState = rememberScaffoldState()
    val topBarTitle = remember {
        mutableStateOf("")
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            InsetAwareTopAppBar(
                title = { Text(text = topBarTitle.value) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_up)
                        )
                    }
                }
            )
        }
    ) {
        LoadingContent(
            empty = postUiState.value.initialLoad,
            emptyContent = {
                FullScreenLoading()
            },
            loading = postUiState.value.loading,
            onRefresh = refreshPost,
            content = {
                postUiState.value.data?.let { topBarTitle.value = it.name }
                DetailsScreenAndContent(postUiState.value)
            }
        )

    }
}

@ExperimentalAnimationApi
@Composable
fun DetailsScreenAndContent(
    launchDataState: UiState<Launch>
) {
    val launch = launchDataState.data
    if (launch == null) {
        LaunchIsNull()
        return
    }

    Column() {
        TopBox(launch = launch)
        BottomBox(launch = launch)
    }

}

@Composable
fun BottomBox(launch: Launch) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 16.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 32.dp
            )
    ) {

        Row() {
            Text(text = "Mission Name: ", fontWeight = FontWeight.Bold)
            Text(text = launch.name)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row {
            Text(text = "Mission Launched On: ", fontWeight = FontWeight.Bold)
            Text(text = launch.getFormattedDate())
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row() {
            Text(text = "Mission Status: ", fontWeight = FontWeight.Bold)
            val imgRes = if (launch.success == true)
                R.drawable.ic_launch_successful_24 else R.drawable.ic_launch_failed_24
            Image(
                painter = painterResource(imgRes),
                contentDescription = "Mission status image."
            )
        }

        Spacer(modifier = Modifier.width(32.dp))

        if (launch.details != null && launch.details.isNotEmpty()) {
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = "Details:",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .verticalScroll(state = rememberScrollState(), true)
            ) {
                Text(text = launch.details, fontStyle = FontStyle.Italic)
            }

        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun TopBox(launch: Launch) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.35f)
            .background(MaterialTheme.colors.primary),
        contentAlignment = Alignment.Center
    ) {

        val isLoadingState = remember {
            mutableStateOf(true)
        }

        if (isLoadingState.value) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.onPrimary
            )
        }

        Column {
            Image(
                painter = rememberImagePainter(
                    data = launch.links.patch.large,
                    builder = {
                        listener(object : ImageRequest.Listener {
                            override fun onError(request: ImageRequest, throwable: Throwable) {
                                super.onError(request, throwable)
                                //TODO handle onError scenario..
                            }

                            override fun onSuccess(
                                request: ImageRequest,
                                metadata: ImageResult.Metadata
                            ) {
                                super.onSuccess(request, metadata)
                                isLoadingState.value = false
                            }
                        })
                    }
                ),
                contentDescription = stringResource(id = R.string.cd_patch_image_details_screen)
            )
        }
    }

}


@Composable
fun LaunchIsNull() {
    Text(text = "The Box", fontWeight = FontWeight.Bold)
}

@Composable
fun NoLaunchIdPassed(
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ups, something went wrong!")
        Button(onClick = onButtonClick) {
            Text("Back to the list")
        }
    }
}