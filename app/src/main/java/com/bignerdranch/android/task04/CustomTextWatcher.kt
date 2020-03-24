package com.bignerdranch.android.task04

import android.text.Editable
import android.text.TextWatcher

abstract class CustomTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }
}