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
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                SmoothColorTransition()
            }
        }
    }
}


@Composable
fun SmoothColorTransition() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current
    val screenHeightPx = with(density) { screenHeight.toPx() }

    val colors = listOf(Color.Blue, Color.Red, Color.Green) // Your 3 colors

    var currentIndex by remember { mutableStateOf(1) } // Start at color2
    val offsetY = remember { Animatable(0f) } // Transition progress between 0f-1f
    val isSwiping = remember { mutableStateOf(false) }
    val lastDragDirection = remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()

    val startColor = colors.getOrNull(currentIndex) ?: Color.Black
    val targetColor = when {
        lastDragDirection.value < 0 && currentIndex > 0 -> colors[currentIndex - 1] // Swipe UP
        lastDragDirection.value > 0 && currentIndex < colors.lastIndex -> colors[currentIndex + 1] // Swipe DOWN
        else -> startColor
    }

    val blendedColor = lerp(startColor, targetColor, offsetY.value)

    val startY: Float
    val endY: Float

    if (lastDragDirection.value < 0) {
        // Swiping UP — new color should appear from the BOTTOM
        startY = screenHeightPx * 2f
        endY = -screenHeightPx
    } else if (lastDragDirection.value > 0) {
        // Swiping DOWN — new color should appear from the TOP
        startY = -screenHeightPx
        endY = screenHeightPx * 2f
    } else {
        // Default
        startY = -screenHeightPx
        endY = screenHeightPx * 2f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(currentIndex) {
                detectVerticalDragGestures(
                    onDragStart = { isSwiping.value = true },
                    onDragEnd = {
                        isSwiping.value = false
                        scope.launch {
                            if (lastDragDirection.value < 0 && currentIndex > 0) {
                                // Animate to full transition
                                offsetY.animateTo(1f, tween(800, easing = LinearOutSlowInEasing))
                                // Reset offset first (so lerp uses 0f before recomposition!)
                                offsetY.snapTo(0f)
                                // Now update index AFTER resetting offset
                                currentIndex -= 1
                            } else if (lastDragDirection.value > 0 && currentIndex < colors.lastIndex) {
                                offsetY.animateTo(1f, tween(800, easing = LinearOutSlowInEasing))
                                offsetY.snapTo(0f)
                                currentIndex += 1
                            } else {
                                // Not enough swipe: reset nicely
                                offsetY.snapTo(0f)
                            }
                        }
                    },
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        lastDragDirection.value = dragAmount
                        scope.launch {
                            val progress =
                                (offsetY.value - dragAmount / screenHeightPx).coerceIn(0f, 1f)
                            offsetY.snapTo(progress)
                        }
                    }
                )
            }
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        startColor,
                        blendedColor,
                        targetColor
                    ),
                    startY = startY,
                    endY = endY
                )
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



