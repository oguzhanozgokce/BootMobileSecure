package com.oguzhanozgokce.bootmobilesecure.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Splash : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data object Register : Screen

    @Serializable
    data object Password : Screen

    @Serializable
    data object Home : Screen
}