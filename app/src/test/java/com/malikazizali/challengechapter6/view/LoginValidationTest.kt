package com.malikazizali.challengechapter6.view

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LoginValidationTest {
    private lateinit var login: LoginValidation

    @Before
    fun setUp() {
        login = LoginValidation
    }

    @Test
    fun emptyUsername() {
        val result = login.validateLogin("","test")
        assertEquals("Username is empty !", result)
    }

    @Test
    fun emptyPassword() {
        val result = login.validateLogin("test","")
        assertEquals("Password is empty !", result)
    }

    @Test
    fun successValidation() {
        val result = login.validateLogin("test","test")
        assertEquals("validated", result)
    }


}