package com.app.trustlink.extension


import android.content.Context

object PrefManager {

    private fun getPrefName(): String {
        return "dci-pref"
    }

    fun putString(context: Context, key: String, `object`: String) {
        context.getSharedPreferences(getPrefName(), 0).edit().putString(key, `object`).apply()
    }

    fun getString(context: Context, key: String, defaultValue: String): String? {
        return context.getSharedPreferences(getPrefName(), 0).getString(key, defaultValue)
    }

    fun putInt(context: Context, key: String, `object`: Int) {
        context.getSharedPreferences(getPrefName(), 0).edit().putInt(key, `object`).apply()
    }

    fun getInt(context: Context, key: String, defaultValue: Int): Int {
        return context.getSharedPreferences(getPrefName(), 0).getInt(key, defaultValue)
    }

    fun remove(context: Context, key: String) {
        context.getSharedPreferences(getPrefName(), 0).edit().remove(key).apply()
    }

    fun logOut(context: Context) {
        val keys = arrayOf(
            Constant.PREFERENCE.DATA_KTP,
            Constant.PREFERENCE.USER_LOGIN,
                    Constant.HEADER.APP_ID,
            Constant.HEADER.API_KEY
        )

        val editor = context.getSharedPreferences(getPrefName(), 0).edit()

        for (prefName in keys) {
            editor.remove(prefName)
        }

        editor.apply()
    }
}