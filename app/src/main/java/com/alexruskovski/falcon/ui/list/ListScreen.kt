package com.alexruskovski.falcon.ui.list

import android.webkit.URLUtil
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.request.ImageResult
import com.alexruskovski.falcon.R
import com.alexruskovski.falcon.exceptions.BadServerResponseException
import com.alexruskovski.falcon.exceptions.NoNetworkConnectionException
import com.alexruskovski.falcon.model.Crew
import com.alexruskovski.falcon.model.Launch
import com.alexruskovski.falcon.model.Rocket
import com.alexruskovski.falcon.ui.components.FullScreenLoading
import com.alexruskovski.falcon.ui.components.InsetAwareTopAppBar
import com.alexruskovski.falcon.ui.components.LoadingContent
import com.alexruskovski.falcon.ui.navigation.NavigationRoutes
import com.alexruskovski.falcon.ui.state.UiState
import com.alexruskovski.falcon.util.produceUiState
import kotlinx.coroutines.launch
import com.alexruskovski.falcon.data.Result

/**
 * Created by Alexander Ruskovski on 14/08/2021
 */

@Composable
fun ListScreen(
    launchesListViewModel: LaunchesListViewModel,
    navHostController: NavHostController,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    val (postUiState, refreshPost, clearError) = produceUiState(launchesListViewModel) {
        getAllLaunches()
    }

    val rocketsResult by launchesListViewModel.getAllRockets()
        .collectAsState(Result.Success(listOf()))
    val crewResult by launchesListViewModel.getCrew()
        .collectAsState(Result.Success(listOf()))

    val rockets = when (rocketsResult) {
        is Result.Success -> (rocketsResult as Result.Success).data
        else -> listOf()
    }

    val crew = when (crewResult) {
        is Result.Success -> (crewResult as Result.Success).data
        else -> listOf()
    }

    if (postUiState.value.hasError) {
        val errorMessage = stringResource(id = R.string.load_error)
        val retryMessage = stringResource(id = R.string.retry)

        val onRefreshLaunchesState by rememberUpdatedState(refreshPost)
        val onErrorDismissState by rememberUpdatedState(clearError)

        LaunchedEffect(scaffoldState.snackbarHostState) {
            val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = errorMessage,
                actionLabel = retryMessage
            )
            when (snackBarResult) {
                SnackbarResult.ActionPerformed -> onRefreshLaunchesState()
                SnackbarResult.Dismissed -> onErrorDismissState()
            }
        }

    }

    //We are all good, display the launches
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListScreenTopAppBar(
                stringResource(id = R.string.list_screen_title)
            )
        }
    ) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        LoadingContent(
            empty = postUiState.value.initialLoad,
            emptyContent = {
                FullScreenLoading()
            },
            loading = postUiState.value.loading,
            onRefresh = refreshPost,
            content = {
                Column() {
                    Spacer(modifier = Modifier.height(8.dp))
                    ListScreenErrorAndContent(
                        modifier,
                        refreshPost,
                        rockets,
                        crew,
                        postUiState,
                        navHostController,
                        scaffoldState
                    )
                }
            }
        )

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CrewList(
    crew: List<Crew>,
    scaffoldState: ScaffoldState
) {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyRow(
        modifier = Modifier
            .padding(top = 16.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .scrollable(scrollState, Orientation.Horizontal)
    ) {
        itemsIndexed(crew) { _, crewMember ->
            CrewListItem(
                modifier = Modifier
                    .padding(6.dp, 0.dp, 6.dp, 0.dp),
                crewMember = crewMember
            ) { crewMember ->
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        "Not handled. (Name: ${crewMember.name})"
                    )
                }
            }
        }
    }
}

