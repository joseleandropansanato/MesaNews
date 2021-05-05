package br.com.jlcampos.mesanews.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.jlcampos.mesanews.R
import br.com.jlcampos.mesanews.databinding.ActivityLoginBinding
import br.com.jlcampos.mesanews.presentation.cadastro.CadastroActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtLogin.setOnClickListener(this)
        binding.loginBtRegister.setOnClickListener(this)

        myObservers()

    }

    override fun onClick(v: View) {

        when(v) {
            binding.loginBtLogin -> {

                if (binding.loginEtUser.text.toString().replace(" ","").isEmpty() ||
                    binding.loginEtPass.text.toString().replace(" ","").isEmpty()) {

                    if (binding.loginEtPass.text.toString().replace(" ","").isEmpty()) {
                        wrong(getString(R.string.write_pass))
                    }

                    if (binding.loginEtUser.text.toString().replace(" ","").isEmpty()) {
                        wrong(getString(R.string.write_user))
                    }

                } else {
                    wrong("OK")
                }

            }

            binding.loginBtRegister -> {
                startActivity(Intent(this, CadastroActivity::class.java))
            }
        }
    }

    private fun myObservers() {
//        TODO("Not yet implemented")
    }

    private fun wrong(msg: String) {
        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
    }
}