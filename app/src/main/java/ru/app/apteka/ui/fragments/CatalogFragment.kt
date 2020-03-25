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
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_catalog.*
import ru.app.apteka.R
import ru.app.apteka.databinding.FragmentCatalogBinding
import ru.app.apteka.ui.activities.MainActivity
import ru.app.apteka.ui.adapters.CatalogAdapter
import ru.app.apteka.viewmodels.CatalogModel

class CatalogFragment : Fragment(), MainActivity.OnBackPressed {

    private lateinit var catalogModel: CatalogModel
    private lateinit var catalogAdapter: CatalogAdapter
    private lateinit var binding: FragmentCatalogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_catalog, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModels()
        initViews()
    }

    private fun initViews() {
        catalogAdapter = CatalogAdapter(catalogModel)
        with(rv_list_catalog) {
            adapter = catalogAdapter
            layoutManager = GridLayoutManager(context, 2)
            //(itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        (activity as MainActivity).toolbar.setNavigationOnClickListener {
            catalogModel.onClickBack()
        }
    }

    private fun initViewModels() {
        catalogModel = ViewModelProviders.of(activity!!).get(CatalogModel::class.java)

        catalogModel.categories.observe(viewLifecycleOwner, Observer {
            catalogAdapter.updateData(it)
        })

        binding.vm = catalogModel

        catalogModel.title.observe(
            viewLifecycleOwner,
            Observer { (activity as MainActivity).supportActionBar?.title = it })

        catalogModel.showBack.observe(viewLifecycleOwner, Observer {
            (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(it)
            (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(it)
        })
    }

    override fun onBackPressed():Boolean {
        return catalogModel.onClickBack()
    }
}