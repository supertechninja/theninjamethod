package com.mcwilliams.theninjamethod.ui.settings.repo

import com.mcwilliams.theninjamethod.network.Result
import com.mcwilliams.theninjamethod.ui.settings.data.Athlete

interface SettingsRepo {
    suspend fun authAthlete(code: String): Result<Athlete?>
}