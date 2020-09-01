package com.mmnuradityo.openrepo.utils

import com.mmnuradityo.openrepo.OpenRepoApp

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
object Injection {

    fun providedRepository() = OpenRepoApp.getInstance().getRepository()

}