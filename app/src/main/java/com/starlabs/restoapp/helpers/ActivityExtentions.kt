package com.starlabs.restoapp.helpers

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import com.starlabs.restoapp.R

fun Activity.getPref() = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

fun Activity.showAlert(){
    val builder = AlertDialog.Builder(this)
    builder.setTitle(getString(R.string.error))
    builder.setMessage(getString(R.string.error_message))
    builder.setPositiveButton(getString(R.string.ok), null)
    val dialog = builder.create()
    dialog.show()
}

fun Activity.isAdmin(): Boolean {
    val prefs = getPref()
    return prefs.getString("rol", null) == "admin"
}