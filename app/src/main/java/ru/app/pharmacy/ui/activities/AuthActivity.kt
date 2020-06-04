package ru.app.pharmacy.ui.activities

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.app.pharmacy.R
import ru.app.pharmacy.network.NetworkState
import ru.app.pharmacy.ui.fragments.AuthFragment
import ru.app.pharmacy.ui.fragments.CodeFragment
import ru.app.pharmacy.utils.extensions.hideKeyboard
import ru.app.pharmacy.utils.extensions.startFragment
import ru.app.pharmacy.viewmodels.AuthModel

class AuthActivity : AppCompatActivity() {

    private val modelAuth: AuthModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        startAuthFragment()

        initObserver()
    }

    private fun initObserver() {
        modelAuth.networkState.observe(this, Observer {
            if (it == NetworkState.FAILED) showError(getString(R.string.errorSnackBar))
            if (it == NetworkState.WRONG_DATA && it.code == 1) showError("Невозможно выполнить вход")
        })

        modelAuth.finishAuth.observe(this, Observer {
            if (it) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })
    }

    private fun showError(msg:String) {
        hideKeyboard()
        Snackbar.make(auth_container, msg, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(resources.getColor(R.color.colorRed))
            .show()
    }

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