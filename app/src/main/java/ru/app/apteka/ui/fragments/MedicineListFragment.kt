package ru.app.apteka.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import kotlinx.android.synthetic.main.fragment_medicine.*
import ru.app.apteka.R
import ru.app.apteka.network.NetworkState
import ru.app.apteka.ui.activities.MainActivity
import ru.app.apteka.ui.adapters.MedicineAdapter
import ru.app.apteka.viewmodels.MedicineModel

class MedicineListFragment : Fragment(), MedicineAdapter.OnClickListener {

    companion object {
        fun getInstance(id: Int, title: String): MedicineListFragment {
            val medicineListFragment = MedicineListFragment()
            val bundle = Bundle()
            bundle.putInt("id", id)
            bundle.putString("title", title)
            medicineListFragment.arguments = bundle
            return medicineListFragment
        }
    }

    private var categoryId = 0
    private val medicineAdapter = MedicineAdapter(this)
    private lateinit var medicineModel: MedicineModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        arguments?.let {
            categoryId = it.getInt("id")
            (activity as MainActivity).supportActionBar?.title = it.getString("title")
        }
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medicine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        configureMenu(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initViews() {
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (val viewType = medicineAdapter.getItemViewType(position)) {
                    MedicineAdapter.ViewType.LOADING.ordinal -> 2
                    MedicineAdapter.ViewType.MEDICINE.ordinal -> 1
                    else -> throw IllegalArgumentException("Unknown view type $viewType")
                }
            }
        }

        with(rv_list_medicine) {
            adapter = medicineAdapter
            this.layoutManager = layoutManager
        }

        btn_refresh_medicine.setOnClickListener { medicineModel.refreshAll() }
    }

    private fun initViewModels() {
        medicineModel =
            ViewModelProviders.of(activity as MainActivity).get(MedicineModel::class.java)

        if (categoryId != 0) medicineModel.getMedicinesByCategoriesID(categoryId)

        medicineModel.medicines.observe(viewLifecycleOwner, Observer {
            medicineAdapter.submitList(it)
        })

        medicineModel.networkState?.observe(viewLifecycleOwner, Observer {
            medicineAdapter.updateNetworkState(it)
        })
    }

    private fun configureMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        searchItem.isVisible = categoryId == 0

        val query = medicineModel.getCurrentQuery()
        val searchView = searchItem.actionView as SearchView

        if (query.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(query, false)
            searchView.clearFocus()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                medicineModel.getMedicinesByName(query)
                return true
            }
        })
    }

    private fun updateUIData(size: Int, networkState: NetworkState?) {
        tv_state_medicine.visibility = View.GONE
        btn_refresh_medicine.visibility = View.GONE

        if(size == 0){
            when(networkState){
                NetworkState.SUCCESS->{
                    tv_state_medicine.text = getString(R.string.empty)
                    tv_state_medicine.visibility = View.VISIBLE
                    btn_refresh_medicine.visibility = View.VISIBLE
                }
                NetworkState.FAILED->{
                    tv_state_medicine.text = getString(R.string.error)
                    tv_state_medicine.visibility = View.VISIBLE
                    btn_refresh_medicine.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateUILoading(size: Int, networkState: NetworkState?) {
        progress_medicine.visibility =
            if (size == 0 && networkState == NetworkState.RUNNING) View.VISIBLE else View.GONE
    }

    override fun onClickBuy() {
        TODO("Not yet implemented")
    }

    override fun onClickCount() {
        TODO("Not yet implemented")
    }

    override fun onClickRefresh() {
        medicineModel.refreshFailedRequest()
    }

    override fun listUpdated(size: Int, networkState: NetworkState?) {
        updateUILoading(size, networkState)
        updateUIData(size, networkState)
    }
}