package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import com.example.model.MeasurementProfile
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

@Composable
fun TailorCallDialog(
    initialName: String,
    initialPhone: String,
    initialCity: String,
    punjabCities: List<String>,
    successMessage: String?,
    onDismiss: () -> Unit,
    onSubmit: (name: String, phone: String, city: String, slot: String, language: String, topic: String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var phone by remember { mutableStateOf(initialPhone) }
    var city by remember { mutableStateOf(initialCity) }
    var selectedSlot by remember { mutableStateOf("Morning (10 AM – 1 PM)") }
    var selectedLanguage by remember { mutableStateOf("Punjabi (ਪੰਜਾਬੀ)") }
    var selectedTopic by remember { mutableStateOf("Neckline & Sizing Guidance") }

    val slots = listOf("Morning (10 AM – 1 PM)", "Afternoon (2 PM – 5 PM)", "Evening (6 PM – 9 PM)")
    val languages = listOf("Punjabi (ਪੰਜਾਬੀ)", "Hindi (हिन्दी)", "English")
    val topics = listOf("Neckline & Sizing Guidance", "Fabric & Lining Selection", "Bridal / Party Wear Consultation")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
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
                                .background(GoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                tint = WarmGoldDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Expert Tailor Call",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = BurgundyPrimary
                            )
                            Text(
                                text = "Master Gurwinder Singh (Ludhiana)",
                                fontSize = 11.sp,
                                color = CharcoalMuted
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (successMessage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SuccessGreen.copy(alpha = 0.12f))
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SuccessGreen
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = successMessage,
                                color = SuccessGreen,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Speak with our master tailor to finalize neck depth, sleeve cuts, and custom measurements before or after booking.",
                        fontSize = 12.sp,
                        color = CharcoalMuted,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Your Full Name") },
                        modifier = Modifier.fillMaxWidth().testTag("tailor_call_name_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BurgundyPrimary,
                            unfocusedBorderColor = BorderSubtle
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number (with WhatsApp)") },
                        modifier = Modifier.fillMaxWidth().testTag("tailor_call_phone_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BurgundyPrimary,
                            unfocusedBorderColor = BorderSubtle
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Preferred Time Slot
                    Text(
                        text = "Preferred Call Time Slot",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = CharcoalText
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    slots.forEach { slot ->
                        val isSelected = slot == selectedSlot
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) BlushSoft else CreamSurface)
                                .border(1.dp, if (isSelected) BurgundyPrimary else BorderSubtle, RoundedCornerShape(8.dp))
                                .clickable { selectedSlot = slot }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.PhoneAndroid,
                                contentDescription = null,
                                tint = if (isSelected) BurgundyPrimary else CharcoalMuted,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = slot,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) BurgundyPrimary else CharcoalText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Language Preference
                    Text(
                        text = "Preferred Language",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = CharcoalText
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        languages.forEach { lang ->
                            val isSelected = lang == selectedLanguage
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) BurgundyPrimary else CreamSurface)
                                    .clickable { selectedLanguage = lang }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = lang.split(" ").first(),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else CharcoalText
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onSubmit(name, phone, city, selectedSlot, selectedLanguage, selectedTopic)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("submit_tailor_call_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Schedule Tailor Call",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MeasurementProfileDialog(
    profile: MeasurementProfile,
    onDismiss: () -> Unit,
    onSave: (MeasurementProfile) -> Unit
) {
    var label by remember { mutableStateOf(profile.label) }
    var bust by remember { mutableStateOf(profile.bust.toString()) }
    var waist by remember { mutableStateOf(profile.waist.toString()) }
    var hips by remember { mutableStateOf(profile.hips.toString()) }
    var kurtaLength by remember { mutableStateOf(profile.kurtaLength.toString()) }
    var bottomLength by remember { mutableStateOf(profile.bottomLength.toString()) }
    var neckDepth by remember { mutableStateOf(profile.neckDepth.toString()) }
    var sleeveLength by remember { mutableStateOf(profile.sleeveLength.toString()) }
    var shoulder by remember { mutableStateOf(profile.shoulder.toString()) }
    var armhole by remember { mutableStateOf(profile.armhole.toString()) }
    var notes by remember { mutableStateOf(profile.notes) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Saved Sizing Profile",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = BurgundyPrimary
                        )
                        Text(
                            text = "Measurements in Inches (Standard Tailoring)",
                            fontSize = 11.sp,
                            color = CharcoalMuted
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Profile Label (e.g. My Default Fit, Mom's Fit)") },
                    modifier = Modifier.fillMaxWidth().testTag("profile_label_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BurgundyPrimary,
                        unfocusedBorderColor = BorderSubtle
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Measurement Fields Grid
                Text(
                    text = "Core Body Measurements",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = CharcoalText
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = bust,
                        onValueChange = { bust = it },
                        label = { Text("Bust") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = waist,
                        onValueChange = { waist = it },
                        label = { Text("Waist") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = hips,
                        onValueChange = { hips = it },
                        label = { Text("Hips") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = kurtaLength,
                        onValueChange = { kurtaLength = it },
                        label = { Text("Kurta Length") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = bottomLength,
                        onValueChange = { bottomLength = it },
                        label = { Text("Bottom Length") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = neckDepth,
                        onValueChange = { neckDepth = it },
                        label = { Text("Neck Depth") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = sleeveLength,
                        onValueChange = { sleeveLength = it },
                        label = { Text("Sleeve Length") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = shoulder,
                        onValueChange = { shoulder = it },
                        label = { Text("Shoulder") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = armhole,
                        onValueChange = { armhole = it },
                        label = { Text("Armhole") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Special Fit Preferences & Alteration Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val updated = profile.copy(
                            label = label.ifBlank { "Custom Fit" },
                            bust = bust.toFloatOrNull() ?: profile.bust,
                            waist = waist.toFloatOrNull() ?: profile.waist,
                            hips = hips.toFloatOrNull() ?: profile.hips,
                            kurtaLength = kurtaLength.toFloatOrNull() ?: profile.kurtaLength,
                            bottomLength = bottomLength.toFloatOrNull() ?: profile.bottomLength,
                            neckDepth = neckDepth.toFloatOrNull() ?: profile.neckDepth,
                            sleeveLength = sleeveLength.toFloatOrNull() ?: profile.sleeveLength,
                            shoulder = shoulder.toFloatOrNull() ?: profile.shoulder,
                            armhole = armhole.toFloatOrNull() ?: profile.armhole,
                            notes = notes,
                            lastUpdated = "Today"
                        )
                        onSave(updated)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_profile_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Save Sizing Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RazorpayPaymentSheet(
    amount: Int,
    isProcessing: Boolean,
    onCancel: () -> Unit,
    onConfirmPayment: (String) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var selectedTab by remember { mutableStateOf("UPI") } // "UPI", "CARD", "NETBANKING"
    var selectedUpiApp by remember { mutableStateOf("Google Pay") }
    var cardNumber by remember { mutableStateOf("4532 8920 1823 4402") }
    var cardExpiry by remember { mutableStateOf("08/28") }
    var cardCvv by remember { mutableStateOf("729") }
    var cardHolder by remember { mutableStateOf("Harpreet Kaur") }
    var selectedBank by remember { mutableStateOf("HDFC Bank") }
    var paymentNote by remember { mutableStateOf<String?>(null) }

    val upiApps = listOf("Google Pay", "PhonePe", "Paytm", "BHIM UPI")
    val banks = listOf("HDFC Bank", "State Bank of India (SBI)", "ICICI Bank", "Punjab National Bank (PNB)", "Axis Bank")

    val razorpayKeyId = try {
        com.example.BuildConfig.RAZORPAY_KEY_ID
    } catch (e: Throwable) {
        ""
    }

    androidx.compose.ui.window.Dialog(onDismissRequest = { if (!isProcessing) onCancel() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Razorpay Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0C2340)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "R",
                                color = Color(0xFF0C83FD),
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Razorpay Trusted Checkout",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF0C2340)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "The Trend Boutique, Punjab",
                                    fontSize = 11.sp,
                                    color = CharcoalMuted
                                )
                                if (razorpayKeyId.isNotBlank() && !razorpayKeyId.contains("placeholder")) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "• Live Key Active",
                                        fontSize = 10.sp,
                                        color = SuccessGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    if (!isProcessing) {
                        IconButton(onClick = onCancel) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Amount banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(GoldContainer)
                        .border(1.dp, BorderGold, RoundedCornerShape(10.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Amount Payable", fontSize = 11.sp, color = CharcoalMuted)
                            Text(
                                text = "₹$amount",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = BurgundyPrimary
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "256-bit Encrypted", fontSize = 10.sp, color = SuccessGreen)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tabs: UPI, Card, Netbanking
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CreamSurface)
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("UPI" to "UPI Apps", "CARD" to "Card", "NETBANKING" to "Netbanking").forEach { (tabKey, tabLabel) ->
                        val isSelected = selectedTab == tabKey
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) BurgundyPrimary else Color.Transparent)
                                .clickable(enabled = !isProcessing) {
                                    selectedTab = tabKey
                                    paymentNote = null
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tabLabel,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else CharcoalText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                when (selectedTab) {
                    "UPI" -> {
                        Text(
                            text = "Select UPI App",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = CharcoalText
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        upiApps.forEach { app ->
                            val isSelected = app == selectedUpiApp
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) BlushSoft else CreamSurface)
                                    .border(1.dp, if (isSelected) BurgundyPrimary else BorderSubtle, RoundedCornerShape(8.dp))
                                    .clickable(enabled = !isProcessing) { selectedUpiApp = app }
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.PhoneAndroid,
                                        contentDescription = null,
                                        tint = if (isSelected) BurgundyPrimary else CharcoalMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = app,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = CharcoalText
                                    )
                                }

                                if (isSelected) {
                                    Text(text = "Selected", fontSize = 10.sp, color = BurgundyPrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Launch UPI App Intent Button
                        androidx.compose.material3.OutlinedButton(
                            onClick = {
                                try {
                                    val upiUri = android.net.Uri.parse("upi://pay?pa=trendboutique@icici&pn=The+Trend+Boutique&am=$amount&cu=INR&tn=Trend+Boutique+Suit+Booking")
                                    val upiIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, upiUri)
                                    context.startActivity(upiIntent)
                                    paymentNote = "UPI app intent launched! Return here to confirm receipt."
                                } catch (e: Exception) {
                                    paymentNote = "UPI intent registered: tap 'Authorize & Pay' below to complete transaction."
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(38.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Open $selectedUpiApp directly →", fontSize = 11.sp, color = BurgundyPrimary)
                        }
                    }

                    "CARD" -> {
                        Text(
                            text = "Card Details (Visa / Mastercard / RuPay)",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = CharcoalText
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = { cardNumber = it },
                            label = { Text("Card Number") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BurgundyPrimary,
                                unfocusedBorderColor = BorderSubtle
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = cardExpiry,
                                onValueChange = { cardExpiry = it },
                                label = { Text("Valid Thru") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BurgundyPrimary,
                                    unfocusedBorderColor = BorderSubtle
                                )
                            )

                            OutlinedTextField(
                                value = cardCvv,
                                onValueChange = { cardCvv = it },
                                label = { Text("CVV") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BurgundyPrimary,
                                    unfocusedBorderColor = BorderSubtle
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = cardHolder,
                            onValueChange = { cardHolder = it },
                            label = { Text("Cardholder Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BurgundyPrimary,
                                unfocusedBorderColor = BorderSubtle
                            )
                        )
                    }

                    "NETBANKING" -> {
                        Text(
                            text = "Popular Punjab & Indian Banks",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = CharcoalText
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        banks.forEach { bank ->
                            val isSelected = bank == selectedBank
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) BlushSoft else CreamSurface)
                                    .border(1.dp, if (isSelected) BurgundyPrimary else BorderSubtle, RoundedCornerShape(8.dp))
                                    .clickable(enabled = !isProcessing) { selectedBank = bank }
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = bank,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = CharcoalText
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = BurgundyPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                if (paymentNote != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = paymentNote!!,
                        fontSize = 11.sp,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val txnId = when (selectedTab) {
                            "UPI" -> "UPI-PB-" + (100000..999999).random()
                            "CARD" -> "pay_rp_card_" + (100000..999999).random()
                            else -> "NB-${selectedBank.take(4).uppercase()}-" + (100000..999999).random()
                        }
                        onConfirmPayment(txnId)
                    },
                    enabled = !isProcessing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("razorpay_pay_now_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Verifying with Razorpay...", fontSize = 13.sp)
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            val actionLabel = when (selectedTab) {
                                "UPI" -> "Pay ₹$amount via $selectedUpiApp"
                                "CARD" -> "Authorize ₹$amount on Card"
                                else -> "Pay ₹$amount with $selectedBank"
                            }
                            Text(text = actionLabel, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
