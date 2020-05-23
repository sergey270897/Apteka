package ru.app.apteka.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.app.apteka.R
import ru.app.apteka.network.NetworkState
import ru.app.apteka.ui.fragments.AuthFragment
import ru.app.apteka.ui.fragments.CodeFragment
import ru.app.apteka.utils.extensions.startFragment
import ru.app.apteka.viewmodels.AuthModel

class AuthActivity : AppCompatActivity() {

    private val modelAuth:AuthModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        startAuthFragment()

        initObserver()
    }

    private fun initObserver() {
        modelAuth.networkState.observe(this, Observer {
            if(it == NetworkState.FAILED) showError()
        })

        modelAuth.finishAuth.observe(this, Observer {
            if(it){
                setResult(Activity.RESULT_OK)
                finish()
            }
        })
    }

    private fun showError() =
        Snackbar.make(auth_container, getString(R.string.errorSnackbar), Snackbar.LENGTH_SHORT)
            .setBackgroundTint(resources.getColor(R.color.colorRed))
            .show()

    private fun startAuthFragment() {
        startFragment(
            R.id.auth_container,
            AuthFragment(),
            AuthFragment::class.simpleName.toString(),
            false
        )
    }

    fun startCodeFragment() {
        startFragment(
            R.id.auth_container,
            CodeFragment(),
            CodeFragment::class.simpleName.toString(),
            setAnim = true
        )
    }


}