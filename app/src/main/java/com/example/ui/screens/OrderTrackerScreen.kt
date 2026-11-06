package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BoutiqueOrder
import com.example.model.OrderStatus
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
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarmGold
import com.example.ui.theme.WarmGoldDark
import com.example.viewmodel.BoutiqueViewModel
import com.example.viewmodel.Screen

@Composable
fun OrderTrackerScreen(
    viewModel: BoutiqueViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val orders by viewModel.orders.collectAsState()
    val selectedOrder by viewModel.selectedOrder.collectAsState()

    // Default to selected order or first active order
    val activeOrder = selectedOrder ?: orders.firstOrNull()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

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
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BurgundyDark)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(Screen.HOME) },
                    modifier = Modifier.testTag("tracker_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Orders & Live Progress Tracker",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Text(
                        text = "5-stage master tailor progress for your custom suits",
                        fontSize = 11.sp,
                        color = WarmGold
                    )
                }
            }

            if (activeOrder == null) {
                // Empty State
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCut,
                        contentDescription = null,
                        tint = CharcoalMuted,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "No Orders Placed Yet",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = CharcoalText
                    )
                    Text(
                        text = "Explore our catalogue to book custom suit stitching with doorstep reference pickup.",
                        fontSize = 12.sp,
                        color = CharcoalMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = { viewModel.navigateTo(Screen.HOME) },
                        colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary)
                    ) {
                        Text(text = "Browse Catalogue")
                    }
                }
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Orders Selector Bar if multiple orders
                    if (orders.size > 1) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            orders.forEach { ord ->
                                val isSelected = ord.id == activeOrder.id
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) BurgundyPrimary else Color.White)
                                        .border(1.dp, if (isSelected) WarmGold else BorderSubtle, RoundedCornerShape(8.dp))
                                        .clickable { viewModel.viewOrderDetails(ord) }
                                        .padding(vertical = 8.dp, horizontal = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = ord.id,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else CharcoalText
                                        )
                                        Text(
                                            text = ord.status.label.split(" ").first(),
                                            fontSize = 10.sp,
                                            color = if (isSelected) WarmGold else BurgundyPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Active Order Summary Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderGold)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "ORDER #${activeOrder.id}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily.Serif,
                                        color = BurgundyPrimary
                                    )
                                    Text(
                                        text = "Booked on ${activeOrder.orderDate}",
                                        fontSize = 11.sp,
                                        color = CharcoalMuted
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            if (activeOrder.status == OrderStatus.DELIVERED) SuccessGreen.copy(alpha = 0.15f) else GoldContainer
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = activeOrder.status.label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (activeOrder.status == OrderStatus.DELIVERED) SuccessGreen else WarmGoldDark
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = activeOrder.customization.suitItem.title,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = CharcoalText
                            )

                            Text(
                                text = "Cut: ${activeOrder.customization.selectedBottom} • ${activeOrder.customization.selectedNeck} • ${activeOrder.customization.selectedSleeve}",
                                fontSize = 12.sp,
                                color = CharcoalMuted
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Pickup City: ${activeOrder.deliveryAddress.city}",
                                    fontSize = 11.sp,
                                    color = CharcoalText
                                )
                                Text(
                                    text = "Amount: ₹${activeOrder.totalAmount}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = BurgundyPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 5-STAGE LIVE PROGRESS TRACKER
                    Text(
                        text = "Live Stitching Progress",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = BurgundyPrimary
                    )
                    Text(
                        text = "Real-time updates directly from our Ludhiana workshop",
                        fontSize = 11.sp,
                        color = CharcoalMuted
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            val stages = listOf(
                                OrderStatus.PICKUP_SCHEDULED,
                                OrderStatus.FABRIC_RECEIVED,
                                OrderStatus.STITCHING_IN_PROGRESS,
                                OrderStatus.QUALITY_CHECK,
                                OrderStatus.OUT_FOR_DELIVERY
                            )

                            stages.forEachIndexed { index, stage ->
                                val isCompleted = activeOrder.status.stepIndex > stage.stepIndex
                                val isCurrent = activeOrder.status.stepIndex == stage.stepIndex

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    // Step icon & vertical bar
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.width(36.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    when {
                                                        isCompleted -> SuccessGreen
                                                        isCurrent -> BurgundyPrimary
                                                        else -> CreamSurface
                                                    }
                                                )
                                                .border(
                                                    1.5.dp,
                                                    when {
                                                        isCompleted -> SuccessGreen
                                                        isCurrent -> WarmGold
                                                        else -> BorderSubtle
                                                    },
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isCompleted) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            } else if (isCurrent) {
                                                Icon(
                                                    imageVector = Icons.Default.HourglassBottom,
                                                    contentDescription = null,
                                                    tint = WarmGold,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            } else {
                                                Text(
                                                    text = "${index + 1}",
                                                    fontSize = 12.sp,
                                                    color = CharcoalMuted,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        // Vertical connector line
                                        if (index < stages.size - 1) {
                                            Box(
                                                modifier = Modifier
                                                    .width(2.dp)
                                                    .height(38.dp)
                                                    .background(
                                                        if (isCompleted) SuccessGreen else BorderSubtle
                                                    )
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.padding(bottom = if (index < stages.size - 1) 16.dp else 0.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = stage.label,
                                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                                                fontSize = 13.sp,
                                                color = if (isCurrent) BurgundyPrimary else if (isCompleted) CharcoalText else CharcoalMuted
                                            )

                                            if (isCurrent) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(GoldContainer)
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "In Progress",
                                                        fontSize = 10.sp,
                                                        color = WarmGoldDark,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }

                                        Text(
                                            text = stage.description,
                                            fontSize = 11.sp,
                                            color = CharcoalMuted,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Master Tailor & Workshop Details Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = GoldContainer),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderGold)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(BurgundyPrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCut,
                                            contentDescription = null,
                                            tint = WarmGold,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Assigned Master Tailor",
                                            fontSize = 11.sp,
                                            color = CharcoalMuted
                                        )
                                        Text(
                                            text = activeOrder.masterTailorName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = BurgundyPrimary
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        val uri = Uri.parse("tel:919876543210")
                                        context.startActivity(Intent(Intent.ACTION_DIAL, uri))
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Call Tailor", fontSize = 11.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "“Fabric received. Replicating the exact waist ease and Patiala pleats as specified from your sample suit.”",
                                fontFamily = FontFamily.Serif,
                                fontSize = 12.sp,
                                color = CharcoalText
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "100% Fit Guarantee • 10-day free doorstep alteration if needed",
                                    fontSize = 10.sp,
                                    color = SuccessGreen,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Interactive Status Simulator for Testing
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "🧪 Interactive Stage Simulator (Demo)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = CharcoalText
                            )
                            Text(
                                text = "Tap any stage to test how the real-time tracker updates live:",
                                fontSize = 11.sp,
                                color = CharcoalMuted
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                listOf(
                                    OrderStatus.PICKUP_SCHEDULED to "1. Pickup",
                                    OrderStatus.FABRIC_RECEIVED to "2. Arrived",
                                    OrderStatus.STITCHING_IN_PROGRESS to "3. Stitching",
                                    OrderStatus.QUALITY_CHECK to "4. Quality",
                                    OrderStatus.OUT_FOR_DELIVERY to "5. Delivery"
                                ).forEach { (st, label) ->
                                    val isCurrent = activeOrder.status == st
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isCurrent) BurgundyPrimary else CreamSurface)
                                            .clickable { viewModel.updateStatusDemo(activeOrder.id, st) }
                                            .padding(vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = label,
                                            fontSize = 9.sp,
                                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isCurrent) Color.White else CharcoalText
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
