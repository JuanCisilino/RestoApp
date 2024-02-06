package com.starlabs.restoapp.helpers

import android.content.Context
import android.content.SharedPreferences
import com.starlabs.restoapp.R

class Prefs(val context: Context) {

    private val sharedPref: SharedPreferences
            by lazy { context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE) }

    /**
     * Convenient method to check if a preference exists or not. <br/>
     *
     * @param keyName preference
     * @return true if the preference exists, false otherwise
     */
    fun contains(keyName: String?): Boolean {
        return sharedPref.contains(keyName)
    }

    /**
     * Remove the given preference from the system. <br/>
     *
     * @param keyName of the field
     */
    fun remove(keyName: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(keyName)
        editor.apply()
    }

    fun clear(){
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    /**
     * Convenient method to persist the value in the preferences.<br/>
     *
     * @param keyName of the data point
     * @param value of the data point
     */
    fun save(keyName: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(keyName, value)
        editor.apply()
    }


    /**
     * Retrieves the string value for the given name. <br/>
     *
     * @param keyName of the field
     */
    fun getString(keyName: String): String? {
        return sharedPref.getString(keyName, "")
    }

    /**
     * Retrieves the boolean value for the given name and returns the default if
     * null.<br/>
     *
     * @param keyName of the field
     * @param defaultValue if it's null
     */
    fun getBoolean(keyName: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(keyName, defaultValue)
    }

}