package com.shah.smartexpensetracker.ui.nav

/**
 * Created by Monil on 09/08/25.
 */

sealed class Route(val route: String) {
    data object Entry : Route("entry")
    data object List : Route("list")
    data object Report : Route("report")
}
