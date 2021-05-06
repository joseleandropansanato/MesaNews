package br.com.jlcampos.mesanews.presentation.cadastro

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import br.com.jlcampos.mesanews.R
import br.com.jlcampos.mesanews.databinding.ActivityCadastroBinding
import br.com.jlcampos.mesanews.utils.Status

class CadastroActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCadastroBinding

    private lateinit var viewModel: CadastroViewModel

    companion object {
        const val LAUNCH_SECOND_ACTIVITY = 1
        const val EXTRA_TOKEN = "EXTRA_TOKEN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(CadastroViewModel::class.java)

        binding.cadBtApply.setOnClickListener(this)

        myObservers()

    }

    override fun onClick(v: View) {
        when(v) {
            binding.cadBtApply -> {

                val name = binding.cadEtUser.text.toString()
                val email = binding.cadEtEmail.text.toString()
                val pass = binding.cadEtPass.text.toString()
                val cPass = binding.cadEtSecPass.text.toString()

                if (name.replace(" ","").isEmpty() ||
                    email.replace(" ","").isEmpty() ||
                    pass.replace(" ","").isEmpty() ||
                    cPass.replace(" ","").isEmpty()) {

                    if (cPass.replace(" ","").isEmpty()) {
                        wrong(getString(R.string.write_pass))
                    }

                    if (pass.replace(" ","").isEmpty()) {
                        wrong(getString(R.string.write_pass))
                    }

                    if (email.replace(" ","").isEmpty()) {
                        wrong(getString(R.string.write_email))
                    }

                    if (name.replace(" ","").isEmpty()) {
                        wrong(getString(R.string.write_name))
                    }

                } else {

                    if (pass.replace(" ","") != cPass.replace(" ","")
                    ) {
                        wrong(getString(R.string.pass_not_correct))
                    } else {
                        cadastrar(name,email, pass)
                    }
                }

            }
        }
    }

    private fun cadastrar(name: String, email: String, pass: String) {
        viewModel.pushSignup(name = name, email = email, pwd = pass)
    }

    private fun myObservers() {
        viewModel.signupLiveData.observe(this, {
            it?.let { myResource ->
                when (myResource.status) {

                    Status.SUCCESS -> {
                        hideProgressbar()

                        myResource.data.let { signup ->
                            if (signup != null && !signup.token.isNullOrEmpty()) {

                                wrong(signup.message)
                                returnLogin(signup.token)

                            } else {
                                wrong(signup?.message)
                            }
                        }

                    }

                    Status.ERROR -> {
                        hideProgressbar()
                        wrong(myResource.message)
                    }

                    Status.LOADING -> {
                        showProgressBar()
                    }

                }
            }
        })
    }

    private fun showProgressBar() {
        binding.cadPbApply.visibility = View.VISIBLE
        binding.cadBtApply.visibility = View.INVISIBLE
    }

    private fun hideProgressbar() {
        binding.cadPbApply.visibility = View.GONE
        binding.cadBtApply.visibility = View.VISIBLE
    }

    private fun returnLogin(token: String?) {

        val returnIntent = Intent()
        returnIntent.putExtra(EXTRA_TOKEN, token)

        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun wrong(msg: String?) {
        Toast.makeText(this@CadastroActivity, msg ?: "Ooops!", Toast.LENGTH_LONG).show()
    }

}