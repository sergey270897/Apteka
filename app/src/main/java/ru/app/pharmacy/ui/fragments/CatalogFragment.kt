package ru.app.pharmacy.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_catalog.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.app.pharmacy.R
import ru.app.pharmacy.models.Category
import ru.app.pharmacy.network.NetworkState
import ru.app.pharmacy.ui.activities.MainActivity
import ru.app.pharmacy.ui.adapters.CatalogAdapter
import ru.app.pharmacy.viewmodels.CatalogModel

class CatalogFragment : Fragment(), CatalogAdapter.OnClickListener {

    private val catalogModel: CatalogModel by viewModel()

    companion object {
        enum class TypeCatalog {
            GROUP,
            CATEGORY
        }

        fun getInstance(type: TypeCatalog, title: String, id: Int): CatalogFragment {
            val catalogFragment = CatalogFragment()
            val bundle = Bundle()
            bundle.putString("type", type.name)
            bundle.putString("title", title)
            bundle.putInt("id", id)
            catalogFragment.arguments = bundle
            return catalogFragment
        }
    }

    private var catalogAdapter = CatalogAdapter(this)
    private lateinit var type: TypeCatalog
    private var categoryId: Int = 0
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = TypeCatalog.valueOf(it.getString("type", TypeCatalog.GROUP.name))
            categoryId = it.getInt("id")
            title = it.getString("title") ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_catalog, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.action_filter).isVisible = false
    }

    override fun onClickItem(category: Category) {
        when (type) {
            TypeCatalog.GROUP -> {
                (activity as MainActivity).openCatalogCategory(category)
            }
            TypeCatalog.CATEGORY -> {
                (activity as MainActivity).openMedicineList(category)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).supportActionBar?.title = title
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(type == TypeCatalog.CATEGORY)

        initViews()
        initObservers()
    }

    private fun initViews() {
        (activity as MainActivity).toolbar.setNavigationOnClickListener {
            (activity as MainActivity).supportFragmentManager.popBackStack()
        }

        with(rv_list_catalog) {
            adapter = catalogAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        btn_refresh_catalog.setOnClickListener {
            catalogModel.loadCategories()
        }
    }

    private fun initObservers() {
        catalogModel.categories.observe(
            viewLifecycleOwner,
            Observer {
                catalogAdapter.updateData(it)
            })

        catalogModel.networkState.observe(viewLifecycleOwner, Observer {
            updateUILoading(it)
            if (it != NetworkState.RUNNING) {
                updateUIData(it)
            }
        })
    }

    private fun updateUILoading(networkState: NetworkState?) {
        tv_state_catalog.visibility = View.GONE
        btn_refresh_catalog.visibility = View.GONE
        rv_list_catalog.visibility = View.GONE

        progress_catalog.visibility =
            if (networkState == NetworkState.RUNNING) View.VISIBLE else View.GONE
    }

    private fun updateUIData(networkState: NetworkState?) {
        tv_state_catalog.visibility = View.GONE
        btn_refresh_catalog.visibility = View.GONE
        rv_list_catalog.visibility = View.GONE

        when (networkState) {
            NetworkState.SUCCESS -> {
                when (type) {
                    TypeCatalog.GROUP -> catalogModel.getGroups()
                    TypeCatalog.CATEGORY -> catalogModel.getCategories(categoryId)
                }

                if (catalogAdapter.itemCount == 0) {
                    tv_state_catalog.text = getString(R.string.empty)
                    tv_state_catalog.visibility = View.VISIBLE
                    btn_refresh_catalog.visibility = View.VISIBLE
                } else {
                    rv_list_catalog.visibility = View.VISIBLE
                }
            }
            NetworkState.FAILED -> {
                tv_state_catalog.text = getString(R.string.error)
                tv_state_catalog.visibility = View.VISIBLE
                btn_refresh_catalog.visibility = View.VISIBLE
            }
        }
    }
}