@ExperimentalCoilApi
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CrewListItem(
    modifier: Modifier,
    crewMember: Crew,
    onCrewMemberClick: (Crew) -> Unit
) {
    var textHeight by remember { mutableStateOf(0) }
    var parentHeight by remember { mutableStateOf(0) }

    val isImageLoadingState = remember {
        mutableStateOf(true)
    }

    Card(
        modifier = modifier
            .size(128.dp)
            .clickable {
                onCrewMemberClick.invoke(crewMember)
            },
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp,
        backgroundColor = Color.White,
    ) {
        val crewMemberStatusColour = if (crewMember.isActive)
            Color.Green
        else
            Color.Red

        Box(modifier = Modifier
            .onGloballyPositioned {
                parentHeight = it.size.height
            }
            .border(
                border = BorderStroke(1.dp, crewMemberStatusColour),
                shape = RoundedCornerShape(8.dp)
            )
        ) {

            if (isImageLoadingState.value) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    color = MaterialTheme.colors.primary.copy(alpha = .8f)
                )
            }

            if (crewMember.image != null && URLUtil.isValidUrl(crewMember.image)) {
                Image(
                    painter = rememberImagePainter(
                        data = crewMember.image,
                        builder = {
                            fadeIn()
                            listener(object : ImageRequest.Listener {
                                override fun onSuccess(
                                    request: ImageRequest,
                                    metadata: ImageResult.Metadata
                                ) {
                                    super.onSuccess(request, metadata)
                                    isImageLoadingState.value = true
                                }
                            })
                        }
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            //Text background, so that the text will be readable no matter the background colour.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = parentHeight - (textHeight * 2f)
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    modifier = Modifier
                        .onGloballyPositioned {
                            textHeight = it.size.height
                        },
                    text = crewMember.name,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
            }

        }
    }


}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RocketsList(
    rockets: List<Rocket>,
    scaffoldState: ScaffoldState
) {

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyRow(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .scrollable(scrollState, Orientation.Horizontal)
    ) {
        itemsIndexed(rockets) { _, rocket ->
            RocketListItem(
                modifier = Modifier
                    .padding(6.dp, 0.dp, 6.dp, 0.dp),
                rocket = rocket
            ) { clickedRocket ->
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        "Not handled. (Rocket name: ${clickedRocket.name})"
                    )
                }
                //todo: handle onRocket clicks
            }
        }
    }

}

