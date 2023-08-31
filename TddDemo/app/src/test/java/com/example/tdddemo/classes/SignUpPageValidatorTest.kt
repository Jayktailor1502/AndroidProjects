package com.example.tdddemo.classes

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SignUpPageValidatorTest {

    private lateinit var SUT : SignUpPageValidator

    @Before
    fun setUp() {
        SUT = SignUpPageValidator()
    }

    @Test
    fun isEmptyCheck_emptyFirstName_returnTrue() {
        val result = SUT.isFirstNameEmpty("")
        Assert.assertTrue(result)
    }

    @Test
    fun isEmptyCheck_emptyFirstName_returnFalse() {
        val result = SUT.isFirstNameEmpty("Mohit")
        Assert.assertFalse(result)
    }

    @Test
    fun isEmptyCheck_emptyLastName_returnTrue() {
        val result = SUT.isLastNameEmpty("")
        Assert.assertTrue(result)
    }

    @Test
    fun isEmptyCheck_emptyLastName_returnFalse() {
        val result = SUT.isLastNameEmpty("Sharma")
        Assert.assertFalse(result)
    }


    @Test
    fun isStringCheck_firstNameContainDigitsOrSpecialCharacters_returnTrue() {
        val result = SUT.isFirstNameContainDigitsOrSpecialCharacters("Mohit@1")
        Assert.assertTrue(result)
    }

    @Test
    fun isStringCheck_firstNameContainDigitsOrSpecialCharacters_returnFalse() {
        val result = SUT.isFirstNameContainDigitsOrSpecialCharacters("Mohit")
        Assert.assertFalse(result)
    }

    @Test
    fun isStringCheck_lastNameContainDigitsOrSpecialCharacters_returnTrue() {
        val result = SUT.isLastNameContainDigitsOrSpecialCharacters("Jain@1")
        Assert.assertTrue(result)
    }

    @Test
    fun isStringCheck_lastNameContainDigitsOrSpecialCharacters_returnFalse() {
        val result = SUT.isLastNameContainDigitsOrSpecialCharacters("Jain")
        Assert.assertFalse(result)
    }

    @Test
    fun isAgeCheck_emptyAgeOrContainsAlphabets_returnTrue() {
        val result = SUT.isAgeEmptyOrContainsAlphabets("asd12")
        Assert.assertTrue(result)
    }

    @Test
    fun isAgeCheck_emptyAgeOrContainsAlphabets_returnFalse() {
        val result = SUT.isAgeEmptyOrContainsAlphabets("12")
        Assert.assertFalse(result)
    }

    @Test
    fun isEmailCheck_emailValidationVerified_returnTrue() {
        val result = SUT.isEmailValidated("abc++1@gmail.com")
        Assert.assertTrue(result)
    }

    @Test
    fun isEmailCheck_emailValidationVerified_returnFalse() {
        val result = SUT.isEmailValidated("abc+@+1@gmail.com")
        Assert.assertFalse(result)
    }

    @Test
    fun isPasswordCheck_passwordValidationVerified_returnTrue() {
        val result = SUT.isPasswordValidated("Password@123")
        Assert.assertTrue(result)
    }

    @Test
    fun isPasswordCheck_passwordValidationVerified_returnFalse() {
        val result = SUT.isPasswordValidated("Password123")
        Assert.assertFalse(result)
    }

    @Test
    fun isPasswordMatch_passwordMatchesConfirmPassword_returnTrue() {
        // password - Password123@
        val result = SUT.isPasswordMatchesConfirmPassword("Password123@")
        Assert.assertTrue(result)
    }

    @Test
    fun isPasswordMatch_passwordMatchesConfirmPassword_returnFalse() {
        // password - Password123@
        val result = SUT.isPasswordMatchesConfirmPassword("Password123")
        Assert.assertFalse(result)
    }
}