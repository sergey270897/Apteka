package ru.app.apteka.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.app.apteka.R
import ru.app.apteka.ui.activities.MainActivity
import ru.app.apteka.viewmodels.ProfileModel

class ProfileFragment : Fragment() {

    private val modelProfile: ProfileModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.hide()
        initViews()
    }

    private fun initViews() {
        setInitials()

        val adapter =
            ArrayAdapter(this.context!!, R.layout.list_item, resources.getStringArray(R.array.city))
        spinner_city_profile.adapter = adapter
        tv_email_profile.text = modelProfile.profile.email.toString()

        if (!modelProfile.isEdit.value!!) {
            modelProfile.profile.city?.let {
                val positionCity = adapter.getPosition(it)
                spinner_city_profile.setSelection(positionCity)
            }
            name_profile.editText?.setText(modelProfile.profile.name)
            editMode(false)
        } else {
            editMode(true)
        }


        ib_edit_profile.setOnClickListener {
            if (modelProfile.isEdit.value!!) save()
            else editMode(true)
        }

        tv_name_profile.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0) name_profile.error = null
            }
        })

        ib_exit_profile.setOnClickListener {
            modelProfile.exitProfile()
            (activity as MainActivity).supportFragmentManager.popBackStack()
        }
    }

    private fun editMode(isEdit:Boolean) {
        spinner_city_profile.isEnabled = isEdit

        tv_name_profile.isEnabled = isEdit
        tv_name_profile.isClickable = isEdit
        if(isEdit){
            ib_edit_profile.setImageDrawable(resources.getDrawable(R.drawable.ic_save_black_24dp))
            ib_edit_profile.setColorFilter(resources.getColor(R.color.colorRed))
        }else{
            ib_edit_profile.setImageDrawable(resources.getDrawable(R.drawable.ic_edit_24dp))
            ib_edit_profile.colorFilter = null
        }
        modelProfile.isEdit.value = isEdit
    }

    private fun save(){
        val name = name_profile.editText?.text.toString().trim().isNotEmpty()
            && name_profile.editText?.text.toString().first().isLetter()
        return if (name) {
            editMode(false)
            name_profile.error = null
            modelProfile.profile.city = spinner_city_profile.selectedItem.toString()
            modelProfile.profile.name = tv_name_profile.text?.trim().toString()
            modelProfile.saveProfile()
            setInitials()
        } else {
            name_profile.error = getString(R.string.fieldError)
        }
    }

    private fun setInitials() {
        val initials = modelProfile.profile.name?.first()?.toUpperCase().toString()
        iv_avatar_profile.setInitials(initials)
    }
}