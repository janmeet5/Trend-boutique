package com.example.data

import com.example.R
import com.example.model.BoutiqueOrder
import com.example.model.CustomizationSelection
import com.example.model.DeliveryAddress
import com.example.model.FabricSource
import com.example.model.MeasurementProfile
import com.example.model.OrderStatus
import com.example.model.RegionalReview
import com.example.model.SizingChoice
import com.example.model.SuitItem
import com.example.model.TailorConsultation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BoutiqueRepository {

    val punjabCities = listOf(
        "Ludhiana",
        "Amritsar",
        "Jalandhar",
        "Patiala",
        "Bathinda",
        "Mohali (SAS Nagar)",
        "Chandigarh",
        "Hoshiarpur",
        "Pathankot",
        "Phagwara",
        "Moga",
        "Barnala",
        "Faridkot",
        "Ferozepur",
        "Kapurthala"
    )

    val categories = listOf(
        "All",
        "Test ₹2",
        "Punjabi Suit",
        "Plazo Suit",
        "Anarkali",
        "Salwar Kameez",
        "Party Wear",
        "Bridal",
        "Kurti",
        "Sharara"
    )

    val suitCatalog: List<SuitItem> = listOf(
        SuitItem(
            id = "suit_patiala_01",
            title = "Royal Patiala Shahi Salwar Suit",
            punjabiTitle = "ਰਵਾਇਤੀ ਪਟਿਆਲਾ ਸ਼ਾਹੀ ਸੂਟ",
            category = "Punjabi Suit",
            fabricOptions = listOf("Pure Raw Silk", "Cotton/Lawn", "Chanderi Silk", "Regal Velvet"),
            includedItems = "Kameez + Heavy 4.5m Gher Patiala Salwar + Pure Chinon Dupatta with Gota Kiran",
            originalPrice = 2899,
            price = 2199,
            rating = 4.95f,
            reviewCount = 312,
            imageRes = R.drawable.img_suit_punjabi_patiala,
            description = "Signature Punjabi craftsmanship with deep pleats, wide authentic Patiala gher, and delicate tilla piping. Tailored by master artisans with 20+ years of heritage.",
            turnaroundDays = "3–4 Business Days",
            neckOptions = listOf(
                "Sweetheart Gala with Tilla Piping",
                "Round Neck with Gota Patti Border",
                "Punjabi V-Neck with Hanging Dori",
                "Angrakha Wrap Gala"
            ),
            sleeveOptions = listOf(
                "Full Sleeves with Golden Border",
                "3/4 Sleeves with Scallop Lace",
                "Elbow Bell Sleeves",
                "Sleeveless with Piping"
            ),
            bottomOptions = listOf(
                "Heavy Royal Patiala (4.5m Gher)",
                "Classic Punjabi Salwar (3m)",
                "Straight Cigarette Trousers"
            ),
            dupattaOptions = listOf(
                "Four-Side Gota Kiran Finish",
                "Scalloped Border Embroidery",
                "Handcrafted Silk Latkan Tassels"
            ),
            isExpressAvailable = true,
            isFeatured = true
        ),
        SuitItem(
            id = "test_pay_02",
            title = "Fabric Swatch & Stitching Trial (₹2 Payment Test)",
            punjabiTitle = "ਟੈਸਟ ਭੁਗਤਾਨ (₹2 ਟਰਾਇਲ ਆਰਡਰ)",
            category = "Test ₹2",
            fabricOptions = listOf("Cotton Sample Swatch", "Raw Silk Swatch", "Georgette Swatch"),
            includedItems = "3 Pure Fabric Swatches + Doorstep Pickup Trial Kit + Measurement Card",
            originalPrice = 99,
            price = 2,
            rating = 5.0f,
            reviewCount = 142,
            imageRes = R.drawable.ic_trend_favicon_glowing,
            description = "Nominal ₹2 test order to verify live Razorpay, native UPI payment apps (Google Pay, PhonePe, Paytm), and SMS notifications on your live account.",
            turnaroundDays = "Instant Verification",
            neckOptions = listOf(
                "Standard Trial Gala",
                "Round Neck Swatch"
            ),
            sleeveOptions = listOf(
                "Standard Trial Sleeves",
                "Sleeveless Cut Sample"
            ),
            bottomOptions = listOf(
                "Trial Salwar Cut",
                "Straight Pant Swatch"
            ),
            dupattaOptions = listOf(
                "Gota Lace Sample",
                "Kiran Finish Swatch"
            ),
            isExpressAvailable = true,
            isFeatured = true
        ),
        SuitItem(
            id = "suit_anarkali_02",
            title = "Regal Zardozi Bridal Anarkali Gown",
            punjabiTitle = "ਸ਼ਾਹੀ ਜ਼ਰਦੋਜ਼ੀ ਅਨਾਰਕਲੀ ਗਾਊਨ",
            category = "Bridal",
            fabricOptions = listOf("Pure Velvet & Raw Silk", "Heavy Georgette", "Tissue Brocade"),
            includedItems = "Floor Length 32-Kali Anarkali + Pure Silk Chooridar + Zardozi Dupatta",
            originalPrice = 6499,
            price = 4899,
            rating = 4.98f,
            reviewCount = 184,
            imageRes = R.drawable.img_suit_anarkali_bridal,
            description = "Grand 32-kali royal flare with opulent hand-embroidered dabka and antique gold zari work. The ultimate bridal statement piece crafted for unforgettable Punjabi celebrations.",
            turnaroundDays = "5–7 Business Days",
            neckOptions = listOf(
                "Deep Sweetheart Gala with Heavy Zardozi",
                "Royal Stand Mandarin Collar",
                "Mughal Boat Neck with Dabka"
            ),
            sleeveOptions = listOf(
                "Fitted Chooridar Sleeves with Zari",
                "Full Sleeves with Embroidered Cuffs",
                "Floor Length Sheer Sleeves"
            ),
            bottomOptions = listOf(
                "Pure Silk Chooridar",
                "Comfort Lycra Chooridar",
                "Full Flared Palazzo"
            ),
            dupattaOptions = listOf(
                "Heavy Scalloped Zardozi Border",
                "Four-Side Royal Kiran Lace",
                "Embroidered Booti Net Dupatta"
            ),
            isExpressAvailable = false,
            isFeatured = true
        ),
        SuitItem(
            id = "suit_plazo_03",
            title = "Gulabi Noor Georgette Plazo Suit",
            punjabiTitle = "ਗੁਲਾਬੀ ਨੂਰ ਪਲਾਜ਼ੋ ਸੂਟ",
            category = "Plazo Suit",
            fabricOptions = listOf("Pure Georgette", "Cotton/Lawn", "Roman Silk", "Organza"),
            includedItems = "Embroidered Straight Kurti + Flared Plazo with Lace + Chiffon Dupatta",
            originalPrice = 2499,
            price = 1899,
            rating = 4.91f,
            reviewCount = 240,
            imageRes = R.drawable.img_suit_plazo_georgette,
            description = "Delicate mirror work accents and pearl borders with graceful fall. Breezy, contemporary ethnic fashion perfect for sangeet nights and summer weddings.",
            turnaroundDays = "3–5 Business Days",
            neckOptions = listOf(
                "Keyhole Neck with Pearl Detailing",
                "V-Neck Lace Placket",
                "Round Gala with Cutwork"
            ),
            sleeveOptions = listOf(
                "Flared Bell Sleeves",
                "3/4 Sleeves with Organza Insert",
                "Straight Sleeves with Lace"
            ),
            bottomOptions = listOf(
                "Wide-Leg Umbrella Plazo",
                "Straight Cut Pants with Side Slit",
                "Tiered Sharara Flair"
            ),
            dupattaOptions = listOf(
                "Pearl Lace Border",
                "Four-Side Gota Patti",
                "Scalloped Floral Trim"
            ),
            isExpressAvailable = true,
            isFeatured = true
        ),
        SuitItem(
            id = "suit_salwar_04",
            title = "Heer Heritage Salwar Kameez",
            punjabiTitle = "ਹੀਰ ਰਵਾਇਤੀ ਸਲਵਾਰ ਕਮੀਜ਼",
            category = "Salwar Kameez",
            fabricOptions = listOf("Cotton/Lawn", "Pure Khaddi Cotton", "Mulmul", "Linen"),
            includedItems = "Straight Cut Long Kameez + Classic Punjabi Salwar + Mulmul Dupatta",
            originalPrice = 1999,
            price = 1499,
            rating = 4.88f,
            reviewCount = 425,
            imageRes = R.drawable.img_suit_punjabi_patiala,
            description = "Timeless daily luxury tailored for effortless comfort and crisp silhouette. Breathable fabric and master tailored seams that withstand daily wear.",
            turnaroundDays = "2–3 Business Days",
            neckOptions = listOf(
                "Classic Round Neck with Contrast Piping",
                "Small V-Neck with Fabric Buttons",
                "Collar Neck with Placket"
            ),
            sleeveOptions = listOf(
                "3/4 Sleeves with Folded Cuffs",
                "Full Sleeves with Button Detailing",
                "Elbow Length Sleeves"
            ),
            bottomOptions = listOf(
                "Authentic Punjabi Salwar with Poucha",
                "Straight Cigarette Trousers",
                "Semi-Patiala Salwar"
            ),
            dupattaOptions = listOf(
                "Interlock Finished Border",
                "Subtle Gota Edge",
                "Tassels on Four Corners"
            ),
            isExpressAvailable = true,
            isFeatured = false
        ),
        SuitItem(
            id = "suit_sharara_05",
            title = "Royal Chandan 3-Tier Sharara Set",
            punjabiTitle = "ਸ਼ਾਹੀ ਚੰਦਨ ਸ਼ਰਾਰਾ ਸੈੱਟ",
            category = "Sharara",
            fabricOptions = listOf("Pure Georgette", "Raw Silk", "Chiffon", "Shimmer Tissue"),
            includedItems = "Short Embroidered Peplum Kurti + 3-Tier Sharara + Heavy Dupatta",
            originalPrice = 3999,
            price = 2999,
            rating = 4.93f,
            reviewCount = 168,
            imageRes = R.drawable.img_suit_plazo_georgette,
            description = "Voluminous three-tier flares with intricate gota borders and flattering peplum silhouette. A showstopper ensemble for pre-wedding festivities.",
            turnaroundDays = "4–6 Business Days",
            neckOptions = listOf(
                "Angrakha Wrap V-Neck",
                "Sweetheart Neck with Gotta",
                "Round Boat Neck"
            ),
            sleeveOptions = listOf(
                "Full Sleeves with Gota Rows",
                "3/4 Sleeves with Lace",
                "Sleeveless with Broad Shoulder Straps"
            ),
            bottomOptions = listOf(
                "Heavy 3-Tier Gathered Sharara",
                "2-Tier Flared Gharara",
                "Wide Umbrella Plazo"
            ),
            dupattaOptions = listOf(
                "Heavy Four-Side Gota Kiran",
                "Handmade Tassels on Pallu",
                "Scallop Cutwork Edge"
            ),
            isExpressAvailable = true,
            isFeatured = false
        ),
        SuitItem(
            id = "suit_velvet_06",
            title = "Zari Velvet Festive Party Wear",
            punjabiTitle = "ਜ਼ਰੀ ਵੈਲਵਟ ਪਾਰਟੀ ਵੀਅਰ",
            category = "Party Wear",
            fabricOptions = listOf("Regal Velvet", "Pure Raw Silk", "Modal Silk"),
            includedItems = "Heavy Zari Velvet Kurta + Silk Cigarette Pants + Organza Zardozi Dupatta",
            originalPrice = 4599,
            price = 3499,
            rating = 4.96f,
            reviewCount = 219,
            imageRes = R.drawable.img_suit_anarkali_bridal,
            description = "Rich 9000 micro velvet paired with tilla handwork. Ideal for winter weddings, night receptions, and grand Diwali celebrations.",
            turnaroundDays = "4–5 Business Days",
            neckOptions = listOf(
                "High Neck Mandarin Collar with Gold Slit",
                "Regal Sweetheart Gala",
                "Deep Round Neck with Border"
            ),
            sleeveOptions = listOf(
                "Full Sleeves with Heavy Zari Border",
                "Fitted 3/4 Sleeves",
                "Bell Sleeves with Gold Fringe"
            ),
            bottomOptions = listOf(
                "Raw Silk Cigarette Pants with Zari Hem",
                "Straight Velvet Trousers",
                "Semi-Chooridar"
            ),
            dupattaOptions = listOf(
                "Organza Four-Side Velvet Border",
                "Zari Embroidered Net",
                "Heavy Kiran Trim"
            ),
            isExpressAvailable = true,
            isFeatured = false
        ),
        SuitItem(
            id = "suit_kurti_07",
            title = "Amritsari Handcrafted Festive Kurti",
            punjabiTitle = "ਅੰਮ੍ਰਿਤਸਰੀ ਫੁਲਕਾਰੀ ਕੁਰਤੀ",
            category = "Kurti",
            fabricOptions = listOf("Cotton/Lawn", "Chanderi Silk", "Pure Georgette"),
            includedItems = "Hand Embroidered Kurti with Soft Cotton Voile Inner Lining",
            originalPrice = 1599,
            price = 1199,
            rating = 4.87f,
            reviewCount = 195,
            imageRes = R.drawable.img_suit_punjabi_patiala,
            description = "Intricate Amritsari threadwork on pure breathable base with delicate mirror highlights. Can be paired with jeans, salwars, or skirts.",
            turnaroundDays = "2–3 Business Days",
            neckOptions = listOf(
                "Yoke Embroidery Round Neck",
                "Notch V-Neck with Fabric Tassels",
                "Boat Neck with Hand Stitching"
            ),
            sleeveOptions = listOf(
                "3/4 Sleeves with Contrast Cuffs",
                "Full Sleeves with Embroidered Wrists",
                "Short Cap Sleeves"
            ),
            bottomOptions = listOf(
                "Kurti Only (Pairs with own bottom)",
                "Add Matching Cotton Pants (+ ₹450)",
                "Add White Chikan Plazo (+ ₹550)"
            ),
            dupattaOptions = listOf(
                "No Dupatta (Kurti Only)",
                "Add Phulkari Chiffon Dupatta (+ ₹499)"
            ),
            isExpressAvailable = true,
            isFeatured = false
        )
    )

    private val _savedProfiles = MutableStateFlow(
        listOf(
            MeasurementProfile(
                id = "prof_default",
                label = "My Default Fit (Patiala Shahi)",
                bust = 36.5f,
                waist = 30.0f,
                hips = 40.0f,
                kurtaLength = 39.0f,
                bottomLength = 38.0f,
                neckDepth = 6.5f,
                sleeveLength = 17.5f,
                shoulder = 14.5f,
                armhole = 16.5f,
                notes = "Prefer 1 inch extra ease on waist; Patiala salwar pouncha 6.5 inches.",
                isDefault = true,
                lastUpdated = "Saved via Sample Suit"
            ),
            MeasurementProfile(
                id = "prof_mom",
                label = "Mom's Fit (Comfort Relaxed)",
                bust = 40.0f,
                waist = 35.0f,
                hips = 44.0f,
                kurtaLength = 42.0f,
                bottomLength = 39.0f,
                neckDepth = 6.0f,
                sleeveLength = 18.0f,
                shoulder = 15.5f,
                armhole = 18.0f,
                notes = "Loose fitting around hips and waist. Modest neck depth.",
                isDefault = false,
                lastUpdated = "12 Oct 2026"
            ),
            MeasurementProfile(
                id = "prof_festive",
                label = "Festive Fitted (Anarkali & Gowns)",
                bust = 36.0f,
                waist = 29.0f,
                hips = 39.5f,
                kurtaLength = 52.0f,
                bottomLength = 38.5f,
                neckDepth = 7.0f,
                sleeveLength = 21.0f,
                shoulder = 14.0f,
                armhole = 16.0f,
                notes = "Snug high waist fit for kalidar gowns and bridal Anarkalis.",
                isDefault = false,
                lastUpdated = "28 Sep 2026"
            )
        )
    )
    val savedProfiles: StateFlow<List<MeasurementProfile>> = _savedProfiles.asStateFlow()

    private val _orders = MutableStateFlow<List<BoutiqueOrder>>(
        listOf(
            BoutiqueOrder(
                id = "TB-PB-84920",
                customization = CustomizationSelection(
                    suitItem = suitCatalog[0],
                    selectedFabric = "Pure Raw Silk",
                    selectedNeck = "Sweetheart Gala with Tilla Piping",
                    selectedSleeve = "Full Sleeves with Golden Border",
                    selectedBottom = "Heavy Royal Patiala (4.5m Gher)",
                    selectedDupatta = "Four-Side Gota Kiran Finish",
                    fabricSource = FabricSource.CUSTOMER_PROVIDED,
                    customNotes = "Please ensure deep traditional pleats on Patiala salwar.",
                    sizingChoice = SizingChoice.DoorstepSample(
                        sampleCount = 1,
                        fitAdjustmentNotes = "Exact same fit as maroon sample suit provided",
                        saveFitToProfile = true
                    )
                ),
                deliveryAddress = DeliveryAddress(
                    fullName = "Harpreet Kaur",
                    phone = "+91 98761 23456",
                    addressLine = "House 142, Street 4, Sarabha Nagar",
                    city = "Ludhiana",
                    pincode = "141001",
                    landmark = "Near Malhar Road Park"
                ),
                pickupDate = "Tomorrow, 10:30 AM",
                pickupSlot = "Morning (10 AM – 1 PM)",
                paymentMethod = "Razorpay (UPI - Google Pay)",
                isPaid = true,
                totalAmount = 2199,
                orderDate = "04 Nov 2026",
                status = OrderStatus.STITCHING_IN_PROGRESS,
                masterTailorName = "Master Gurwinder Singh (Ludhiana Master)",
                riderName = "Manpreet Singh (Express Fleet)",
                riderContact = "+91 98765 43210"
            ),
            BoutiqueOrder(
                id = "TB-PB-82190",
                customization = CustomizationSelection(
                    suitItem = suitCatalog[2],
                    selectedFabric = "Pure Georgette",
                    selectedNeck = "Keyhole Neck with Pearl Detailing",
                    selectedSleeve = "Flared Bell Sleeves",
                    selectedBottom = "Wide-Leg Umbrella Plazo",
                    selectedDupatta = "Pearl Lace Border",
                    fabricSource = FabricSource.BOUTIQUE_FABRIC,
                    customNotes = "Stitch with soft cotton voile inner lining",
                    sizingChoice = SizingChoice.SavedProfile("prof_default")
                ),
                deliveryAddress = DeliveryAddress(
                    fullName = "Harpreet Kaur",
                    phone = "+91 98761 23456",
                    addressLine = "House 142, Street 4, Sarabha Nagar",
                    city = "Ludhiana",
                    pincode = "141001",
                    landmark = "Near Malhar Road Park"
                ),
                pickupDate = "22 Oct 2026",
                pickupSlot = "Afternoon (2 PM – 5 PM)",
                paymentMethod = "Cash on Delivery / Pickup",
                isPaid = true,
                totalAmount = 2549,
                orderDate = "22 Oct 2026",
                status = OrderStatus.DELIVERED,
                masterTailorName = "Master Baldev Singh",
                riderName = "Gurpreet Singh",
                riderContact = "+91 98140 11223"
            )
        )
    )
    val orders: StateFlow<List<BoutiqueOrder>> = _orders.asStateFlow()

    val reviews: List<RegionalReview> = listOf(
        RegionalReview(
            id = "rev_1",
            name = "Harpreet Kaur",
            city = "Ludhiana (Sarabha Nagar)",
            rating = 5.0f,
            quote = "Perfect Patiala Salwar gher! Masterji matched my sample suit 100%. Usually boutique tailors in town take 3 weeks, but The Trend Boutique delivered in 4 days at my doorstep with pure Punjabi grace.",
            suitStyle = "Royal Patiala Shahi Suit",
            timeAgo = "2 days ago"
        ),
        RegionalReview(
            id = "rev_2",
            name = "Simranjit Brar",
            city = "Amritsar (Ranjit Avenue)",
            rating = 5.0f,
            quote = "Best boutique stitching for my sister's wedding Anarkali. Unbelievable zardozi finishing and neckline fit. The tailor consultation call cleared all our doubts about neck depth.",
            suitStyle = "Regal Bridal Anarkali Gown",
            timeAgo = "1 week ago"
        ),
        RegionalReview(
            id = "rev_3",
            name = "Navneet Kaur",
            city = "Jalandhar (Model Town)",
            rating = 5.0f,
            quote = "Doorstep pickup in Model Town was super prompt. Rider gave me a receipt bag for my unstitched georgette cloth and my best reference suit. The final fit is sheer perfection!",
            suitStyle = "Gulabi Noor Plazo Suit",
            timeAgo = "2 weeks ago"
        ),
        RegionalReview(
            id = "rev_4",
            name = "Jasleen Dhillon",
            city = "Patiala (Leela Bhawan)",
            rating = 5.0f,
            quote = "Authentic royal cut, pure Punjabi craftsmanship. Being from Patiala, I am very picky about my salwar pleats — Master Gurwinder nailed every fold. The Trend Boutique is my go-to now.",
            suitStyle = "Shahi Patiala Suit",
            timeAgo = "3 weeks ago"
        ),
        RegionalReview(
            id = "rev_5",
            name = "Amanjot Sandhu",
            city = "Mohali (Phase 7)",
            rating = 5.0f,
            quote = "Saved size profile feature is pure genius! Once they saved my measurements from my first sample pickup, I reordered two more suits in under 2 minutes without re-measuring anything.",
            suitStyle = "3-Tier Chandan Sharara",
            timeAgo = "1 month ago"
        )
    )

    // User profile state
    val userName = MutableStateFlow("Harpreet Kaur")
    val userPhone = MutableStateFlow("+91 98761 23456")
    val userCity = MutableStateFlow("Ludhiana")
    val userAddress = MutableStateFlow("House 142, Street 4, Sarabha Nagar")
    val userPincode = MutableStateFlow("141001")
    val userLandmark = MutableStateFlow("Near Malhar Road Park")
    val isLoggedIn = MutableStateFlow(true)
    val isDarkMode = MutableStateFlow(false) // Default Light Mode (warm, friendly, breathable boutique)

    // Saved Addresses in Punjab
    val savedAddresses = MutableStateFlow(
        listOf(
            DeliveryAddress(
                fullName = "Harpreet Kaur",
                phone = "+91 98761 23456",
                addressLine = "House 142, Street 4, Sarabha Nagar",
                city = "Ludhiana",
                pincode = "141001",
                landmark = "Near Malhar Road Park",
                mapsLink = "https://maps.app.goo.gl/ludhiana-sarabha",
                label = "Home"
            ),
            DeliveryAddress(
                fullName = "Harpreet Kaur",
                phone = "+91 98761 23456",
                addressLine = "Shop 18, Commercial Plaza, Model Town",
                city = "Jalandhar",
                pincode = "144003",
                landmark = "Opposite Nikku Park",
                mapsLink = "https://maps.app.goo.gl/jalandhar-modeltown",
                label = "Boutique / Work"
            ),
            DeliveryAddress(
                fullName = "Mrs. Gurmeet Kaur",
                phone = "+91 98140 55667",
                addressLine = "Kothi 88, B-Block, Ranjit Avenue",
                city = "Amritsar",
                pincode = "143001",
                landmark = "Near District Courts",
                mapsLink = "https://maps.app.goo.gl/amritsar-ranjit-ave",
                label = "Mom's Residence"
            )
        )
    )

    val selectedAddress = MutableStateFlow(
        DeliveryAddress(
            fullName = "Harpreet Kaur",
            phone = "+91 98761 23456",
            addressLine = "House 142, Street 4, Sarabha Nagar",
            city = "Ludhiana",
            pincode = "141001",
            landmark = "Near Malhar Road Park",
            mapsLink = "https://maps.app.goo.gl/ludhiana-sarabha",
            label = "Home"
        )
    )


    fun addOrder(order: BoutiqueOrder) {
        val current = _orders.value.toMutableList()
        current.add(0, order)
        _orders.value = current
    }

    fun saveProfile(profile: MeasurementProfile) {
        val current = _savedProfiles.value.toMutableList()
        val index = current.indexOfFirst { it.id == profile.id }
        if (index >= 0) {
            current[index] = profile
        } else {
            current.add(0, profile)
        }
        _savedProfiles.value = current
    }

    fun deleteProfile(profileId: String) {
        _savedProfiles.value = _savedProfiles.value.filter { it.id != profileId }
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _orders.value = _orders.value.map {
            if (it.id == orderId) it.copy(status = newStatus) else it
        }
    }
}
