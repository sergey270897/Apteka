package ru.app.apteka.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.app.apteka.R
import ru.app.apteka.ui.fragments.AuthFragment
import ru.app.apteka.ui.fragments.CodeFragment
import ru.app.apteka.utils.extensions.startFragment

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        startAuthFragment()
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