package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.ripple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.R
import com.example.data.Category
import com.example.data.CategoryItem
import com.example.data.LocalizationManager
import com.example.data.Product
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.LuxuryButton
import com.example.ui.components.ParticleRain
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: GiftViewModel) {
    val currentTab by viewModel.currentTabFlow.collectAsStateWithLifecycle()
    val isDark by viewModel.isDarkModeFlow.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQueryFlow.collectAsStateWithLifecycle()
    val selectedProduct by viewModel.selectedProductFlow.collectAsStateWithLifecycle()
    val confirmProduct by viewModel.confirmPurchaseProductFlow.collectAsStateWithLifecycle()
    val showAssistant by viewModel.showAssistantSheetFlow.collectAsStateWithLifecycle()
    val isListening by viewModel.isVoiceListeningFlow.collectAsStateWithLifecycle()
    val favoriteCount by viewModel.favoriteProducts.map { it.size }.collectAsStateWithLifecycle(0)
    val cartCount by viewModel.cartProducts.map { it.size }.collectAsStateWithLifecycle(0)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize().testTag("main_screen"),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            // World-Class Floating Glass Bottom Navigation Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    cornerRadius = 32.dp,
                    elevation = 16.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomNavItem(
                            iconActive = Icons.Filled.Home,
                            iconInactive = Icons.Outlined.Home,
                            label = LocalizationManager.string("nav_home"),
                            isSelected = currentTab == "Home",
                            testTag = "tab_home",
                            onClick = { viewModel.changeTab("Home") }
                        )
                        BottomNavItem(
                            iconActive = Icons.Filled.Category,
                            iconInactive = Icons.Outlined.Category,
                            label = LocalizationManager.string("nav_categories"),
                            isSelected = currentTab == "Categories",
                            testTag = "tab_categories",
                            onClick = { viewModel.changeTab("Categories") }
                        )
                        BottomNavItem(
                            iconActive = Icons.Filled.Favorite,
                            iconInactive = Icons.Outlined.FavoriteBorder,
                            label = LocalizationManager.string("nav_favorites"),
                            isSelected = currentTab == "Favorites",
                            badgeCount = favoriteCount,
                            testTag = "tab_favorites",
                            onClick = { viewModel.changeTab("Favorites") }
                        )
                        BottomNavItem(
                            iconActive = Icons.Filled.ShoppingCart,
                            iconInactive = Icons.Outlined.ShoppingCart,
                            label = LocalizationManager.string("nav_cart"),
                            isSelected = currentTab == "Cart",
                            badgeCount = cartCount,
                            testTag = "tab_cart",
                            onClick = { viewModel.changeTab("Cart") }
                        )
                        BottomNavItem(
                            iconActive = Icons.Filled.Person,
                            iconInactive = Icons.Outlined.Person,
                            label = LocalizationManager.string("nav_profile"),
                            isSelected = currentTab == "Profile",
                            testTag = "tab_profile",
                            onClick = { viewModel.changeTab("Profile") }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // Animated Page Swapper
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Immersive Ambient Glows
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (isDark) {
                    // Top-right golden blur: center at (width * 0.95, height * -0.05)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x1AD4AF37), Color.Transparent),
                            center = Offset(size.width * 0.95f, size.height * -0.05f),
                            radius = size.width * 0.75f
                        ),
                        center = Offset(size.width * 0.95f, size.height * -0.05f),
                        radius = size.width * 0.75f
                    )
                    // Bottom-left sage-gold blur: center at (width * -0.05, height * 0.9)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x135A5A40), Color.Transparent),
                            center = Offset(size.width * -0.05f, size.height * 0.9f),
                            radius = size.width * 0.85f
                        ),
                        center = Offset(size.width * -0.05f, size.height * 0.9f),
                        radius = size.width * 0.85f
                    )
                } else {
                    // Subtle elegant glow for light mode
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x0CD4AF37), Color.Transparent),
                            center = Offset(size.width * 0.95f, size.height * -0.05f),
                            radius = size.width * 0.75f
                        ),
                        center = Offset(size.width * 0.95f, size.height * -0.05f),
                        radius = size.width * 0.75f
                    )
                }
            }

            // Elegant Background Sparkles
            ParticleRain(
                modifier = Modifier.fillMaxSize(),
                particleColor = if (isDark) Color(0x18D4AF37) else Color(0x108C6239),
                maxParticles = 20
            )

            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
                },
                label = "tab_fade"
            ) { tab ->
                when (tab) {
                    "Home" -> HomeScreenLayout(viewModel)
                    "Categories" -> CategoriesScreenLayout(viewModel)
                    "Favorites" -> FavoritesScreenLayout(viewModel)
                    "Cart" -> CartScreenLayout(viewModel)
                    "Profile" -> ProfileScreenLayout(viewModel)
                }
            }

            // Voice Listening Full-Screen Overlay
            if (isListening) {
                VoiceListeningOverlay {
                    viewModel.updateSearchQuery("") // cancel trigger
                }
            }
        }
    }

    // --- Product Details Slide-up Sheet ---
    if (selectedProduct != null) {
        ProductDetailSheet(
            product = selectedProduct!!,
            onClose = { viewModel.viewProductDetails(null) },
            onFavoriteToggle = { viewModel.toggleFavorite(selectedProduct!!.id) },
            onAddToCart = { prod, qty ->
                viewModel.addToCart(prod.id, qty)
                Toast.makeText(context, "${prod.localizedTitle} added to cart", Toast.LENGTH_SHORT).show()
                viewModel.viewProductDetails(null)
            },
            onBuyNow = { prod ->
                viewModel.initiatePurchase(prod)
            },
            isFavoriteFlow = viewModel.favoriteProducts.map { list -> list.any { it.id == selectedProduct!!.id } }
        )
    }

    // --- Direct Purchase Phone Dialog ---
    if (confirmProduct != null) {
        ConfirmPurchaseDialog(
            product = confirmProduct!!,
            onDismiss = { viewModel.initiatePurchase(null) },
            onConfirm = {
                viewModel.initiatePurchase(null)
                // Execute premium dial trigger
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:+251921935862")
                }
                context.startActivity(dialIntent)
            }
        )
    }


}

