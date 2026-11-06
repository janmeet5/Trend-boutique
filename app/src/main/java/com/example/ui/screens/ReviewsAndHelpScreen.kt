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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RegionalReview
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
fun ReviewsAndHelpScreen(
    viewModel: BoutiqueViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val reviews = viewModel.reviews
    val userName by viewModel.userName.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()
    val userCity by viewModel.userCity.collectAsState()

    var orderSmsNotifications by remember { mutableStateOf(true) }
    var whatsappUpdates by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BurgundyDark)
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "Customer Support & Reviews",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Punjab's trusted custom ethnic tailoring boutique",
                            fontSize = 11.sp,
                            color = WarmGold
                        )
                    }
                }
            }

            // User Profile Overview Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(BurgundyPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userName.take(1),
                                color = WarmGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userName,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = CharcoalText
                            )
                            Text(
                                text = "$userPhone • $userCity, Punjab",
                                fontSize = 12.sp,
                                color = CharcoalMuted
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(GoldContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Verified Patron",
                                fontSize = 10.sp,
                                color = WarmGoldDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Consult Master Tailor Call Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = GoldContainer),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderGold)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(BurgundyPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    tint = WarmGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Request Expert Tailor Call Back",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = BurgundyPrimary
                                )
                                Text(
                                    text = "Speak with Master Gurwinder Singh at your preferred time",
                                    fontSize = 11.sp,
                                    color = CharcoalMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Need styling advice on neckline depth, heavy Patiala salwar pleats, or fabric suitability? Pick a convenient time and our master tailor will personally call you.",
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = CharcoalText
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.openTailorCallModal() },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("help_request_callback_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = BurgundyPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Request Call Back", fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    val uri = Uri.parse("https://wa.me/919876543210?text=Hello%20The%20Trend%20Boutique,%20I%20need%20styling%20guidance.")
                                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("help_whatsapp_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "WhatsApp Chat", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Notification Preferences
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Notification Preferences",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = CharcoalText
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "WhatsApp Tailoring Alerts", fontSize = 13.sp, color = CharcoalText)
                                Text(text = "Live updates when fabric is cut & dispatched", fontSize = 11.sp, color = CharcoalMuted)
                            }
                            Switch(
                                checked = whatsappUpdates,
                                onCheckedChange = { whatsappUpdates = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = BurgundyPrimary, checkedTrackColor = BlushSoft)
                            )
                        }

                        HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "SMS Order Updates", fontSize = 13.sp, color = CharcoalText)
                                Text(text = "Rider contact number & OTP alerts", fontSize = 11.sp, color = CharcoalMuted)
                            }
                            Switch(
                                checked = orderSmsNotifications,
                                onCheckedChange = { orderSmsNotifications = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = BurgundyPrimary, checkedTrackColor = BlushSoft)
                            )
                        }
                    }
                }
            }

            // Punjab Atelier Workshop Locations
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CreamSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Boutique Workshops in Punjab",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = BurgundyPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = BurgundyPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Ludhiana: Master Atelier, Sarabha Nagar Main Market",
                                fontSize = 12.sp,
                                color = CharcoalText
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = BurgundyPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Amritsar: Heritage Bridal Studio, Court Road",
                                fontSize = 12.sp,
                                color = CharcoalText
                            )
                        }
                    }
                }
            }

            // Customer Reviews & Regional Testimonials Section
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Text(
                        text = "Patron Love Across Punjab",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = BurgundyPrimary
                    )
                    Text(
                        text = "Client feedback from Ludhiana, Amritsar, Jalandhar & Patiala",
                        fontSize = 11.sp,
                        color = CharcoalMuted
                    )
                }
            }

            // Reviews list
            items(reviews, key = { it.id }) { review ->
                RegionalReviewCard(review = review)
            }
        }
    }
}

@Composable
fun RegionalReviewCard(review: RegionalReview) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = review.name,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = CharcoalText
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = BurgundyPrimary, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = review.city,
                            fontSize = 11.sp,
                            color = BurgundyPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = StarGold,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "“${review.quote}”",
                fontFamily = FontFamily.Serif,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = CharcoalText
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Suit: ${review.suitStyle}",
                    fontSize = 10.sp,
                    color = CharcoalMuted,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = review.timeAgo,
                    fontSize = 10.sp,
                    color = CharcoalMuted
                )
            }
        }
    }
}
