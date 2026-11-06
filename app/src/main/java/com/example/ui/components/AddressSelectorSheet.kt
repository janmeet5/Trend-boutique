package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeliveryAddress
import com.example.ui.theme.BurgundyDark
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GlowGold
import com.example.ui.theme.GlowPeach
import com.example.ui.theme.GlowSunset
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightCard
import com.example.ui.theme.SoftPeachContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSelectorSheet(
    selectedAddress: DeliveryAddress,
    savedAddresses: List<DeliveryAddress>,
    punjabCities: List<String>,
    statusMessage: String?,
    onDismiss: () -> Unit,
    onSelectAddress: (DeliveryAddress) -> Unit,
    onDetectCurrentLocation: () -> Unit,
    onSaveGoogleMapsLink: (String, String) -> Unit,
    onSaveManualAddress: (DeliveryAddress) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var activeTab by remember { mutableStateOf("SAVED") } // SAVED, GPS, MAPS_LINK, MANUAL

    // Manual Form Inputs
    var manualAddressLine by remember { mutableStateOf("") }
    var manualCity by remember { mutableStateOf("Ludhiana") }
    var manualPincode by remember { mutableStateOf("141001") }
    var manualLandmark by remember { mutableStateOf("") }

    // Maps Link Input
    var mapsLinkInput by remember { mutableStateOf("") }
    var mapsLandmarkInput by remember { mutableStateOf("") }

    val infiniteTransition = rememberInfiniteTransition(label = "gps_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null,
        modifier = modifier.testTag("address_selector_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SoftPeachContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = BurgundyPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Pickup & Delivery Location",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Doorstep fabric & sample suit pickup across Punjab",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_address_sheet_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Status notification if active
            if (statusMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SuccessGreenLight)
                        .border(1.dp, SuccessGreen.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = statusMessage,
                            fontSize = 12.sp,
                            color = SuccessGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Method Selector Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Tab 1: Current GPS
                val isGps = activeTab == "GPS"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isGps) BurgundyPrimary else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { activeTab = "GPS" }
                        .padding(vertical = 10.dp, horizontal = 6.dp)
                        .testTag("tab_gps_location"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "GPS",
                            tint = if (isGps) Color.White else BurgundyPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "GPS Location",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isGps) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Tab 2: Google Maps Link
                val isMaps = activeTab == "MAPS_LINK"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isMaps) BurgundyPrimary else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { activeTab = "MAPS_LINK" }
                        .padding(vertical = 10.dp, horizontal = 6.dp)
                        .testTag("tab_maps_link"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = "Maps Link",
                            tint = if (isMaps) Color.White else BurgundyPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Maps Link",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isMaps) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Tab 3: Enter Manually
                val isManual = activeTab == "MANUAL"
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isManual) BurgundyPrimary else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { activeTab = "MANUAL" }
                        .padding(vertical = 10.dp, horizontal = 6.dp)
                        .testTag("tab_manual_address"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.EditLocation,
                            contentDescription = "Manual",
                            tint = if (isManual) Color.White else BurgundyPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Manual Entry",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isManual) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TAB 1: GPS Auto-Detect
            AnimatedVisibility(visible = activeTab == "GPS") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftPeachContainer),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlowPeach.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .scale(pulseScale)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.5.dp, BurgundyPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "GPS Radar",
                                tint = BurgundyPrimary,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "One-Tap Live GPS Location",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Automatically locates your house or shop in Punjab for precise doorstep sample suit and fabric pickup.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 17.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(BurgundyPrimary)
                                .clickable { onDetectCurrentLocation() }
                                .testTag("btn_detect_gps"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Detect My Current Punjab Location",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // TAB 2: Google Maps Link
            AnimatedVisibility(visible = activeTab == "MAPS_LINK") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Paste Google Maps Pin / Location Link",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Share location link from Google Maps app (e.g. https://maps.app.goo.gl/...)",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = mapsLinkInput,
                            onValueChange = { mapsLinkInput = it },
                            placeholder = { Text("https://maps.app.goo.gl/...", fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Link, contentDescription = null, tint = BurgundyPrimary)
                            },
                            modifier = Modifier.fillMaxWidth().testTag("maps_link_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = BurgundyPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = mapsLandmarkInput,
                            onValueChange = { mapsLandmarkInput = it },
                            placeholder = { Text("House no., building name, or landmark (optional)", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth().testTag("maps_landmark_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = BurgundyPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(BurgundyPrimary)
                                .clickable {
                                    if (mapsLinkInput.isNotBlank()) {
                                        onSaveGoogleMapsLink(mapsLinkInput, mapsLandmarkInput)
                                    }
                                }
                                .testTag("btn_save_maps_link"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Verify & Pin This Address",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // TAB 3: Enter Manually
            AnimatedVisibility(visible = activeTab == "MANUAL") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Manual Punjab Address Details",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = manualAddressLine,
                            onValueChange = { manualAddressLine = it },
                            label = { Text("House No., Street, Colony", fontSize = 12.sp) },
                            placeholder = { Text("e.g. 142 Model Town / Sarabha Nagar") },
                            modifier = Modifier.fillMaxWidth().testTag("manual_address_line_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = BurgundyPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = manualLandmark,
                            onValueChange = { manualLandmark = it },
                            label = { Text("Nearby Landmark", fontSize = 12.sp) },
                            placeholder = { Text("e.g. Near Gurdwara Sahib / Clock Tower") },
                            modifier = Modifier.fillMaxWidth().testTag("manual_landmark_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = BurgundyPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // City selector chips
                        Text(
                            text = "Select City in Punjab:",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            punjabCities.take(8).forEach { city ->
                                val isCitySelected = city == manualCity
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (isCitySelected) BurgundyPrimary else MaterialTheme.colorScheme.surface
                                        )
                                        .border(
                                            1.dp,
                                            if (isCitySelected) BurgundyPrimary else MaterialTheme.colorScheme.outline,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable { manualCity = city }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = city,
                                        fontSize = 11.sp,
                                        fontWeight = if (isCitySelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isCitySelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = manualPincode,
                            onValueChange = { manualPincode = it },
                            label = { Text("Pincode", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth().testTag("manual_pincode_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = BurgundyPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(BurgundyPrimary)
                                .clickable {
                                    val newAddr = DeliveryAddress(
                                        fullName = selectedAddress.fullName,
                                        phone = selectedAddress.phone,
                                        addressLine = manualAddressLine.ifBlank { "Punjab Address" },
                                        city = manualCity,
                                        pincode = manualPincode.ifBlank { "141001" },
                                        landmark = manualLandmark,
                                        label = "Custom Address"
                                    )
                                    onSaveManualAddress(newAddr)
                                }
                                .testTag("btn_save_manual_address"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Save & Use This Address",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SAVED ADDRESSES SECTION
            Text(
                text = "SAVED ADDRESSES (${savedAddresses.size})",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 1.sp,
                color = BurgundyPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            savedAddresses.forEach { address ->
                val isSelected = address.addressLine == selectedAddress.addressLine && address.city == selectedAddress.city
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.2.dp,
                            if (isSelected) BurgundyPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { onSelectAddress(address) }
                        .testTag("saved_address_${address.label}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) SoftPeachContainer else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) BurgundyPrimary else MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isSelected) Icons.Default.Check else Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else BurgundyPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = address.label,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (isSelected) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Selected",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BurgundyPrimary
                                        )
                                    }
                                }
                                Text(
                                    text = "${address.addressLine}, ${address.city} - ${address.pincode}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                                if (address.landmark.isNotBlank()) {
                                    Text(
                                        text = "Landmark: ${address.landmark}",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
