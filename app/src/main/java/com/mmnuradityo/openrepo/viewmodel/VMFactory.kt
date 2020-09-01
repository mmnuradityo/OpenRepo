package com.mmnuradityo.openrepo.viewmodel

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mmnuradityo.openrepo.data.repository.Repository
import com.mmnuradityo.openrepo.utils.Injection

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class VMFactory private constructor(
    private val application: Application,
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = with(modelClass) {
        when {
            isAssignableFrom(MainVM::class.java) -> MainVM(application, repository)
            isAssignableFrom(LoginVM::class.java) -> LoginVM(application, repository)
            isAssignableFrom(HomeVM::class.java) -> HomeVM(application, repository)
            isAssignableFrom(FollowerVM::class.java) -> FollowerVM(application, repository)
            isAssignableFrom(SearchVM::class.java) -> SearchVM(application, repository)
            isAssignableFrom(RepositoryVM::class.java) -> RepositoryVM(application)
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T

    companion object {
        @Volatile
        private var INSTANCE: VMFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(VMFactory::class.java) {
                INSTANCE ?: VMFactory(
                    application,
                    Injection.providedRepository()
                )
                    .also { INSTANCE = it }
            }

        @VisibleForTesting
        fun detach() {
            INSTANCE = null
        }
    }

}