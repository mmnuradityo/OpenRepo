package com.mmnuradityo.openrepo.data.viewstate

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
data class ViewState(
    var mode: Int? = 0,
    var onEvent: Boolean? = false,
    var onLoad: Boolean? = false,
    var onSuccess: Boolean? = false,
    var onError: Throwable? = null
)