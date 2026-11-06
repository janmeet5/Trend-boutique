package com.example.data

import android.content.Context
import android.util.Log
import com.example.R
import com.example.model.BoutiqueOrder
import com.example.model.CustomizationSelection
import com.example.model.DeliveryAddress
import com.example.model.FabricSource
import com.example.model.MeasurementProfile
import com.example.model.OrderStatus
import com.example.model.SizingChoice
import com.example.model.SuitItem
import com.example.model.TailorConsultation
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import kotlin.coroutines.cancellation.CancellationException

enum class OperationType(val value: String) {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete"),
    LIST("list"),
    GET("get"),
    WRITE("write"),
}

fun handleFirestoreError(exception: Throwable, operationType: OperationType, path: String?): String {
    val causes = generateSequence(exception) { it.cause }
    val rootError = causes.firstOrNull { it is FirebaseFirestoreException }
        ?: causes.firstOrNull { it !is CancellationException }
        ?: exception

    val auth = Firebase.auth
    val currentUser = auth.currentUser

    val providerInfoList = currentUser?.providerData?.map { provider ->
        JSONObject().apply {
            put("providerId", provider.providerId)
            put("email", provider.email)
        }
    } ?: emptyList()

    val authInfoJson = JSONObject().apply {
        put("userId", currentUser?.uid)
        put("email", currentUser?.email)
        put("emailVerified", currentUser?.isEmailVerified)
        put("tenantId", currentUser?.tenantId)
        put("providerInfo", JSONArray(providerInfoList))
    }

    val errorInfoJson = JSONObject().apply {
        put("error", rootError.message ?: rootError.toString())
        put("operationType", operationType.value)
        put("path", path)
        put("authInfo", authInfoJson)
    }

    val jsonString = errorInfoJson.toString()
    Log.e("FirestoreError", "Firestore Error: $jsonString")
    return jsonString
}

data class UserProfile(
    val userId: String = "",
    val fullName: String = "",
    val phone: String = "",
    val email: String? = null,
    val city: String? = null,
    val defaultAddress: String? = null
)

