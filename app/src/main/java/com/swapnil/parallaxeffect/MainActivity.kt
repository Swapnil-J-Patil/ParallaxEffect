package com.swapnil.parallaxeffect

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.swapnil.parallaxeffect.ui.theme.ParallaxEffectTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParallaxEffectTheme {

                Box {

                    //ParallaxEffect()
                    VerticalPagerExample()
                    //Scene()
                }
            }
        }
    }
}

@Composable
fun Scene(
    modifier: Modifier = Modifier,
    bgImage: Int,
    isMiddleImg: Boolean=false,
    middleImg: Int=0,
    bottomImg: Int,
    text: String,
    title: String,
    animAligner: Alignment= Alignment.TopCenter,
    animModifier: Modifier=Modifier,
    colAligner: Alignment= Alignment.Center,
    colModifier: Modifier=Modifier,
    bottomImgModifier: Modifier,
    isVisible: Boolean
) {
    val scale = remember { Animatable(1f) }
    var visibility by remember { mutableStateOf(false) }
    var middleImgVisibility by remember { mutableStateOf(false) }
    val configuration= LocalConfiguration.current
    val screenHeight= configuration.screenHeightDp.dp
    val screenWidth= configuration.screenWidthDp.dp

    LaunchedEffect(isVisible) {
        //delay(500)
        if (isVisible) {

            visibility = true
            delay(200)
            middleImgVisibility = true
            delay(1000)
            while (true) {
                scale.animateTo(
                    targetValue = 1.8f,
                    animationSpec = tween(
                        durationMillis = 6000,
                        easing = FastOutSlowInEasing
                    )
                )
                //delay(800)
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 6000,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize())
    {
        Box(
            modifier.fillMaxSize()
        ) {
            Image(
                painterResource(id = bgImage),
                contentDescription = "AnimeLogo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale.value)

                //.aspectRatio(1f)
            )
            Box(
                modifier.fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.3f))
            )
        }

        AnimatedVisibility(
            visible = visibility,
            enter = slideInVertically(
                initialOffsetY = { it / 2 }, // Starts from full height (bottom)
                animationSpec = tween(
                    durationMillis = 1000, // Slightly increased duration for a smoother feel
                    easing = FastOutSlowInEasing // Smoother easing for entering animation
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { it / 2 }, // Moves to full height (bottom)
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing // Keeps a natural exit motion
                )
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing
                )
            ),
            modifier = animModifier
                .align(animAligner)
        ) {
            Column(
                modifier = colModifier
                    .align(colAligner)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h2,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Left,
                    // fontFamily = Poppins,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    textAlign = TextAlign.Left,
                    // fontFamily = Poppins,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 10.dp, end = 5.dp)
                )
            }
        }

        if(isMiddleImg)
        {
            AnimatedVisibility(
                visible = middleImgVisibility,
                enter = slideInVertically(
                    initialOffsetY = { it }, // Starts from full height (bottom)
                    animationSpec = tween(
                        durationMillis = 1000, // Slightly increased duration for a smoother feel
                        easing = FastOutSlowInEasing // Smoother easing for entering animation
                    )
                ) + fadeIn(
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it }, // Moves to full height (bottom)
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = FastOutSlowInEasing // Keeps a natural exit motion
                    )
                ) + fadeOut(
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = FastOutSlowInEasing
                    )
                ),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
            Image(
                painterResource(id = middleImg),
                contentDescription = "couple",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(start = 15.dp)
                    .aspectRatio(1f)
                    .align(Alignment.Center)
                    .scale(scale.value)

                // Reduce scrolling rate by half.
            )
                }
        }

        AnimatedVisibility(
            visible = visibility,
            enter = slideInVertically(
                initialOffsetY = { it }, // Starts from full height (bottom)
                animationSpec = tween(
                    durationMillis = 1000, // Slightly increased duration for a smoother feel
                    easing = FastOutSlowInEasing // Smoother easing for entering animation
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Moves to full height (bottom)
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing // Keeps a natural exit motion
                )
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing
                )
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Image(
                painterResource(id = bottomImg),
                contentDescription = "couple",
                contentScale = ContentScale.Crop,
                modifier = bottomImgModifier
                    .align(Alignment.BottomCenter)
                // Reduce scrolling rate by half.
            )
        }
    }
}



