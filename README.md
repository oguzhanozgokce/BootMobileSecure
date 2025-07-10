# 🛡️ BootMobileSecure

**A modern, secure Android application built with Spring Boot backend integration**

![Android](https://img.shields.io/badge/Android-API%2024+-green.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Compatible-brightgreen.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

---

## 📱 Overview

BootMobileSecure is a production-ready Android application showcasing modern mobile development
practices with enterprise-grade security features. Built with **Kotlin**, **Jetpack Compose**, and *
*Clean Architecture**, it provides seamless integration with Spring Boot backends.

### ✨ Key Features

- 🔐 **Enterprise Security**: Hardware-backed encryption with Android Keystore
- 🎨 **Modern UI**: Beautiful Material Design 3 with Jetpack Compose
- 🏗️ **Clean Architecture**: MVVM + Repository pattern with Dependency Injection
- 🌐 **Network Safety**: Professional error handling and retry mechanisms
- 🔄 **Auto Token Management**: Secure JWT handling with automatic refresh
- 🎯 **Type Safety**: Full Kotlin coroutines with Result-based error handling

---

## 🏗️ Architecture

```
📱 BootMobileSecure
├── 🎨 UI Layer (Compose)
│   ├── Screens (Login, Register, Home)
│   ├── Components (Reusable UI)
│   └── ViewModels (MVI Pattern)
├── 🔄 Domain Layer
│   ├── Repositories (Interfaces)
│   └── Use Cases
├── 🗄️ Data Layer
│   ├── Repository Implementations
│   ├── Network (Retrofit + OkHttp)
│   ├── Security (TokenManager)
│   └── Models (DTOs)
└── 🔧 DI (Hilt)
```

### 🎯 Design Patterns

- **MVI (Model-View-Intent)**: Unidirectional data flow
- **Repository Pattern**: Clean data layer abstraction
- **Dependency Injection**: Hilt for loose coupling
- **Result Pattern**: Type-safe error handling

---

## 🔐 Security Features

### 🛡️ Multi-Layer Security

1. **Hardware Security Module (HSM)**
    - Android Keystore with StrongBox support
    - Hardware-backed encryption keys
    - Tamper-resistant storage

2. **EncryptedSharedPreferences**
    - AES-256 encryption for sensitive data
    - Automatic key rotation
    - Fallback mechanisms

3. **Network Security**
    - Certificate pinning
    - Request/Response encryption
    - Automatic token refresh

```kotlin
// Example: Secure token storage
tokenManager.saveTokensWithKeystore(
    accessToken = "jwt_token",
    refreshToken = "refresh_token",
    expiresIn = 3600
)
```

---

## 🚀 Quick Start

### Prerequisites

- **Android Studio**: Hedgehog or newer
- **Minimum SDK**: API 27 (Android 7.0)
- **Target SDK**: API 35 (Android 14)
- **Kotlin**: 2.0.0+

### 🔧 Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/oguzhanozgokce/BootMobileSecure.git
   cd BootMobileSecure
   ```

2. **Open in Android Studio**
   ```bash
   # Open the project in Android Studio
   # Build → Sync Project with Gradle Files
   ```

3. **Configure Backend URL**
   ```kotlin
   // In NetworkModule.kt
   private const val BASE_URL = "http://your-backend-url:8080/api/"
   ```

4. **Run the application**
   ```bash
   # Select device/emulator and click Run
   # Or use: ./gradlew installDebug
   ```

---

## 🎨 UI Components

### 🎯 Reusable Components

```kotlin
// Modern TextField with encryption-ready design
BMTextField(
    value = email,
    onValueChange = { email = it },
    placeholder = "Enter your email",
    leadingIcon = Icons.Default.Email,
    keyboardType = KeyboardType.Email
)

// Secure Button with loading states
BMPrimaryButton(
    text = "Login",
    onClick = { /* Handle click */ },
    isLoading = isLoading
)

// Professional Card layouts
BMFormCard {
    // Your form content
}
```

### 🎨 Design System

- **Colors**: Spring Boot green theme (`#6DB33F`)
- **Typography**: Material Design 3 type scale
- **Spacing**: 8dp grid system
- **Animations**: Smooth fade and slide transitions

---

## 🌐 Network Layer

### 📡 Safe API Calls

```kotlin
// Professional error handling
suspend fun login(request: LoginRequest): Result<AuthResponse> {
    return safeCall {
        authService.login(request)
    }.onSuccess { response ->
        tokenManager.saveAuthResponse(response.token, response.tokenType)
    }
}
```

### 🔄 Error Handling

| Error Type | Description | User Action |
|------------|-------------|-------------|
| `AuthException` | Authentication failed | Re-login required |
| `NetworkException` | Connection issues | Check internet |
| `ServerException` | Backend problems | Try again later |
| `ValidationException` | Input validation | Fix input data |

---

## 🧪 Testing

### 🔬 Test Structure

```bash
src/test/                    # Unit tests
├── repositories/           # Repository tests
├── viewmodels/            # ViewModel tests
└── network/               # Network layer tests

src/androidTest/            # Integration tests
├── ui/                    # UI tests
└── database/              # Database tests
```

### 🏃‍♂️ Running Tests

```bash
# Unit tests
./gradlew test

# UI tests
./gradlew connectedAndroidTest

# All tests
./gradlew check
```

---

## 📦 Dependencies

### 🔧 Core Libraries

```kotlin
// UI & Architecture
implementation("androidx.compose.ui:ui:$compose_version")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
implementation("com.google.dagger:hilt-android:$hilt_version")

// Network & Security
implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
implementation("androidx.security:security-crypto:$security_version")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")
```

---

## 🔧 Configuration

### 🌐 Backend Integration

#### Spring Boot Endpoints

```kotlin
interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>
    
    @POST("auth/register") 
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthResponse>>
    
    @POST("auth/refresh")
    suspend fun refreshToken(): Response<ApiResponse<AuthResponse>>
}
```

#### Expected API Response Format

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "user": {
      "id": 1,
      "username": "oguzhan33",
      "email": "oguzhan33@gmail.com",
      "firstName": "Oğuzhan",
      "lastName": "Özgökçe",
      "role": "USER"
    }
  }
}
```

---

## 🎯 Usage Examples

### 🔐 Authentication Flow

```kotlin
class LoginViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            val request = repository.createLoginRequest(username, password)
            
            repository.login(request).fold(
                onSuccess = { authResponse ->
                    // Navigate to home screen
                    emitUiEffect(UiEffect.NavigateToHome)
                },
                onFailure = { exception ->
                    // Handle error
                    val message = exception.getUserMessage()
                    updateUiState { copy(errorMessage = message) }
                }
            )
        }
    }
}
```

### 🏠 Home Screen Implementation

```kotlin
@Composable
fun HomeScreen(
    uiState: UiState,
    onAction: (UiAction) -> Unit
) {
    BMBaseScreen(
        isLoading = uiState.isLoading,
        containerColor = Color(0xFFF5F5F5)
    ) {
        LazyColumn {
            item { WelcomeCard(user = uiState.user) }
            item { UserInfoCard(user = uiState.user) }
            item { QuickActionsSection(actions = uiState.quickActions) }
        }
    }
}
```

---

## 🚀 Deployment

### 📱 Release Build

```bash
# Generate signed APK
./gradlew assembleRelease

