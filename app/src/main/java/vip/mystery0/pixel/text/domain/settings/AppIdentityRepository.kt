package vip.mystery0.pixel.text.domain.settings

import kotlinx.coroutines.flow.StateFlow

interface AppIdentityRepository {
    val identity: StateFlow<AppIdentity>
    suspend fun apply(identity: AppIdentity): Result<Unit>
    fun reconcile()
}
