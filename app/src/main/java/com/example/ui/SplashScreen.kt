package com.example.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.LocalizationManager
import com.example.ui.components.ParticleRain
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    // Timer to automatically finish splash after 3.5 seconds
    LaunchedEffect(Unit) {
        delay(3500)
        onSplashFinished()
    }

    // Dynamic animators for premium cinematic depth motion
    val infiniteTransition = rememberInfiniteTransition(label = "splash_loops")
    
    // Rotating 3D box tilt
    val rotationY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    // Floating vertical hover
    val hoverY by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hover"
    )

    var startAnims by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        startAnims = true
    }

    // Breathing logo scale
    val logoScale by animateFloatAsState(
        targetValue = if (startAnims) 1.0f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessVeryLow
        ),
        label = "scale"
    )

    // Smooth typography fade in
    val textAlpha by animateFloatAsState(
        targetValue = if (startAnims) 1.0f else 0f,
        animationSpec = tween(1200, delayMillis = 400),
        label = "text_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1B1B1F), // Dark slate center glow
                        Color(0xFF09090A)  // Obsidian edges
                    )
                )
            )
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Luxury golden particle dust
        ParticleRain(
            modifier = Modifier.fillMaxSize(),
            particleColor = Color(0x2BD4AF37),
            maxParticles = 40
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Master 3D Hovering Box Container
            Box(
                modifier = Modifier
                    .scale(logoScale)
                    .offset(y = hoverY.dp)
                    .graphicsLayer {
                        this.rotationY = rotationY
                        cameraDistance = 16f
                    }
                    .size(160.dp)
                    .shadow(
                        elevation = 24.dp,
                        shape = CircleShape,
                        clip = false,
                        spotColor = Color(0xFFD4AF37)
                    )
                    .border(2.dp, Color(0xFFD4AF37), CircleShape)
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF09090A))
            ) {
                // Luxury Logo asset
                Image(
                    painter = painterResource(id = R.drawable.img_luxury_gift_logo),
                    contentDescription = "Teke Promotion",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Brand Typography reveal
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(textAlpha)
            ) {
                Text(
                    text = "TEKE PROMOTION",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color(0xFFD4AF37),
                    letterSpacing = 6.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = LocalizationManager.string("app_tagline"),
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.5f),
                    letterSpacing = 2.sp
                )
            }
        }
    }
}
