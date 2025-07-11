package com.oguzhanozgokce.bootmobilesecure.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object PermissionManager {

    // Permission types for different features
    enum class PermissionType {
        CAMERA,
        STORAGE,
    }

    // Permission result states
    sealed class PermissionResult {
        object Granted : PermissionResult()
        object Denied : PermissionResult()
        object PermanentlyDenied : PermissionResult()
        data class PartiallyGranted(val grantedPermissions: List<String>) : PermissionResult()
    }

    // Permission group for related permissions
    data class PermissionGroup(
        val type: PermissionType,
        val permissions: List<String>,
        val title: String,
        val description: String,
        val isRequired: Boolean = true
    )

    // Get required permissions for each type
    fun getPermissionsForType(type: PermissionType): PermissionGroup {
        return when (type) {
            PermissionType.CAMERA -> PermissionGroup(
                type = PermissionType.CAMERA,
                permissions = listOf(Manifest.permission.CAMERA),
                title = "Camera Permission",
                description = "Required to take photos"
            )

            PermissionType.STORAGE -> PermissionGroup(
                type = PermissionType.STORAGE,
                permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    listOf(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                },
                title = "Storage Permission",
                description = "Required to access photos from gallery"
            )
        }
    }

    private fun checkPermissionGroup(context: Context, permissionGroup: PermissionGroup): PermissionResult {
        if (permissionGroup.permissions.isEmpty()) {
            return PermissionResult.Granted
        }

        val grantedPermissions = mutableListOf<String>()
        val deniedPermissions = mutableListOf<String>()

        permissionGroup.permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permission)
            } else {
                deniedPermissions.add(permission)
            }
        }

        return when {
            deniedPermissions.isEmpty() -> PermissionResult.Granted
            grantedPermissions.isEmpty() -> PermissionResult.Denied
            else -> PermissionResult.PartiallyGranted(grantedPermissions)
        }
    }

    // Check single permission type
    fun checkPermission(context: Context, type: PermissionType): PermissionResult {
        val permissionGroup = getPermissionsForType(type)
        return checkPermissionGroup(context, permissionGroup)
    }

    // Get permissions array for launcher
    fun getPermissionArray(vararg types: PermissionType): Array<String> {
        return types.flatMap { type ->
            getPermissionsForType(type).permissions
        }.distinct().toTypedArray()
    }

    // Check if any permission in group is granted (useful for partial access)
    fun hasAnyPermission(context: Context, type: PermissionType): Boolean {
        val permissionGroup = getPermissionsForType(type)
        return permissionGroup.permissions.any { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Get human-readable permission status
    fun getPermissionStatusText(context: Context, type: PermissionType): String {
        val result = checkPermission(context, type)
        val permissionGroup = getPermissionsForType(type)

        return when (result) {
            is PermissionResult.Granted -> "✅ ${permissionGroup.title} granted"
            is PermissionResult.Denied -> "❌ ${permissionGroup.title} denied"
            is PermissionResult.PermanentlyDenied -> "⚠️ ${permissionGroup.title} permanently denied"
            is PermissionResult.PartiallyGranted -> "⚠️ ${permissionGroup.title} partially granted"
        }
    }
}

// Extension functions for easier usage
fun Context.hasPermission(type: PermissionManager.PermissionType): Boolean {
    return PermissionManager.checkPermission(this, type) == PermissionManager.PermissionResult.Granted
}
