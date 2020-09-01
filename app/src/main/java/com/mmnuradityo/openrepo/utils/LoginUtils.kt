package com.mmnuradityo.openrepo.utils

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
class LoginUtils {

    companion object {
        const val VALID = "valid"

        fun validate(userName: String, password: String): String {
            if (userName == "" || password == "") {
                return "Username or Password cannot empty!"
            }

            var textMail = ""
            var index = userName.lastIndexOf('.')
            if (index > 0) index = userName.lastIndexOf('@', index)
            if (index > 0) textMail = userName.substring(index)

            if (textMail.contains('@') && textMail.contains('.')) {
                return "Sorry login with email not supported!"
            }

            return VALID
        }
    }

}