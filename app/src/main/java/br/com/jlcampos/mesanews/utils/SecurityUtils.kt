package br.com.jlcampos.mesanews.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import br.com.jlcampos.mesanews.R
import br.com.jlcampos.mesanews.presentation.login.LoginActivity

class SecurityUtils {

    fun logout(context: Context) {
        val session = AppPrefs(context)
        session.logout()
        context.startActivity(Intent(context.applicationContext, LoginActivity::class.java))
        Toast.makeText(context, context.getString(R.string.disconnected), Toast.LENGTH_LONG).show()
    }
}