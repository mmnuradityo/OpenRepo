package com.mmnuradityo.openrepo.utils

import android.content.Context
import android.text.Html
import android.text.Spanned
import com.mmnuradityo.openrepo.R

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class AppDetail {

    companion object {

        fun getAppDetail(context: Context): Spanned? {
            var appDetail = ""
            val enter = "<br>"
            val bold = "<b>"
            val endBold = "</b>"
            val font = "<font color='#FF5722'>"
            val endFont = "</font>"

            appDetail =
                "App Name : $bold $font ${context.getString(R.string.app_name)} $endFont $endBold $enter"

            appDetail += "App Version : $bold $font ${context.packageManager.getPackageInfo(
                context.packageName,
                0
            ).versionName} $endFont $endBold $enter"

            appDetail += "App Detail : $enter ${context.getString(R.string.app_detail)}"

            return Html.fromHtml(appDetail, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL)
        }
    }

}