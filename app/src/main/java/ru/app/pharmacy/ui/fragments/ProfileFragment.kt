package ru.app.pharmacy.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.app.pharmacy.R
import ru.app.pharmacy.models.Pharmacy
import ru.app.pharmacy.network.NetworkState
import ru.app.pharmacy.ui.activities.MainActivity
import ru.app.pharmacy.ui.adapters.CityAdapter
import ru.app.pharmacy.utils.extensions.getInitials
import ru.app.pharmacy.utils.extensions.onTextChanged
import ru.app.pharmacy.viewmodels.ProfileModel
import java.util.*

class ProfileFragment : Fragment() {

    private val modelProfile: ProfileModel by viewModel()
    private lateinit var adapter: CityAdapter
    private val calendar = Calendar.getInstance()

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
        initObservers()
    }

    private fun initObservers() {
        modelProfile.listPharmacy.observe(viewLifecycleOwner, Observer {
            adapter.addAll(it)
        })

        modelProfile.networkState.observe(viewLifecycleOwner, Observer {
            if (it == NetworkState.SUCCESS) {
                setCurrentCity()
            }

            if (it == NetworkState.RUNNING) adapter.setShowProgress(true) else adapter.setShowProgress(
                false
            )
        })
    }

    private fun initViews() {
        setInitials()
        adapter = CityAdapter(this.context!!, R.layout.list_item, R.id.list_item_tv)

        spinner_city_profile.adapter = adapter
        tv_email_profile.text = modelProfile.profile.email.toString()


        if (!modelProfile.isEdit.value!!) {
            name_profile.editText?.setText(modelProfile.profile.name)
            tv_bd_profile.text = modelProfile.profile.bd
            editMode(false)
        } else {
            editMode(true)
        }

        ib_edit_profile.setOnClickListener {
            if (modelProfile.isEdit.value!!) save()
            else editMode(true)
        }

        tv_name_profile.onTextChanged {
            if (it?.length == 0) name_profile.error = null
        }

        ib_exit_profile.setOnClickListener {
            modelProfile.exitProfile()
            (activity as MainActivity).supportFragmentManager.popBackStack()
        }

        tv_bd_profile.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        calendar.time = modelProfile.profile.getDate()
        val datePicker = DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                modelProfile.profile.setDate(calendar.time)
                tv_bd_profile.text = modelProfile.profile.bd
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE)
        )
        datePicker.show()
    }

    private fun setCurrentCity() {
        if (modelProfile.profile.pharmacyId > 0) {
            val positionCity = adapter.getPositionById(modelProfile.profile.pharmacyId)
            spinner_city_profile.setSelection(positionCity)
        }
    }

    private fun editMode(isEdit: Boolean) {
        spinner_city_profile.isEnabled = isEdit
        tv_bd_profile.isEnabled = isEdit
        tv_name_profile.isEnabled = isEdit
        tv_name_profile.isClickable = isEdit
        if (isEdit) {
            ib_edit_profile.setImageDrawable(resources.getDrawable(R.drawable.ic_save_black_24dp))
            ib_edit_profile.setColorFilter(resources.getColor(R.color.colorRed))
        } else {
            ib_edit_profile.setImageDrawable(resources.getDrawable(R.drawable.ic_edit_24dp))
            ib_edit_profile.colorFilter = null
        }
        modelProfile.isEdit.value = isEdit
    }

    private fun save() {
        val name = name_profile.editText?.text.toString().trim().isNotEmpty()
                && name_profile.editText?.text.toString().first().isLetter()
        return if (name) {
            editMode(false)
            name_profile.error = null
            modelProfile.profile.pharmacyId = (spinner_city_profile.selectedItem as Pharmacy).id
            modelProfile.profile.name = tv_name_profile.text?.trim().toString()
            modelProfile.saveProfile()
            setInitials()
        } else {
            name_profile.error = getString(R.string.fieldError)
        }
    }

    private fun setInitials() {
        val initials = modelProfile.profile.name?.getInitials()
        iv_avatar_profile.setInitials(initials!!)
    }
}