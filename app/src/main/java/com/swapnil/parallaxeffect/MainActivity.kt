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
import androidx.compose.animation.core.animateFloatAsState
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

    val offsetY = remember { Animatable(screenHeightPx) } // Start fully on color2
    val isSwiping = remember { mutableStateOf(false) }
    val lastDragDirection = remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()

    val color1 = Color.Blue // Teal
    val color2 = Color.Red // Tomato

    val transitionProgress = (offsetY.value  / screenHeightPx).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = {
                        isSwiping.value = true
                    },
                    onDragEnd = {
                        isSwiping.value = false

                        scope.launch {
                            val target = if (lastDragDirection.value < 0) {
                                // Swiped UP → reveal color1
                                0f
                            } else {
                                // Swiped DOWN → return to color2
                                screenHeightPx
                            }

                            offsetY.animateTo(
                                targetValue = target,
                                animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
                            )
                        }
                    },
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        lastDragDirection.value = dragAmount

                        if (isSwiping.value) {
                            scope.launch {
                                val newOffset = (offsetY.value + dragAmount).coerceIn(0f, screenHeightPx)
                                offsetY.snapTo(newOffset)
                            }
                        }
                    }
                )
            }
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        color1,
                        lerp(color1, color2, transitionProgress),
                        color2
                    ),
                    startY = -screenHeightPx,
                    endY = screenHeightPx * 2f
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



