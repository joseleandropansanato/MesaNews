package br.com.jlcampos.mesanews.presentation.login

import android.app.Activity
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
                startActivityForResult(Intent(this, CadastroActivity::class.java), CadastroActivity.LAUNCH_SECOND_ACTIVITY)
            }
        }
    }

    private fun myObservers() {
        viewModel.signinLiveData.observe(this, {
            it?.let { myResource ->
                when (myResource.status) {

                    Status.SUCCESS -> {
                        hideProgressBar()

                        myResource.data.let { signin ->

                            if (signin != null && !signin.token.isNullOrEmpty()) {
                                darAcesso(signin.token)
                            } else {
                                wrong(/*signin?.code + " - " +*/ signin?.message)
                            }

                        }

                    }

                    Status.ERROR -> {
                        hideProgressBar()
                        wrong(myResource.message!!)
                    }

                    Status.LOADING -> {
                        showProgressBar()
                    }

                }
            }
        })

        viewModel.loggedLiveData.observe(this, {
            it?.let { myResource ->

                when(myResource.status) {

                    Status.SUCCESS -> {
                        if (myResource.data!!) {
                            goFeed()
                        } else {
                            wrong(myResource.message!!)
                        }
                    }

                    Status.ERROR -> {
                        hideProgressBar()
                        wrong(myResource.message!!)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CadastroActivity.LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                darAcesso(data?.getStringExtra(CadastroActivity.EXTRA_TOKEN))
            }
        }
    }

    private fun wrong(msg: String?) {
        Toast.makeText(this@LoginActivity, msg ?: "Oops!", Toast.LENGTH_LONG).show()
    }
}