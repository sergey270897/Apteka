package ru.app.apteka.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_auth.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.app.apteka.R
import ru.app.apteka.ui.activities.AuthActivity
import ru.app.apteka.utils.extensions.onTextChanged
import ru.app.apteka.utils.extensions.simpleValidationEmail
import ru.app.apteka.viewmodels.AuthModel

class AuthFragment : Fragment() {

    private val modelAuth: AuthModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        auth_name.editText?.onTextChanged {
            if (it?.length == 0) auth_name.error = null
        }

        auth_email.editText?.onTextChanged {
            if (it?.length == 0) auth_email.error = null
        }

        auth_continue.setOnClickListener {
            auth_name.error = null
            auth_email.error = null
            val name = auth_name.editText?.text.toString().trim()
                .isNotEmpty() && auth_name.editText?.text.toString().first().isLetter()
            val email = auth_email.editText?.text.toString().trim().simpleValidationEmail()
            if (name && email) sendCode()
            else {
                if (!name) auth_name.error = getString(R.string.fieldError)
                if (!email) auth_email.error = getString(R.string.errorEmail)
            }
        }

        auth_exit.setOnClickListener {
            (activity as AuthActivity).setResult(Activity.RESULT_CANCELED)
            (activity as AuthActivity).finish()
        }
    }

    private fun sendCode() {
        modelAuth.sendEmail(auth_email.editText?.text.toString().trim())
        (activity as AuthActivity).startCodeFragment()
        modelAuth.setProfile(
            name = auth_name.editText?.text.toString().trim(),
            email = auth_email.editText?.text.toString().trim()
        )
    }
}