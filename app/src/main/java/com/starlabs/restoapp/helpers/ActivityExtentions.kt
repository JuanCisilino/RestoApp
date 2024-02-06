package com.starlabs.restoapp.helpers

import android.app.Activity
import android.app.AlertDialog
import com.starlabs.restoapp.R

fun Activity.showAlert(){
    val builder = AlertDialog.Builder(this)
    builder.setTitle(getString(R.string.error))
    builder.setMessage(getString(R.string.error_message))
    builder.setPositiveButton(getString(R.string.ok), null)
    val dialog = builder.create()
    dialog.show()
}