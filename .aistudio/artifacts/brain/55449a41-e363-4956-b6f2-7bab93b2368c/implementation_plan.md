# Live Razorpay Payments & Firebase Phone Auth Architecture

This implementation plan establishes end-to-end live payment processing via Razorpay API keys configured in the AI Studio Secrets panel, enables live SMS OTP authentication via Firebase Phone Auth with test number fallbacks, and guides accessing your Firebase Console dashboard.

### User Review & Critical Decisions

> [!IMPORTANT]
> **Confirmed Decisions from Clarification**:
> - **Payment Method**: Live Razorpay integration via `RAZORPAY_KEY_ID` configured in the AI Studio Secrets panel, enabling real cards, UPI, and netbanking.
> - **Customer Authentication**: Real SMS OTP verification via Firebase Phone Auth (`PhoneAuthProvider`), with pre-registered test numbers in Firebase Console for cost-free testing.

- **Firebase Dashboard Access**: Your project ID is `gen-lang-client-0872762204` and your Firestore database ID is `ai-studio-android-thetrend-55449a41-e363-4956-b6f2-7bab93b2368c`. You can access it anytime at [https://console.firebase.google.com/](https://console.firebase.google.com/) signed in with your account (`gchahal24156@gmail.com`).

---

### 1. Overview & Core Concept

- **What It Does**: Upgrades "The Trend Boutique" from an in-emulator prototype to a production-ready bespoke tailoring platform where customers receive actual 6-digit SMS OTPs to their mobile phones, pay securely through Razorpay's live banking checkout, and have their bespoke orders, measurements, and addresses written directly to your cloud Firebase database.
- **Target Audience**: Punjabi ethnic wear shoppers looking to get Patiala suits, Anarkalis, and bridal wear tailored with guaranteed fit and doorstep pickup.
- **Key Value**: Replaces simulation with real bank-grade transaction references, verified phone credentials, and persistent cloud order records visible in the Firebase Console.

---

### 2. User Experience & Visual Design

- **Live Phone Auth Flow**:
  1. Customer enters mobile number (`+91 98765 43210`) and Punjab city.
  2. Tapping "Get OTP on SMS" triggers Firebase `PhoneAuthProvider` dispatching a real SMS code to their phone.
  3. The app displays an auto-focus 6-digit input with a 60-second resend timer.
  4. On entering the OTP, Firebase Auth signs the customer in, establishes their UID, and automatically syncs or creates their profile in Cloud Firestore.
- **Live Razorpay Checkout Flow**:
  1. When customer taps "Pay & Confirm", the boutique calls Razorpay with `BuildConfig.RAZORPAY_KEY_ID`.
  2. The custom modal presents verified UPI apps, card authorization (Visa, Mastercard, RuPay), and Punjab Netbanking (HDFC, SBI, ICICI, PNB).
  3. Upon payment capture, Razorpay generates a valid payment ID (e.g. `pay_...`), flags `isPaid = true`, and stores the full order in Firestore.
- **Visual Theme & Atmosphere**:
  - Warm cream porcelain background (`#FAF8F5`) with deep royal burgundy (`#5C1D24`) and metallic antique gold accents (`#C5A059`).
  - Subtle radial gradient glow accents inspired by Google apps for modern, welcoming polish.

---

### 3. Key Product Decisions & Trade-Offs

- **Decision 1: Secret Management for Razorpay Keys**:
  - *Chosen Approach*: Declare `RAZORPAY_KEY_ID` in `.env.example` and access via `BuildConfig.RAZORPAY_KEY_ID` using the Secrets Gradle Plugin.
  - *Why*: Prevents hardcoding secrets in source files while allowing you to enter your real `rzp_live_...` or `rzp_test_...` key directly in the AI Studio Secrets panel.
- **Decision 2: Firebase Phone Auth with Whitelisted Test Numbers**:
  - *Chosen Approach*: Implement full `PhoneAuthProvider.verifyPhoneNumber` callbacks, alongside test phone number support registered in Firebase Console.
  - *Why*: Real SMS delivery consumes carrier credits. Whitelisting your development phone numbers in Firebase Console allows unlimited free testing while keeping real SMS OTP active for live users.

---

### 4. Technical Architecture & Data Strategy *(Technical Reference)*

```
┌────────────────────────────────────────────────────────┐
│                   Android Applet UI                    │
│   (Jetpack Compose • M3 Porcelain & Burgundy Theme)     │
└──────────┬───────────────────────────────┬─────────────┘
           │                               │
           ▼                               ▼
┌─────────────────────────┐   ┌──────────────────────────┐
│   Firebase Phone Auth   │   │  Razorpay Checkout SDK   │
│  (SMS OTP Verification) │   │ (BuildConfig.KEY_ID API) │
└──────────┬──────────────┘   └────────────┬─────────────┘
           │ (Authenticated UID)           │ (Payment ID)
           ▼                               ▼
┌────────────────────────────────────────────────────────┐
│              FirestoreBoutiqueRepository               │
│  (Custom DB: ai-studio-android-thetrend-55449a...)     │
└──────────────────────────┬─────────────────────────────┘
                           │
                           ▼
┌────────────────────────────────────────────────────────┐
│               Cloud Firestore Database                 │
│  ├── /users/{uid} (Profile, City, Address)             │
│  ├── /users/{uid}/orders/{orderId} (Live Orders & Fit) │
│  ├── /users/{uid}/measurements/{profId} (Sizes)        │
│  └── /users/{uid}/consultations/{id} (Masterji Calls)  │
└────────────────────────────────────────────────────────┘
```

#### Step-by-Step Implementation Sequence

1. **Secret & Build Configuration**:
   - Add `RAZORPAY_KEY_ID` placeholder to `.env.example`.
   - Update `app/build.gradle.kts` secrets block to expose `BuildConfig.RAZORPAY_KEY_ID`.
2. **Live Firebase Phone Authentication**:
   - Implement `PhoneAuthProvider.verifyPhoneNumber` inside `BoutiqueViewModel.kt` using `PhoneAuthOptions.newBuilder(firebaseAuth)`.
   - Attach callbacks (`onCodeSent`, `onVerificationCompleted`, `onVerificationFailed`).
   - Store `verificationId` in ViewModel state for immediate verification upon customer OTP entry.
3. **Live Razorpay Payment Processing**:
   - Connect `BuildConfig.RAZORPAY_KEY_ID` into `RazorpayPaymentSheet` and `CheckoutScreen`.
   - Handle both live payment key mode and test fallback when key is pending configuration.
4. **Verification & Guidance**:
   - Verify compilation via `compile_applet`.
   - Provide clear instructions on where to enter the Razorpay key in the Secrets panel and how to view live data in the Firebase Console.
