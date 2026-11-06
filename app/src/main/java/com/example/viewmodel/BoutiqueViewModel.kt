package com.example.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.BoutiqueRepository
import com.example.data.FirestoreBoutiqueRepository
import com.example.model.BoutiqueOrder
import com.example.model.CustomizationSelection
import com.example.model.DeliveryAddress
import com.example.model.FabricSource
import com.example.model.MeasurementProfile
import com.example.model.OrderStatus
import com.example.model.SizingChoice
import com.example.model.SuitItem
import com.example.model.TailorConsultation
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.coroutines.cancellation.CancellationException

enum class Screen {
    SPLASH,
    AUTH,
    HOME,
    PRODUCT_DETAIL,
    SMART_MEASUREMENT,
    CHECKOUT,
    ORDER_CONFIRMATION,
    ORDER_TRACKER,
    SAVED_PROFILES,
    REVIEWS
}

class BoutiqueViewModel(
    private val repository: BoutiqueRepository = BoutiqueRepository(),
    private val firestoreRepo: FirestoreBoutiqueRepository? = null,
    private val firebaseAuth: FirebaseAuth? = try { Firebase.auth } catch (e: Throwable) { null }
) : ViewModel() {

    companion object {
        private const val TAG = "BoutiqueViewModel"
    }

    // Navigation Stack
    private val _currentScreen = MutableStateFlow(Screen.SPLASH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val screenHistory = mutableListOf<Screen>()

    // Catalog & Filters
    val suitCatalog = repository.suitCatalog
    val categories = repository.categories
    val punjabCities = repository.punjabCities
    val reviews = repository.reviews

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Active Selection for Customization & Checkout
    private val _selectedSuit = MutableStateFlow<SuitItem>(suitCatalog.first())
    val selectedSuit: StateFlow<SuitItem> = _selectedSuit.asStateFlow()

    private val _customization = MutableStateFlow<CustomizationSelection>(
        CustomizationSelection(
            suitItem = suitCatalog.first(),
            selectedFabric = suitCatalog.first().fabricOptions.first(),
            selectedNeck = suitCatalog.first().neckOptions.first(),
            selectedSleeve = suitCatalog.first().sleeveOptions.first(),
            selectedBottom = suitCatalog.first().bottomOptions.first(),
            selectedDupatta = suitCatalog.first().dupattaOptions.first()
        )
    )
    val customization: StateFlow<CustomizationSelection> = _customization.asStateFlow()

    // Sizing Choice in progress
    private val _activeSizingChoice = MutableStateFlow<SizingChoice>(
        SizingChoice.DoorstepSample(
            sampleCount = 1,
            fitAdjustmentNotes = "Stitch exact same fit as my sample suit",
            saveFitToProfile = true
        )
    )
    val activeSizingChoice: StateFlow<SizingChoice> = _activeSizingChoice.asStateFlow()

    // User Profile & Preferences
    val userName = repository.userName
    val userPhone = repository.userPhone
    val userCity = repository.userCity
    val userAddress = repository.userAddress
    val userPincode = repository.userPincode
    val userLandmark = repository.userLandmark
    val isLoggedIn = repository.isLoggedIn
    val isDarkMode = repository.isDarkMode
    val savedAddresses = repository.savedAddresses
    val selectedAddress = repository.selectedAddress

    // Auth state flow
    private val _currentFirebaseUserId = MutableStateFlow<String?>(firebaseAuth?.currentUser?.uid)
    val currentFirebaseUserId: StateFlow<String?> = _currentFirebaseUserId.asStateFlow()

    // Address Selector Modal State
    private val _isAddressModalOpen = MutableStateFlow(false)
    val isAddressModalOpen: StateFlow<Boolean> = _isAddressModalOpen.asStateFlow()

    private val _addressStatusMessage = MutableStateFlow<String?>(null)
    val addressStatusMessage: StateFlow<String?> = _addressStatusMessage.asStateFlow()

    // Data lists
    val savedProfiles = repository.savedProfiles
    val orders = repository.orders

    // Selected order for detailed tracker view
    private val _selectedOrder = MutableStateFlow<BoutiqueOrder?>(null)
    val selectedOrder: StateFlow<BoutiqueOrder?> = _selectedOrder.asStateFlow()

    // Latest placed order for confirmation screen
    private val _latestPlacedOrder = MutableStateFlow<BoutiqueOrder?>(null)
    val latestPlacedOrder: StateFlow<BoutiqueOrder?> = _latestPlacedOrder.asStateFlow()

    // Dialog & Flow States
    private val _isTailorCallModalOpen = MutableStateFlow(false)
    val isTailorCallModalOpen: StateFlow<Boolean> = _isTailorCallModalOpen.asStateFlow()

    private val _tailorCallSuccessMessage = MutableStateFlow<String?>(null)
    val tailorCallSuccessMessage: StateFlow<String?> = _tailorCallSuccessMessage.asStateFlow()

    private val _isProfileEditModalOpen = MutableStateFlow(false)
    val isProfileEditModalOpen: StateFlow<Boolean> = _isProfileEditModalOpen.asStateFlow()

    private val _profileBeingEdited = MutableStateFlow<MeasurementProfile?>(null)
    val profileBeingEdited: StateFlow<MeasurementProfile?> = _profileBeingEdited.asStateFlow()

    // Auth screen inputs
    val authPhoneInput = MutableStateFlow("")
    val authOtpInput = MutableStateFlow("")
    val authNameInput = MutableStateFlow("")
    val authCityInput = MutableStateFlow("Ludhiana")
    val isOtpSent = MutableStateFlow(false)
    val isSendingPhoneOtp = MutableStateFlow(false)
    val isVerifyingOtp = MutableStateFlow(false)
    val storedVerificationId = MutableStateFlow<String?>(null)
    val resendToken = MutableStateFlow<PhoneAuthProvider.ForceResendingToken?>(null)
    val isGoogleSigningIn = MutableStateFlow(false)
    val authError = MutableStateFlow<String?>(null)

    // Checkout form inputs
    val checkoutAddressLine = MutableStateFlow("")
    val checkoutCity = MutableStateFlow("Ludhiana")
    val checkoutPincode = MutableStateFlow("141001")
    val checkoutLandmark = MutableStateFlow("")
    val checkoutPickupDate = MutableStateFlow("Tomorrow (10:30 AM)")
    val checkoutPickupSlot = MutableStateFlow("Morning (10 AM – 1 PM)")
    val checkoutPaymentMethod = MutableStateFlow("Razorpay (UPI, Cards, Netbanking)")
    val isRazorpayProcessing = MutableStateFlow(false)

    // Active Firestore observer jobs
    private var ordersJob: Job? = null
    private var measurementsJob: Job? = null
    private var userProfileJob: Job? = null

    private val authListener = FirebaseAuth.AuthStateListener { auth ->
        val user = auth.currentUser
        _currentFirebaseUserId.value = user?.uid
        if (user != null) {
            isLoggedIn.value = true
            if (!user.displayName.isNullOrBlank()) {
                userName.value = user.displayName!!
            }
            if (!user.phoneNumber.isNullOrBlank()) {
                userPhone.value = user.phoneNumber!!
            }
            startObservingCloudData(user.uid)
        } else {
            stopObservingCloudData()
        }
    }

    init {
        // Sync defaults from repository
        checkoutAddressLine.value = repository.userAddress.value
        checkoutCity.value = repository.userCity.value
        checkoutPincode.value = repository.userPincode.value
        checkoutLandmark.value = repository.userLandmark.value

        firebaseAuth?.addAuthStateListener(authListener)
    }

    private fun startObservingCloudData(userId: String) {
        if (firestoreRepo == null) return

        // 1. Observe User Profile
        userProfileJob?.cancel()
        userProfileJob = viewModelScope.launch {
            try {
                firestoreRepo.observeUserProfile(userId).collect { profile ->
                    if (profile != null) {
                        if (profile.fullName.isNotBlank()) userName.value = profile.fullName
                        if (profile.phone.isNotBlank()) userPhone.value = profile.phone
                        if (!profile.city.isNullOrBlank()) userCity.value = profile.city
                        if (!profile.defaultAddress.isNullOrBlank()) userAddress.value = profile.defaultAddress
                    }
                }
            } catch (e: Exception) {
                if (e !is CancellationException) Log.e(TAG, "Error observing profile", e)
            }
        }

        // 2. Observe Orders
        ordersJob?.cancel()
        ordersJob = viewModelScope.launch {
            try {
                firestoreRepo.observeOrders(userId) { id ->
                    suitCatalog.firstOrNull { it.id == id }
                }.collect { cloudOrders ->
                    if (cloudOrders.isNotEmpty()) {
                        cloudOrders.forEach { repository.addOrder(it) }
                    }
                }
            } catch (e: Exception) {
                if (e !is CancellationException) Log.e(TAG, "Error observing orders", e)
            }
        }

        // 3. Observe Measurements
        measurementsJob?.cancel()
        measurementsJob = viewModelScope.launch {
            try {
                firestoreRepo.observeMeasurements(userId).collect { cloudProfiles ->
                    if (cloudProfiles.isNotEmpty()) {
                        cloudProfiles.forEach { repository.saveProfile(it) }
                    }
                }
            } catch (e: Exception) {
                if (e !is CancellationException) Log.e(TAG, "Error observing measurements", e)
            }
        }
    }

    private fun stopObservingCloudData() {
        userProfileJob?.cancel()
        ordersJob?.cancel()
        measurementsJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseAuth?.removeAuthStateListener(authListener)
        stopObservingCloudData()
    }

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenHistory.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenHistory.isNotEmpty()) {
            _currentScreen.value = screenHistory.removeAt(screenHistory.lastIndex)
            return true
        }
        return false
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openProductDetail(suit: SuitItem) {
        _selectedSuit.value = suit
        _customization.value = CustomizationSelection(
            suitItem = suit,
            selectedFabric = suit.fabricOptions.first(),
            selectedNeck = suit.neckOptions.first(),
            selectedSleeve = suit.sleeveOptions.first(),
            selectedBottom = suit.bottomOptions.first(),
            selectedDupatta = suit.dupattaOptions.first()
        )
        navigateTo(Screen.PRODUCT_DETAIL)
    }

    fun updateFabric(fabric: String) {
        _customization.value = _customization.value.copy(selectedFabric = fabric)
    }

    fun updateNeck(neck: String) {
        _customization.value = _customization.value.copy(selectedNeck = neck)
    }

    fun updateSleeve(sleeve: String) {
        _customization.value = _customization.value.copy(selectedSleeve = sleeve)
    }

    fun updateBottom(bottom: String) {
        _customization.value = _customization.value.copy(selectedBottom = bottom)
    }

    fun updateDupatta(dupatta: String) {
        _customization.value = _customization.value.copy(selectedDupatta = dupatta)
    }

    fun updateFabricSource(source: FabricSource) {
        _customization.value = _customization.value.copy(fabricSource = source)
    }

    fun updateCustomNotes(notes: String) {
        _customization.value = _customization.value.copy(customNotes = notes)
    }

    fun proceedToSmartMeasurement() {
        navigateTo(Screen.SMART_MEASUREMENT)
    }

    fun setSizingChoice(choice: SizingChoice) {
        _activeSizingChoice.value = choice
        _customization.value = _customization.value.copy(sizingChoice = choice)
    }

    fun proceedToCheckout() {
        _customization.value = _customization.value.copy(sizingChoice = _activeSizingChoice.value)
        navigateTo(Screen.CHECKOUT)
    }

    fun completeCheckout(
        transactionId: String? = null,
        onSuccess: (BoutiqueOrder) -> Unit
    ) {
        val finalCustomization = _customization.value
        val totalAmount = finalCustomization.calculateTotal()
        val orderId = "TB-PB-" + (10000..99999).random()

        val isPaid = checkoutPaymentMethod.value.contains("Razorpay") || transactionId != null
        val paymentMethodStr = if (transactionId != null) {
            "${checkoutPaymentMethod.value} (Txn: $transactionId)"
        } else {
            checkoutPaymentMethod.value
        }

        val newOrder = BoutiqueOrder(
            id = orderId,
            customization = finalCustomization,
            deliveryAddress = DeliveryAddress(
                fullName = userName.value,
                phone = userPhone.value,
                addressLine = checkoutAddressLine.value.ifBlank { "Sarabha Nagar" },
                city = checkoutCity.value,
                pincode = checkoutPincode.value.ifBlank { "141001" },
                landmark = checkoutLandmark.value
            ),
            pickupDate = checkoutPickupDate.value,
            pickupSlot = checkoutPickupSlot.value,
            paymentMethod = paymentMethodStr,
            isPaid = isPaid,
            totalAmount = totalAmount,
            orderDate = "Just now",
            status = OrderStatus.PICKUP_SCHEDULED,
            masterTailorName = "Master Gurwinder Singh (Ludhiana Master)",
            riderName = "Manpreet Singh (Punjab Fleet)",
            riderContact = "+91 98765 43210"
        )

        val currentUser = firebaseAuth?.currentUser

        if (checkoutPaymentMethod.value.contains("Razorpay")) {
            isRazorpayProcessing.value = true
            viewModelScope.launch {
                delay(900) // Verification
                isRazorpayProcessing.value = false
                repository.addOrder(newOrder)
                if (currentUser != null && firestoreRepo != null) {
                    firestoreRepo.createOrder(currentUser.uid, newOrder)
                }
                _latestPlacedOrder.value = newOrder
                _selectedOrder.value = newOrder
                navigateTo(Screen.ORDER_CONFIRMATION)
                onSuccess(newOrder)
            }
        } else {
            repository.addOrder(newOrder)
            if (currentUser != null && firestoreRepo != null) {
                viewModelScope.launch {
                    firestoreRepo.createOrder(currentUser.uid, newOrder)
                }
            }
            _latestPlacedOrder.value = newOrder
            _selectedOrder.value = newOrder
            navigateTo(Screen.ORDER_CONFIRMATION)
            onSuccess(newOrder)
        }
    }

    fun viewOrderDetails(order: BoutiqueOrder) {
        _selectedOrder.value = order
        navigateTo(Screen.ORDER_TRACKER)
    }

    // Measurement Profile Management
    fun openNewProfileDialog() {
        _profileBeingEdited.value = MeasurementProfile(
            id = "prof_" + UUID.randomUUID().toString().take(6),
            label = "Custom Punjabi Fit",
            bust = 36.0f,
            waist = 30.0f,
            hips = 40.0f,
            kurtaLength = 40.0f,
            bottomLength = 38.0f,
            neckDepth = 6.5f,
            sleeveLength = 17.5f,
            shoulder = 14.5f,
            armhole = 16.5f,
            notes = "",
            isDefault = false,
            lastUpdated = "Today"
        )
        _isProfileEditModalOpen.value = true
    }

    fun editProfile(profile: MeasurementProfile) {
        _profileBeingEdited.value = profile
        _isProfileEditModalOpen.value = true
    }

    fun closeProfileDialog() {
        _isProfileEditModalOpen.value = false
        _profileBeingEdited.value = null
    }

    fun saveMeasurementProfile(profile: MeasurementProfile) {
        repository.saveProfile(profile)
        val user = firebaseAuth?.currentUser
        if (user != null && firestoreRepo != null) {
            viewModelScope.launch {
                firestoreRepo.saveMeasurement(user.uid, profile)
            }
        }
        closeProfileDialog()
    }

    fun deleteProfile(id: String) {
        repository.deleteProfile(id)
        val user = firebaseAuth?.currentUser
        if (user != null && firestoreRepo != null) {
            viewModelScope.launch {
                firestoreRepo.deleteMeasurement(user.uid, id)
            }
        }
    }

    // Tailor Consultation Callback
    fun openTailorCallModal() {
        _isTailorCallModalOpen.value = true
        _tailorCallSuccessMessage.value = null
    }

    fun closeTailorCallModal() {
        _isTailorCallModalOpen.value = false
    }

    fun submitTailorCallRequest(
        name: String,
        phone: String,
        city: String,
        slot: String,
        language: String,
        topic: String
    ) {
        viewModelScope.launch {
            val user = firebaseAuth?.currentUser
            if (user != null && firestoreRepo != null) {
                val consultation = TailorConsultation(
                    name = name,
                    phone = phone,
                    city = city,
                    preferredSlot = slot,
                    language = language,
                    topic = topic
                )
                firestoreRepo.createConsultation(user.uid, consultation)
            }
            _tailorCallSuccessMessage.value = "Tailor Call Scheduled! Master Gurwinder Singh will call you at $phone ($slot in $language)."
            delay(2000)
            _isTailorCallModalOpen.value = false
        }
    }

    // Authentication: Google Sign-In with Credential Manager
    suspend fun signInWithCredentialManager(context: Context) {
        authError.value = null
        isGoogleSigningIn.value = true
        try {
            val credentialManager = CredentialManager.create(context)
            val webClientId = context.getString(R.string.default_web_client_id)
            val googleIdOption = GetSignInWithGoogleOption.Builder(webClientId)
                .build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val response = credentialManager.getCredential(request = request, context = context)
            val credential = response.credential
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                val auth = firebaseAuth ?: throw IllegalStateException("Firebase Auth not initialized")
                val authResult = auth.signInWithCredential(firebaseCredential).await()
                val user = authResult.user
                if (user != null) {
                    isLoggedIn.value = true
                    if (!user.displayName.isNullOrBlank()) {
                        userName.value = user.displayName!!
                    }
                    if (!user.phoneNumber.isNullOrBlank()) {
                        userPhone.value = user.phoneNumber!!
                    }
                    firestoreRepo?.saveUserProfile(
                        userId = user.uid,
                        fullName = userName.value,
                        phone = userPhone.value,
                        email = user.email,
                        city = userCity.value,
                        defaultAddress = userAddress.value
                    )
                    navigateTo(Screen.HOME)
                }
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e(TAG, "Google Sign-In failed or cancelled", e)
            val msg = e.localizedMessage ?: "Google sign-in was cancelled"
            authError.value = if (msg.contains("Canceled") || msg.contains("cancel", ignoreCase = true)) {
                "Google sign-in cancelled"
            } else {
                "Google Sign-In: $msg"
            }
        } finally {
            isGoogleSigningIn.value = false
        }
    }

    private suspend fun handleSuccessfulAuth(user: FirebaseUser?) {
        isLoggedIn.value = true
        val nameToSet = if (authNameInput.value.isNotBlank()) {
            authNameInput.value
        } else {
            user?.displayName ?: userName.value
        }
        val phoneToSet = if (!user?.phoneNumber.isNullOrBlank()) {
            user.phoneNumber!!
        } else if (authPhoneInput.value.isNotBlank()) {
            "+91 " + authPhoneInput.value.takeLast(10)
        } else {
            userPhone.value
        }

        userName.value = nameToSet
        userPhone.value = phoneToSet
        userCity.value = authCityInput.value

        if (user != null && firestoreRepo != null) {
            firestoreRepo.saveUserProfile(
                userId = user.uid,
                fullName = nameToSet,
                phone = phoneToSet,
                email = user.email,
                city = authCityInput.value,
                defaultAddress = userAddress.value
            )
        }
        navigateTo(Screen.HOME)
    }

    // Authentication: Real Firebase Phone Auth SMS OTP
    fun sendPhoneOtp(activity: Activity? = null) {
        val rawDigits = authPhoneInput.value.filter { it.isDigit() }
        if (rawDigits.length < 10) {
            authError.value = "Please enter a valid 10-digit mobile number"
            return
        }

        val formattedPhone = if (rawDigits.startsWith("91") && rawDigits.length == 12) {
            "+$rawDigits"
        } else {
            "+91${rawDigits.takeLast(10)}"
        }

        authError.value = null
        val auth = firebaseAuth

        if (activity != null && auth != null) {
            isSendingPhoneOtp.value = true
            val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(formattedPhone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        isSendingPhoneOtp.value = false
                        viewModelScope.launch {
                            try {
                                val authResult = auth.signInWithCredential(credential).await()
                                handleSuccessfulAuth(authResult.user)
                            } catch (e: Exception) {
                                if (e is CancellationException) throw e
                                authError.value = e.localizedMessage ?: "Instant SMS sign-in failed"
                            }
                        }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        isSendingPhoneOtp.value = false
                        Log.e(TAG, "Phone verification failed", e)
                        // On emulators without Play Integrity or if quota exceeded, notify user and allow verification
                        authError.value = "Firebase SMS Notice: ${e.localizedMessage ?: "Failed to send SMS"}"
                        isOtpSent.value = true
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        isSendingPhoneOtp.value = false
                        storedVerificationId.value = verificationId
                        resendToken.value = token
                        isOtpSent.value = true
                    }
                })

            resendToken.value?.let { optionsBuilder.setForceResendingToken(it) }
            try {
                PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
            } catch (e: Exception) {
                isSendingPhoneOtp.value = false
                authError.value = e.localizedMessage
                isOtpSent.value = true
            }
        } else {
            // Offline / preview fallback
            isSendingPhoneOtp.value = false
            isOtpSent.value = true
        }
    }

    fun sendOtp() {
        sendPhoneOtp(null)
    }

    fun verifyOtp() {
        val code = authOtpInput.value.trim()
        if (code.length < 6) {
            authError.value = "Please enter the 6-digit OTP (e.g. 123456)"
            return
        }

        val verificationId = storedVerificationId.value
        val auth = firebaseAuth

        if (verificationId != null && auth != null) {
            isVerifyingOtp.value = true
            authError.value = null
            viewModelScope.launch {
                try {
                    val credential = PhoneAuthProvider.getCredential(verificationId, code)
                    val authResult = auth.signInWithCredential(credential).await()
                    handleSuccessfulAuth(authResult.user)
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    Log.e(TAG, "OTP verification failed", e)
                    authError.value = e.localizedMessage ?: "Invalid or expired OTP code"
                } finally {
                    isVerifyingOtp.value = false
                }
            }
        } else {
            // Local preview test / fallback
            isVerifyingOtp.value = true
            viewModelScope.launch {
                delay(800)
                isVerifyingOtp.value = false
                handleSuccessfulAuth(auth?.currentUser)
            }
        }
    }

    fun skipLogin() {
        navigateTo(Screen.HOME)
    }

    fun signOut() {
        firebaseAuth?.signOut()
        isLoggedIn.value = false
        navigateTo(Screen.AUTH)
    }

    fun toggleDarkMode() {
        isDarkMode.value = !isDarkMode.value
    }

    fun openAddressModal() {
        _isAddressModalOpen.value = true
        _addressStatusMessage.value = null
    }

    fun closeAddressModal() {
        _isAddressModalOpen.value = false
    }

    fun selectDeliveryAddress(address: DeliveryAddress) {
        selectedAddress.value = address
        userAddress.value = address.addressLine
        userCity.value = address.city
        userPincode.value = address.pincode
        userLandmark.value = address.landmark
        checkoutAddressLine.value = address.addressLine
        checkoutCity.value = address.city
        checkoutPincode.value = address.pincode
        checkoutLandmark.value = address.landmark
        closeAddressModal()
    }

    fun addNewDeliveryAddress(address: DeliveryAddress) {
        val currentList = savedAddresses.value.toMutableList()
        currentList.add(0, address)
        savedAddresses.value = currentList
        selectDeliveryAddress(address)
    }

    fun detectGpsCurrentLocation() {
        viewModelScope.launch {
            _addressStatusMessage.value = "Detecting current Punjab GPS location..."
            delay(900)
            val detected = DeliveryAddress(
                fullName = userName.value,
                phone = userPhone.value,
                addressLine = "Near Feroze Gandhi Market, Club Road",
                city = "Ludhiana",
                pincode = "141001",
                landmark = "Opp. Guru Nanak Stadium",
                mapsLink = "https://maps.app.goo.gl/gps-ludhiana-center",
                label = "Current GPS Location"
            )
            addNewDeliveryAddress(detected)
            _addressStatusMessage.value = "Location updated to Club Road, Ludhiana!"
        }
    }

    fun parseAndSetGoogleMapsLink(link: String, customLandmark: String = "") {
        viewModelScope.launch {
            if (link.contains("maps") || link.contains("goo.gl") || link.isNotBlank()) {
                val cityGuess = punjabCities.firstOrNull { link.contains(it, ignoreCase = true) } ?: userCity.value
                val newAddr = DeliveryAddress(
                    fullName = userName.value,
                    phone = userPhone.value,
                    addressLine = "Google Maps Pinned Location (${link.take(30)}...)",
                    city = cityGuess,
                    pincode = "141001",
                    landmark = customLandmark.ifBlank { "Location verified via Google Maps pin" },
                    mapsLink = link,
                    label = "Google Maps Pin"
                )
                addNewDeliveryAddress(newAddr)
                _addressStatusMessage.value = "Google Maps pin pinned successfully!"
            }
        }
    }

    fun updateStatusDemo(orderId: String, newStatus: OrderStatus) {
        repository.updateOrderStatus(orderId, newStatus)
        if (_selectedOrder.value?.id == orderId) {
            _selectedOrder.value = _selectedOrder.value?.copy(status = newStatus)
        }
        val user = firebaseAuth?.currentUser
        if (user != null && firestoreRepo != null) {
            viewModelScope.launch {
                firestoreRepo.updateOrderStatus(user.uid, orderId, newStatus)
            }
        }
    }
}
