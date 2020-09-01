package com.mmnuradityo.openrepo.utils

import android.content.Context
import android.widget.Toast

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */

const val TEXT_SHORT = 0
const val TEXT_LONG = 1

fun Context.showText(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.showText(text: String, duration: Int) {
    Toast.makeText(this, text, duration).show()
}

fun Context.showText(resId: Int, duration: Int) {
    Toast.makeText(this, resId, duration).show()
}