// --- Dynamic Tab Bottom Item Component ---
@Composable
fun BottomNavItem(
    iconActive: androidx.compose.ui.graphics.vector.ImageVector,
    iconInactive: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    badgeCount: Int = 0,
    testTag: String,
    onClick: () -> Unit
) {
    val duration = 350
    val activeScale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1.0f,
        animationSpec = tween(duration),
        label = "tab_scale"
    )
    val activeGlowAlpha by animateFloatAsState(
        targetValue = if (isSelected) 0.15f else 0f,
        animationSpec = tween(duration),
        label = "tab_glow"
    )

    Box(
        modifier = Modifier
            .testTag(testTag)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .scale(activeScale)
                    .graphicsLayer {
                        shadowElevation = if (isSelected) 4f else 0f
                    }
                    .background(
                        color = Color(0xFFD4AF37).copy(alpha = activeGlowAlpha),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (isSelected) iconActive else iconInactive,
                    contentDescription = label,
                    tint = if (isSelected) Color(0xFFD4AF37) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(24.dp)
                )

                if (badgeCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 6.dp, y = (-6).dp)
                            .background(Color(0xFFC62828), CircleShape)
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badgeCount.toString(),
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                color = if (isSelected) Color(0xFFD4AF37) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )

            // Premium Gold Indicator Under Selected Item (from Immersive UI template)
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(16.dp)
                        .height(3.dp)
                        .background(Color(0xFFD4AF37), RoundedCornerShape(1.5.dp))
                        .shadow(elevation = 6.dp, shape = RoundedCornerShape(1.5.dp), spotColor = Color(0xFFD4AF37))
                )
            }
        }
    }
}

