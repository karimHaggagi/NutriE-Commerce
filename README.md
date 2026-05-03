# 🛍️ E-Commerce KMP App

A modern **E-Commerce mobile application** built using **Kotlin Multiplatform (KMP)** targeting both **Android** and **iOS** platforms. The app provides a seamless shopping experience for users and a powerful admin panel for managing products.

---

## 🚀 Features

### 👤 User Features

* Browse products by categories
* View product details
* Add products to cart
* Manage shopping basket
* Secure checkout process
* Online payment integration using **PayPal**
* View payment status

### 🛠️ Admin Features

* Add new products
* Manage product listings
* Control product visibility

---

## 🏗️ Tech Stack

* **Kotlin Multiplatform (KMP)** – Shared business logic for Android & iOS
* **Jetpack Compose** – Shared UI
* **Firebase**:

  * Authentication
  * Firestore / Realtime Database
* **PayPal SDK** – Payment processing
* **Koin** – Dependency Injection
* **Coroutines & Flow** – Asynchronous programming

---

## 🧱 Architecture

The project follows **Clean Architecture** principles combined with a **multi-module structure** for scalability and maintainability.

### 📦 Core Modules

```
core/
│
├── data            # Repository implementations
├── domain          # Use cases & business logic
├── model           # Data models
├── di              # Dependency Injection setup
├── navigation      # Navigation handling
├── designsystem    # Shared UI components & theming
└── dataSource/
    ├── local       # Local storage (DB, cache)
    └── network     # API & remote data sources
```

---

### 🎯 Feature Modules

```
feature/
│
├── auth                # Authentication (Login/Register)
├── home                # Home screen
├── profile             # User profile
├── adminPanel          # Admin dashboard
├── manageProduct       # Product management
├── productOverview     # Product listing
├── details             # Product details
├── cart                # Shopping cart
├── category            # Categories listing
├── categoryProducts    # Products by category
├── checkout            # Checkout flow
├── paymentStatus       # Payment result screen
```

---

## 🔄 App Flow

1. User signs in / registers
2. Browse products from home or categories
3. View product details
4. Add items to cart
5. Proceed to checkout
6. Complete payment via PayPal
7. View payment status

---

## 📱 Platforms

* ✅ Android
* ✅ iOS

Shared business logic ensures consistency across both platforms while allowing native UI implementations.

---

## ⚙️ Setup & Installation

### Prerequisites

* Android Studio (latest)
* Xcode (latest)
* Kotlin Multiplatform setup
* Firebase project configured

### Steps

1. Clone the repository:

```bash
git clone https://github.com/your-username/your-repo.git
```

2. Open the project in Android Studio

3. Configure Firebase:

   * Add `google-services.json` for Android
   * Add `GoogleService-Info.plist` for iOS

4. Run the project:

   * Android: Run from Android Studio
   * iOS: Open `iosApp` in Xcode and run

---

## 🔐 Environment Configuration

Make sure to configure:

* Firebase credentials
* PayPal API keys

---

## 🧪 Testing

* Unit tests for domain and data layers
* ViewModel testing with coroutines

---

## 📌 Future Improvements

* Order history
* Push notifications
* Wishlist feature
* Product reviews & ratings
* Offline support

---

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

---

## 📄 License

This project is licensed under the MIT License.

---

## 👨‍💻 Author

**Karim Haggagi**

Android Developer | Kotlin Multiplatform Enthusiast

---
