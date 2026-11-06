const { initializeTestEnvironment, assertFails, assertSucceeds } = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

const PROJECT_ID = "demo-no-project";
const DB_ID = "ai-studio-android-thetrend-55449a41-e363-4956-b6f2-7bab93b2368c";

let testEnv;

before(async () => {
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules: fs.readFileSync("firestore.rules", "utf8"),
      host: "127.0.0.1",
      port: 8085
    }
  });
});

beforeEach(async () => {
  await testEnv.clearFirestore();
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

test("unauthenticated user cannot read or write user data", async () => {
  const unauthedDb = testEnv.unauthenticatedContext().firestore();
  const userRef = unauthedDb.collection("users").doc("user_harpreet");

  await assertFails(userRef.get());
  await assertFails(userRef.set({
    userId: "user_harpreet",
    fullName: "Harpreet Kaur",
    phone: "+91 98761 23456",
    createdAt: new Date(),
    updatedAt: new Date()
  }));
});

test("authenticated owner can create and read user profile", async () => {
  const aliceDb = testEnv.authenticatedContext("user_alice").firestore();
  const aliceRef = aliceDb.collection("users").doc("user_alice");

  await assertSucceeds(aliceRef.set({
    userId: "user_alice",
    fullName: "Alice Kaur",
    phone: "+91 98765 43210",
    city: "Ludhiana",
    createdAt: new Date(),
    updatedAt: new Date()
  }));

  await assertSucceeds(aliceRef.get());
});

test("cross-user cannot read or write another user profile or orders", async () => {
  const aliceDb = testEnv.authenticatedContext("user_alice").firestore();
  const bobDb = testEnv.authenticatedContext("user_bob").firestore();

  // Alice creates her profile
  await assertSucceeds(aliceDb.collection("users").doc("user_alice").set({
    userId: "user_alice",
    fullName: "Alice Kaur",
    phone: "+91 98765 43210",
    createdAt: new Date(),
    updatedAt: new Date()
  }));

  // Bob cannot read Alice's profile
  await assertFails(bobDb.collection("users").doc("user_alice").get());

  // Bob cannot create order under Alice's path
  await assertFails(bobDb.collection("users").doc("user_alice").collection("orders").doc("order_1").set({
    orderId: "order_1",
    userId: "user_alice",
    suitTitle: "Royal Patiala Shahi Suit",
    totalAmount: 2499,
    paymentMethod: "Razorpay UPI",
    status: "PICKUP_SCHEDULED",
    createdAt: new Date(),
    updatedAt: new Date()
  }));
});

test("authenticated owner can create order and measurement profile with valid schema", async () => {
  const aliceDb = testEnv.authenticatedContext("user_alice").firestore();

  // Create order
  const orderRef = aliceDb.collection("users").doc("user_alice").collection("orders").doc("order_101");
  await assertSucceeds(orderRef.set({
    orderId: "order_101",
    userId: "user_alice",
    suitTitle: "Royal Patiala Shahi Suit",
    totalAmount: 2499,
    paymentMethod: "Razorpay (UPI - Google Pay)",
    status: "PICKUP_SCHEDULED",
    isPaid: true,
    sampleCount: 1,
    createdAt: new Date(),
    updatedAt: new Date()
  }));

  // Create measurement profile
  const measRef = aliceDb.collection("users").doc("user_alice").collection("measurements").doc("meas_1");
  await assertSucceeds(measRef.set({
    profileId: "meas_1",
    userId: "user_alice",
    label: "Standard Punjabi Suit Fit",
    bust: 36,
    waist: 30,
    hips: 40,
    createdAt: new Date(),
    updatedAt: new Date()
  }));
});
