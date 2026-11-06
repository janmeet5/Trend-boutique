package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ui.theme.BurgundyDark
import com.example.ui.theme.BurgundyPrimary
import com.example.ui.theme.GlowGold
import com.example.ui.theme.GlowPeach
import com.example.ui.theme.GlowSunset
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightCard
import com.example.ui.theme.SoftAmberContainer
import com.example.ui.theme.SoftGreenContainer
import com.example.ui.theme.SoftPeachContainer
import com.example.ui.theme.StarGold
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenLight
import com.example.viewmodel.BoutiqueViewModel

@Composable
fun SmartMeasurementScreen(
    viewModel: BoutiqueViewModel,
    modifier: Modifier = Modifier
) {
    val activeChoice by viewModel.activeSizingChoice.collectAsState()
    val savedProfiles by viewModel.savedProfiles.collectAsState()

    var selectedMode by remember {
        mutableStateOf(
            when (activeChoice) {
                is SizingChoice.SavedProfile -> "SAVED"
                is SizingChoice.TailorCall -> "CALL"
                is SizingChoice.DoorstepSample -> "SAMPLE"
            }
        )
    }

    // State for Option A (Saved Size)
    var selectedProfileId by remember {
        mutableStateOf(savedProfiles.firstOrNull()?.id ?: "")
    }

    // State for Option B (Call)
    var callSlot by remember { mutableStateOf("Morning (10 AM – 1 PM)") }
    var callLanguage by remember { mutableStateOf("Punjabi (ਪੰਜਾਬੀ)") }
    var callNotes by remember { mutableStateOf("Please advise on Patiala salwar gher and neck depth") }

    // State for Option C (Doorstep Reference Sample Pickup)
    var sampleSuitsCount by remember { mutableIntStateOf(1) }
    var sampleAdjustmentNotes by remember {
        mutableStateOf("Exact same fit as my reference suit. Just keep kurta length 1 inch longer.")
    }
    var saveFitForNextTime by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                    .background(MaterialTheme.colorScheme.surface)
                    .border(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.testTag("smart_measurement_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Column {
                    Text(
                        text = "Smart Fitting & Measurement",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Zero measuring tape needed! How should we take your fit?",
                        fontSize = 11.sp,
                        color = BurgundyPrimary
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // FIRST TIME CUSTOMER NOTICE CALLOUT
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(SoftPeachContainer)
                        .border(1.dp, GlowPeach.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCut,
                                contentDescription = null,
                                tint = BurgundyPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "First Time Stitching With The Trend Boutique?",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Most Punjab customers simply hand over their favorite fitting suit to our rider (Option 1), or schedule an expert tailor call (Option 2). We stitch to perfection and save your size forever!",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // OPTION 1: DOORSTEP REFERENCE SAMPLE PICKUP (MOST POPULAR FOR FIRST TIMERS)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = if (selectedMode == "SAMPLE") 1.8.dp else 0.8.dp,
                            color = if (selectedMode == "SAMPLE") BurgundyPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            selectedMode = "SAMPLE"
                            viewModel.setSizingChoice(
                                SizingChoice.DoorstepSample(
                                    sampleCount = sampleSuitsCount,
                                    fitAdjustmentNotes = sampleAdjustmentNotes,
                                    saveFitToProfile = saveFitForNextTime
                                )
                            )
                        }
                        .testTag("sizing_option_sample_card"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedMode == "SAMPLE") SoftPeachContainer else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (selectedMode == "SAMPLE") 3.dp else 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                RadioButton(
                                    selected = selectedMode == "SAMPLE",
                                    onClick = {
                                        selectedMode = "SAMPLE"
                                        viewModel.setSizingChoice(
                                            SizingChoice.DoorstepSample(
                                                sampleCount = sampleSuitsCount,
                                                fitAdjustmentNotes = sampleAdjustmentNotes,
                                                saveFitToProfile = saveFitForNextTime
                                            )
                                        )
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = BurgundyPrimary,
                                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "1. Send a Reference Fitting Suit",
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Rider picks up your best-fitting suit from home",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SuccessGreen)
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "FREE PICKUP",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Expanded controls when selected
                        if (selectedMode == "SAMPLE") {
                            Spacer(modifier = Modifier.height(14.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "How many reference suits will you hand over?",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                                    .clickable {
                                                        if (sampleSuitsCount > 1) {
                                                            sampleSuitsCount--
                                                            viewModel.setSizingChoice(
                                                                SizingChoice.DoorstepSample(
                                                                    sampleCount = sampleSuitsCount,
                                                                    fitAdjustmentNotes = sampleAdjustmentNotes,
                                                                    saveFitToProfile = saveFitForNextTime
                                                                )
                                                            )
                                                        }
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("-", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                            }

                                            Text(
                                                text = "$sampleSuitsCount suit",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp)
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(CircleShape)
                                                    .background(BurgundyPrimary)
                                                    .clickable {
                                                        if (sampleSuitsCount < 3) {
                                                            sampleSuitsCount++
                                                            viewModel.setSizingChoice(
                                                                SizingChoice.DoorstepSample(
                                                                    sampleCount = sampleSuitsCount,
                                                                    fitAdjustmentNotes = sampleAdjustmentNotes,
                                                                    saveFitToProfile = saveFitForNextTime
                                                                )
                                                            )
                                                        }
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("+", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = sampleAdjustmentNotes,
                                        onValueChange = {
                                            sampleAdjustmentNotes = it
                                            viewModel.setSizingChoice(
                                                SizingChoice.DoorstepSample(
                                                    sampleCount = sampleSuitsCount,
                                                    fitAdjustmentNotes = it,
                                                    saveFitToProfile = saveFitForNextTime
                                                )
                                            )
                                        },
                                        label = { Text("Fit adjustment notes (optional)", fontSize = 11.sp) },
                                        placeholder = { Text("e.g. Same fit as sample, but keep sleeves 1 inch longer") },
                                        modifier = Modifier.fillMaxWidth().testTag("sample_adjustment_notes_input"),
                                        maxLines = 2,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = BurgundyPrimary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable {
                                            saveFitForNextTime = !saveFitForNextTime
                                            viewModel.setSizingChoice(
                                                SizingChoice.DoorstepSample(
                                                    sampleCount = sampleSuitsCount,
                                                    fitAdjustmentNotes = sampleAdjustmentNotes,
                                                    saveFitToProfile = saveFitForNextTime
                                                )
                                            )
                                        }
                                    ) {
                                        Checkbox(
                                            checked = saveFitForNextTime,
                                            onCheckedChange = {
                                                saveFitForNextTime = it
                                                viewModel.setSizingChoice(
                                                    SizingChoice.DoorstepSample(
                                                        sampleCount = sampleSuitsCount,
                                                        fitAdjustmentNotes = sampleAdjustmentNotes,
                                                        saveFitToProfile = it
                                                    )
                                                )
                                            },
                                            colors = CheckboxDefaults.colors(checkedColor = BurgundyPrimary)
                                        )
                                        Text(
                                            text = "Save this fit as 'My Saved Size' for 1-click reorders in future",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // OPTION 2: TALK TO MASTERJI (EXPERT CALL)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = if (selectedMode == "CALL") 1.8.dp else 0.8.dp,
                            color = if (selectedMode == "CALL") BurgundyPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            selectedMode = "CALL"
                            viewModel.setSizingChoice(
                                SizingChoice.TailorCall(
                                    preferredSlot = callSlot,
                                    language = callLanguage,
                                    notes = callNotes
                                )
                            )
                        }
                        .testTag("sizing_option_call_card"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedMode == "CALL") SoftAmberContainer else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (selectedMode == "CALL") 3.dp else 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                RadioButton(
                                    selected = selectedMode == "CALL",
                                    onClick = {
                                        selectedMode = "CALL"
                                        viewModel.setSizingChoice(
                                            SizingChoice.TailorCall(
                                                preferredSlot = callSlot,
                                                language = callLanguage,
                                                notes = callNotes
                                            )
                                        )
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = BurgundyPrimary,
                                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "2. Talk to Masterji (Tailor Consultation)",
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Free video/phone call to guide neck, gher & sleeves",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF9A6000))
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "FREE CALL",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        if (selectedMode == "CALL") {
                            Spacer(modifier = Modifier.height(14.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Preferred Language:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        listOf("Punjabi (ਪੰਜਾਬੀ)", "Hindi (हिंदी)", "English").forEach { lang ->
                                            val isLangSelected = callLanguage == lang
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(if (isLangSelected) BurgundyPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                                    .clickable {
                                                        callLanguage = lang
                                                        viewModel.setSizingChoice(
                                                            SizingChoice.TailorCall(
                                                                preferredSlot = callSlot,
                                                                language = lang,
                                                                notes = callNotes
                                                            )
                                                        )
                                                    }
                                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                            ) {
                                                Text(
                                                    text = lang,
                                                    fontSize = 11.sp,
                                                    fontWeight = if (isLangSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isLangSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = callNotes,
                                        onValueChange = {
                                            callNotes = it
                                            viewModel.setSizingChoice(
                                                SizingChoice.TailorCall(
                                                    preferredSlot = callSlot,
                                                    language = callLanguage,
                                                    notes = it
                                                )
                                            )
                                        },
                                        label = { Text("What would you like to discuss with Masterji?", fontSize = 11.sp) },
                                        modifier = Modifier.fillMaxWidth().testTag("tailor_call_notes_input"),
                                        maxLines = 2,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = BurgundyPrimary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // OPTION 3: SAVED MEASUREMENT PROFILE (1-CLICK REORDER)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = if (selectedMode == "SAVED") 1.8.dp else 0.8.dp,
                            color = if (selectedMode == "SAVED") BurgundyPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            selectedMode = "SAVED"
                            if (savedProfiles.isNotEmpty()) {
                                viewModel.setSizingChoice(SizingChoice.SavedProfile(selectedProfileId))
                            }
                        }
                        .testTag("sizing_option_saved_card"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedMode == "SAVED") SoftGreenContainer else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (selectedMode == "SAVED") 3.dp else 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                RadioButton(
                                    selected = selectedMode == "SAVED",
                                    onClick = {
                                        selectedMode = "SAVED"
                                        if (savedProfiles.isNotEmpty()) {
                                            viewModel.setSizingChoice(SizingChoice.SavedProfile(selectedProfileId))
                                        }
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = BurgundyPrimary,
                                        unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "3. Use Saved Measurement Profile",
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Instant 30-sec order using your previously tailored size",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SuccessGreen)
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "${savedProfiles.size} Saved",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        if (selectedMode == "SAVED") {
                            Spacer(modifier = Modifier.height(14.dp))

                            savedProfiles.forEach { profile ->
                                val isProfileSelected = profile.id == selectedProfileId
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White)
                                        .border(
                                            1.2.dp,
                                            if (isProfileSelected) BurgundyPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable {
                                            selectedProfileId = profile.id
                                            viewModel.setSizingChoice(SizingChoice.SavedProfile(profile.id))
                                        }
                                        .padding(14.dp)
                                ) {
                                    Column {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = profile.label,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            if (profile.isDefault) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(SoftPeachContainer)
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "Default",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = BurgundyPrimary
                                                    )
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Bust: ${profile.bust}\" • Waist: ${profile.waist}\" • Hips: ${profile.hips}\" • Kurta: ${profile.kurtaLength}\" • Bottom: ${profile.bottomLength}\"",
                                            fontSize = 11.sp,
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

        // Sticky Bottom CTA: Continue to Pickup / Checkout
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(
                0.8.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Selected Fit Method",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = when (selectedMode) {
                            "SAVED" -> "Saved Fit Profile"
                            "CALL" -> "Expert Tailor Call"
                            else -> "Doorstep Sample Pickup"
                        },
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = BurgundyPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(BurgundyPrimary)
                        .clickable { viewModel.proceedToCheckout() }
                        .padding(horizontal = 18.dp)
                        .testTag("continue_to_checkout_cta"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Proceed to Pickup",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
