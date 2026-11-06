package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import com.example.ui.components.AddressSelectorSheet
import com.example.ui.components.BoutiqueBottomBar
import com.example.ui.components.BoutiqueTopBar
import com.example.ui.components.TailorCallDialog
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderConfirmationScreen
import com.example.ui.screens.OrderTrackerScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ReviewsAndHelpScreen
import com.example.ui.screens.SavedProfilesScreen
import com.example.ui.screens.SmartMeasurementScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.TrendBoutiqueTheme
import com.example.viewmodel.BoutiqueViewModel
import com.example.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val databaseId = androidx.compose.ui.res.stringResource(id = R.string.firestore_database_id)
            val db = androidx.compose.runtime.remember { com.google.firebase.firestore.FirebaseFirestore.getInstance(databaseId) }
            val firestoreRepo = androidx.compose.runtime.remember { com.example.data.FirestoreBoutiqueRepository(db) }
            val viewModel: BoutiqueViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = androidx.lifecycle.viewmodel.viewModelFactory {
                    initializer {
                        BoutiqueViewModel(firestoreRepo = firestoreRepo)
                    }
                }
            )
            val isDarkMode by viewModel.isDarkMode.collectAsState()

            TrendBoutiqueTheme(darkTheme = isDarkMode) {
                BoutiqueApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BoutiqueApp(
    viewModel: BoutiqueViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val userCity by viewModel.userCity.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()
    val selectedAddress by viewModel.selectedAddress.collectAsState()
    val savedAddresses by viewModel.savedAddresses.collectAsState()
    val isAddressModalOpen by viewModel.isAddressModalOpen.collectAsState()
    val addressStatusMessage by viewModel.addressStatusMessage.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val punjabCities = viewModel.punjabCities
    val isTailorCallModalOpen by viewModel.isTailorCallModalOpen.collectAsState()
    val tailorCallSuccessMessage by viewModel.tailorCallSuccessMessage.collectAsState()

    // Android Hardware / Gesture BackHandler
    BackHandler(enabled = currentScreen != Screen.HOME && currentScreen != Screen.SPLASH) {
        if (!viewModel.navigateBack()) {
            viewModel.navigateTo(Screen.HOME)
        }
    }

    val showBottomBar = currentScreen == Screen.HOME ||
            currentScreen == Screen.ORDER_TRACKER ||
            currentScreen == Screen.SAVED_PROFILES ||
            currentScreen == Screen.REVIEWS

    val showTopBar = currentScreen == Screen.HOME

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            if (showTopBar) {
                BoutiqueTopBar(
                    selectedAddress = selectedAddress,
                    ordersCount = orders.size,
                    isDarkMode = isDarkMode,
                    onAddressClick = { viewModel.openAddressModal() },
                    onOrdersClick = { viewModel.navigateTo(Screen.ORDER_TRACKER) },
                    onTailorClick = { viewModel.openTailorCallModal() },
                    onToggleTheme = { viewModel.toggleDarkMode() }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BoutiqueBottomBar(
                    currentScreen = currentScreen,
                    activeOrdersCount = orders.count { it.status.stepIndex < 5 },
                    onTabSelected = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.SPLASH -> {
                    SplashScreen(
                        onExploreClick = { viewModel.navigateTo(Screen.HOME) },
                        onLoginClick = { viewModel.navigateTo(Screen.AUTH) }
                    )
                }

                Screen.AUTH -> {
                    AuthScreen(viewModel = viewModel)
                }

                Screen.HOME -> {
                    HomeScreen(viewModel = viewModel)
                }

                Screen.PRODUCT_DETAIL -> {
                    ProductDetailScreen(viewModel = viewModel)
                }

                Screen.SMART_MEASUREMENT -> {
                    SmartMeasurementScreen(viewModel = viewModel)
                }

                Screen.CHECKOUT -> {
                    CheckoutScreen(viewModel = viewModel)
                }

                Screen.ORDER_CONFIRMATION -> {
                    OrderConfirmationScreen(viewModel = viewModel)
                }

                Screen.ORDER_TRACKER -> {
                    OrderTrackerScreen(viewModel = viewModel)
                }

                Screen.SAVED_PROFILES -> {
                    SavedProfilesScreen(viewModel = viewModel)
                }

                Screen.REVIEWS -> {
                    ReviewsAndHelpScreen(viewModel = viewModel)
                }
            }
        }
    }

    // Top Location / Address Selector Modal Sheet
    if (isAddressModalOpen) {
        AddressSelectorSheet(
            selectedAddress = selectedAddress,
            savedAddresses = savedAddresses,
            punjabCities = punjabCities,
            statusMessage = addressStatusMessage,
            onDismiss = { viewModel.closeAddressModal() },
            onSelectAddress = { addr -> viewModel.selectDeliveryAddress(addr) },
            onDetectCurrentLocation = { viewModel.detectGpsCurrentLocation() },
            onSaveGoogleMapsLink = { link, landmark -> viewModel.parseAndSetGoogleMapsLink(link, landmark) },
            onSaveManualAddress = { addr -> viewModel.addNewDeliveryAddress(addr) }
        )
    }

    // Global Tailor Consultation Call Dialog
    if (isTailorCallModalOpen) {
        TailorCallDialog(
            initialName = userName,
            initialPhone = userPhone,
            initialCity = userCity,
            punjabCities = punjabCities,
            successMessage = tailorCallSuccessMessage,
            onDismiss = { viewModel.closeTailorCallModal() },
            onSubmit = { name, phone, city, slot, language, topic ->
                viewModel.submitTailorCallRequest(name, phone, city, slot, language, topic)
            }
        )
    }
}