class FirestoreBoutiqueRepository(
    private val db: FirebaseFirestore
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    companion object {
        private const val TAG = "FirestoreBoutiqueRepo"
    }

    // -------------------------------------------------------------
    // USER PROFILE
    // -------------------------------------------------------------
    fun observeUserProfile(userId: String): Flow<UserProfile?> = callbackFlow {
        val docRef = db.collection("users").document(userId)
        val listener: ListenerRegistration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.GET, docRef.path)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val profile = UserProfile(
                    userId = snapshot.getString("userId") ?: userId,
                    fullName = snapshot.getString("fullName") ?: "",
                    phone = snapshot.getString("phone") ?: "",
                    email = snapshot.getString("email"),
                    city = snapshot.getString("city"),
                    defaultAddress = snapshot.getString("defaultAddress")
                )
                trySend(profile)
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun saveUserProfile(
        userId: String,
        fullName: String,
        phone: String,
        email: String? = null,
        city: String? = null,
        defaultAddress: String? = null
    ): Result<Unit> {
        val docRef = db.collection("users").document(userId)
        val data = mutableMapOf<String, Any>(
            "userId" to userId,
            "fullName" to fullName.ifBlank { "Valued Customer" },
            "phone" to phone.ifBlank { "+91 98765 00000" },
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (!email.isNullOrBlank()) data["email"] = email
        if (!city.isNullOrBlank()) data["city"] = city
        if (!defaultAddress.isNullOrBlank()) data["defaultAddress"] = defaultAddress

        return try {
            docRef.set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            Result.failure(e)
        }
    }

    // -------------------------------------------------------------
    // ORDERS
    // -------------------------------------------------------------
    fun observeOrders(userId: String, catalogLookup: (String) -> SuitItem?): Flow<List<BoutiqueOrder>> = callbackFlow {
        val colRef = db.collection("users").document(userId).collection("orders")
        val listener: ListenerRegistration = colRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.LIST, colRef.path)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val ordersList = snapshot.documents.mapNotNull { doc ->
                    try {
                        val orderId = doc.getString("orderId") ?: doc.id
                        val suitTitle = doc.getString("suitTitle") ?: "Punjabi Suit"
                        val suitId = doc.getString("suitId") ?: "suit_patiala_01"
                        val suitItem = catalogLookup(suitId) ?: SuitItem(
                            id = suitId,
                            title = suitTitle,
                            punjabiTitle = "ਰਵਾਇਤੀ ਪੰਜਾਬੀ ਸੂਟ",
                            category = doc.getString("suitCategory") ?: "Punjabi Suit",
                            fabricOptions = listOf("Pure Raw Silk"),
                            includedItems = "Kameez + Salwar + Dupatta",
                            originalPrice = (doc.getLong("totalAmount") ?: 2199L).toInt(),
                            price = (doc.getLong("totalAmount") ?: 2199L).toInt(),
                            rating = 4.95f,
                            reviewCount = 100,
                            imageRes = R.drawable.img_suit_punjabi_patiala,
                            description = "Bespoke handcrafted suit from The Trend Boutique.",
                            turnaroundDays = "3–4 Business Days",
                            neckOptions = listOf("Sweetheart Gala"),
                            sleeveOptions = listOf("Full Sleeves"),
                            bottomOptions = listOf("Patiala Salwar"),
                            dupattaOptions = listOf("Four-Side Gota Kiran Finish")
                        )

                        val fabricSourceStr = doc.getString("fabricSource") ?: FabricSource.CUSTOMER_PROVIDED.name
                        val fabricSource = try {
                            FabricSource.valueOf(fabricSourceStr)
                        } catch (e: Exception) {
                            FabricSource.CUSTOMER_PROVIDED
                        }

                        val customization = CustomizationSelection(
                            suitItem = suitItem,
                            selectedFabric = doc.getString("fabric") ?: "Pure Raw Silk",
                            selectedNeck = doc.getString("neck") ?: "Sweetheart Gala",
                            selectedSleeve = doc.getString("sleeve") ?: "Full Sleeves",
                            selectedBottom = doc.getString("bottom") ?: "Patiala Salwar",
                            selectedDupatta = doc.getString("dupatta") ?: "Gota Kiran Finish",
                            fabricSource = fabricSource,
                            customNotes = doc.getString("customNotes") ?: "",
                            sizingChoice = SizingChoice.DoorstepSample(
                                sampleCount = (doc.getLong("sampleCount") ?: 1L).toInt(),
                                fitAdjustmentNotes = doc.getString("fitNotes") ?: "Match sample fit",
                                saveFitToProfile = true
                            )
                        )

                        val deliveryAddress = DeliveryAddress(
                            fullName = doc.getString("deliveryFullName") ?: "",
                            phone = doc.getString("deliveryPhone") ?: "",
                            addressLine = doc.getString("deliveryAddress") ?: "",
                            city = doc.getString("deliveryCity") ?: "Ludhiana",
                            pincode = doc.getString("deliveryPincode") ?: "141001",
                            landmark = doc.getString("deliveryLandmark") ?: ""
                        )

                        val statusStr = doc.getString("status") ?: OrderStatus.PICKUP_SCHEDULED.name
                        val orderStatus = try {
                            OrderStatus.valueOf(statusStr)
                        } catch (e: Exception) {
                            OrderStatus.PICKUP_SCHEDULED
                        }

                        BoutiqueOrder(
                            id = orderId,
                            customization = customization,
                            deliveryAddress = deliveryAddress,
                            pickupDate = doc.getString("pickupDate") ?: "Tomorrow",
                            pickupSlot = doc.getString("pickupSlot") ?: "Morning (10 AM – 1 PM)",
                            paymentMethod = doc.getString("paymentMethod") ?: "Razorpay (UPI)",
                            isPaid = doc.getBoolean("isPaid") ?: false,
                            totalAmount = (doc.getLong("totalAmount") ?: 2199L).toInt(),
                            orderDate = doc.getString("orderDateDisplay") ?: "Recently",
                            status = orderStatus,
                            masterTailorName = doc.getString("masterTailorName") ?: "Master Gurwinder Singh",
                            riderName = doc.getString("riderName") ?: "Manpreet Singh (Express Fleet)",
                            riderContact = doc.getString("riderContact") ?: "+91 98765 43210"
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error mapping order doc ${doc.id}", e)
                        null
                    }
                }
                trySend(ordersList)
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun createOrder(userId: String, order: BoutiqueOrder): Result<Unit> {
        val docRef = db.collection("users").document(userId).collection("orders").document(order.id)
        val sampleCount = if (order.customization.sizingChoice is SizingChoice.DoorstepSample) {
            order.customization.sizingChoice.sampleCount
        } else {
            1
        }
        val fitNotes = when (val sc = order.customization.sizingChoice) {
            is SizingChoice.DoorstepSample -> sc.fitAdjustmentNotes
            is SizingChoice.TailorCall -> "Tailor call requested (${sc.language})"
            is SizingChoice.SavedProfile -> "Saved profile: ${sc.profileId}"
        }

        val data = mapOf<String, Any>(
            "userId" to userId,
            "orderId" to order.id,
            "suitTitle" to order.customization.suitItem.title,
            "suitId" to order.customization.suitItem.id,
            "suitCategory" to order.customization.suitItem.category,
            "totalAmount" to order.totalAmount,
            "paymentMethod" to order.paymentMethod,
            "status" to order.status.name,
            "isPaid" to order.isPaid,
            "sampleCount" to sampleCount,
            "fitNotes" to fitNotes,
            "fabric" to order.customization.selectedFabric,
            "neck" to order.customization.selectedNeck,
            "sleeve" to order.customization.selectedSleeve,
            "bottom" to order.customization.selectedBottom,
            "dupatta" to order.customization.selectedDupatta,
            "fabricSource" to order.customization.fabricSource.name,
            "customNotes" to order.customization.customNotes,
            "deliveryFullName" to order.deliveryAddress.fullName,
            "deliveryPhone" to order.deliveryAddress.phone,
            "deliveryAddress" to order.deliveryAddress.addressLine,
            "deliveryCity" to order.deliveryAddress.city,
            "deliveryPincode" to order.deliveryAddress.pincode,
            "deliveryLandmark" to order.deliveryAddress.landmark,
            "pickupDate" to order.pickupDate,
            "pickupSlot" to order.pickupSlot,
            "orderDateDisplay" to order.orderDate,
            "masterTailorName" to order.masterTailorName,
            "riderName" to order.riderName,
            "riderContact" to order.riderContact,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )

        return try {
            docRef.set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun updateOrderStatus(userId: String, orderId: String, newStatus: OrderStatus): Result<Unit> {
        val docRef = db.collection("users").document(userId).collection("orders").document(orderId)
        return try {
            docRef.update(
                mapOf(
                    "status" to newStatus.name,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            Result.failure(e)
        }
    }

    // -------------------------------------------------------------
    // MEASUREMENTS
    // -------------------------------------------------------------
    fun observeMeasurements(userId: String): Flow<List<MeasurementProfile>> = callbackFlow {
        val colRef = db.collection("users").document(userId).collection("measurements")
        val listener: ListenerRegistration = colRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.LIST, colRef.path)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = snapshot.documents.mapNotNull { doc ->
                    try {
                        MeasurementProfile(
                            id = doc.getString("profileId") ?: doc.id,
                            label = doc.getString("label") ?: "Saved Fit",
                            bust = (doc.getDouble("bust") ?: 36.0).toFloat(),
                            waist = (doc.getDouble("waist") ?: 30.0).toFloat(),
                            hips = (doc.getDouble("hips") ?: 40.0).toFloat(),
                            kurtaLength = (doc.getDouble("kurtaLength") ?: 40.0).toFloat(),
                            bottomLength = (doc.getDouble("bottomLength") ?: 38.0).toFloat(),
                            neckDepth = (doc.getDouble("neckDepth") ?: 6.5).toFloat(),
                            sleeveLength = (doc.getDouble("sleeveLength") ?: 17.5).toFloat(),
                            shoulder = (doc.getDouble("shoulder") ?: 14.5).toFloat(),
                            armhole = (doc.getDouble("armhole") ?: 16.5).toFloat(),
                            notes = doc.getString("notes") ?: "",
                            isDefault = doc.getBoolean("isDefault") ?: false,
                            lastUpdated = "Synced with Cloud"
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error mapping measurement ${doc.id}", e)
                        null
                    }
                }
                trySend(list)
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun saveMeasurement(userId: String, profile: MeasurementProfile): Result<Unit> {
        val docRef = db.collection("users").document(userId).collection("measurements").document(profile.id)
        val data = mapOf<String, Any>(
            "userId" to userId,
            "profileId" to profile.id,
            "label" to profile.label.take(60),
            "bust" to profile.bust.toDouble(),
            "waist" to profile.waist.toDouble(),
            "hips" to profile.hips.toDouble(),
            "kurtaLength" to profile.kurtaLength.toDouble(),
            "bottomLength" to profile.bottomLength.toDouble(),
            "neckDepth" to profile.neckDepth.toDouble(),
            "sleeveLength" to profile.sleeveLength.toDouble(),
            "shoulder" to profile.shoulder.toDouble(),
            "armhole" to profile.armhole.toDouble(),
            "notes" to profile.notes,
            "isDefault" to profile.isDefault,
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )

        return try {
            docRef.set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun deleteMeasurement(userId: String, profileId: String): Result<Unit> {
        val docRef = db.collection("users").document(userId).collection("measurements").document(profileId)
        return try {
            docRef.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            Result.failure(e)
        }
    }

    // -------------------------------------------------------------
    // TAILOR CONSULTATIONS
    // -------------------------------------------------------------
    suspend fun createConsultation(userId: String, consultation: TailorConsultation): Result<Unit> {
        val consultationId = UUID.randomUUID().toString()
        val docRef = db.collection("users").document(userId).collection("consultations").document(consultationId)
        val data = mapOf<String, Any>(
            "userId" to userId,
            "consultationId" to consultationId,
            "name" to consultation.name,
            "phone" to consultation.phone,
            "city" to consultation.city,
            "preferredSlot" to consultation.preferredSlot,
            "language" to consultation.language,
            "topic" to consultation.topic,
            "createdAt" to FieldValue.serverTimestamp()
        )

        return try {
            docRef.set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            Result.failure(e)
        }
    }
}
