package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random

// --- Luxury Glassmorphic & Frosted Glass Container ---
@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 8.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.red < 0.2f
    
    // Smooth dynamic glass values based on active mode
    val backgroundColor = if (isDark) {
        Color(0x0DFFFFFF) // Exact 5% white tint (bg-white/5) on dark velvet
    } else {
        Color(0x75FFFFFF) // Stronger white tint on warm ivory
    }
    
    val borderColor = if (isDark) {
        Brush.linearGradient(
            listOf(
                Color(0x22FFFFFF),
                Color(0x05FFFFFF),
                Color(0x10D4AF37) // Rich gold reflection on borders
            )
        )
    } else {
        Brush.linearGradient(
            listOf(
                Color(0x44FFFFFF),
                Color(0x221C1C1E),
                Color(0x33D4AF37)
            )
        )
    }

    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(cornerRadius),
                clip = false,
                ambientColor = Color(0x20000000),
                spotColor = if (isDark) Color(0x33D4AF37) else Color(0x15000000)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
    ) {
        // Blur background simulated layer underneath
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(30.dp, edgeTreatment = androidx.compose.ui.draw.BlurredEdgeTreatment.Unbounded)
        )
        Box(
            modifier = Modifier.padding(1.dp),
            content = content
        )
    }
}

// --- Gold Brushed Premium Button ---
@Composable
fun LuxuryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    // Elite touch scaling physics
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "button_scale"
    )

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFD4AF37), // Metallic Royal Gold
            Color(0xFFF3E5AB), // Warm Champagne Glow
            Color(0xFF996515)  // Burnished Gold
        ),
        start = Offset(0f, 0f),
        end = Offset(400f, 400f)
    )

    Box(
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .shadow(
                elevation = if (isPressed) 2.dp else 10.dp,
                shape = RoundedCornerShape(30.dp),
                clip = false,
                spotColor = Color(0xFFD4AF37)
            )
            .clip(RoundedCornerShape(30.dp))
            .background(if (enabled) gradient else Brush.linearGradient(listOf(Color.Gray, Color.Gray)))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = Color(0x55FFFFFF)),
                enabled = enabled,
                onClick = onClick
            )
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val changes = event.changes
                        if (changes.any { it.pressed }) {
                            isPressed = true
                        } else if (changes.any { !it.pressed }) {
                            isPressed = false
                        }
                    }
                }
            }
            .padding(vertical = 14.dp, horizontal = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            content = content
        )
    }
}

// --- Sparkling Particle Glow System ---
@Composable
fun ParticleRain(
    modifier: Modifier = Modifier,
    particleColor: Color = Color(0xAAFFDF00), // Gold dust
    maxParticles: Int = 30
) {
    val transition = rememberInfiniteTransition(label = "particles")
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    // Remember random particles static points so recomposition remains efficient at 60/120 FPS
    val particles = remember {
        List(maxParticles) {
            ParticleData(
                x = Random.nextFloat(),
                yOffset = Random.nextFloat(),
                speed = Random.nextFloat() * 0.4f + 0.1f,
                size = Random.nextFloat() * 4f + 2f,
                alphaSpeed = Random.nextFloat() * 2f + 1f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        clipRect {
            particles.forEach { p ->
                val currentY = ((p.yOffset + (time * p.speed)) % 1.0f) * size.height
                val currentX = p.x * size.width
                
                // Subtle alpha breathing
                val alpha = (0.3f + 0.5f * kotlin.math.sin((time * p.alphaSpeed * Math.PI * 2).toFloat())).coerceIn(0.1f, 0.9f)
                
                drawCircle(
                    color = particleColor.copy(alpha = alpha),
                    radius = p.size,
                    center = Offset(currentX, currentY)
                )
            }
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
