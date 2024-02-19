package com.starlabs.restoapp.ui.extentions

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.IOException

fun showToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

@Throws(IOException::class)
fun Activity.createImageFile(name: String): File {
    // Create an image file name
    val timeStamp: String = name
    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        timeStamp, /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}
