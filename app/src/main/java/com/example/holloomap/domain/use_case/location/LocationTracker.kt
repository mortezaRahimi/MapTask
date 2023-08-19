package com.example.holloomap.domain.use_case.location

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}