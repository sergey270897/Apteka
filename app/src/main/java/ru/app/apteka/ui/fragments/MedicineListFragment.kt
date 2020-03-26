package ru.app.apteka.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_catalog.*
import kotlinx.android.synthetic.main.fragment_medicine.*
import ru.app.apteka.R
import ru.app.apteka.ui.activities.MainActivity
import ru.app.apteka.ui.adapters.MedicineAdapter
import ru.app.apteka.viewmodels.MedicineModel

class MedicineListFragment : Fragment() {

    companion object{
        fun getInstance(id:Int, title:String): MedicineListFragment{
            val medicineListFragment = MedicineListFragment()
            val bundle = Bundle()
            bundle.putInt("id", id)
            bundle.putString("title", title)
            medicineListFragment.arguments = bundle
            return medicineListFragment
        }
    }

    private var categoryId=  0
    private val medicineAdapter = MedicineAdapter()
    private lateinit var medicineModel: MedicineModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.let {
            categoryId = it.getInt("id")
            (activity as MainActivity).supportActionBar?.title = it.getString("title")
        }

        (activity as MainActivity).ed_search_catalog.visibility = View.GONE
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)


        val view = inflater.inflate(R.layout.fragment_medicine, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModels()
        initViews()
    }

    private fun initViews() {
        with(rv_list_medicine) {
            adapter = medicineAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun initViewModels() {
        medicineModel = ViewModelProviders.of(activity as MainActivity).get(MedicineModel::class.java)
        medicineModel.medicines.observe(viewLifecycleOwner, Observer {
            medicineAdapter.submitList(it)
        })
    }
}