package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.DeliveryAddress
import com.example.model.SuitItem
import com.example.ui.theme.BlushRose
import com.example.ui.theme.BurgundyDark
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GlowCrimson
import com.example.ui.theme.GlowGold
import com.example.ui.theme.GlowPeach
import com.example.ui.theme.GlowRose
import com.example.ui.theme.GlowSunset
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightBorderSubtle
import com.example.ui.theme.LightCard
import com.example.ui.theme.LightCardSoft
import com.example.ui.theme.SoftAmberContainer
import com.example.ui.theme.SoftPeachContainer
import com.example.ui.theme.StarGold
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenLight
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.WarmGold
import com.example.ui.theme.WhatsAppGreen
import com.example.viewmodel.Screen

/**
 * Top App Bar with brand logo, quick tailor call, theme switcher,
 * orders counter, and the streamlined delivery location selector.
 */
@Composable
fun BoutiqueTopBar(
    selectedAddress: DeliveryAddress,
    ordersCount: Int,
    isDarkMode: Boolean,
    onAddressClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onTailorClick: () -> Unit,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "favicon_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 0.8.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Upper Brand Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Tagline
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Official Glowing Favicon Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SoftPeachContainer)
                        .border(
                            1.5.dp,
                            Brush.sweepGradient(
                                listOf(GlowCrimson, GlowPeach, GlowGold, GlowRose, GlowCrimson)
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_trend_favicon_glowing),
                        contentDescription = "The Trend Boutique Icon",
                        modifier = Modifier
                            .size(34.dp)
                            .padding(2.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "THE TREND BOUTIQUE",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 0.8.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(GlowPeach.copy(alpha = glowAlpha))
                        )
                    }
                    Text(
                        text = "Punjab's Luxury Custom Tailoring",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 11.sp,
                        color = BurgundyPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Quick Actions: Theme, Tailor Call, Orders Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Theme Toggle
                IconButton(
                    onClick = onToggleTheme,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .testTag("toggle_dark_mode_btn")
                ) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Toggle Theme",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Expert Tailor Call
                IconButton(
                    onClick = onTailorClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SoftPeachContainer)
                        .testTag("top_bar_tailor_help")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Book Tailor Call",
                        tint = BurgundyPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Active Orders Bag
                IconButton(
                    onClick = onOrdersClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .testTag("top_bar_orders_btn")
                ) {
                    BadgedBox(
                        badge = {
                            if (ordersCount > 0) {
                                Badge(
                                    containerColor = BurgundyPrimary,
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = ordersCount.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "My Orders",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // FRIENDLY DELIVERY LOCATION PILL (Clean Google / Dipstore style)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    width = 0.8.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(22.dp)
                )
                .clickable { onAddressClick() }
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .testTag("top_address_selector_bar")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(BurgundyPrimary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Delivery Location",
                            tint = BurgundyPrimary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Deliver to: ",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = "${selectedAddress.label} • ${selectedAddress.city}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Change",
                        fontSize = 11.sp,
                        color = BurgundyPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Change Address",
                        tint = BurgundyPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

/**
 * Modern Bottom Navigation Bar with clean tabs, soft active pill,
 * and high accessibility.
 */
@Composable
fun BoutiqueBottomBar(
    currentScreen: Screen,
    activeOrdersCount: Int,
    onTabSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .testTag("bottom_nav_bar")
            .border(
                width = 0.6.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.HOME,
            onClick = { onTabSelected(Screen.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == Screen.HOME) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                    contentDescription = "Boutique Catalog"
                )
            },
            label = {
                Text(
                    text = "Boutique",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = if (currentScreen == Screen.HOME) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BurgundyPrimary,
                selectedTextColor = BurgundyPrimary,
                indicatorColor = SoftPeachContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("tab_home")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.ORDER_TRACKER,
            onClick = { onTabSelected(Screen.ORDER_TRACKER) },
            icon = {
                BadgedBox(
                    badge = {
                        if (activeOrdersCount > 0) {
                            Badge(
                                containerColor = BurgundyPrimary,
                                contentColor = Color.White
                            ) {
                                Text(text = activeOrdersCount.toString())
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentScreen == Screen.ORDER_TRACKER) Icons.Filled.CheckCircle else Icons.Outlined.ContentCut,
                        contentDescription = "Live Orders"
                    )
                }
            },
            label = {
                Text(
                    text = "Live Orders",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = if (currentScreen == Screen.ORDER_TRACKER) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BurgundyPrimary,
                selectedTextColor = BurgundyPrimary,
                indicatorColor = SoftPeachContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("tab_orders")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.SAVED_PROFILES,
            onClick = { onTabSelected(Screen.SAVED_PROFILES) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == Screen.SAVED_PROFILES) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "My Sizes"
                )
            },
            label = {
                Text(
                    text = "My Sizes",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = if (currentScreen == Screen.SAVED_PROFILES) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BurgundyPrimary,
                selectedTextColor = BurgundyPrimary,
                indicatorColor = SoftPeachContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("tab_profiles")
        )

        NavigationBarItem(
            selected = currentScreen == Screen.REVIEWS,
            onClick = { onTabSelected(Screen.REVIEWS) },
            icon = {
                Icon(
                    imageVector = if (currentScreen == Screen.REVIEWS) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Reviews & Help"
                )
            },
            label = {
                Text(
                    text = "Help & Reviews",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = if (currentScreen == Screen.REVIEWS) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BurgundyPrimary,
                selectedTextColor = BurgundyPrimary,
                indicatorColor = SoftPeachContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag("tab_reviews")
        )
    }
}

/**
 * Clean, breathable category pills inspired by the Dipstore e-commerce reference.
 */
@Composable
fun CategoryPills(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        if (isSelected) BurgundyPrimary else MaterialTheme.colorScheme.surface
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) BurgundyPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 16.dp, vertical = 9.dp)
                    .testTag("category_pill_$category")
            ) {
                Text(
                    text = category,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 13.sp,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Premium, friendly e-commerce suit card with generous whitespace,
 * clear typography, fabric badges, pricing breakdown, and 1-tap booking.
 */
@Composable
fun CatalogSuitCard(
    suit: SuitItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("suit_card_${suit.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            0.8.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Card Image with Express badge & category tag
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(185.dp)
            ) {
                Image(
                    painter = painterResource(id = suit.imageRes),
                    contentDescription = suit.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Category pill top-left
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.92f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = suit.category,
                        color = BurgundyPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                }

                // Favorite Wishlist Button top-right
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .padding(6.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.88f))
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isFavorite) GlowCrimson else Color(0xFF6B5E59),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Express Delivery badge bottom-left
                if (suit.isExpressAvailable) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SuccessGreen)
                            .padding(horizontal = 7.dp, vertical = 2.5.dp)
                            .align(Alignment.BottomStart)
                    ) {
                        Text(
                            text = "⚡ 4-Day Delivery",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Card details with friendly spacing
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = suit.title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = suit.punjabiTitle,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    color = BurgundyPrimary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Included items
                Text(
                    text = suit.includedItems,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Fabrics available pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    suit.fabricOptions.take(2).forEach { fabric ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 6.dp, vertical = 2.5.dp)
                        ) {
                            Text(
                                text = fabric,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    if (suit.fabricOptions.size > 2) {
                        Text(
                            text = "+${suit.fabricOptions.size - 2}",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Price and Rating Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "₹${suit.price}",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = BurgundyPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "₹${suit.originalPrice}",
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 11.sp,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "Stitching + Fabric",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Rating badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SoftAmberContainer)
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = StarGold,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = String.format("%.1f", suit.rating),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8C5300)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Friendly Primary Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BurgundyPrimary)
                        .padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Book Stitching",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Floating friendly assistant pill offering quick advice with Masterji or WhatsApp.
 */
@Composable
fun FloatingTailorHelpBar(
    onBookTailorCall: () -> Unit,
    onWhatsAppClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SoftPeachContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCut,
                        contentDescription = "Master Tailor",
                        tint = BurgundyPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "Need Fit Guidance?",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Talk to Master Gurwinder Singh",
                        fontSize = 11.sp,
                        color = BurgundyPrimary
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Book Call button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(BurgundyPrimary)
                        .clickable { onBookTailorCall() }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                        .testTag("book_tailor_call_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Call",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }

                // WhatsApp connect
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(WhatsAppGreen)
                        .clickable { onWhatsAppClick() }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                        .testTag("whatsapp_connect_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "WhatsApp",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Chat",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
