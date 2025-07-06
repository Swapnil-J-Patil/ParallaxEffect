package com.swapnil.parallaxeffect

import android.graphics.Bitmap
import android.graphics.Color.alpha
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.swapnil.parallaxeffect.ui.theme.ParallaxEffectTheme
import com.swapnil.parallaxeffect.ui.theme.Poppins
import com.swapnil.parallaxeffect.ui.theme.blue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
fun Scene(modifier: Modifier = Modifier) {
    val scale = remember { Animatable(1f) }
    var visibility by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(500)
        visibility=true
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
    Box(modifier = Modifier.fillMaxSize())
    {
        Image(
            painterResource(id = R.drawable.img_6),
            contentDescription = "AnimeLogo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .scale(scale.value)

            //.aspectRatio(1f)
        )


        AnimatedVisibility(
            visible = visibility,
            enter = slideInVertically(
                initialOffsetY = { it/2 }, // Starts from full height (bottom)
                animationSpec = tween(
                    durationMillis = 800, // Slightly increased duration for a smoother feel
                    easing = LinearOutSlowInEasing // Smoother easing for entering animation
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = 800,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { it/2 }, // Moves to full height (bottom)
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing // Keeps a natural exit motion
                )
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing
                )
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(start = 20.dp,top=25.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(bottom = 380.dp)
            ) {
                Text(
                    text = "OUR VISION",
                    style = MaterialTheme.typography.h2,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Left,
                    // fontFamily = Poppins,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = stringResource(id = R.string.lorem_ipsum),
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
        Image(
            painterResource(id = R.drawable.moon),
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
        AnimatedVisibility(
            visible = visibility,
            enter = slideInVertically(
                initialOffsetY = { it }, // Starts from full height (bottom)
                animationSpec = tween(
                    durationMillis = 800, // Slightly increased duration for a smoother feel
                    easing = LinearOutSlowInEasing // Smoother easing for entering animation
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = 800,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }, // Moves to full height (bottom)
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing // Keeps a natural exit motion
                )
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing
                )
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Image(
                painterResource(id = R.drawable.couple),
                contentDescription = "couple",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    // .aspectRatio(1f)
                    .align(Alignment.BottomCenter)
                // Reduce scrolling rate by half.
            )
        }
    }
}
@Composable
fun VerticalPagerExample() {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = {5})
    val scope = rememberCoroutineScope()

    VerticalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        beyondViewportPageCount = 0, // optional: disables offscreen rendering
        pageSpacing = 0.dp // This removes the gap between pages
    ) { page ->
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
        Scene()
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
            painterResource(id = R.drawable.img_6),
            contentDescription = "Android logo",
            contentScale = ContentScale.FillBounds,
            // Reduce scrolling rate by half.
            modifier = Modifier
                .height(600.dp)
                .fillMaxWidth()
                .parallaxLayoutModifier(scrollState, 2)
        )


        Image(
            painterResource(id = R.drawable.img_6),
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
        ) {  }
       /* Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .offset(y=-300.dp)
                .background(Color.Red)
        ) {  }*/
    }
}








