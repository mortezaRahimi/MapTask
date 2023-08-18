package com.example.holloomap.domain.location

import android.location.Location
import androidx.activity.result.IntentSenderRequest

interface LocationTracker {
    suspend fun getCurrentLocation(onEnabled :() ->Unit , onDisabled :((IntentSenderRequest)) ->Unit): Location?
}