// --- Home Screen Layout ---
@Composable
fun HomeScreenLayout(viewModel: GiftViewModel) {
    val searchQuery by viewModel.searchQueryFlow.collectAsStateWithLifecycle()
    val products by viewModel.searchedProducts.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategoryFlow.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Top luxury greeting and voice search
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Teke ",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "Gift",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF388E3C), // Beautiful premium Green
                            letterSpacing = (-0.5).sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val context = LocalContext.current
                    // Frost glass notification bell
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0x0DFFFFFF), CircleShape)
                            .border(1.dp, Color(0x1AFFFFFF), CircleShape)
                            .clickable {
                                Toast.makeText(context, "No new notifications", Toast.LENGTH_SHORT).show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔔", fontSize = 18.sp)
                    }

                    // Luxury Gold Gradient Avatar
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .border(2.dp, Color(0xFFD4AF37), CircleShape)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFFD4AF37), Color(0xFFF5E6BE))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AT",
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Search Bar Area
        item {
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = {
                    Text(
                        text = LocalizationManager.string("search_hint"),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFFD4AF37)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { viewModel.startVoiceSearch() },
                            modifier = Modifier.testTag("voice_search_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice Search",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .testTag("search_input"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0x0DFFFFFF),
                    unfocusedContainerColor = Color(0x0DFFFFFF),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { /* Done */ })
            )
        }

        // Hero 3D Perspective Infinite Slide Section
        item {
            val bannerImages = remember {
                listOf(
                    "https://images.unsplash.com/photo-1549465220-1a8b9238cd48?q=80&w=1200&auto=format&fit=crop",
                    "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?q=80&w=1200&auto=format&fit=crop",
                    "https://images.unsplash.com/photo-1518199266791-5375a83190b7?q=80&w=1200&auto=format&fit=crop",
                    "https://images.unsplash.com/photo-1522312346375-d1a52e2b99b3?q=80&w=1200&auto=format&fit=crop",
                    "https://images.unsplash.com/photo-1576092768241-dec231879fc3?q=80&w=1200&auto=format&fit=crop"
                )
            }
            val bannerTitles = remember {
                listOf(
                    "Signature Handcrafted Chests",
                    "Majestic Floral Designs",
                    "Crimson Rose & Chocolate Box",
                    "Gold Chronograph Watches",
                    "Gilded Royal Porcelain Set"
                )
            }
            val bannerDescs = remember {
                listOf(
                    "Exquisite handcrafted obsidian wood packaging.",
                    "Bespoke arrangements with imported rare orchids.",
                    "Preserved luxury red roses with 24K gold truffles.",
                    "Masterful luxury timepieces and executive pens.",
                    "Fine bone china delicately detailed in gold."
                )
            }

            var currentSlideIndex by remember { mutableStateOf(0) }

            LaunchedEffect(Unit) {
                while (true) {
                    kotlinx.coroutines.delay(4000)
                    currentSlideIndex = (currentSlideIndex + 1) % bannerImages.size
                }
            }

            val rotationY3D by animateFloatAsState(
                targetValue = -3f + (kotlin.math.sin(currentSlideIndex.toFloat() * 1.5f) * 2f),
                animationSpec = tween(1200, easing = FastOutSlowInEasing),
                label = "3DRotation"
            )
            val scaleBanner by animateFloatAsState(
                targetValue = 1.0f + (kotlin.math.cos(currentSlideIndex.toFloat() * 1.5f) * 0.02f),
                animationSpec = tween(1200, easing = FastOutSlowInEasing),
                label = "3DScale"
            )

            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .graphicsLayer {
                        rotationY = rotationY3D
                        scaleX = scaleBanner
                        scaleY = scaleBanner
                        cameraDistance = 12f
                    }
                    .shadow(12.dp, RoundedCornerShape(24.dp))
                    .testTag("hero_banner"),
            ) {
                Crossfade(
                    targetState = currentSlideIndex,
                    animationSpec = tween(1000),
                    label = "BannerSlide"
                ) { index ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = bannerImages[index],
                            contentDescription = "Promo Banner",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.img_luxury_hero_banner),
                            error = painterResource(id = R.drawable.img_luxury_hero_banner)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color(0xE6000000))
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(20.dp)
                        ) {
                            Text(
                                text = bannerTitles[index],
                                style = MaterialTheme.typography.titleLarge,
                                color = Color(0xFFD4AF37),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = bannerDescs[index],
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // Shop by Occasion Categories Row
        item {
            Column {
                Text(
                    text = LocalizationManager.string("categories_title"),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFD4AF37),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(Category.list) { cat ->
                        CategoryChip(
                            category = cat,
                            isSelected = selectedCategory == cat.id,
                            onClick = {
                                if (selectedCategory == cat.id) {
                                    viewModel.selectCategory(null)
                                } else {
                                    viewModel.selectCategory(cat.id)
                                }
                            }
                        )
                    }
                }
            }
        }

        // Luxury Product Catalog
        item {
            Text(
                text = if (selectedCategory == null || selectedCategory == "All") LocalizationManager.string("all_products") else "${LocalizationManager.string("trending_products")} (${selectedCategory})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Render filtered catalog
        val filteredList = if (selectedCategory == null || selectedCategory == "All") {
            products
        } else {
            products.filter { it.category == selectedCategory }
        }

        if (filteredList.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = "Not Found",
                        tint = Color(0xFFD4AF37).copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = LocalizationManager.string("empty_search"),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            items(filteredList.chunked(2)) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        ProductCard(
                            product = pair[0],
                            onClick = { viewModel.viewProductDetails(pair[0]) },
                            onFavoriteToggle = { viewModel.toggleFavorite(pair[0].id) },
                            isFavoriteFlow = viewModel.favoriteProducts.map { list -> list.any { it.id == pair[0].id } }
                        )
                    }
                    if (pair.size > 1) {
                        Box(modifier = Modifier.weight(1f)) {
                            ProductCard(
                                product = pair[1],
                                onClick = { viewModel.viewProductDetails(pair[1]) },
                                onFavoriteToggle = { viewModel.toggleFavorite(pair[1].id) },
                                isFavoriteFlow = viewModel.favoriteProducts.map { list -> list.any { it.id == pair[1].id } }
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Safety margin spacer
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// --- Elastic Category Chip ---
@Composable
fun CategoryChip(
    category: CategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "chip_scale"
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .background(
                color = if (isSelected) Color(0xFFD4AF37) else Color(0x0DFFFFFF),
                shape = RoundedCornerShape(14.dp)
            )
            .border(
                1.dp,
                if (isSelected) Color(0xFFD4AF37) else Color(0x1AFFFFFF),
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 10.dp)
            .testTag("category_chip_${category.id}"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = category.localizedLabel,
            color = if (isSelected) Color(0xFF0A0A0A) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

// --- Luxury Glassmorphic Product Card ---
@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    isFavoriteFlow: Flow<Boolean>
) {
    val isFavorite by isFavoriteFlow.collectAsStateWithLifecycle(false)

    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("product_card_${product.id}"),
        cornerRadius = 20.dp,
        elevation = 6.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Product image + favorite icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                if (!product.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.localizedTitle,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.img_luxury_gift_logo),
                        error = painterResource(id = R.drawable.img_luxury_gift_logo)
                    )
                } else {
                    Image(
                        painter = painterResource(id = product.imageRes),
                        contentDescription = product.localizedTitle,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Favorite Button Overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(36.dp)
                        .background(Color(0x7F000000), CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false),
                            onClick = onFavoriteToggle
                        )
                        .testTag("fav_btn_${product.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color(0xFFC62828) else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Discount/Luxury Badge
                if (product.isTrending) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(10.dp)
                            .background(
                                Brush.horizontalGradient(listOf(Color(0xFFD4AF37), Color(0xFF996515))),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "VIP CHOICE",
                            color = Color.Black,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Text info
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.localizedTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "%,.0f ETB".format(product.price),
                        color = Color(0xFFD4AF37),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFD4AF37),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = product.rating.toString(),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

// --- Categories Tab Layout ---
@Composable
fun CategoriesScreenLayout(viewModel: GiftViewModel) {
    val selectedCategory by viewModel.selectedCategoryFlow.collectAsStateWithLifecycle()
    val products by viewModel.searchedProducts.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = LocalizationManager.string("nav_categories"),
            style = MaterialTheme.typography.displayMedium,
            color = Color(0xFFD4AF37),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Occasions horizontal list
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            items(Category.list) { cat ->
                CategoryChip(
                    category = cat,
                    isSelected = selectedCategory == cat.id,
                    onClick = {
                        if (selectedCategory == cat.id) {
                            viewModel.selectCategory(null)
                        } else {
                            viewModel.selectCategory(cat.id)
                        }
                    }
                )
            }
        }

        // Vertical scrollable catalog list
        val filteredList = if (selectedCategory == null || selectedCategory == "All") {
            products
        } else {
            products.filter { it.category == selectedCategory }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(filteredList) { prod ->
                ProductCard(
                    product = prod,
                    onClick = { viewModel.viewProductDetails(prod) },
                    onFavoriteToggle = { viewModel.toggleFavorite(prod.id) },
                    isFavoriteFlow = viewModel.favoriteProducts.map { list -> list.any { it.id == prod.id } }
                )
            }
        }

        Spacer(modifier = Modifier.height(86.dp))
    }
}

// --- Favorites Tab Layout ---
@Composable
fun FavoritesScreenLayout(viewModel: GiftViewModel) {
    val favoriteList by viewModel.favoriteProducts.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = LocalizationManager.string("nav_favorites"),
            style = MaterialTheme.typography.displayMedium,
            color = Color(0xFFD4AF37),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = LocalizationManager.string("favorites_count").format(favoriteList.size),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        if (favoriteList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Empty Wishlist",
                    tint = Color(0xFFD4AF37).copy(alpha = 0.2f),
                    modifier = Modifier.size(96.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = LocalizationManager.string("empty_favorites"),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = LocalizationManager.string("empty_favorites_desc"),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(favoriteList) { prod ->
                    ProductCard(
                        product = prod,
                        onClick = { viewModel.viewProductDetails(prod) },
                        onFavoriteToggle = { viewModel.toggleFavorite(prod.id) },
                        isFavoriteFlow = flowOf(true)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(86.dp))
    }
}

// --- Cart Tab Layout ---
@Composable
fun CartScreenLayout(viewModel: GiftViewModel) {
    val cartList by viewModel.cartProducts.collectAsStateWithLifecycle()
    val totalSum by viewModel.cartTotalValue.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = LocalizationManager.string("nav_cart"),
            style = MaterialTheme.typography.displayMedium,
            color = Color(0xFFD4AF37),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        if (cartList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "Empty Bag",
                    tint = Color(0xFFD4AF37).copy(alpha = 0.2f),
                    modifier = Modifier.size(96.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = LocalizationManager.string("empty_cart"),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = LocalizationManager.string("empty_cart_desc"),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(cartList) { cartItem ->
                    GlassmorphicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("cart_item_${cartItem.product.id}"),
                        cornerRadius = 20.dp,
                        elevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Product image
                            if (!cartItem.product.imageUrl.isNullOrBlank()) {
                                AsyncImage(
                                    model = cartItem.product.imageUrl,
                                    contentDescription = cartItem.product.localizedTitle,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(id = R.drawable.img_luxury_gift_logo),
                                    error = painterResource(id = R.drawable.img_luxury_gift_logo)
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = cartItem.product.imageRes),
                                    contentDescription = cartItem.product.localizedTitle,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Product details
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cartItem.product.localizedTitle,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "%,.0f ETB".format(cartItem.product.price),
                                    fontSize = 13.sp,
                                    color = Color(0xFFD4AF37),
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Quantity Controls
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(cartItem.product.id, cartItem.quantity - 1) },
                                        modifier = Modifier.size(28.dp).background(MaterialTheme.colorScheme.surface, CircleShape)
                                    ) {
                                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Minus", modifier = Modifier.size(14.dp))
                                    }

                                    Text(
                                        text = cartItem.quantity.toString(),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(cartItem.product.id, cartItem.quantity + 1) },
                                        modifier = Modifier.size(28.dp).background(MaterialTheme.colorScheme.surface, CircleShape)
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(14.dp))
                                    }
                                }
                            }

                            // Delete button
                            IconButton(onClick = { viewModel.removeFromCart(cartItem.product.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove",
                                    tint = Color(0xFFC62828).copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }

            // Totals and Checkout area
            Spacer(modifier = Modifier.height(10.dp))
            GlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                cornerRadius = 24.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocalizationManager.string("cart_total"),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "%,.0f ETB".format(totalSum),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD4AF37)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LuxuryButton(
                        onClick = {
                            val cartItemTitles = cartList.joinToString(", ") { "${it.quantity}x ${it.product.localizedTitle}" }
                            viewModel.initiatePurchase(
                                Product(
                                    id = 999,
                                    titleEn = "Your full gift cart: $cartItemTitles",
                                    titleAm = "የመረጡት ቅርጫት ሙሉ ዕቃዎች",
                                    descEn = "",
                                    descAm = "",
                                    price = totalSum,
                                    imageRes = R.drawable.img_luxury_gift_logo,
                                    category = "",
                                    rating = 5.0f,
                                    reviewsCount = 1,
                                    specificationsEn = emptyList(),
                                    specificationsAm = emptyList()
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.PhoneCallback, contentDescription = "Checkout")
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = LocalizationManager.string("checkout"),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(86.dp))
    }
}

// --- Profile Tab Layout ---
@Composable
fun ProfileScreenLayout(viewModel: GiftViewModel) {
    val isDark by viewModel.isDarkModeFlow.collectAsStateWithLifecycle()
    val currentLang by viewModel.currentLanguageFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = LocalizationManager.string("profile_title"),
                style = MaterialTheme.typography.displayMedium,
                color = Color(0xFFD4AF37),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Language Config Card
        item {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Language, contentDescription = "Language", tint = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = LocalizationManager.string("profile_lang"),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Switch(
                        checked = currentLang == com.example.data.Language.Amharic,
                        onCheckedChange = { viewModel.toggleLanguage() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFFD4AF37),
                            checkedTrackColor = Color(0xFF996515)
                        )
                    )
                }
            }
        }

        // Dark/Light Theme Config Card
        item {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = "Visual Mode",
                            tint = Color(0xFFD4AF37)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = LocalizationManager.string("profile_theme"),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isDark) LocalizationManager.string("profile_theme_dark") else LocalizationManager.string("profile_theme_light"),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        Switch(
                            checked = isDark,
                            onCheckedChange = { viewModel.setDarkMode(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFFD4AF37),
                                checkedTrackColor = Color(0xFF996515)
                            )
                        )
                    }
                }
            }
        }

        // Contact Channels Header
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = LocalizationManager.string("contact_title"),
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFD4AF37)
            )
            Text(
                text = LocalizationManager.string("contact_desc"),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }

        // Contact 1: Telegram
        item {
            ContactCard(
                icon = Icons.Default.Send,
                channelName = "Telegram VIP Channel",
                handle = "@Teke_Man_Promotion",
                color = Color(0xFF24A1DE),
                onActionClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Teke_Man_Promotion"))
                    context.startActivity(intent)
                },
                onCopyClick = {
                    clipboardManager.setText(AnnotatedString("https://t.me/Teke_Man_Promotion"))
                    Toast.makeText(context, LocalizationManager.string("copy_success"), Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Contact 2: TikTok
        item {
            ContactCard(
                icon = Icons.Default.MusicNote,
                channelName = "TikTok Showcase",
                handle = "@teke_man_promotion",
                color = Color(0xFF00F2FE),
                onActionClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tiktok.com/@teke_man_promotion"))
                    context.startActivity(intent)
                },
                onCopyClick = {
                    clipboardManager.setText(AnnotatedString("@teke_man_promotion"))
                    Toast.makeText(context, LocalizationManager.string("copy_success"), Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Contact 3: Call VIP Lines
        item {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = "Phones", tint = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Direct Concierge Hotline",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val phoneNumbers = listOf("+251921935862", "+251911518012", "+251983838309")
                    phoneNumbers.forEach { num ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = num, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(onClick = {
                                    clipboardManager.setText(AnnotatedString(num))
                                    Toast.makeText(context, LocalizationManager.string("copy_success"), Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color(0xFFD4AF37), modifier = Modifier.size(16.dp))
                                }

                                IconButton(onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$num"))
                                    context.startActivity(intent)
                                }) {
                                    Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Custom Showroom Info (About Teke Promotion)
        item {
            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Teke Promotion Gift Studio",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD4AF37),
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "We craft luxurious, unforgettable emotional bonds through high-end gift collections. Centered in Addis Ababa, Ethiopia, we pride ourselves on unmatched design and swift personalized delivery.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// --- Custom Contact Card Component ---
@Composable
fun ContactCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    channelName: String,
    handle: String,
    color: Color,
    onActionClick: () -> Unit,
    onCopyClick: () -> Unit
) {
    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(color.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = channelName, tint = color, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = channelName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = handle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }
            }

            Row {
                IconButton(onClick = onCopyClick) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color(0xFFD4AF37), modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onActionClick) {
                    Icon(imageVector = Icons.Default.OpenInNew, contentDescription = "Open", tint = Color(0xFFD4AF37), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

// --- Immersive Slide-Up Product Detail Sheet ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailSheet(
    product: Product,
    onClose: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onAddToCart: (Product, Int) -> Unit,
    onBuyNow: (Product) -> Unit,
    isFavoriteFlow: Flow<Boolean>
) {
    val isFavorite by isFavoriteFlow.collectAsStateWithLifecycle(false)
    var quantity by remember { mutableStateOf(1) }

    ModalBottomSheet(
        onDismissRequest = onClose,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFFD4AF37)) },
        modifier = Modifier.fillMaxHeight(0.85f).testTag("product_detail_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Hero display image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                if (!product.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.localizedTitle,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.img_luxury_gift_logo),
                        error = painterResource(id = R.drawable.img_luxury_gift_logo)
                    )
                } else {
                    Image(
                        painter = painterResource(id = product.imageRes),
                        contentDescription = product.localizedTitle,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Favorite badge overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(44.dp)
                        .background(Color(0x99000000), CircleShape)
                        .clickable(onClick = onFavoriteToggle),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color(0xFFC62828) else Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title & Price Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = product.localizedTitle,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "%,.0f ETB".format(product.price),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFFD4AF37),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = product.localizedDesc,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Specifications Bullet List
            Text(
                text = LocalizationManager.string("specifications"),
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFD4AF37)
            )
            Spacer(modifier = Modifier.height(8.dp))
            product.localizedSpecs.forEach { spec ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 3.dp)
                ) {
                    Icon(imageVector = Icons.Default.Circle, contentDescription = null, tint = Color(0xFFD4AF37), modifier = Modifier.size(6.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = spec, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Delivery and packaging details
            Text(
                text = LocalizationManager.string("delivery_info"),
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFD4AF37)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.LocalShipping, contentDescription = "Delivery", tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = LocalizationManager.string("delivery_desc"),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quantity selector Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = LocalizationManager.string("quantity"),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.surface, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Minus", modifier = Modifier.size(16.dp))
                    }

                    Text(
                        text = quantity.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(
                        onClick = { quantity++ },
                        modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.surface, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Buy Now and Add to Cart double actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { onAddToCart(product, quantity) },
                    border = BorderStroke(1.5.dp, Color(0xFFD4AF37)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("add_to_cart_btn"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD4AF37))
                ) {
                    Icon(imageVector = Icons.Default.AddShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = LocalizationManager.string("add_to_cart"), fontWeight = FontWeight.Bold)
                }

                LuxuryButton(
                    onClick = {
                        onClose()
                        onBuyNow(
                            Product(
                                id = product.id,
                                titleEn = "${quantity}x ${product.titleEn}",
                                titleAm = "${quantity}x ${product.titleAm}",
                                descEn = "",
                                descAm = "",
                                price = product.price * quantity,
                                imageRes = product.imageRes,
                                category = "",
                                rating = 5.0f,
                                reviewsCount = 1,
                                specificationsEn = emptyList(),
                                specificationsAm = emptyList()
                            )
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("buy_now_btn")
                ) {
                    Icon(imageVector = Icons.Default.PhoneCallback, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = LocalizationManager.string("buy_now"), color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// --- Elegant Animated Confirmation Dialog ---
@Composable
fun ConfirmPurchaseDialog(
    product: Product,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 8.dp,
        modifier = Modifier
            .border(1.5.dp, Color(0xFFD4AF37), RoundedCornerShape(28.dp))
            .testTag("buy_now_confirm_dialog"),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = "VIP Order Desk",
                    tint = Color(0xFFD4AF37),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = LocalizationManager.string("buy_confirm_title"),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD4AF37)
                )
            }
        },
        text = {
            Column {
                Text(
                    text = LocalizationManager.string("buy_confirm_desc").format(product.localizedTitle),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Visual gift summary
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    cornerRadius = 16.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = product.imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = product.localizedTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Value: %,.0f ETB".format(product.price),
                                color = Color(0xFFD4AF37),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            LuxuryButton(
                onClick = onConfirm,
                modifier = Modifier.testTag("dialog_call_btn")
            ) {
                Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = LocalizationManager.string("call_agent"), color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("dialog_cancel_btn")
            ) {
                Text(text = LocalizationManager.string("cancel"), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
    )
}

// --- Voice Recognition Immersive Listening Overlay ---
@Composable
fun VoiceListeningOverlay(onCancel: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRatio by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xEE09090A))
            .clickable(onClick = onCancel)
            .testTag("voice_listening_overlay"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .scale(pulseRatio)
                    .size(100.dp)
                    .background(Color(0xFFD4AF37).copy(alpha = 0.2f), CircleShape)
                    .border(2.dp, Color(0xFFD4AF37).copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFFD4AF37), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Mic,
                        contentDescription = "Listening",
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = LocalizationManager.string("search_voice"),
                color = Color(0xFFD4AF37),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Try saying \"Wedding Gold Chronograph\" or \"Valentine Chocolate Box\"",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 40.dp)
            )
        }
    }
}

// --- Floating Glass Drawer / Overlay for AI assistant ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIAssistantDrawer(
    viewModel: GiftViewModel,
    onClose: () -> Unit
) {
    val prompt by viewModel.assistantPromptFlow.collectAsStateWithLifecycle()
    val response by viewModel.assistantResponseFlow.collectAsStateWithLifecycle()
    val isLoading by viewModel.isAssistantLoadingFlow.collectAsStateWithLifecycle()
    
    var localPrompt by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onClose,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFFD4AF37)) },
        modifier = Modifier.fillMaxHeight(0.9f).testTag("ai_assistant_drawer")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFFD4AF37),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = LocalizationManager.string("gift_assistance"),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFD4AF37),
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dialog Bubble Scrollable Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                        RoundedCornerShape(20.dp)
                    )
                    .border(1.dp, Color(0x1AD4AF37), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                if (isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color(0xFFD4AF37))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = LocalizationManager.string("generating"),
                            color = Color(0xFFD4AF37),
                            fontSize = 13.sp
                        )
                    }
                } else if (response != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // User message bubble if they sent something
                        if (prompt.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .background(Color(0xFF2C2C2E), RoundedCornerShape(16.dp))
                                    .padding(12.dp)
                            ) {
                                Text(text = prompt, color = Color.White, fontSize = 13.sp)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Gemini response bubble
                        Box(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .border(1.dp, Color(0x33D4AF37), RoundedCornerShape(16.dp))
                                .background(Color(0x0BD4AF37), RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Text(
                                text = response!!,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Suggestions fast chips
            Text(
                text = "Prompt Curations:",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.padding(bottom = 6.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val suggestions = listOf("Best wedding gift", "Luxury anniversary roses", "VIP male birthday idea")
                items(suggestions) { sug ->
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                            .clickable {
                                localPrompt = sug
                                viewModel.sendAssistantMessage(sug)
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = sug, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Message send box
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextField(
                    value = localPrompt,
                    onValueChange = { localPrompt = it },
                    placeholder = {
                        Text(
                            text = LocalizationManager.string("gift_assistance_sub"),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.dp, Color(0x33D4AF37), RoundedCornerShape(24.dp))
                        .testTag("ai_prompt_input"),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFFD4AF37), Color(0xFF996515))),
                            CircleShape
                        )
                        .clickable {
                            if (localPrompt.isNotBlank()) {
                                viewModel.sendAssistantMessage(localPrompt)
                                localPrompt = ""
                            }
                        }
                        .testTag("ai_send_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Color.Black, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