# Generate App Bundle (recommended)
./gradlew bundleRelease
```

### 🔐 Security Checklist

- [ ] ProGuard/R8 enabled
- [ ] Certificate pinning configured
- [ ] Debug logs removed
- [ ] API keys secured
- [ ] Hardware security validated

---

## 🤝 Contributing

### 📝 Guidelines

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### 📋 Code Standards

- **Kotlin coding conventions**
- **Material Design 3 guidelines**
- **Clean Architecture principles**
- **Comprehensive documentation**

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Oğuzhan Özgökçe**

- GitHub: [@oguzhanozgokce](https://github.com/oguzhanozgokce)
- Email: oguzhan33@gmail.com

---

## 🙏 Acknowledgments

- **Spring Boot Team** for excellent backend framework
- **Android Team** for Jetpack Compose
- **Square** for Retrofit and OkHttp
- **Google** for Material Design and Security libraries

---

## 📱 Screenshots

<div align="center">

### 🔐 Login Screen

<img src="screenshots/login.png" width="250" alt="Login Screen"/>

### 📝 Register Screen

<img src="screenshots/register.png" width="250" alt="Register Screen"/>

### 🏠 Home Screen

<img src="screenshots/home.png" width="250" alt="Home Screen"/>

</div>

---

## 🔮 Roadmap

- [ ] **Biometric Authentication** (Fingerprint/Face ID)
- [ ] **Dark Theme** support
- [ ] **Multi-language** support
- [ ] **Offline Mode** with local caching
- [ ] **Push Notifications** integration
- [ ] **Analytics** and crash reporting

---

<div align="center">

**⭐ Star this repository if you found it helpful!**

[![GitHub stars](https://img.shields.io/github/stars/oguzhanozgokce/BootMobileSecure.svg?style=social&label=Star)](https://github.com/oguzhanozgokce/BootMobileSecure)

</div>