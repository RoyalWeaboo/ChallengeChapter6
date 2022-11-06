package com.malikazizali.challengechapter6.view

object LoginValidation {
    fun validateLogin(username : String, password : String) : String{
        var validateStatus = ""
        validateStatus = if (username.isEmpty()) {
            "Username is empty !"
        } else if (password.isEmpty()) {
            "Password is empty !"
        } else "validated"
        return validateStatus
    }
}