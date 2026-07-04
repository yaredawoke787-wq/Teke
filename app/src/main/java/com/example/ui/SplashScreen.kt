package com.example.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class SplashTimeline {
    Start,       // 0.0s - Pure white bg, tiny particles, studio lights
    Ambient,     // 0.3s - Ambient lights glowing
    Particles,   // 0.6s - Shimmering particle flows
    Assemble,    // 1.0s - Logo starts assembling from particles
    Reflect,     // 1.8s - Logo is completed, reflection sweep, golden rim glow
    BoxFadeIn,   // 2.2s - Gift box fades in under the logo
    BoxFloat,    // 2.8s - Breathing animation and float begins
    GiftOpen,    // 3.3s - Ribbon opens, lid tilts and lifts
    GoldenLight, // 3.7s - Warm golden volumetric light cone emerges
    IconsEmerge, // 4.0s - Premium 3D reward icons fly upward
    Settle,      // 4.5s - Animation elements settle
    Finish       // 5.0s - Fade into home screen
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    var timeline by remember { mutableStateOf(SplashTimeline.Start) }

    // Sequential timing sequencer
    LaunchedEffect(Unit) {
        // 0.0s to 0.3s
        delay(300)
        timeline = SplashTimeline.Ambient
        // 0.3s to 0.6s
        delay(300)
        timeline = SplashTimeline.Particles
        // 0.6s to 1.0s
        delay(400)
        timeline = SplashTimeline.Assemble
        // 1.0s to 1.8s
        delay(800)
        timeline = SplashTimeline.Reflect
        // 1.8s to 2.2s
        delay(400)
        timeline = SplashTimeline.BoxFadeIn
        // 2.2s to 2.8s
        delay(600)
        timeline = SplashTimeline.BoxFloat
        // 2.8s to 3.3s
        delay(500)
        timeline = SplashTimeline.GiftOpen
        // 3.3s to 3.7s
        delay(400)
        timeline = SplashTimeline.GoldenLight
        // 3.7s to 4.0s
        delay(300)
        timeline = SplashTimeline.IconsEmerge
        // 4.0s to 4.5s
        delay(500)
        timeline = SplashTimeline.Settle
        // 4.5s to 5.0s
        delay(500)
        timeline = SplashTimeline.Finish
        onSplashFinished()
    }

    // Interactive loops for floating/breathing (60/120 fps physics)
    val infiniteTransition = rememberInfiniteTransition(label = "studio_loops")
    
    val breathingFloat by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_float"
    )

    val boxHover by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "box_float"
    )

    val ribbonWiggle by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ribbon_wiggle"
    )

    // Cinematic Camera Dolly & Perspective Animations
    val cameraScale by animateFloatAsState(
        targetValue = if (timeline >= SplashTimeline.Finish) 1.06f else 1.0f,
        animationSpec = tween(5000, easing = EaseInOutSine),
        label = "camera_scale"
    )

    val cameraRotationY by animateFloatAsState(
        targetValue = if (timeline >= SplashTimeline.Particles) 1.8f else 0f,
        animationSpec = tween(4000, easing = EaseInOutQuad),
        label = "camera_rotation"
    )

    // Logo state animators
    val logoAlpha by animateFloatAsState(
        targetValue = if (timeline >= SplashTimeline.Assemble) 1f else 0f,
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "logo_alpha"
    )

    val logoScale by animateFloatAsState(
        targetValue = when {
            timeline >= SplashTimeline.Reflect -> 1.0f
            timeline >= SplashTimeline.Assemble -> 0.96f
            else -> 0.5f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logo_scale"
    )

    val logoRotationY by animateFloatAsState(
        targetValue = if (timeline >= SplashTimeline.Reflect) 2f else 0f,
        animationSpec = tween(1500, easing = EaseInOutCubic),
        label = "logo_rotation_3d"
    )

    // Reflection Sweep animator (sweeps across the completed logo at 1.8s)
    val reflectionOffset by animateFloatAsState(
        targetValue = if (timeline >= SplashTimeline.Reflect) 1.2f else -0.3f,
        animationSpec = tween(1800, easing = EaseInOutSine),
        label = "reflection_sweep"
    )

    // Gift box fade in
    val giftBoxAlpha by animateFloatAsState(
        targetValue = if (timeline >= SplashTimeline.BoxFadeIn) 1f else 0f,
        animationSpec = tween(800, easing = EaseInOutQuad),
        label = "giftbox_alpha"
    )

    // Gift Opening timelines
    val ribbonOpenProgress by animateFloatAsState(
        targetValue = if (timeline >= SplashTimeline.GiftOpen) 1f else 0f,
        animationSpec = tween(1200, easing = EaseOutBack),
        label = "ribbon_open"
    )

    val lidOpenProgress by animateFloatAsState(
        targetValue = if (timeline >= SplashTimeline.GiftOpen) 1f else 0f,
        animationSpec = tween(1400, easing = EaseInOutCubic),
        label = "lid_open"
    )

    val goldenLightProgress by animateFloatAsState(
        targetValue = if (timeline >= SplashTimeline.GoldenLight) 1f else 0f,
        animationSpec = tween(1000, easing = EaseInOutSine),
        label = "golden_light"
    )

    val iconsEmergeProgress by animateFloatAsState(
        targetValue = if (timeline >= SplashTimeline.IconsEmerge) 1f else 0f,
        animationSpec = tween(1800, easing = EaseOutBack),
        label = "icons_emerge"
    )

    // Master Screen Container
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // 1. Cinematic Studio Background with Soft Radial Light Glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF), // Pure white studio center
                            Color(0xFFF7F8FA), // Volumetric soft lighting edge
                            Color(0xFFEEF0F4)  // Luxury ambient falloff
                        ),
                        center = Offset.Unspecified,
                        radius = 1200f
                    )
                )
        )

        // 2. High-end Cinematic Dust & Gold Particles (Unreal Engine style)
        CinemaGoldDustParticles(modifier = Modifier.fillMaxSize(), timeline = timeline)

        // 3. Translucent Glass Spheres
        FloatingGlassSpheres(modifier = Modifier.fillMaxSize())

        // 4. Virtual Camera Container (Dolly-in + Micro-orbit)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    scaleX = cameraScale
                    scaleY = cameraScale
                    rotationY = cameraRotationY
                    cameraDistance = 18f
                }
        ) {
            // --- TOP LOGO SECTION (Golden Ratio balanced spacing) ---
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .scale(logoScale)
                    .offset(y = breathingFloat.dp)
                    .graphicsLayer {
                        rotationY = logoRotationY
                        cameraDistance = 16f
                    }
                    .size(175.dp)
                    .shadow(
                        elevation = 24.dp,
                        shape = CircleShape,
                        clip = false,
                        spotColor = Color(0x7FD4AF37) // Premium gold drop shadow
                    )
                    .border(2.5.dp, Color(0xFFD4AF37), CircleShape)
                    .padding(5.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .alpha(logoAlpha)
            ) {
                // Official Brand Logo (Exact unmodified aspect ratio & colors)
                Image(
                    painter = painterResource(id = R.drawable.img_splash_logo),
                    contentDescription = "Teke Man Promotion Logo",
                    modifier = Modifier.fillMaxSize()
                )

                // 1.8s Reflection sweep shine effect overlay
                if (timeline >= SplashTimeline.Reflect) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0f),
                                        Color.White.copy(alpha = 0.1f),
                                        Color.White.copy(alpha = 0.5f),
                                        Color.White.copy(alpha = 0.8f),
                                        Color.White.copy(alpha = 0.5f),
                                        Color.White.copy(alpha = 0.1f),
                                        Color.White.copy(alpha = 0f)
                                    ),
                                    start = Offset(reflectionOffset * 350f, 0f),
                                    end = Offset(reflectionOffset * 350f + 100f, 250f)
                                )
                            )
                    )
                }

                // Beautiful outer radial gold bloom glow
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // --- INTERACTIVE PREMIUM 3D GIFT BOX SECTION ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .alpha(giftBoxAlpha)
                    .offset(y = boxHover.dp),
                contentAlignment = Alignment.Center
            ) {
                // Interactive Volumetric Golden Light Beam Emerging from opened box
                if (timeline >= SplashTimeline.GoldenLight) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .graphicsLayer { translationY = -50f }
                    ) {
                        val path = Path().apply {
                            moveTo(size.width / 2f - 40.dp.toPx(), size.height)
                            lineTo(size.width / 2f - 110.dp.toPx(), 0f)
                            lineTo(size.width / 2f + 110.dp.toPx(), 0f)
                            lineTo(size.width / 2f + 40.dp.toPx(), size.height)
                            close()
                        }
                        drawPath(
                            path = path,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFFFD700).copy(alpha = 0.5f * goldenLightProgress),
                                    Color(0xFFFFD700).copy(alpha = 0.18f * goldenLightProgress),
                                    Color.Transparent
                                )
                            )
                        )
                    }
                }

                // Rising Reward Icons (3D Feel, Floating up)
                if (timeline >= SplashTimeline.IconsEmerge) {
                    RewardIconsEmerge(progress = iconsEmergeProgress)
                }

                // Ultra-Realistic Physically-Based Isometric Gift Box with dynamic ribbon and lid
                Canvas(
                    modifier = Modifier
                        .size(150.dp)
                        .graphicsLayer {
                            rotationZ = ribbonWiggle
                        }
                ) {
                    val boxCenter = Offset(size.width / 2f, size.height / 2f + 20.dp.toPx())
                    val w = 85.dp.toPx()
                    val h = 75.dp.toPx()

                    // Draw subtle soft contact shadow on pristine floor
                    drawOval(
                        color = Color(0x1F000000),
                        topLeft = Offset(boxCenter.x - w / 1.3f, boxCenter.y + h / 1.8f),
                        size = Size(w * 1.6f, 24.dp.toPx())
                    )

                    // DRAW BOX BODY (Matte White with professional PBR gradient shading)
                    // Front-Left Face
                    val leftFacePath = Path().apply {
                        moveTo(boxCenter.x, boxCenter.y)
                        lineTo(boxCenter.x - w / 2f, boxCenter.y - h / 4f)
                        lineTo(boxCenter.x - w / 2f, boxCenter.y + h / 2f)
                        lineTo(boxCenter.x, boxCenter.y + h * 0.75f)
                        close()
                    }
                    drawPath(
                        path = leftFacePath,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFFAFAFA), Color(0xFFE5E6EA))
                        )
                    )

                    // Front-Right Face (shading for absolute 3D depth)
                    val rightFacePath = Path().apply {
                        moveTo(boxCenter.x, boxCenter.y)
                        lineTo(boxCenter.x + w / 2f, boxCenter.y - h / 4f)
                        lineTo(boxCenter.x + w / 2f, boxCenter.y + h / 2f)
                        lineTo(boxCenter.x, boxCenter.y + h * 0.75f)
                        close()
                    }
                    drawPath(
                        path = rightFacePath,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFE1E2E6), Color(0xFFC7CBD1))
                        )
                    )

                    // GOLD METALLIC WRAPPING RIBBON (Base Vertical Bands)
                    val ribbonW = 12.dp.toPx()
                    // Left face vertical ribbon band
                    val leftRibbon = Path().apply {
                        moveTo(boxCenter.x - w / 4f, boxCenter.y - h / 8f)
                        lineTo(boxCenter.x - w / 4f + ribbonW, boxCenter.y - h / 8f + ribbonW / 4.0f)
                        lineTo(boxCenter.x - w / 4f + ribbonW, boxCenter.y + h * 0.62f + ribbonW / 4.0f)
                        lineTo(boxCenter.x - w / 4f, boxCenter.y + h * 0.62f)
                        close()
                    }
                    drawPath(
                        path = leftRibbon,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFFFD700), Color(0xFFB8860B))
                        )
                    )

                    // Right face vertical ribbon band
                    val rightRibbon = Path().apply {
                        moveTo(boxCenter.x + w / 4f, boxCenter.y - h / 8f)
                        lineTo(boxCenter.x + w / 4f - ribbonW, boxCenter.y - h / 8f + ribbonW / 4.0f)
                        lineTo(boxCenter.x + w / 4f - ribbonW, boxCenter.y + h * 0.62f + ribbonW / 4.0f)
                        lineTo(boxCenter.x + w / 4f, boxCenter.y + h * 0.62f)
                        close()
                    }
                    drawPath(
                        path = rightRibbon,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFFFD700), Color(0xFFB8860B))
                        )
                    )

                    // DYNAMIC OPENING LID (Moves upward and rotates when lidOpenProgress > 0)
                    val lidOffsetY = -55.dp.toPx() * lidOpenProgress
                    val lidRotateZ = 12f * lidOpenProgress

                    translate(top = lidOffsetY) {
                        rotate(degrees = lidRotateZ, pivot = boxCenter) {
                            val lidY = boxCenter.y - h / 4.5f
                            val lidH = 18.dp.toPx()

                            // Lid Left Face
                            val lidLeftPath = Path().apply {
                                moveTo(boxCenter.x, lidY)
                                lineTo(boxCenter.x - w / 1.9f, lidY - h / 4f)
                                lineTo(boxCenter.x - w / 1.9f, lidY - h / 4f + lidH)
                                lineTo(boxCenter.x, lidY + lidH)
                                close()
                            }
                            drawPath(
                                path = lidLeftPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFFFFFFFF), Color(0xFFECEFF1))
                                )
                            )

                            // Lid Right Face
                            val lidRightPath = Path().apply {
                                moveTo(boxCenter.x, lidY)
                                lineTo(boxCenter.x + w / 1.9f, lidY - h / 4f)
                                lineTo(boxCenter.x + w / 1.9f, lidY - h / 4f + lidH)
                                lineTo(boxCenter.x, lidY + lidH)
                                close()
                            }
                            drawPath(
                                path = lidRightPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFFECEFF1), Color(0xFFCFD8DC))
                                )
                            )

                            // Lid Top Face
                            val lidTopPath = Path().apply {
                                moveTo(boxCenter.x, lidY)
                                lineTo(boxCenter.x - w / 1.9f, lidY - h / 4f)
                                lineTo(boxCenter.x, lidY - h / 2f)
                                lineTo(boxCenter.x + w / 1.9f, lidY - h / 4f)
                                close()
                            }
                            drawPath(
                                path = lidTopPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFFFFFFFF), Color(0xFFECEFF1))
                                )
                            )

                            // Lid Gold Ribbon Wrap (Top & Sides)
                            val lidRibbonLeft = Path().apply {
                                moveTo(boxCenter.x - w / 4f, lidY - h / 8f)
                                lineTo(boxCenter.x - w / 4f + ribbonW, lidY - h / 8f + ribbonW / 4.0f)
                                lineTo(boxCenter.x - w / 4f + ribbonW, lidY + lidH + ribbonW / 4.0f)
                                lineTo(boxCenter.x - w / 4f, lidY + lidH)
                                close()
                            }
                            drawPath(
                                path = lidRibbonLeft,
                                color = Color(0xFFD4AF37)
                            )

                            val lidRibbonRight = Path().apply {
                                moveTo(boxCenter.x + w / 4f, lidY - h / 8f)
                                lineTo(boxCenter.x + w / 4f - ribbonW, lidY - h / 8f + ribbonW / 4.0f)
                                lineTo(boxCenter.x + w / 4f - ribbonW, lidY + lidH + ribbonW / 4.0f)
                                lineTo(boxCenter.x + w / 4f, lidY + lidH)
                                close()
                            }
                            drawPath(
                                path = lidRibbonRight,
                                color = Color(0xFFD4AF37)
                            )

                            // UNTYING METALLIC GOLDEN BOW/RIBBON KNOT ON TOP
                            if (ribbonOpenProgress < 1f) {
                                val bowScale = 1f - ribbonOpenProgress
                                translate(
                                    left = boxCenter.x - 12.dp.toPx(),
                                    top = lidY - h / 2.5f
                                ) {
                                    // Left bow loop
                                    drawOval(
                                        color = Color(0xFFD4AF37),
                                        topLeft = Offset(-10.dp.toPx() * bowScale, -5.dp.toPx()),
                                        size = Size(20.dp.toPx() * bowScale, 12.dp.toPx()),
                                        alpha = 1f - ribbonOpenProgress
                                    )
                                    // Right bow loop
                                    drawOval(
                                        color = Color(0xFFD4AF37),
                                        topLeft = Offset(10.dp.toPx() * bowScale, -5.dp.toPx()),
                                        size = Size(20.dp.toPx() * bowScale, 12.dp.toPx()),
                                        alpha = 1f - ribbonOpenProgress
                                    )
                                    // Ribbon ties hanging down
                                    drawCircle(
                                        color = Color(0xFFB8860B),
                                        radius = 5.dp.toPx() * bowScale
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- PREMIUM TYPOGRAPHY SECTION ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                // Main Header "Teke Man Promotion" (High contrast luxury style)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TEKE MAN ",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Black,
                        fontSize = 26.sp,
                        color = Color(0xFF111111), // Midnight Black
                        letterSpacing = 4.sp
                    )
                    Text(
                        text = "PROMOTION",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Black,
                        fontSize = 26.sp,
                        color = Color(0xFF4CAF50), // Majestic Brand Green as requested
                        letterSpacing = 4.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Elegant luxury tagline
                Text(
                    text = "GIFT • REWARD • CELEBRATE",
                    fontSize = 11.sp,
                    color = Color(0xFF757575), // Silver Charcoal Grey
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 3.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// Sparkly Cinematically Glowing Dust Particles with Physics-based speeds
@Composable
fun CinemaGoldDustParticles(modifier: Modifier, timeline: SplashTimeline) {
    val transition = rememberInfiniteTransition(label = "particles")
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_time"
    )

    val particleCount = if (timeline >= SplashTimeline.Particles) 45 else 20
    val particles = remember {
        List(particleCount) {
            ParticleData(
                x = Random.nextFloat(),
                yOffset = Random.nextFloat(),
                speed = Random.nextFloat() * 0.35f + 0.1f,
                size = Random.nextFloat() * 5f + 2f,
                alphaSpeed = Random.nextFloat() * 1.5f + 0.5f
            )
        }
    }

    Canvas(modifier = modifier) {
        clipRect {
            particles.forEach { p ->
                // Calculate fluid flow of dust
                val currentY = ((p.yOffset + (time * p.speed)) % 1.0f) * size.height
                val currentX = (p.x * size.width + kotlin.math.sin(time * Math.PI * 2 + p.yOffset).toFloat() * 15.dp.toPx()) % size.width
                
                // Breathing shimmering opacity
                val alpha = (0.2f + 0.6f * kotlin.math.sin((time * p.alphaSpeed * Math.PI * 2).toFloat())).coerceIn(0.1f, 0.9f)
                
                drawCircle(
                    color = Color(0xFFD4AF37).copy(alpha = alpha), // True Luxury Gold Particles
                    radius = p.size,
                    center = Offset(currentX, currentY)
                )
            }
        }
    }
}

// Translucent Floating Glass Spheres for high-end Depth of Field look
@Composable
fun FloatingGlassSpheres(modifier: Modifier) {
    val transition = rememberInfiniteTransition(label = "glass")
    val floatAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "glass_orbit"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        
        // Sphere 1 (Top Left)
        val s1X = w * 0.12f + kotlin.math.sin(floatAnim * Math.PI * 2).toFloat() * 14.dp.toPx()
        val s1Y = h * 0.25f + kotlin.math.cos(floatAnim * Math.PI * 2).toFloat() * 18.dp.toPx()
        drawCircle(
            color = Color.White.copy(alpha = 0.14f),
            radius = 18.dp.toPx(),
            center = Offset(s1X, s1Y)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.45f), Color.Transparent),
                center = Offset(s1X - 6.dp.toPx(), s1Y - 6.dp.toPx()),
                radius = 9.dp.toPx()
            ),
            radius = 18.dp.toPx(),
            center = Offset(s1X, s1Y)
        )

        // Sphere 2 (Bottom Right)
        val s2X = w * 0.85f + kotlin.math.cos(floatAnim * Math.PI * 2).toFloat() * 12.dp.toPx()
        val s2Y = h * 0.55f + kotlin.math.sin(floatAnim * Math.PI * 2).toFloat() * 15.dp.toPx()
        drawCircle(
            color = Color.White.copy(alpha = 0.11f),
            radius = 22.dp.toPx(),
            center = Offset(s2X, s2Y)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.38f), Color.Transparent),
                center = Offset(s2X - 7.dp.toPx(), s2Y - 7.dp.toPx()),
                radius = 11.dp.toPx()
            ),
            radius = 22.dp.toPx(),
            center = Offset(s2X, s2Y)
        )
    }
}

