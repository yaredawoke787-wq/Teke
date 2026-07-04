package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private const val PI_F = 3.1415927f

@Composable
fun OnboardingScreen(onOnboardingFinished: () -> Unit) {
    var currentSlide by remember { mutableStateOf(0) }
    var dragAmount by remember { mutableStateOf(0f) }
    var autoSlideEnabled by remember { mutableStateOf(true) }

    // Auto-slide effect: slide every 4 seconds
    LaunchedEffect(currentSlide, autoSlideEnabled) {
        if (autoSlideEnabled) {
            delay(4000)
            currentSlide = (currentSlide + 1) % 5
        }
    }

    // Interactive loops for floating/breathing (60/120 fps physics)
    val infiniteTransition = rememberInfiniteTransition(label = "onboarding_loops")
    
    val breathingFloat by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "onboarding_breathing"
    )

    val microOrbit by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "onboarding_orbit"
    )

    val swingAngle by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "swing_tags"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag("onboarding_screen")
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        autoSlideEnabled = false // Pause auto-slide once user interacts
                        if (dragAmount > 60) {
                            // Swipe Right -> Prev
                            currentSlide = if (currentSlide > 0) currentSlide - 1 else 4
                        } else if (dragAmount < -60) {
                            // Swipe Left -> Next
                            currentSlide = if (currentSlide < 4) currentSlide + 1 else 0
                        }
                        dragAmount = 0f
                    },
                    onHorizontalDrag = { _, amount ->
                        dragAmount += amount
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // --- 1. PRISTINE LUXURY WHITE STUDIO BACKGROUND ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF), // Pure white center
                            Color(0xFFF9FBFC), // Volumetric soft studio glow
                            Color(0xFFF0F2F6)  // Luxury ambient light falloff
                        ),
                        center = Offset.Unspecified,
                        radius = 1400f
                    )
                )
        )

        // Custom ambient lighting overlay spheres (Apple keynote style)
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color(0xFFF4E2B8).copy(alpha = 0.12f),
                radius = 280.dp.toPx(),
                center = Offset(size.width * 0.15f, size.height * 0.2f)
            )
            drawCircle(
                color = Color(0xFFD4AF37).copy(alpha = 0.05f),
                radius = 320.dp.toPx(),
                center = Offset(size.width * 0.85f, size.height * 0.7f)
            )
        }

        // --- 2. FLOATING SPARKLY PARTICLES & RIBBONS BACKGROUND ---
        OnboardingDustParticles(modifier = Modifier.fillMaxSize())

        // --- 3. THE CINEMATIC INTERACTIVE CAROUSEL ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(bottom = 96.dp), // Space for bottom actions
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Upper Header Brand Area
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color(0xFFD4AF37), CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_splash_logo),
                            contentDescription = "Logo Mini",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "TEKE MAN",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 2.sp,
                        color = Color(0xFF111111)
                    )
                }

                // Premium Tag
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF4E2B8).copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        .border(0.5.dp, Color(0xFFD4AF37).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "PREMIUM",
                        color = Color(0xFFB8860B),
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Central Carousel with Slide Animation
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = currentSlide,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { width -> width / 2 } + fadeIn(animationSpec = tween(400, easing = EaseInOutCubic))) togetherWith
                                    (slideOutHorizontally { width -> -width / 2 } + fadeOut(animationSpec = tween(400, easing = EaseInOutCubic)))
                        } else {
                            (slideInHorizontally { width -> -width / 2 } + fadeIn(animationSpec = tween(400, easing = EaseInOutCubic))) togetherWith
                                    (slideOutHorizontally { width -> width / 2 } + fadeOut(animationSpec = tween(400, easing = EaseInOutCubic)))
                        }.using(SizeTransform(clip = false))
                    },
                    label = "slide_transition"
                ) { targetSlide ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                    ) {
                        // 3D Animated Illustration Area
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .graphicsLayer {
                                    translationY = breathingFloat.dp.toPx()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            when (targetSlide) {
                                0 -> SlideWelcomeGraphics(microOrbit)
                                1 -> SlideRewardsGraphics(microOrbit)
                                2 -> SlideGiftsGraphics(microOrbit)
                                3 -> SlidePromoGraphics(microOrbit, swingAngle)
                                4 -> SlideStartGraphics(microOrbit, glowAlpha)
                            }
                        }

                        // Premium Typography Block
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = getSlideHeadline(targetSlide),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Serif,
                                color = Color(0xFF111111),
                                letterSpacing = 0.5.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = getSlideSubtitle(targetSlide),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF666666),
                                letterSpacing = 0.2.sp,
                                lineHeight = 22.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }

        // --- 4. BOTTOM NAVIGATION & ACTION BAR (Absolute Luxury Alignment) ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Action: Skip (fades on the last screen)
                if (currentSlide < 4) {
                    TextButton(
                        onClick = { onOnboardingFinished() },
                        modifier = Modifier.testTag("onboarding_skip")
                    ) {
                        Text(
                            text = "Skip",
                            color = Color(0xFF666666),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(60.dp))
                }

                // Middle: Animated Dots Indicator
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0..4) {
                        val active = i == currentSlide
                        val width by animateDpAsState(
                            targetValue = if (active) 24.dp else 8.dp,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium),
                            label = "dot_width"
                        )
                        val color by animateColorAsState(
                            targetValue = if (active) Color(0xFFD4AF37) else Color(0xFFE5E5E5),
                            animationSpec = tween(300),
                            label = "dot_color"
                        )

                        Box(
                            modifier = Modifier
                                .size(width = width, height = 8.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }

                // Right Action: Next or Get Started (Premium gold gradient button)
                if (currentSlide < 4) {
                    IconButton(
                        onClick = {
                            autoSlideEnabled = false
                            currentSlide++
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFFD4AF37), Color(0xFFFFE082))
                                ),
                                CircleShape
                            )
                            .shadow(4.dp, CircleShape)
                            .testTag("onboarding_next")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next Slide",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = { onOnboardingFinished() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .widthIn(min = 130.dp)
                            .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = Color(0x66D4AF37))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF2E7D32), Color(0xFF4CAF50)) // Elegant Majestic Green & Gold vibe
                                ),
                                RoundedCornerShape(24.dp)
                            )
                            .testTag("onboarding_finish_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "Get Started",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Helper methods for screen texts
private fun getSlideHeadline(slide: Int): String = when (slide) {
    0 -> "Welcome to Teke Man Promotion"
    1 -> "Earn Amazing Rewards"
    2 -> "Share Happiness"
    3 -> "Unlock Exclusive Promotions"
    else -> "Your Rewards Journey Starts Here"
}

private fun getSlideSubtitle(slide: Int): String = when (slide) {
    0 -> "Discover exclusive gifts, rewards, and exciting promotions designed just for you."
    1 -> "Complete simple tasks, invite friends, and unlock valuable rewards effortlessly."
    2 -> "Send thoughtful gifts and receive exciting surprises from friends and promotions."
    3 -> "Enjoy special offers, discounts, cashback, and limited-time deals crafted for you."
    else -> "Join thousands of happy users and start collecting rewards today."
}

// --- 3D GRAPHICS RENDERERS ON CANVAS ---

@Composable
fun SlideWelcomeGraphics(orbit: Float) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(260.dp)
    ) {
        // Luxury Radial Background Glow
        Box(
            modifier = Modifier
                .size(240.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFFFF9E6).copy(alpha = 0.5f), Color.Transparent),
                    )
                )
        )

        // Rotating golden ribbon arcs around the center
        Canvas(modifier = Modifier.size(220.dp)) {
            rotate(degrees = orbit) {
                // Gold Ribbon Arc 1
                drawArc(
                    color = Color(0xFFD4AF37).copy(alpha = 0.35f),
                    startAngle = 45f,
                    sweepAngle = 90f,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                            floatArrayOf(15f, 10f), 0f
                        )
                    )
                )
                // Gold Ribbon Arc 2
                drawArc(
                    color = Color(0xFFFFE082).copy(alpha = 0.3f),
                    startAngle = 225f,
                    sweepAngle = 70f,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 1.5.dp.toPx()
                    )
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Official Logo floating perfectly centered
            Box(
                modifier = Modifier
                    .size(115.dp)
                    .shadow(16.dp, CircleShape, spotColor = Color(0x40D4AF37))
                    .border(2.dp, Color(0xFFD4AF37), CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_splash_logo),
                    contentDescription = "Teke Man Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Isometric Premium White and Gold Gift Box floating directly below
            Canvas(modifier = Modifier.size(80.dp)) {
                val boxCenter = Offset(size.width / 2f, size.height / 2f)
                val w = 55.dp.toPx()
                val h = 48.dp.toPx()

                // Bottom contact shadow
                drawOval(
                    color = Color(0x1A000000),
                    topLeft = Offset(boxCenter.x - w / 1.5f, boxCenter.y + h / 3f),
                    size = Size(w * 1.3f, 14.dp.toPx())
                )

                // Left face
                val leftFace = Path().apply {
                    moveTo(boxCenter.x, boxCenter.y)
                    lineTo(boxCenter.x - w / 2f, boxCenter.y - h / 4f)
                    lineTo(boxCenter.x - w / 2f, boxCenter.y + h / 2f)
                    lineTo(boxCenter.x, boxCenter.y + h * 0.75f)
                    close()
                }
                drawPath(leftFace, Brush.verticalGradient(listOf(Color(0xFFFAFAFA), Color(0xFFE5E6EA))))

                // Right face
                val rightFace = Path().apply {
                    moveTo(boxCenter.x, boxCenter.y)
                    lineTo(boxCenter.x + w / 2f, boxCenter.y - h / 4f)
                    lineTo(boxCenter.x + w / 2f, boxCenter.y + h / 2f)
                    lineTo(boxCenter.x, boxCenter.y + h * 0.75f)
                    close()
                }
                drawPath(rightFace, Brush.verticalGradient(listOf(Color(0xFFE1E2E6), Color(0xFFC7CBD1))))

                // Lid top
                val lidTop = Path().apply {
                    moveTo(boxCenter.x, boxCenter.y - h / 4.5f)
                    lineTo(boxCenter.x - w / 1.8f, boxCenter.y - h / 2.2f)
                    lineTo(boxCenter.x, boxCenter.y - h * 0.65f)
                    lineTo(boxCenter.x + w / 1.8f, boxCenter.y - h / 2.2f)
                    close()
                }
                drawPath(lidTop, Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFECEFF1))))

                // Gold ribbon wraps
                val ribbonW = 8.dp.toPx()
                val leftRibbon = Path().apply {
                    moveTo(boxCenter.x - w / 4f, boxCenter.y - h / 8f)
                    lineTo(boxCenter.x - w / 4f + ribbonW, boxCenter.y - h / 8f + ribbonW / 4f)
                    lineTo(boxCenter.x - w / 4f + ribbonW, boxCenter.y + h * 0.62f + ribbonW / 4f)
                    lineTo(boxCenter.x - w / 4f, boxCenter.y + h * 0.62f)
                    close()
                }
                drawPath(leftRibbon, Color(0xFFD4AF37))

                val rightRibbon = Path().apply {
                    moveTo(boxCenter.x + w / 4f, boxCenter.y - h / 8f)
                    lineTo(boxCenter.x + w / 4f - ribbonW, boxCenter.y - h / 8f + ribbonW / 4f)
                    lineTo(boxCenter.x + w / 4f - ribbonW, boxCenter.y + h * 0.62f + ribbonW / 4f)
                    lineTo(boxCenter.x + w / 4f, boxCenter.y + h * 0.62f)
                    close()
                }
                drawPath(rightRibbon, Color(0xFFD4AF37))
            }
        }
    }
}

