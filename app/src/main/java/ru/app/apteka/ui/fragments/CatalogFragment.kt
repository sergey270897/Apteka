package ru.app.apteka.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_catalog.*
import ru.app.apteka.R
import ru.app.apteka.databinding.FragmentCatalogBinding
import ru.app.apteka.ui.activities.MainActivity
import ru.app.apteka.ui.adapters.CatalogAdapter
import ru.app.apteka.viewmodels.CatalogModel

class CatalogFragment : Fragment() {

    companion object {
        fun getInstance(type: String, title: String, id: Int): CatalogFragment {
            val catalogFragment = CatalogFragment()
            val bundle = Bundle()
            bundle.putString("type", type)
            bundle.putString("title", title)
            bundle.putInt("id", id)
            catalogFragment.arguments = bundle
            return catalogFragment
        }
    }

    private lateinit var type: String
    private var categoryId: Int = 0

    private lateinit var catalogModel: CatalogModel
    private lateinit var catalogAdapter: CatalogAdapter
    private lateinit var binding: FragmentCatalogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.let {
            type = it.getString("type", "group")
            categoryId = it.getInt("id")
            (activity as MainActivity).supportActionBar?.title = it.getString("title")
        }


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_catalog, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModels()
        initViews()
    }

    private fun initViews() {
        catalogAdapter = CatalogAdapter {
            when (type) {
                "group" -> {
                    (activity as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.container,
                            getInstance("category", it.title, it.id),
                            CatalogFragment::class.simpleName + it.title + it.id
                        )
                        .addToBackStack(CatalogFragment::class.simpleName + it.title + it.id)
                        .commit()
                }
                "category" -> {
                    (activity as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.container,
                            MedicineListFragment.getInstance(it.id, it.title),
                            MedicineListFragment::class.simpleName + it.title + it.id
                        )
                        .addToBackStack(MedicineListFragment::class.simpleName + it.title + it.id)
                        .commit()
                }
            }
        }

        if (type == "category") {
            (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

            (activity as MainActivity).toolbar.setNavigationOnClickListener {
                (activity as MainActivity).supportFragmentManager.popBackStack()
            }
        }

        with(rv_list_catalog) {
            adapter = catalogAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun initViewModels() {
        catalogModel = ViewModelProviders.of(activity as MainActivity).get(CatalogModel::class.java)

        catalogModel.categories.observe(
            viewLifecycleOwner,
            Observer { catalogAdapter.updateData(it) })

        binding.vm = catalogModel

        catalogModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (!it) when (type) {
                "group" -> catalogModel.getGroups()
                "category" -> catalogModel.getCategories(categoryId)
            }
        })
    }
}