@Composable
fun VerticalPagerExample() {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val scope = rememberCoroutineScope()

    VerticalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        beyondViewportPageCount = 0, // optional: disables offscreen rendering
        pageSpacing = 0.dp // This removes the gap between pages
    ) { page ->
        val isVisible = pagerState.currentPage == page

        when (page % 3)
        {
            0 -> Scene(
                bgImage = R.drawable.bg1,
                bottomImg = R.drawable.couple,
                text = stringResource(id = R.string.content1),
                title = stringResource(id = R.string.title1),
                isMiddleImg = true,
                middleImg = R.drawable.moon,
                animAligner = Alignment.Center,
                animModifier = Modifier
                    .padding(start = 20.dp, top = 25.dp)
                ,
                colAligner = Alignment.Center,
                colModifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 200.dp),
                bottomImgModifier = Modifier
                    .fillMaxWidth()
                // .aspectRatio(1f)
                ,
                isVisible = isVisible
            )
            1 -> Scene(
                bgImage = R.drawable.bg2,
                bottomImg = R.drawable.standing_boy,
                text = stringResource(id = R.string.content2),
                title = stringResource(id = R.string.title2),
                isMiddleImg = false,
                animAligner = Alignment.BottomCenter,
                animModifier = Modifier
                    .padding(start = 20.dp, top = 25.dp)
                ,
                colAligner = Alignment.Center,
                colModifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 200.dp),
                bottomImgModifier = Modifier
                    .fillMaxWidth()
                .aspectRatio(1f)
                ,
                isVisible = isVisible
            )
            2->
                Scene(
                    bgImage = R.drawable.bg3,
                    bottomImg = R.drawable.astronaut,
                    text = stringResource(id = R.string.content3),
                    title = stringResource(id = R.string.title3),
                    isMiddleImg = false,
                    animAligner = Alignment.Center,
                    animModifier = Modifier
                        .padding(start = 20.dp, top = 25.dp)
                    ,
                    colAligner = Alignment.Center,
                    colModifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 380.dp),
                    bottomImgModifier = Modifier
                        .fillMaxWidth()
                       // .aspectRatio(1f)
                    ,
                    isVisible = isVisible
                )
            else -> "No Content"
        }
        /*Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    when (page % 3) {
                        0 -> Color.Red
                        1 -> Color.Green
                        else -> Color.Blue
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Page #$page", style = MaterialTheme.typography.h1, color = Color.White)
        }*/
        // Scene()
        //ParallaxEffect()
    }
}

@Composable
fun ParallaxEffect() {
    fun Modifier.parallaxLayoutModifier(scrollState: ScrollState, rate: Int) =
        layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            val height = if (rate > 0) scrollState.value / rate else scrollState.value
            layout(placeable.width, placeable.height) {
                placeable.place(0, height)
            }
        }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
    ) {

        Image(
            painterResource(id = R.drawable.bg1),
            contentDescription = "Android logo",
            contentScale = ContentScale.FillBounds,
            // Reduce scrolling rate by half.
            modifier = Modifier
                .height(600.dp)
                .fillMaxWidth()
                .parallaxLayoutModifier(scrollState, 2)
        )


        Image(
            painterResource(id = R.drawable.bg1),
            contentDescription = "Android logo",
            contentScale = ContentScale.Fit,
            // Reduce scrolling rate by half.
            modifier = Modifier
                .height(300.dp)
                .offset(y = -200.dp)
                .fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .offset(y = -300.dp)
                .background(Color.Black)
                .parallaxLayoutModifier(scrollState, 2)
        ) { }
        /* Box(
             modifier = Modifier
                 .fillMaxWidth()
                 .height(500.dp)
                 .offset(y=-300.dp)
                 .background(Color.Red)
         ) {  }*/
    }
}








