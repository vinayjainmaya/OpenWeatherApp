package com.example.openweatherapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar


fun View.hideKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.hide() {
    isVisible = false
}

fun View.show() {
    isVisible = true
}

fun View.snackbar(message: String) {

    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
}