@OptIn(ExperimentalCoilApi::class)
@ExperimentalAnimationApi
@Composable
fun RocketListItem(
    modifier: Modifier = Modifier,
    rocket: Rocket,
    onRocketClick: (Rocket) -> Unit
) {
    Card(
        modifier = modifier
            .size(128.dp)
            .clickable {
                onRocketClick.invoke(rocket)
            },
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp,
        backgroundColor = Color.White,
    ) {
        if (rocket.flickrImages != null && rocket.flickrImages.isNotEmpty()) {
            //try to load only the first one
            if (URLUtil.isValidUrl(rocket.flickrImages[0])) {

                val isImageLoadingState = remember {
                    mutableStateOf(true)
                }

                if (isImageLoadingState.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .wrapContentSize(),
                        color = MaterialTheme.colors.primary.copy(alpha = .8f)
                    )
                }

                Image(
                    painter = rememberImagePainter(
                        data = rocket.flickrImages[0],
                        builder = {
                            fadeIn()
                            listener(object : ImageRequest.Listener {
                                override fun onSuccess(
                                    request: ImageRequest,
                                    metadata: ImageResult.Metadata
                                ) {
                                    super.onSuccess(request, metadata)
                                    isImageLoadingState.value = false
                                }
                            })
                        }
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun ListScreenTopAppBar(
    title: String
) {
    InsetAwareTopAppBar(
        title = { Text(text = title) },
    )
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ListScreenErrorAndContent(
    modifier: Modifier = Modifier,
    refreshPosts: () -> Unit,
    rockets: List<Rocket>,
    crew: List<Crew>,
    launchesState: State<UiState<List<Launch>>>,
    navHostController: NavHostController,
    scaffoldState: ScaffoldState
) {


    //check for exceptions
    if (launchesState.value.hasError) {
        val message = when (val exception = launchesState.value.exception) {
            is NoNetworkConnectionException -> exception.userFriendlyMessage()
            is BadServerResponseException -> exception.userFriendlyMessage()
            else -> stringResource(id = R.string.launches_no_data_to_display)
        }

        Text(
            modifier = modifier
                .fillMaxSize()
                .clickable {
                    refreshPosts()
                },
            text = message
        )
        return
    }

    val launches = launchesState.value

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val scrollToTopVisibility = remember {
        mutableStateOf(false)
    }

    Box {
        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 64.dp)
                .graphicsLayer {
                    //Seems to have better performance when the check is done inside graphicsLayer block.
                    //see https://developer.android.com/reference/kotlin/androidx/compose/ui/graphics/package-summary
                    scrollToTopVisibility.value = listState.firstVisibleItemIndex > 5
                }
        ) {
            item { RocketsList(rockets, scaffoldState) }
            item { CrewList(crew, scaffoldState) }
            launches.data?.let {
                itemsIndexed(it) { _, launch ->
                    LaunchListItem(launch, navHostController)
                }
            }
        }

        AnimatedVisibility(
            visible = scrollToTopVisibility.value,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            ScrollToTopButton {
                scope.launch {
                    listState.animateScrollToItem(0)
                }
            }
        }
    }
}

@Composable
fun ScrollToTopButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp, end = 16.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        FloatingActionButton(
            modifier = modifier,
            onClick = onClick,
            backgroundColor = MaterialTheme.colors.primary,
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = stringResource(id = R.string.cd_scroll_to_top_of_the_list)
            )
        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@ExperimentalAnimationApi
@Composable
private fun LaunchListItem(
    launch: Launch,
    navHostController: NavHostController
) {

    val isImageLoadingState = remember {
        mutableStateOf(true)
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable {
                navHostController.navigate(
                    NavigationRoutes.DetailsScreen.routeWithId(launchId = launch.id)
                )
            },
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp,
        backgroundColor = Color.White,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //load the launch patch logo
            if (launch.links.patch.small != null) {
                Box() {
                    if (isImageLoadingState.value) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .wrapContentSize(),
                            color = MaterialTheme.colors.primary.copy(alpha = .8f)
                        )
                    }

                    Image(
                        painter = rememberImagePainter(
                            data = launch.links.patch.small,
                            builder = {
                                fadeIn()
                                listener(object : ImageRequest.Listener {
                                    override fun onSuccess(
                                        request: ImageRequest,
                                        metadata: ImageResult.Metadata
                                    ) {
                                        super.onSuccess(request, metadata)
                                        isImageLoadingState.value = false
                                    }
                                })
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .fillMaxHeight()
                            .padding(16.dp)
                            .size(128.dp)
                    )
                }
            } else {
                CorruptedImage()
            }

            CardLaunchDetails(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                launch = launch
            )
        }
    }
}

@Composable
fun CorruptedImage() {
    Image(
        painter = painterResource(id = R.drawable.ic_baseline_broken_image_24),
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .padding(16.dp),
        contentScale = ContentScale.Fit,
        contentDescription = stringResource(id = R.string.cd_launch_details_patch_logo_list_item)
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CardLaunchDetails(modifier: Modifier, launch: Launch) {

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            fontWeight = FontWeight.Bold,
            text = launch.name
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = launch.getFormattedDate())
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(text = stringResource(id = R.string.launch_details_list_item_missions_status))
            Spacer(modifier = Modifier.width(8.dp))
            val imgRes = if (launch.success == true)
                R.drawable.ic_launch_successful_24 else R.drawable.ic_launch_failed_24
            Image(
                painter = painterResource(imgRes),
                contentDescription = stringResource(id = R.string.cd_launch_details_list_item)
            )
        }
    }
}
