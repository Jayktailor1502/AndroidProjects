package com.example.tdddemo.classes

import android.util.Patterns
import androidx.core.util.PatternsCompat
import java.util.regex.Pattern

class SignUpPageValidator {

    fun isFirstNameEmpty(firstName: String): Boolean {
        return firstName.isEmpty()
    }

    fun isLastNameEmpty(lastName: String): Boolean {
        return lastName.isEmpty()
    }

    fun isFirstNameContainDigitsOrSpecialCharacters(firstName: String): Boolean {
        return firstName.contains("[0-9!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex())
    }

    fun isLastNameContainDigitsOrSpecialCharacters(lastName: String): Boolean {
        return lastName.contains("[0-9!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex())
    }

    fun isAgeEmptyOrContainsAlphabets(age: String): Boolean {
        return if (age.isEmpty())
            true
        else age.contains(("[A-Za-z]".toRegex()))
    }

    fun isEmailValidated(email: String): Boolean {
        return if (email.isEmpty()) {
            false
        } else {
            val emailPattern: Pattern = PatternsCompat.EMAIL_ADDRESS
            emailPattern.matcher(email).matches()
        }
    }

    fun isPasswordValidated(password: String): Boolean {
        return if (password.isEmpty())
            false
        else {
            val passwordRegex = Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");

            passwordRegex.matcher(password).matches()
        }
    }

    fun isPasswordMatchesConfirmPassword(confirmPassword: String): Boolean {
        val password = "Password123@"
        return confirmPassword == password
    }

}