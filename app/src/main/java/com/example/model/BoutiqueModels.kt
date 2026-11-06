package com.example.model

import androidx.annotation.DrawableRes

data class SuitItem(
    val id: String,
    val title: String,
    val punjabiTitle: String,
    val category: String,
    val fabricOptions: List<String>,
    val includedItems: String,
    val originalPrice: Int,
    val price: Int,
    val rating: Float,
    val reviewCount: Int,
    @DrawableRes val imageRes: Int,
    val description: String,
    val turnaroundDays: String,
    val neckOptions: List<String>,
    val sleeveOptions: List<String>,
    val bottomOptions: List<String>,
    val dupattaOptions: List<String>,
    val isExpressAvailable: Boolean = true,
    val isFeatured: Boolean = false
)

data class MeasurementProfile(
    val id: String,
    val label: String,
    val bust: Float,
    val waist: Float,
    val hips: Float,
    val kurtaLength: Float,
    val bottomLength: Float,
    val neckDepth: Float,
    val sleeveLength: Float,
    val shoulder: Float,
    val armhole: Float,
    val notes: String = "",
    val isDefault: Boolean = false,
    val lastUpdated: String = "Today"
)

enum class FabricSource(val title: String, val surcharge: Int, val description: String) {
    CUSTOMER_PROVIDED(
        "I will hand over my unstitched fabric to rider",
        0,
        "Rider collects your cloth at your doorstep"
    ),
    BOUTIQUE_FABRIC(
        "Use Boutique's Pure Heritage Fabric",
        650,
        "Hand-picked premium pure fabric from our Ludhiana workshop"
    )
}

sealed class SizingChoice {
    data class SavedProfile(val profileId: String) : SizingChoice()
    data class TailorCall(
        val preferredSlot: String,
        val language: String,
        val notes: String = ""
    ) : SizingChoice()
    data class DoorstepSample(
        val sampleCount: Int,
        val fitAdjustmentNotes: String,
        val saveFitToProfile: Boolean = true
    ) : SizingChoice()
}

data class CustomizationSelection(
    val suitItem: SuitItem,
    val selectedFabric: String,
    val selectedNeck: String,
    val selectedSleeve: String,
    val selectedBottom: String,
    val selectedDupatta: String,
    val fabricSource: FabricSource = FabricSource.CUSTOMER_PROVIDED,
    val customNotes: String = "",
    val sizingChoice: SizingChoice = SizingChoice.DoorstepSample(
        sampleCount = 1,
        fitAdjustmentNotes = "Match sample suit fit accurately",
        saveFitToProfile = true
    )
) {
    fun calculateTotal(): Int {
        if (suitItem.price <= 2) {
            return suitItem.price
        }
        val basePrice = suitItem.price
        val fabricCost = fabricSource.surcharge
        val premiumFinishing = if (selectedNeck.contains("Zardozi") || selectedNeck.contains("Tilla")) 200 else 0
        return basePrice + fabricCost + premiumFinishing
    }
}

data class DeliveryAddress(
    val fullName: String,
    val phone: String,
    val addressLine: String,
    val city: String,
    val pincode: String,
    val landmark: String = "",
    val mapsLink: String = "",
    val label: String = "Home"
)


enum class OrderStatus(val label: String, val stepIndex: Int, val description: String) {
    PICKUP_SCHEDULED("Fabric Pickup Scheduled", 0, "Boutique rider dispatched to collect fabric & sample"),
    FABRIC_RECEIVED("Fabric Received at Workshop", 1, "Inspected by cutting master at Ludhiana workshop"),
    STITCHING_IN_PROGRESS("Stitching by Master Tailor", 2, "Hand-cut & tailored with reinforced French seams"),
    QUALITY_CHECK("Quality & Finishing Check", 3, "Measurement verification & steam press"),
    OUT_FOR_DELIVERY("Out for Delivery", 4, "Express doorstep delivery in progress"),
    DELIVERED("Delivered & Fit Guaranteed", 5, "Completed with 10-day free alteration guarantee")
}

data class BoutiqueOrder(
    val id: String,
    val customization: CustomizationSelection,
    val deliveryAddress: DeliveryAddress,
    val pickupDate: String,
    val pickupSlot: String,
    val paymentMethod: String,
    val isPaid: Boolean,
    val totalAmount: Int,
    val orderDate: String,
    val status: OrderStatus = OrderStatus.PICKUP_SCHEDULED,
    val masterTailorName: String = "Master Gurwinder Singh",
    val riderName: String = "Manpreet Singh (Express Fleet)",
    val riderContact: String = "+91 98765 43210"
)

data class RegionalReview(
    val id: String,
    val name: String,
    val city: String,
    val rating: Float,
    val quote: String,
    val suitStyle: String,
    val timeAgo: String
)

data class TailorConsultation(
    val name: String,
    val phone: String,
    val city: String,
    val preferredSlot: String,
    val language: String,
    val topic: String = "General Suit Guidance & Sizing"
)
