package br.com.jlcampos.mesanews.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import br.com.jlcampos.mesanews.R
import br.com.jlcampos.mesanews.databinding.ActivityLoginBinding
import br.com.jlcampos.mesanews.presentation.cadastro.CadastroActivity
import br.com.jlcampos.mesanews.presentation.feed.FeedActivity
import br.com.jlcampos.mesanews.utils.AppPrefs
import br.com.jlcampos.mesanews.utils.Status

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.loginBtLogin.setOnClickListener(this)
        binding.loginBtRegister.setOnClickListener(this)

        myObservers()

    }

    override fun onResume() {
        super.onResume()

        if (AppPrefs(applicationContext).isLogged()) {
            goFeed()
        }
    }

    private fun logar(user: String, pass: String) {
        viewModel.getSignin(user,pass)
    }

    private fun darAcesso(token: String?) {
        viewModel.tratarLogin(token)
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
                    logar(binding.loginEtUser.text.toString(), binding.loginEtPass.text.toString())
                }

            }

            binding.loginBtRegister -> {
                startActivity(Intent(this, CadastroActivity::class.java))
            }
        }
    }

    private fun myObservers() {
        viewModel.signinLiveData.observe(this, {
            it?.let { myResouce ->
                when (myResouce.status) {

                    Status.SUCCESS -> {
                        hideProgressBar()

                        it.data.let { signin ->

                            if (signin != null && !signin.token.isNullOrEmpty()) {
                                darAcesso(signin.token)
                            } else {
                                wrong(/*signin?.code + " - " +*/ signin?.message!!)
                            }

                        }

                    }

                    Status.ERROR -> {
                        hideProgressBar()
                        wrong(it.message!!)
                    }

                    Status.LOADING -> {
                        showProgressBar()
                    }

                }
            }
        })

        viewModel.loggedLiveData.observe(this, {
            it?.let {
                when(it.status) {

                    Status.SUCCESS -> {
                        if (it.data!!) {
                            goFeed()
                        } else {
                            wrong(it.message!!)
                        }
                    }

                    Status.ERROR -> {
                        hideProgressBar()
                        wrong(it.message!!)
                    }

                    Status.LOADING -> {
                        showProgressBar()
                    }

                }
            }
        })
    }

    private fun showProgressBar() {
        binding.loginPbLogin.visibility = View.VISIBLE
        binding.loginBtLogin.visibility = View.INVISIBLE
    }

    private fun hideProgressBar() {
        binding.loginPbLogin.visibility = View.GONE
        binding.loginBtLogin.visibility = View.VISIBLE
    }

    private fun goFeed() {
        startActivity(Intent(this@LoginActivity, FeedActivity::class.java))
        finish()
    }

    private fun wrong(msg: String) {
        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
    }
}