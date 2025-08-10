package com.shah.smartexpensetracker.utils

import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

/**
 * Created by Monil on 10/08/25.
 */

object AppClock {
    @Volatile
    var clock: Clock = Clock.systemUTC()
    fun today(zoneId: ZoneId = ZoneId.of("UTC")): LocalDate = LocalDate.now(clock.withZone(zoneId))
}
