package ru.app.apteka.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.fragmnet_info.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.app.apteka.R
import ru.app.apteka.databinding.FragmnetInfoBinding
import ru.app.apteka.models.Medicine
import ru.app.apteka.ui.activities.MainActivity
import ru.app.apteka.viewmodels.MedicineModel

class InfoFragment() : Fragment() {

    companion object {

        fun getInstance(item: Medicine): InfoFragment {
            val fragment = InfoFragment()
            val bundle = Bundle()
            bundle.putLong("id", item.id)
            bundle.putLong("categoryId", item.categoryId)
            bundle.putString("title", item.title)
            bundle.putString("categoryName", item.categoryName)
            bundle.putString("description", item.description)
            bundle.putString("image", item.image)
            bundle.putFloat("price", item.price)
            bundle.putFloat("rating", item.rating)
            bundle.putBoolean("available", item.available)
            bundle.putInt("count", item.count.value!!)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val medicineModel: MedicineModel by viewModel { parametersOf(0) }
    private lateinit var medicine: Medicine

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmnetInfoBinding>(
            inflater,
            R.layout.fragmnet_info,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.medicine = medicine
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val id = it.getLong("id")
            val categoryId = it.getLong("categoryId")
            val title = it.getString("title") ?: ""
            val categoryName = it.getString("categoryName") ?: ""
            val description = it.getString("description") ?: ""
            val image = it.getString("image") ?: ""
            val price = it.getFloat("price")
            val rating = it.getFloat("rating")
            val available = it.getBoolean("available")
            val count = it.getInt("count")
            medicine = Medicine(
                id,
                title,
                categoryId,
                categoryName,
                image,
                price,
                rating,
                available,
                description
            )
            medicine.count = MutableLiveData(count)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = medicine.categoryName

        initViews()
    }

    private fun initViews() {
        rating_medicine_info.rating = medicine.rating

        btn_buy_medicine_info.setOnClickListener {
            medicine.count.value = medicine.count.value?.plus(1)
            onClickBuy(medicine)
        }
        btn_dec_medicine_info.setOnClickListener {
            medicine.count.value = medicine.count.value?.minus(1)
            onClickBuy(medicine)
        }
        btn_inc_medicine_info.setOnClickListener {
            medicine.count.value = medicine.count.value?.plus(1)
            onClickBuy(medicine)
        }

    }

    private fun onClickBuy(item: Medicine) {
        if (item.count.value!! > 0)
            medicineModel.addCartItem(item = item.toMedicineCart())
        else
            medicineModel.deleteCartItem(item = item.toMedicineCart())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        configureMenu(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun configureMenu(menu: Menu) {
        menu.findItem(R.id.action_search).isVisible = false
        menu.findItem(R.id.action_filter).isVisible = false
    }
}