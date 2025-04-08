package com.swapnil.parallaxeffect

import android.graphics.Bitmap
import android.graphics.BlendMode
import android.graphics.ComposeShader
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.core.graphics.drawable.toBitmap

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BlendImagesWithComposeShader(image1: ImageBitmap, image2: ImageBitmap) {

    val shaderBrush = remember(image1, image2) {
        val shaderA = android.graphics.BitmapShader(
            image1.asAndroidBitmap(),
            Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        val shaderB = android.graphics.BitmapShader(
            image1.asAndroidBitmap(),
            Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )

        val composed = ComposeShader(
            shaderA,
            shaderB,
            BlendMode.LUMINOSITY // Or try BlendMode.SCREEN, DARKEN, etc.
        )

        ShaderBrush(composed)
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(brush = shaderBrush)
    }
}
