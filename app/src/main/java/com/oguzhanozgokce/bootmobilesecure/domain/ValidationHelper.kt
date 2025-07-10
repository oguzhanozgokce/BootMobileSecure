package com.oguzhanozgokce.bootmobilesecure.domain

object ValidationHelper {
    
    data class RegisterValidationResult(
        val usernameError: String? = null,
        val emailError: String? = null,
        val passwordError: String? = null,
        val firstNameError: String? = null,
        val lastNameError: String? = null
    ) {
        fun hasErrors() = usernameError != null || emailError != null || 
                          passwordError != null || firstNameError != null || lastNameError != null
    }

    data class LoginWithUsernameValidationResult(
        val usernameError: String? = null,
        val passwordError: String? = null
    ) {
        fun hasErrors() = usernameError != null || passwordError != null
    }

    fun validateRegister(
        username: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): RegisterValidationResult {
        return RegisterValidationResult(
            usernameError = validateUsername(username),
            emailError = validateEmail(email),
            passwordError = validatePassword(password),
            firstNameError = validateFirstName(firstName),
            lastNameError = validateLastName(lastName)
        )
    }

    fun validateLoginWithUsername(username: String, password: String): LoginWithUsernameValidationResult {
        return LoginWithUsernameValidationResult(
            usernameError = validateLoginUsername(username),
            passwordError = validateLoginPassword(password)
        )
    }

    private fun validateUsername(username: String): String? {
        return when {
            username.isBlank() -> "Username is required"
            username.length < 3 -> "Username must be at least 3 characters"
            username.length > 20 -> "Username must be less than 20 characters"
            !username.matches(Regex("^[a-zA-Z0-9_]+$")) -> "Username can only contain letters, numbers and underscore"
            else -> null
        }
    }

    private fun validateLoginUsername(username: String): String? {
        return when {
            username.isBlank() -> "Username is required"
            else -> null
        }
    }

    private fun validateEmail(email: String): String? {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return when {
            email.isBlank() -> "Email is required"
            !email.matches(emailPattern.toRegex()) -> "Please enter a valid email address"
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "Password is required"
            password.length < 6 -> "Password must be at least 6 characters"
            password.length > 100 -> "Password is too long"
            else -> null
        }
    }

    private fun validateLoginPassword(password: String): String? {
        return when {
            password.isBlank() -> "Password is required"
            else -> null
        }
    }

    private fun validateFirstName(firstName: String): String? {
        return when {
            firstName.isBlank() -> "First name is required"
            firstName.length < 2 -> "First name must be at least 2 characters"
            firstName.length > 50 -> "First name is too long"
            else -> null
        }
    }

    private fun validateLastName(lastName: String): String? {
        return when {
            lastName.isBlank() -> "Last name is required"
            lastName.length < 2 -> "Last name must be at least 2 characters"
            lastName.length > 50 -> "Last name is too long"
            else -> null
        }
    }
}
