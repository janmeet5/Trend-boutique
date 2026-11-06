package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FabricSource
import com.example.ui.theme.BlushRose
import com.example.ui.theme.BlushSoft
import com.example.ui.theme.BorderGold
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BurgundyDark
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.StarGold
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarmGold
import com.example.ui.theme.WarmGoldDark
import com.example.viewmodel.BoutiqueViewModel

@Composable
fun ProductDetailScreen(
    viewModel: BoutiqueViewModel,
    modifier: Modifier = Modifier
) {
    val suit by viewModel.selectedSuit.collectAsState()
    val customization by viewModel.customization.collectAsState()

    val totalAmount = customization.calculateTotal()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 96.dp)
        ) {
            // Hero Image Container with back button & badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
            ) {
                Image(
                    painter = painterResource(id = suit.imageRes),
                    contentDescription = suit.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top shadow gradient for back button visibility
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(BurgundyDeep.copy(alpha = 0.6f), Color.Transparent)
                            )
                        )
                )

                // Back Button
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.45f))
                        .align(Alignment.TopStart)
                        .testTag("product_detail_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                // Category & Rating overlay bottom
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, BurgundyDeep.copy(alpha = 0.9f))
                            )
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = suit.category.uppercase(),
                            color = WarmGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = suit.punjabiTitle,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(BurgundyDeep.copy(alpha = 0.8f))
                            .border(1.dp, BorderGold, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = StarGold, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${suit.rating} (${suit.reviewCount})",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Title & Price Section
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = suit.title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = CharcoalText,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "₹${suit.price}",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = BurgundyPrimary
                    )

                    Text(
                        text = "₹${suit.originalPrice}",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.LineThrough,
                        color = CharcoalMuted
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(GoldContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Save ₹${suit.originalPrice - suit.price}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarmGoldDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Included items card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = CreamSurface)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCut,
                            contentDescription = null,
                            tint = BurgundyPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Included in Stitching Set:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = BurgundyPrimary
                            )
                            Text(
                                text = suit.includedItems,
                                fontSize = 12.sp,
                                color = CharcoalText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Turnaround timeline
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Turnaround: ${suit.turnaroundDays} • Free pickup & delivery across Punjab",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = SuccessGreen
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = suit.description,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = CharcoalMuted
                )
            }

            HorizontalDivider(color = BorderSubtle, thickness = 1.dp)

            // CUSTOMIZATION OPTIONS SECTION
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Boutique Tailoring Customization",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = BurgundyPrimary
                )
                Text(
                    text = "Personalize every detail before our master tailor cuts your suit",
                    fontSize = 12.sp,
                    color = CharcoalMuted
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 1. Fabric Source Selection
                Text(
                    text = "Fabric Option",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = CharcoalText
                )
                Spacer(modifier = Modifier.height(6.dp))

                FabricSource.entries.forEach { source ->
                    val isSelected = customization.fabricSource == source
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) BlushSoft else Color.White)
                            .border(1.dp, if (isSelected) BurgundyPrimary else BorderSubtle, RoundedCornerShape(10.dp))
                            .clickable { viewModel.updateFabricSource(source) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { viewModel.updateFabricSource(source) },
                            colors = RadioButtonDefaults.colors(selectedColor = BurgundyPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = source.title,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = CharcoalText
                                )
                                if (source.surcharge > 0) {
                                    Text(
                                        text = "+ ₹${source.surcharge}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BurgundyPrimary
                                    )
                                }
                            }
                            Text(
                                text = source.description,
                                fontSize = 11.sp,
                                color = CharcoalMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Fabric Type Selection
                Text(
                    text = "Preferred Fabric Texture",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = CharcoalText
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    suit.fabricOptions.forEach { fabric ->
                        val isSelected = customization.selectedFabric == fabric
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) BurgundyPrimary else CreamSurface)
                                .border(1.dp, if (isSelected) WarmGold else BorderSubtle, RoundedCornerShape(8.dp))
                                .clickable { viewModel.updateFabric(fabric) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = fabric,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else CharcoalText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3. Neckline Customization
                Text(
                    text = "Neckline Style",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = CharcoalText
                )
                Spacer(modifier = Modifier.height(6.dp))
                suit.neckOptions.forEach { neck ->
                    val isSelected = customization.selectedNeck == neck
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) BlushSoft else CreamSurface)
                            .border(1.dp, if (isSelected) BurgundyPrimary else BorderSubtle, RoundedCornerShape(8.dp))
                            .clickable { viewModel.updateNeck(neck) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.ContentCut,
                            contentDescription = null,
                            tint = if (isSelected) BurgundyPrimary else CharcoalMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = neck,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = CharcoalText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4. Sleeve Style
                Text(
                    text = "Sleeve Cut",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = CharcoalText
                )
                Spacer(modifier = Modifier.height(6.dp))
                suit.sleeveOptions.forEach { sleeve ->
                    val isSelected = customization.selectedSleeve == sleeve
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) BlushSoft else CreamSurface)
                            .border(1.dp, if (isSelected) BurgundyPrimary else BorderSubtle, RoundedCornerShape(8.dp))
                            .clickable { viewModel.updateSleeve(sleeve) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.ContentCut,
                            contentDescription = null,
                            tint = if (isSelected) BurgundyPrimary else CharcoalMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = sleeve,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = CharcoalText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 5. Bottom Silhouette
                Text(
                    text = "Salwar / Bottom Cut",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = CharcoalText
                )
                Spacer(modifier = Modifier.height(6.dp))
                suit.bottomOptions.forEach { bottom ->
                    val isSelected = customization.selectedBottom == bottom
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) BlushSoft else CreamSurface)
                            .border(1.dp, if (isSelected) BurgundyPrimary else BorderSubtle, RoundedCornerShape(8.dp))
                            .clickable { viewModel.updateBottom(bottom) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.ContentCut,
                            contentDescription = null,
                            tint = if (isSelected) BurgundyPrimary else CharcoalMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = bottom,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = CharcoalText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Special tailoring notes
                OutlinedTextField(
                    value = customization.customNotes,
                    onValueChange = { viewModel.updateCustomNotes(it) },
                    label = { Text("Special Instructions for Master Tailor") },
                    placeholder = { Text("e.g. Please add 2 inches margin inside and inner cotton lining") },
                    modifier = Modifier.fillMaxWidth().testTag("custom_tailor_notes_input"),
                    minLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BurgundyPrimary,
                        unfocusedBorderColor = BorderSubtle
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Price Breakdown Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderGold)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Transparent Price Breakdown",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = BurgundyPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Master Stitching & Pattern Cut", fontSize = 12.sp, color = CharcoalMuted)
                            Text(text = "₹${suit.price}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }

                        if (customization.fabricSource.surcharge > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Boutique Pure Fabric Set", fontSize = 12.sp, color = CharcoalMuted)
                                Text(text = "₹${customization.fabricSource.surcharge}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Doorstep Sample Pickup & Delivery", fontSize = 12.sp, color = SuccessGreen)
                            Text(text = "FREE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = BorderSubtle)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Total Stitching Estimate", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                text = "₹$totalAmount",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = BurgundyPrimary
                            )
                        }
                    }
                }
            }
        }

        // Sticky Bottom CTA Bar: "Book Stitching"
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Total Estimate", fontSize = 11.sp, color = CharcoalMuted)
                    Text(
                        text = "₹$totalAmount",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = BurgundyPrimary
                    )
                }

                Button(
                    onClick = { viewModel.proceedToSmartMeasurement() },
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("book_stitching_cta"),
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Book Stitching",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
