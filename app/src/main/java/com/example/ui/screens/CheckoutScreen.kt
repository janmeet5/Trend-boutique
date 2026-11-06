package com.example.ui.screens

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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SizingChoice
import com.example.ui.components.RazorpayPaymentSheet
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
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarmGold
import com.example.ui.theme.WarmGoldDark
import com.example.ui.theme.GlowPeach

import com.example.viewmodel.BoutiqueViewModel

@Composable
fun CheckoutScreen(
    viewModel: BoutiqueViewModel,
    modifier: Modifier = Modifier
) {
    val customization by viewModel.customization.collectAsState()
    val totalAmount = customization.calculateTotal()

    val addressLine by viewModel.checkoutAddressLine.collectAsState()
    val city by viewModel.checkoutCity.collectAsState()
    val pincode by viewModel.checkoutPincode.collectAsState()
    val landmark by viewModel.checkoutLandmark.collectAsState()
    val pickupDate by viewModel.checkoutPickupDate.collectAsState()
    val pickupSlot by viewModel.checkoutPickupSlot.collectAsState()
    val paymentMethod by viewModel.checkoutPaymentMethod.collectAsState()
    val isRazorpayProcessing by viewModel.isRazorpayProcessing.collectAsState()

    val punjabCities = viewModel.punjabCities

    var showRazorpaySheet by remember { mutableStateOf(false) }

    val dateOptions = listOf("Tomorrow, 10:30 AM", "Day After Tomorrow", "This Weekend")
    val slotOptions = listOf("Morning (10 AM – 1 PM)", "Afternoon (2 PM – 5 PM)", "Evening (6 PM – 9 PM)")

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
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BurgundyDark)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.testTag("checkout_back_btn")
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
                        text = "Doorstep Pickup & Checkout",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Schedule fabric/sample pickup and choose payment",
                        fontSize = 11.sp,
                        color = WarmGold
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Order Item Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(BurgundyPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCut,
                                contentDescription = null,
                                tint = WarmGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = customization.suitItem.title,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = CharcoalText
                            )
                            Text(
                                text = "Cut: ${customization.selectedBottom} • ${customization.selectedNeck}",
                                fontSize = 11.sp,
                                color = CharcoalMuted
                            )
                            Text(
                                text = "Sizing: " + when (customization.sizingChoice) {
                                    is SizingChoice.SavedProfile -> "Saved Profile Fit"
                                    is SizingChoice.TailorCall -> "Tailor Measurement Call"
                                    is SizingChoice.DoorstepSample -> "Doorstep Sample Match"
                                },
                                fontSize = 11.sp,
                                color = BurgundyPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Text(
                            text = "₹$totalAmount",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = BurgundyPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 1. DELIVERY & PICKUP ADDRESS
                Text(
                    text = "1. Doorstep Pickup & Delivery Address",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BurgundyPrimary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Where our boutique rider should collect and deliver your suits",
                        fontSize = 11.sp,
                        color = CharcoalMuted,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Change Address",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlowPeach,
                        modifier = Modifier
                            .clickable { viewModel.openAddressModal() }
                            .padding(4.dp)
                            .testTag("checkout_change_address_btn")
                    )
                }


                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = addressLine,
                    onValueChange = { viewModel.checkoutAddressLine.value = it },
                    label = { Text("House / Flat No., Street, Colony") },
                    placeholder = { Text("e.g. House 142, Street 4, Sarabha Nagar") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = CharcoalMuted)
                    },
                    modifier = Modifier.fillMaxWidth().testTag("checkout_address_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BurgundyPrimary,
                        unfocusedBorderColor = BorderSubtle
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // City selector
                    OutlinedTextField(
                        value = city,
                        onValueChange = { viewModel.checkoutCity.value = it },
                        label = { Text("Punjab City") },
                        modifier = Modifier.weight(1.3f).testTag("checkout_city_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BurgundyPrimary,
                            unfocusedBorderColor = BorderSubtle
                        )
                    )

                    // PIN Code
                    OutlinedTextField(
                        value = pincode,
                        onValueChange = { viewModel.checkoutPincode.value = it },
                        label = { Text("PIN Code") },
                        modifier = Modifier.weight(1f).testTag("checkout_pin_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BurgundyPrimary,
                            unfocusedBorderColor = BorderSubtle
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = landmark,
                    onValueChange = { viewModel.checkoutLandmark.value = it },
                    label = { Text("Nearby Landmark") },
                    placeholder = { Text("e.g. Near Malhar Road Park / Gurudwara Sahib") },
                    modifier = Modifier.fillMaxWidth().testTag("checkout_landmark_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BurgundyPrimary,
                        unfocusedBorderColor = BorderSubtle
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 2. PICKUP DATE & TIME SLOT
                Text(
                    text = "2. Fabric & Sample Pickup Slot",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BurgundyPrimary
                )
                Text(
                    text = "Rider arrives with a sealed tamper-evident bag for your fabric",
                    fontSize = 11.sp,
                    color = CharcoalMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Date options
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    dateOptions.forEach { d ->
                        val isSelected = d == pickupDate
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) BurgundyPrimary else CreamSurface)
                                .border(1.dp, if (isSelected) WarmGold else BorderSubtle, RoundedCornerShape(8.dp))
                                .clickable { viewModel.checkoutPickupDate.value = d }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = d,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else CharcoalText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Time slots
                slotOptions.forEach { slot ->
                    val isSelected = slot == pickupSlot
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) BlushSoft else Color.White)
                            .border(1.dp, if (isSelected) BurgundyPrimary else BorderSubtle, RoundedCornerShape(8.dp))
                            .clickable { viewModel.checkoutPickupSlot.value = slot }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { viewModel.checkoutPickupSlot.value = slot },
                            colors = RadioButtonDefaults.colors(selectedColor = BurgundyPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = slot,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = CharcoalText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 3. PAYMENT METHOD SELECTION
                Text(
                    text = "3. Payment Method",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BurgundyPrimary
                )
                Text(
                    text = "Choose prepaid online via Razorpay or Cash on Delivery (COD)",
                    fontSize = 11.sp,
                    color = CharcoalMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Option 1: Razorpay
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            1.dp,
                            if (paymentMethod.contains("Razorpay")) BurgundyPrimary else BorderSubtle,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            viewModel.checkoutPaymentMethod.value = "Razorpay (UPI, Cards, Netbanking)"
                        }
                        .testTag("payment_razorpay_option"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (paymentMethod.contains("Razorpay")) BlushSoft else Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = paymentMethod.contains("Razorpay"),
                            onClick = {
                                viewModel.checkoutPaymentMethod.value = "Razorpay (UPI, Cards, Netbanking)"
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = BurgundyPrimary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Razorpay (Prepaid)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = CharcoalText
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GoldContainer)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Instant UPI / Cards",
                                        fontSize = 10.sp,
                                        color = WarmGoldDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = "Google Pay, PhonePe, Paytm, Debit/Credit Card, Netbanking",
                                fontSize = 11.sp,
                                color = CharcoalMuted
                            )
                        }
                    }
                }

                // Option 2: Cash on Delivery (COD) / Pay on Pickup
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            1.dp,
                            if (paymentMethod.contains("Cash on Delivery")) BurgundyPrimary else BorderSubtle,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            viewModel.checkoutPaymentMethod.value = "Cash on Delivery / Pay on Pickup"
                        }
                        .testTag("payment_cod_option"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (paymentMethod.contains("Cash on Delivery")) BlushSoft else Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = paymentMethod.contains("Cash on Delivery"),
                            onClick = {
                                viewModel.checkoutPaymentMethod.value = "Cash on Delivery / Pay on Pickup"
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = BurgundyPrimary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Cash on Delivery / Pay on Pickup (COD)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = CharcoalText
                            )
                            Text(
                                text = "Pay cash or UPI directly to rider upon fabric pickup or delivery",
                                fontSize = 11.sp,
                                color = CharcoalMuted
                            )
                        }
                    }
                }
            }
        }

        // Sticky Bottom CTA
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
                    Text(text = "Payable Amount", fontSize = 11.sp, color = CharcoalMuted)
                    Text(
                        text = "₹$totalAmount",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = BurgundyPrimary
                    )
                }

                Button(
                    onClick = {
                        if (paymentMethod.contains("Razorpay")) {
                            showRazorpaySheet = true
                        } else {
                            viewModel.completeCheckout { }
                        }
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("confirm_booking_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (paymentMethod.contains("Razorpay")) "Pay & Confirm" else "Confirm Booking (COD)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Razorpay Modal Trigger
        if (showRazorpaySheet) {
            RazorpayPaymentSheet(
                amount = totalAmount,
                isProcessing = isRazorpayProcessing,
                onCancel = { showRazorpaySheet = false },
                onConfirmPayment = { txnId ->
                    viewModel.completeCheckout(transactionId = txnId) {
                        showRazorpaySheet = false
                    }
                }
            )
        }
    }
}