@Composable
fun SlideRewardsGraphics(orbit: Float) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(260.dp)
    ) {
        // Radial Background Glow
        Box(
            modifier = Modifier
                .size(240.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFE8F5E9).copy(alpha = 0.5f), Color.Transparent),
                    )
                )
        )

        // 3D Isometric Reward Card floating, surrounded by spinning gold coins and stars
        Canvas(modifier = Modifier.size(240.dp)) {
            val cx = size.width / 2f
            val cy = size.height / 2f

            // 1. Draw central premium Card with rounded corners, slanted for 3D look
            rotate(degrees = -15f, pivot = Offset(cx, cy)) {
                val cardW = 140.dp.toPx()
                val cardH = 85.dp.toPx()
                
                // Card Drop Shadow
                drawRoundRect(
                    color = Color(0x1F000000),
                    topLeft = Offset(cx - cardW / 2f + 8.dp.toPx(), cy - cardH / 2f + 12.dp.toPx()),
                    size = Size(cardW, cardH),
                    cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
                )

                // Card Body - White & Gold elegant gradient with thin gold border
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFFAFAFA), Color(0xFFECEFF1))
                    ),
                    topLeft = Offset(cx - cardW / 2f, cy - cardH / 2f),
                    size = Size(cardW, cardH),
                    cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
                )

                // Gold Accent Inner Ring
                drawRoundRect(
                    color = Color(0xFFD4AF37),
                    topLeft = Offset(cx - cardW / 2f + 6.dp.toPx(), cy - cardH / 2f + 6.dp.toPx()),
                    size = Size(cardW - 12.dp.toPx(), cardH - 12.dp.toPx()),
                    cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5.dp.toPx())
                )

                // Draw a gold crown/reward emblem inside the card
                drawCircle(
                    color = Color(0xFF4CAF50),
                    radius = 16.dp.toPx(),
                    center = Offset(cx - 30.dp.toPx(), cy)
                )
                drawCircle(
                    color = Color.White,
                    radius = 14.dp.toPx(),
                    center = Offset(cx - 30.dp.toPx(), cy)
                )

                // Text Lines representation
                drawLine(
                    color = Color(0xFF111111),
                    start = Offset(cx + 2.dp.toPx(), cy - 12.dp.toPx()),
                    end = Offset(cx + 45.dp.toPx(), cy - 12.dp.toPx()),
                    strokeWidth = 3.dp.toPx()
                )
                drawLine(
                    color = Color(0xFF666666),
                    start = Offset(cx + 2.dp.toPx(), cy + 2.dp.toPx()),
                    end = Offset(cx + 35.dp.toPx(), cy + 2.dp.toPx()),
                    strokeWidth = 2.dp.toPx()
                )
                drawLine(
                    color = Color(0xFFD4AF37),
                    start = Offset(cx + 2.dp.toPx(), cy + 14.dp.toPx()),
                    end = Offset(cx + 25.dp.toPx(), cy + 14.dp.toPx()),
                    strokeWidth = 2.dp.toPx()
                )
            }

            // 2. Spinning Gold Coins orbiting the card
            val coinRadius = 14.dp.toPx()
            
            // Coin 1 (Top Right)
            val coin1X = cx + 80.dp.toPx() + sin(orbit * PI_F / 180f) * 15.dp.toPx()
            val coin1Y = cy - 60.dp.toPx() + cos(orbit * PI_F / 180f) * 10.dp.toPx()
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFF9C4), Color(0xFFFBC02D), Color(0xFFF57F17))
                ),
                radius = coinRadius,
                center = Offset(coin1X, coin1Y)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.6f),
                radius = coinRadius * 0.7f,
                center = Offset(coin1X, coin1Y),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
            )

            // Coin 2 (Bottom Left)
            val coin2X = cx - 90.dp.toPx() + cos(orbit * PI_F / 180f) * 12.dp.toPx()
            val coin2Y = cy + 50.dp.toPx() + sin(orbit * PI_F / 180f) * 12.dp.toPx()
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFF9C4), Color(0xFFFBC02D), Color(0xFFF57F17))
                ),
                radius = coinRadius * 0.85f,
                center = Offset(coin2X, coin2Y)
            )

            // Coin 3 (Right Center, slightly behind)
            val coin3X = cx + 105.dp.toPx()
            val coin3Y = cy + 15.dp.toPx()
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFF9C4), Color(0xFFFBC02D), Color(0xFFF57F17))
                ),
                radius = coinRadius * 0.7f,
                center = Offset(coin3X, coin3Y)
            )

            // 3. Shimmering Star vectors
            drawStar(cx - 70.dp.toPx(), cy - 50.dp.toPx(), 8.dp.toPx())
            drawStar(cx + 50.dp.toPx(), cy + 60.dp.toPx(), 10.dp.toPx())
        }
    }
}

