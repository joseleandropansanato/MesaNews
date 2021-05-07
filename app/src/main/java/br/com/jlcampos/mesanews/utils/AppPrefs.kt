package br.com.jlcampos.mesanews.utils

import android.content.Context
import android.content.SharedPreferences

class AppPrefs(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(Constants.IDENTIFICATION, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    fun setToken(token: String?) {
        editor.putString(Constants.MY_TOKEN, token)
        editor.commit()
    }

    fun getToken() = prefs.getString(Constants.MY_TOKEN, null)

    fun setFavorite(favorite: String?) {
        editor.putString(Constants.MY_FAVORITE, favorite)
        editor.commit()
    }

    fun getFavorite() = prefs.getString(Constants.MY_FAVORITE, "{}")

    fun setLogged(logged: Boolean) {
        editor.putBoolean(Constants.LOGGED, logged)
        editor.commit()
    }

    fun isLogged() = prefs.getBoolean(Constants.LOGGED, false)

    fun logout() {
        setToken(null)
        setFavorite(null)
        setLogged(false)
    }
}