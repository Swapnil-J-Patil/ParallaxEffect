package com.swapnil.parallaxeffect

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
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.swapnil.parallaxeffect.ui.theme.ParallaxEffectTheme
import com.swapnil.parallaxeffect.ui.theme.blue

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParallaxEffectTheme {
                val images = listOf(
                    R.drawable.image_1,
                    R.drawable.image_2,
                    R.drawable.image_3,
                    R.drawable.image_4,
                )

                /*Column {
                    Image(
                        painter = painterResource(id = images[0]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(200.dp)
                            .border()

                    )
                    Image(
                        painter = painterResource(id = images[1]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(200.dp)
                    )
                }*/
               // VerticalImagePager()
                val image1 = ImageBitmap.imageResource(R.drawable.image_1)
                val image2 = ImageBitmap.imageResource(R.drawable.image_2)

               // BlendImagesWithComposeShader(image1, image2)
               /* DripStretchEffect(
                    image = image1,
                    modifier = Modifier
                        .fillMaxSize()
                )*/
                SwipeToTransitionScreen()
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToTransitionScreen() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenHeightPx = with(LocalDensity.current) { screenHeight.toPx() }

    val anchors = mapOf(0f to 0, screenHeightPx to 1)

    val swipeState = rememberSwipeableState(0)
    val swipeableModifier = Modifier.swipeable(
        state = swipeState,
        anchors = anchors,
        thresholds = { _, _ -> FractionalThreshold(0.3f) },
        orientation = Orientation.Vertical
    )

    // Animate progress from 0f to 1f based on swipe state
    val animatedProgress by animateFloatAsState(
        targetValue = swipeState.offset.value  / screenHeightPx ,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "SwipeProgress"
    )


    val colorStart = Color.Red
    val colorEnd = Color.Blue

    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0f to colorStart,
            1f to lerp(colorStart, colorEnd, animatedProgress),
            //1f to colorEnd
        )
    )



    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(swipeableModifier)
            .background(brush)

    )
}




@Composable
fun DripStretchEffect(image: ImageBitmap, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val topImageHeightRatio = 0.6f
        val canvasWidth = size.width
        val canvasHeight = size.height

        val topHeight = (canvasHeight * topImageHeightRatio).toInt()
        val bottomHeight = canvasHeight.toInt() - topHeight

        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint()

            // Convert to Android Bitmap
            val androidBitmap = image.asAndroidBitmap()

            // Create Android Canvas
            val nativeCanvas = canvas.nativeCanvas

            // Draw the top part of the image
            val srcTop = android.graphics.Rect(0, 0, androidBitmap.width, androidBitmap.height)
            val dstTop = android.graphics.Rect(0, 0, canvasWidth.toInt(), topHeight)

            nativeCanvas.drawBitmap(androidBitmap, srcTop, dstTop, paint)

            // Take a 1px height strip from the bottom of the image
            val stripHeight = 1
            val srcBottom = android.graphics.Rect(
                0,
                androidBitmap.height - stripHeight,
                androidBitmap.width,
                androidBitmap.height
            )

            // Stretch this bottom strip downward to fill the rest
            val dstBottom = android.graphics.Rect(
                0,
                topHeight,
                canvasWidth.toInt(),
                canvasHeight.toInt()
            )

            nativeCanvas.drawBitmap(androidBitmap, srcBottom, dstBottom, paint)
        }
    }
}

@Composable
fun SmoothColorTransitionScreen() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenHeightPx = with(LocalDensity.current) { screenHeight.toPx() }

    // Your start and end colors
    val colorA = Color(0xFF1E88E5) // Blue
    val colorB = Color(0xFFE53935) // Red

    // Track the drag offset
    var dragOffset by remember { mutableStateOf(0f) }

    // Clamp between 0f and screen height
    val clampedOffset = dragOffset.coerceIn(0f, screenHeightPx)

    // Progress for gradient blending
    val progress = clampedOffset / screenHeightPx

    // Interpolated brush
    val brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0f to lerp(colorA, colorB, progress),
            1f to colorB
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush)
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    dragOffset += delta
                }
            )
    )
}

@Composable
fun VerticalImagePager() {
    val images = listOf(
        R.drawable.image_1,
        R.drawable.image_2,
        R.drawable.image_3,
        R.drawable.image_4,
    )

    val pagerState = rememberPagerState(pageCount = { images.size })

    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        Box(modifier = Modifier.fillMaxSize()) {
            // 🔹 Background Image
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )


        }
    }
}
@Composable
fun BlendedVerticalImages(
    image1: ImageBitmap,
    image2: ImageBitmap,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // Bottom image with gradient fade-in from top
        Image(
            bitmap = image2,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    alpha = 1f
                }
                .drawWithCache {
                    val gradient = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 0f,
                        endY = size.height * 0.2f
                    )
                    onDrawWithContent {
                        drawContent()
                        drawRect(gradient, blendMode = BlendMode.DstIn)
                    }
                }
        )

        // Top image with gradient fade-out at bottom
        Image(
            bitmap = image1,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    alpha = 1f
                }
                .drawWithCache {
                    val gradient = Brush.verticalGradient(
                        colors = listOf(Color.Black, Color.Transparent),
                        startY = size.height * 0.8f,
                        endY = size.height
                    )
                    onDrawWithContent {
                        drawContent()
                        drawRect(gradient, blendMode = BlendMode.DstIn)
                    }
                }
        )
    }
}



