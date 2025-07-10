package com.oguzhanozgokce.bootmobilesecure.data.network

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    companion object {
        private const val KEY_ALIAS = "auth_token_key"
        private const val PREF_ENCRYPTED_TOKEN = "encrypted_token"
        private const val PREF_TOKEN_IV = "token_iv"
        private const val PREF_TOKEN_TYPE = "token_type"
        private const val CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"
    }

    init {
        generateKeyIfNotExists()
    }

    private fun generateKeyIfNotExists() {
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

            val keySpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true)
                .build()

            keyGenerator.init(keySpec)
            keyGenerator.generateKey()
        }
    }

    private fun getSecretKey(): SecretKey {
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    private fun encrypt(data: String): Pair<String, String>? {
        return try {
            val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())

            val encryptedData = cipher.doFinal(data.toByteArray())
            val iv = cipher.iv

            val encryptedBase64 = Base64.encodeToString(encryptedData, Base64.DEFAULT)
            val ivBase64 = Base64.encodeToString(iv, Base64.DEFAULT)

            Pair(encryptedBase64, ivBase64)
        } catch (e: Exception) {
            null
        }
    }

    private fun decrypt(encryptedData: String, iv: String): String? {
        return try {
            val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
            val ivBytes = Base64.decode(iv, Base64.DEFAULT)
            val spec = GCMParameterSpec(128, ivBytes)

            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)

            val encryptedBytes = Base64.decode(encryptedData, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(encryptedBytes)

            String(decryptedBytes)
        } catch (e: Exception) {
            null
        }
    }

    fun saveToken(token: String, tokenType: String = "Bearer") {
        val encryptResult = encrypt(token)

        if (encryptResult != null) {
            val (encryptedToken, iv) = encryptResult
            prefs.edit {
                putString(PREF_ENCRYPTED_TOKEN, encryptedToken)
                putString(PREF_TOKEN_IV, iv)
                putString(PREF_TOKEN_TYPE, tokenType)
            }
        }
    }

    fun getToken(): String? {
        val encryptedToken = prefs.getString(PREF_ENCRYPTED_TOKEN, null)
        val iv = prefs.getString(PREF_TOKEN_IV, null)

        return if (encryptedToken != null && iv != null) {
            decrypt(encryptedToken, iv)
        } else {
            null
        }
    }

    fun getTokenType(): String {
        return prefs.getString(PREF_TOKEN_TYPE, "Bearer") ?: "Bearer"
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    fun clearToken() {
        prefs.edit {
            remove(PREF_ENCRYPTED_TOKEN)
            remove(PREF_TOKEN_IV)
            remove(PREF_TOKEN_TYPE)
        }
    }
}
