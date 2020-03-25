package ru.app.apteka.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import ru.app.apteka.R
import ru.app.apteka.ui.activities.MainActivity

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


        // (activity as MainActivity).toolbar.setNavigationOnClickListener {
        //     (activity as MainActivity).supportFragmentManager.popBackStack()
        // }
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)
        return view
    }
}