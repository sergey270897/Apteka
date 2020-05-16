package ru.app.apteka.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_auth.*
import ru.app.apteka.R
import ru.app.apteka.ui.activities.AuthActivity
import ru.app.apteka.utils.extensions.simpleValidationEmail

class AuthFragment : Fragment() {
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
        auth_continue.setOnClickListener {
            auth_name.error = null
            auth_email.error = null
            val name = auth_name.editText?.text.toString().trim().isNotEmpty()
            val email = auth_email.editText?.text.toString().trim().simpleValidationEmail()

            if (name && email) sendCode()
            else {
                if (!name) auth_name.error = "Поле не может быть пустым"
                if (!email) auth_email.error = "Некорректный e-mail"
            }
        }

        auth_exit.setOnClickListener {

        }
    }

    private fun sendCode(){
        (activity as AuthActivity).startCodeFragment()
    }
}