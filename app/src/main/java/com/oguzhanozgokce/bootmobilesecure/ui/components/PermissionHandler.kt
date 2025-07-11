package com.oguzhanozgokce.bootmobilesecure.ui.components

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.oguzhanozgokce.bootmobilesecure.common.PermissionManager
import com.oguzhanozgokce.bootmobilesecure.common.hasPermission

@Composable
fun PermissionHandler(
    permissions: Array<PermissionManager.PermissionType>,
    onPermissionResult: (Map<PermissionManager.PermissionType, PermissionManager.PermissionResult>) -> Unit,
    onAllPermissionsGranted: () -> Unit,
    showCustomDeniedMessage: Boolean = true,
    customDeniedMessage: String? = null
): PermissionLauncher {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        val results = mutableMapOf<PermissionManager.PermissionType, PermissionManager.PermissionResult>()
        var allGranted = true

        permissions.forEach { permissionType ->
            val result = PermissionManager.checkPermission(context, permissionType)
            results[permissionType] = result

            if (result != PermissionManager.PermissionResult.Granted) {
                allGranted = false
            }
        }

        onPermissionResult(results)

        if (allGranted) {
            onAllPermissionsGranted()
        } else if (showCustomDeniedMessage) {
            val message = customDeniedMessage ?: buildDeniedMessage(permissions, context)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    return PermissionLauncher(
        launcher = permissionLauncher,
        context = context,
        permissions = permissions
    )
}

class PermissionLauncher(
    private val launcher: androidx.activity.result.ActivityResultLauncher<Array<String>>,
    private val context: Context,
    private val permissions: Array<PermissionManager.PermissionType>
) {

    fun requestPermissions() {
        val permissionArray = PermissionManager.getPermissionArray(*permissions)
        launcher.launch(permissionArray)
    }

    fun hasAllPermissions(): Boolean {
        return permissions.all { context.hasPermission(it) }
    }
}

private fun buildDeniedMessage(
    permissions: Array<PermissionManager.PermissionType>,
    context: Context
): String {
    val deniedPermissions = permissions.filter {
        !context.hasPermission(it)
    }.map {
        PermissionManager.getPermissionsForType(it).title
    }

    return when (deniedPermissions.size) {
        1 -> "${deniedPermissions.first()} is required for this feature"
        else -> "The following permissions are required: ${deniedPermissions.joinToString(", ")}"
    }
}

// Composable for specific image picker permissions
@Composable
fun ImagePickerPermissionHandler(
    onGalleryReady: () -> Unit,
    onCameraReady: () -> Unit,
    onPermissionDenied: (String) -> Unit = {}
): ImagePickerLauncher {
    val context = LocalContext.current

    val galleryPermissionHandler = PermissionHandler(
        permissions = arrayOf(PermissionManager.PermissionType.STORAGE),
        onPermissionResult = { results ->
            val storageResult = results[PermissionManager.PermissionType.STORAGE]
            if (storageResult is PermissionManager.PermissionResult.Granted) {
                onGalleryReady()
            } else {
                onPermissionDenied("Storage permission is required to select images from gallery")
            }
        },
        onAllPermissionsGranted = {},
        showCustomDeniedMessage = false
    )

    val cameraPermissionHandler = PermissionHandler(
        permissions = arrayOf(PermissionManager.PermissionType.CAMERA, PermissionManager.PermissionType.STORAGE),
        onPermissionResult = { results ->
            val cameraResult = results[PermissionManager.PermissionType.CAMERA]
            val storageResult = results[PermissionManager.PermissionType.STORAGE]

            when {
                cameraResult is PermissionManager.PermissionResult.Granted &&
                        storageResult is PermissionManager.PermissionResult.Granted -> {
                    onCameraReady()
                }

                cameraResult is PermissionManager.PermissionResult.Granted -> {
                    onPermissionDenied("Storage permission is also required for camera")
                }

                storageResult is PermissionManager.PermissionResult.Granted -> {
                    onPermissionDenied("Camera permission is required to take photos")
                }

                else -> {
                    onPermissionDenied("Camera and Storage permissions are required")
                }
            }
        },
        onAllPermissionsGranted = {},
        showCustomDeniedMessage = false
    )

    return ImagePickerLauncher(
        galleryLauncher = galleryPermissionHandler,
        cameraLauncher = cameraPermissionHandler,
        context = context
    )
}

class ImagePickerLauncher(
    private val galleryLauncher: PermissionLauncher,
    private val cameraLauncher: PermissionLauncher,
    private val context: Context
) {

    fun requestGalleryPermission() {
        if (galleryLauncher.hasAllPermissions()) {
            return
        }
        galleryLauncher.requestPermissions()
    }

    fun requestCameraPermission() {
        if (cameraLauncher.hasAllPermissions()) {
            return
        }
        cameraLauncher.requestPermissions()
    }

    fun hasGalleryPermission(): Boolean {
        return context.hasPermission(PermissionManager.PermissionType.STORAGE)
    }

    fun hasCameraPermission(): Boolean {
        return context.hasPermission(PermissionManager.PermissionType.CAMERA) &&
                context.hasPermission(PermissionManager.PermissionType.STORAGE)
    }
}