package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderGold
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BurgundyDark
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarmGold
import com.example.ui.theme.WarmGoldDark
import com.example.viewmodel.BoutiqueViewModel
import com.example.viewmodel.Screen

@Composable
fun OrderConfirmationScreen(
    viewModel: BoutiqueViewModel,
    modifier: Modifier = Modifier
) {
    val order by viewModel.latestPlacedOrder.collectAsState()

    val bookingId = order?.id ?: "TB-PB-84920"
    val suitTitle = order?.customization?.suitItem?.title ?: "Royal Patiala Shahi Suit"
    val totalAmount = order?.totalAmount ?: 2199
    val pickupDate = order?.pickupDate ?: "Tomorrow"
    val pickupSlot = order?.pickupSlot ?: "Morning (10 AM – 1 PM)"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Animated Success Icon with Gold & Green Accents
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(SuccessGreen.copy(alpha = 0.12f))
                    .border(2.dp, SuccessGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = SuccessGreen,
                    modifier = Modifier.size(46.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Suit Stitching Booked!",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = BurgundyPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Punjab's royal craftsmanship is getting ready for your fabric",
                fontFamily = FontFamily.SansSerif,
                fontSize = 12.sp,
                color = CharcoalMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Booking ID Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(GoldContainer)
                    .border(1.dp, BorderGold, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "BOOKING REFERENCE ID", fontSize = 10.sp, color = CharcoalMuted, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Text(
                            text = bookingId,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = BurgundyPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(WarmGold)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "Confirmed", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BurgundyDeep)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pickup Next Step Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BurgundyDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = null,
                                tint = WarmGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Next Step: Doorstep Rider Pickup",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = BurgundyPrimary
                            )
                            Text(
                                text = "$pickupDate • $pickupSlot",
                                fontSize = 12.sp,
                                color = CharcoalMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "• Keep your unstitched fabric ready along with your reference sample suit.\n• Rider will place your items in a secure barcode-labeled bag and hand you a digital receipt.\n• Our master tailor in Ludhiana will measure your sample suit and replicate the fit 100%.",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = CharcoalText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Order Details",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = CharcoalText
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = suitTitle, fontSize = 12.sp, color = CharcoalText)
                        Text(text = "₹$totalAmount", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BurgundyPrimary)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Delivery Location", fontSize = 12.sp, color = CharcoalMuted)
                        Text(text = order?.deliveryAddress?.city ?: "Ludhiana", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Payment Method", fontSize = 12.sp, color = CharcoalMuted)
                        Text(
                            text = if (order?.isPaid == true) "Paid via Razorpay" else "Pay on Pickup / COD",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (order?.isPaid == true) SuccessGreen else BurgundyPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Button(
                onClick = {
                    if (order != null) {
                        viewModel.viewOrderDetails(order!!)
                    } else {
                        viewModel.navigateTo(Screen.ORDER_TRACKER)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("track_live_order_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Track Live Tailoring Progress",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = { viewModel.navigateTo(Screen.HOME) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("back_to_catalog_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Back to Boutique Catalog", color = CharcoalText, fontSize = 14.sp)
            }
        }
    }
}