@Composable
fun SlideGiftsGraphics(orbit: Float) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(260.dp)
    ) {
        // Radial Background Glow
        Box(
            modifier = Modifier
                .size(240.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFFFF3E0).copy(alpha = 0.5f), Color.Transparent),
                    )
                )
        )

        // Opening 3D Gift Box releasing rewards
        Canvas(modifier = Modifier.size(240.dp)) {
            val cx = size.width / 2f
            val cy = size.height / 2f + 24.dp.toPx()
            val w = 70.dp.toPx()
            val h = 60.dp.toPx()

            // 1. Shadow
            drawOval(
                color = Color(0x1F000000),
                topLeft = Offset(cx - w, cy + h / 4f),
                size = Size(w * 2f, 16.dp.toPx())
            )

            // 2. Opened Box Body Front Faces
            // Left Face
            val leftFace = Path().apply {
                moveTo(cx, cy)
                lineTo(cx - w / 2f, cy - h / 4f)
                lineTo(cx - w / 2f, cy + h / 2f)
                lineTo(cx, cy + h * 0.75f)
                close()
            }
            drawPath(leftFace, Brush.verticalGradient(listOf(Color(0xFFFAFAFA), Color(0xFFECEFF1))))

            // Right Face
            val rightFace = Path().apply {
                moveTo(cx, cy)
                lineTo(cx + w / 2f, cy - h / 4f)
                lineTo(cx + w / 2f, cy + h / 2f)
                lineTo(cx, cy + h * 0.75f)
                close()
            }
            drawPath(rightFace, Brush.verticalGradient(listOf(Color(0xFFE1E2E6), Color(0xFFCFD8DC))))

            // 3. Floating lid lifted and tilted upward
            val lidY = cy - 45.dp.toPx()
            val lidH = 15.dp.toPx()
            rotate(degrees = -15f, pivot = Offset(cx, lidY)) {
                // Lid Left
                val lidLeft = Path().apply {
                    moveTo(cx, lidY)
                    lineTo(cx - w / 1.8f, lidY - h / 4f)
                    lineTo(cx - w / 1.8f, lidY - h / 4f + lidH)
                    lineTo(cx, lidY + lidH)
                    close()
                }
                drawPath(lidLeft, Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFECEFF1))))

                // Lid Right
                val lidRight = Path().apply {
                    moveTo(cx, lidY)
                    lineTo(cx + w / 1.8f, lidY - h / 4f)
                    lineTo(cx + w / 1.8f, lidY - h / 4f + lidH)
                    lineTo(cx, lidY + lidH)
                    close()
                }
                drawPath(lidRight, Brush.verticalGradient(listOf(Color(0xFFECEFF1), Color(0xFFCFD8DC))))

                // Lid Top
                val lidTop = Path().apply {
                    moveTo(cx, lidY)
                    lineTo(cx - w / 1.8f, lidY - h / 4f)
                    lineTo(cx, lidY - h / 2f)
                    lineTo(cx + w / 1.8f, lidY - h / 4f)
                    close()
                }
                drawPath(lidTop, Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFECEFF1))))
            }

            // 4. Volumetric Golden Beam rising out of the box
            drawArc(
                color = Color(0xFFFFD700).copy(alpha = 0.25f),
                startAngle = 220f,
                sweepAngle = 100f,
                useCenter = true,
                topLeft = Offset(cx - 30.dp.toPx(), cy - 70.dp.toPx()),
                size = Size(60.dp.toPx(), 70.dp.toPx())
            )

            // 5. Emerging floating particles & reward icons
            drawCircle(color = Color(0xFFE91E63), radius = 4.dp.toPx(), center = Offset(cx - 40.dp.toPx(), cy - 35.dp.toPx())) // Pink gift particle
            drawCircle(color = Color(0xFF4CAF50), radius = 3.dp.toPx(), center = Offset(cx + 35.dp.toPx(), cy - 50.dp.toPx())) // Green particle
            drawCircle(color = Color(0xFF2196F3), radius = 4.5.dp.toPx(), center = Offset(cx - 15.dp.toPx(), cy - 65.dp.toPx())) // Blue card particle
            drawStar(cx + 15.dp.toPx(), cy - 40.dp.toPx(), 6.dp.toPx()) // Sparkle
        }

        // Beautiful physical overlays using standard icons
        Box(
            modifier = Modifier
                .size(240.dp)
                .graphicsLayer { translationY = -35.dp.value }
        ) {
            // Left gift card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .size(width = 50.dp, height = 35.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = 16.dp, y = (-20).dp)
                    .graphicsLayer { rotationZ = -20f }
                    .shadow(4.dp, RoundedCornerShape(8.dp))
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(imageVector = Icons.Default.CardGiftcard, contentDescription = null, tint = Color(0xFFE91E63), modifier = Modifier.size(18.dp))
                }
            }

            // Right heart balloon
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = (-20).dp, y = (-40).dp)
                    .graphicsLayer { rotationZ = 15f }
                    .shadow(4.dp, CircleShape)
                    .background(Color.White, CircleShape)
                    .padding(6.dp)
            ) {
                Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFFF1744), modifier = Modifier.size(16.dp))
            }

            // Center star
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 10.dp)
                    .shadow(4.dp, CircleShape)
                    .background(Color.White, CircleShape)
                    .padding(5.dp)
            ) {
                Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
fun SlidePromoGraphics(orbit: Float, swing: Float) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(260.dp)
    ) {
        // Radial Background Glow
        Box(
            modifier = Modifier
                .size(240.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFE3F2FD).copy(alpha = 0.5f), Color.Transparent),
                    )
                )
        )

        // 3D Promo Board + Swing Tags
        Canvas(modifier = Modifier.size(240.dp)) {
            val cx = size.width / 2f
            val cy = size.height / 2f

            // Sleek white glass card with glowing blue edge
            val cardW = 125.dp.toPx()
            val cardH = 110.dp.toPx()

            // Card Shadow
            drawRoundRect(
                color = Color(0x14000000),
                topLeft = Offset(cx - cardW / 2f + 6.dp.toPx(), cy - cardH / 2f + 10.dp.toPx()),
                size = Size(cardW, cardH),
                cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
            )

            // Card Body
            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFFAFAFA), Color(0xFFF0F4C3))
                ),
                topLeft = Offset(cx - cardW / 2f, cy - cardH / 2f),
                size = Size(cardW, cardH),
                cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
            )

            // Accent Frame
            drawRoundRect(
                color = Color(0xFF4CAF50).copy(alpha = 0.4f),
                topLeft = Offset(cx - cardW / 2f + 4.dp.toPx(), cy - cardH / 2f + 4.dp.toPx()),
                size = Size(cardW - 8.dp.toPx(), cardH - 8.dp.toPx()),
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
            )

            // Decorative lines
            drawLine(
                color = Color(0xFF111111),
                start = Offset(cx - cardW / 2.5f, cy - 30.dp.toPx()),
                end = Offset(cx + cardW / 2.5f, cy - 30.dp.toPx()),
                strokeWidth = 4.dp.toPx()
            )

            // Shopping Bag Representation
            drawRoundRect(
                color = Color(0xFF4CAF50),
                topLeft = Offset(cx - 20.dp.toPx(), cy - 10.dp.toPx()),
                size = Size(40.dp.toPx(), 35.dp.toPx()),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )
            // Bag Handle
            drawArc(
                color = Color(0xFF2E7D32),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(cx - 10.dp.toPx(), cy - 18.dp.toPx()),
                size = Size(20.dp.toPx(), 16.dp.toPx()),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
            )
        }

        // SWINGING DISCOUNT TAGS (Physical swing rotation overlay)
        Box(
            modifier = Modifier
                .size(240.dp)
                .graphicsLayer {
                    translationY = 25.dp.value
                },
            contentAlignment = Alignment.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                // Tag 1
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD4AF37)),
                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp, topStart = 12.dp, topEnd = 12.dp),
                    modifier = Modifier
                        .size(width = 36.dp, height = 55.dp)
                        .graphicsLayer {
                            rotationZ = swing
                            cameraDistance = 8f
                        }
                        .shadow(4.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxSize().padding(vertical = 4.dp)
                    ) {
                        // Punch hole
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color.White, CircleShape)
                        )
                        Text(
                            text = "%",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }

                // Tag 2
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp, topStart = 12.dp, topEnd = 12.dp),
                    modifier = Modifier
                        .size(width = 36.dp, height = 55.dp)
                        .graphicsLayer {
                            rotationZ = -swing
                            cameraDistance = 8f
                        }
                        .shadow(4.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxSize().padding(vertical = 4.dp)
                    ) {
                        // Punch hole
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color.White, CircleShape)
                        )
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SlideStartGraphics(orbit: Float, glow: Float) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(260.dp)
    ) {
        // Spectacular Ambient Glow halo
        Box(
            modifier = Modifier
                .size(230.dp)
                .alpha(glow)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFFFD700).copy(alpha = 0.45f), Color.Transparent),
                    )
                )
        )

        // Cosmic stars revolving around the main center
        Canvas(modifier = Modifier.size(220.dp)) {
            rotate(degrees = orbit * 0.7f) {
                drawStar(size.width * 0.15f, size.height * 0.25f, 6.dp.toPx())
                drawStar(size.width * 0.85f, size.height * 0.75f, 8.dp.toPx())
                drawStar(size.width * 0.75f, size.height * 0.2f, 5.dp.toPx())
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Golden Rim glowing logo frame
            Box(
                modifier = Modifier
                    .size(125.dp)
                    .shadow(20.dp, CircleShape, spotColor = Color(0xFFFFD700))
                    .border(2.5.dp, Color(0xFFD4AF37), CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_splash_logo),
                    contentDescription = "Teke Man Promotion Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Beautiful glowing award box below
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFF4CAF50).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFFD4AF37),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "START COLLECTING NOW",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50),
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