// Emerging Premium 3D Floating Reward Icons
@Composable
fun BoxScope.RewardIconsEmerge(progress: Float) {
    val items = remember {
        listOf(
            RewardItem(Icons.Default.CardGiftcard, Color(0xFFE91E63), -80, -210, 15f), // Gift
            RewardItem(Icons.Default.ConfirmationNumber, Color(0xFF009688), 75, -170, -20f), // Voucher
            RewardItem(Icons.Default.Star, Color(0xFFFFC107), -40, -250, 25f), // Reward Star
            RewardItem(Icons.Default.LocalOffer, Color(0xFF4CAF50), 90, -230, -15f), // Discount Tag
            RewardItem(Icons.Default.ShoppingBag, Color(0xFF3F51B5), -95, -150, -30f), // Shopping Bag
            RewardItem(Icons.Default.ReceiptLong, Color(0xFFFF9800), 45, -200, 10f), // Coupon
            RewardItem(Icons.Default.MonetizationOn, Color(0xFFFFD700), -20, -180, 5f), // Coins
            RewardItem(Icons.Default.Savings, Color(0xFFE91E63), 85, -130, 22f), // Cashback
            RewardItem(Icons.Default.CreditCard, Color(0xFF2196F3), -65, -120, -10f), // Gift Card
            RewardItem(Icons.Default.AutoAwesome, Color(0xFFFFE082), 15, -240, 18f), // Celebration Stars
            RewardItem(Icons.Default.Redeem, Color(0xFFD32F2F), -115, -190, -12f), // Ribbon Gift
            RewardItem(Icons.Default.Favorite, Color(0xFFFF1744), 115, -180, 35f) // Hearts
        )
    }

    items.forEach { item ->
        val xOffset = item.targetX * progress
        val yOffset = item.targetY * progress
        val scale = progress.coerceIn(0.2f, 1.0f)
        val alpha = if (progress < 0.2f) progress * 5f else (1.0f - progress).coerceIn(0f, 1.0f)

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = xOffset.dp, y = (yOffset - 30).dp)
                .scale(scale)
                .alpha(alpha)
                .shadow(4.dp, CircleShape)
                .background(Color.White, CircleShape)
                .border(1.dp, item.color.copy(alpha = 0.5f), CircleShape)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = item.color,
                modifier = Modifier
                    .size(18.dp)
                    .graphicsLayer {
                        rotationZ = item.rotate * progress
                    }
            )
        }
    }
}

private data class ParticleData(
    val x: Float,
    val yOffset: Float,
    val speed: Float,
    val size: Float,
    val alphaSpeed: Float
)

private data class RewardItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color,
    val targetX: Int,
    val targetY: Int,
    val rotate: Float
)
