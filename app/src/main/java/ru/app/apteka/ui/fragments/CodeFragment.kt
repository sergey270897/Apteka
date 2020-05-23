package ru.app.apteka.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_auth_code.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.app.apteka.R
import ru.app.apteka.network.NetworkState
import ru.app.apteka.ui.activities.AuthActivity
import ru.app.apteka.utils.extensions.onTextChanged
import ru.app.apteka.viewmodels.AuthModel

class CodeFragment : Fragment() {

    private val authModel: AuthModel by sharedViewModel()
    private var lastCode = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth_code, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        initObservers()

        authModel.startTimer()
    }

    private fun initObservers() {
        authModel.time.observe(viewLifecycleOwner, Observer {
            if (it == 0L) {
                auth_tv_timerOrRetry.text = getString(R.string.retry)
                auth_tv_timerOrRetry.setTextColor(resources.getColor(R.color.colorAccent))
                auth_tv_timerOrRetry.isClickable = true
            } else {
                auth_tv_timerOrRetry.text =
                    getString(R.string.timer, if (it < 10) "0$it" else "$it")
                auth_tv_timerOrRetry.setTextColor(Color.BLACK)
                auth_tv_timerOrRetry.isClickable = false
            }
        })

        authModel.networkState.observe(viewLifecycleOwner, Observer {
            auth_progress.visibility = if (it == NetworkState.RUNNING) View.VISIBLE else View.GONE
            if (it == NetworkState.WRONGDATA)
                auth_code.error = getString(R.string.wrongCode)

        })
    }

    private fun initViews() {
        auth_back.setOnClickListener {
            (activity as AuthActivity).supportFragmentManager.popBackStack()
        }

        auth_tv_timerOrRetry.setOnClickListener {
            authModel.sendEmail(authModel.profile.email!!)
            authModel.startTimer()
        }

        auth_send.setOnClickListener {
            val code = auth_code.editText?.text.toString().trim()
            if (code.isEmpty() || code.length != 6)
                auth_code.error = getString(R.string.errorFormatCode)
            else {
                if (lastCode != code) {
                    authModel.auth(authModel.profile.email!!, code)
                }
                lastCode = code
            }
        }

        tv_auth_code.onTextChanged {
            if(it?.length == 0) auth_code.error = null
        }
    }
}