// Simple Helper Extension to draw beautiful multi-point stars on Compose Canvas
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStar(x: Float, y: Float, radius: Float) {
    val path = Path().apply {
        moveTo(x, y - radius)
        lineTo(x + radius * 0.3f, y - radius * 0.3f)
        lineTo(x + radius, y)
        lineTo(x + radius * 0.3f, y + radius * 0.3f)
        lineTo(x, y + radius)
        lineTo(x - radius * 0.3f, y + radius * 0.3f)
        lineTo(x - radius, y)
        lineTo(x - radius * 0.3f, y - radius * 0.3f)
        close()
    }
    drawPath(path, Color(0xFFD4AF37))
}

// Sparkly Onboarding Dust Particles Flow
@Composable
fun OnboardingDustParticles(modifier: Modifier) {
    val transition = rememberInfiniteTransition(label = "onboarding_particles")
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dust_time"
    )

    val particles = remember {
        List(25) {
            ParticleItem(
                x = Random.nextFloat(),
                yOffset = Random.nextFloat(),
                speed = Random.nextFloat() * 0.3f + 0.1f,
                size = Random.nextFloat() * 4.5f + 1.5f,
                alphaFactor = Random.nextFloat() * 0.6f + 0.2f
            )
        }
    }

    Canvas(modifier = modifier) {
        clipRect {
            particles.forEach { p ->
                val currentY = ((p.yOffset + (time * p.speed)) % 1.0f) * size.height
                val currentX = (p.x * size.width + sin(time * PI_F * 2f + p.yOffset) * 12.dp.toPx()) % size.width
                val alpha = (p.alphaFactor + 0.3f * sin(time * PI_F * 2f)).coerceIn(0.1f, 0.9f)

                drawCircle(
                    color = Color(0xFFD4AF37).copy(alpha = alpha),
                    radius = p.size,
                    center = Offset(currentX, currentY)
                )
            }
        }
    }
}

private data class ParticleItem(
    val x: Float,
    val yOffset: Float,
    val speed: Float,
    val size: Float,
    val alphaFactor: Float
)
