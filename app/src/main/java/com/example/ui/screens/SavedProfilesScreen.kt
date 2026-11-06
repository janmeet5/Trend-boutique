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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MeasurementProfile
import com.example.ui.components.MeasurementProfileDialog
import com.example.ui.theme.BlushRose
import com.example.ui.theme.BorderGold
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BurgundyDark
import com.example.ui.theme.BurgundyDeep
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.CharcoalText
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamCard
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.GoldContainer
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCard
import com.example.ui.theme.GeminiPillBorderBrush
import com.example.ui.theme.GlowGold
import com.example.ui.theme.GlowPeach
import com.example.ui.theme.WarmGold
import com.example.ui.theme.WarmGoldDark

import com.example.viewmodel.BoutiqueViewModel

@Composable
fun SavedProfilesScreen(
    viewModel: BoutiqueViewModel,
    modifier: Modifier = Modifier
) {
    val profiles by viewModel.savedProfiles.collectAsState()
    val isEditModalOpen by viewModel.isProfileEditModalOpen.collectAsState()
    val profileBeingEdited by viewModel.profileBeingEdited.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BurgundyDark)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Saved Sizing Profiles",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Your custom fits for 1-click Punjabi suit orders",
                        fontSize = 11.sp,
                        color = WarmGold
                    )
                }

                Button(
                    onClick = { viewModel.openNewProfileDialog() },
                    colors = ButtonDefaults.buttonColors(containerColor = WarmGold),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("add_new_profile_btn")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = BurgundyDeep, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Add Fit", color = BurgundyDeep, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val selectedAddress by viewModel.selectedAddress.collectAsState()

            // APP SETTINGS CARD (Dark Theme Toggle & Address Shortcut)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CreamCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = null,
                                tint = BurgundyPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "App Theme",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = CharcoalText
                                )
                                Text(
                                    text = if (isDarkMode) "Midnight Dark Mode (Active)" else "Classic Boutique Light (Active)",
                                    fontSize = 11.sp,
                                    color = BurgundyPrimary
                                )
                            }
                        }

                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { viewModel.toggleDarkMode() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = BurgundyPrimary,
                                uncheckedThumbColor = BurgundyPrimary,
                                uncheckedTrackColor = BorderSubtle
                            ),
                            modifier = Modifier.testTag("app_settings_theme_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = BorderSubtle)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.openAddressModal() },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = BurgundyPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Punjab Pickup & Delivery Addresses",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = CharcoalText
                                )
                                Text(
                                    text = "${selectedAddress.label}: ${selectedAddress.addressLine}, ${selectedAddress.city}",
                                    fontSize = 11.sp,
                                    color = CharcoalMuted,
                                    maxLines = 1
                                )
                            }
                        }

                        Text(
                            text = "Manage →",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GlowPeach,
                            modifier = Modifier.testTag("manage_addresses_btn")
                        )
                    }
                }
            }

            // Explanatory Banner

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GoldContainer)
                    .border(1.dp, BorderGold, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Straighten,
                        contentDescription = null,
                        tint = WarmGoldDark,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Profiles are automatically created when our master tailor measures your reference sample suit. You can also label and edit them manually anytime.",
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        color = CharcoalText
                    )
                }
            }

            // Profiles List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(profiles, key = { it.id }) { profile ->
                    ProfileCard(
                        profile = profile,
                        onEdit = { viewModel.editProfile(profile) },
                        onDelete = { viewModel.deleteProfile(profile.id) }
                    )
                }
            }
        }

        // Profile Editor / Creator Dialog
        if (isEditModalOpen && profileBeingEdited != null) {
            MeasurementProfileDialog(
                profile = profileBeingEdited!!,
                onDismiss = { viewModel.closeProfileDialog() },
                onSave = { updated -> viewModel.saveMeasurementProfile(updated) }
            )
        }
    }
}

@Composable
fun ProfileCard(
    profile: MeasurementProfile,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("profile_card_${profile.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(BurgundyPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = WarmGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = profile.label,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = CharcoalText
                            )
                            if (profile.isDefault) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GoldContainer)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Default Fit",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = WarmGoldDark
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Last updated: ${profile.lastUpdated}",
                            fontSize = 11.sp,
                            color = CharcoalMuted
                        )
                    }
                }

                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = BurgundyPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Profile",
                            tint = CharcoalMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3x3 Grid of core measurements in inches
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MeasurementBadge(label = "Bust", value = "${profile.bust}\"")
                MeasurementBadge(label = "Waist", value = "${profile.waist}\"")
                MeasurementBadge(label = "Hips", value = "${profile.hips}\"")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MeasurementBadge(label = "Kurta Length", value = "${profile.kurtaLength}\"")
                MeasurementBadge(label = "Bottom Length", value = "${profile.bottomLength}\"")
                MeasurementBadge(label = "Neck Depth", value = "${profile.neckDepth}\"")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MeasurementBadge(label = "Sleeves", value = "${profile.sleeveLength}\"")
                MeasurementBadge(label = "Shoulder", value = "${profile.shoulder}\"")
                MeasurementBadge(label = "Armhole", value = "${profile.armhole}\"")
            }

            if (profile.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = BorderSubtle)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Notes: ${profile.notes}",
                    fontSize = 11.sp,
                    color = CharcoalMuted
                )
            }
        }
    }
}

@Composable
fun MeasurementBadge(label: String, value: String) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CreamSurface)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, fontSize = 10.sp, color = CharcoalMuted)
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BurgundyPrimary)
        }
    }
}
