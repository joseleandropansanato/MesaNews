package br.com.jlcampos.mesanews.presentation.cadastro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.jlcampos.mesanews.R
import br.com.jlcampos.mesanews.databinding.ActivityCadastroBinding

class CadastroActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cadBtApply.setOnClickListener(this)

        myObservers()
    }

    override fun onClick(v: View) {
        when(v) {
            binding.cadBtApply -> {
                if (binding.cadEtUser.text.toString().replace(" ","").isEmpty() ||
                    binding.cadEtPass.text.toString().replace(" ","").isEmpty() ||
                    binding.cadEtSecPass.text.toString().replace(" ","").isEmpty()) {

                    if (binding.cadEtSecPass.text.toString().replace(" ","").isEmpty()) {
                        wrong(getString(R.string.write_pass))
                    }

                    if (binding.cadEtPass.text.toString().replace(" ","").isEmpty()) {
                        wrong(getString(R.string.write_pass))
                    }

                    if (binding.cadEtUser.text.toString().replace(" ","").isEmpty()) {
                        wrong(getString(R.string.write_user))
                    }

                } else {

                    if (binding.cadEtPass.text.toString().replace(" ","") != binding.cadEtSecPass.text.toString().replace(" ","")
                    ) {
                        wrong(getString(R.string.pass_not_correct))
                    } else {
                        wrong("OK")
                    }


                }

            }
        }
    }

    private fun myObservers() {
//        TODO("Not yet implemented")
    }

    private fun wrong(msg: String) {
        Toast.makeText(this@CadastroActivity, msg, Toast.LENGTH_LONG).show()
    }

}