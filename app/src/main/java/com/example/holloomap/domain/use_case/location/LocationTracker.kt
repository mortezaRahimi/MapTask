package com.example.holloomap.domain.use_case.location

import android.location.Location
import androidx.activity.result.IntentSenderRequest

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}