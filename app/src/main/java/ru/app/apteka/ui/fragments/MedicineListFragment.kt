package ru.app.apteka.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar
import kotlinx.android.synthetic.main.fragment_medicine.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.app.apteka.R
import ru.app.apteka.databinding.DialogFilterBinding
import ru.app.apteka.network.NetworkState
import ru.app.apteka.ui.activities.MainActivity
import ru.app.apteka.ui.adapters.MedicineAdapter
import ru.app.apteka.ui.custom.CustomGridLayoutManager
import ru.app.apteka.utils.extensions.onTextChanged
import ru.app.apteka.viewmodels.MedicineModel

class MedicineListFragment : Fragment(), MedicineAdapter.OnClickListener {

    private val medicineModel: MedicineModel by viewModel { parametersOf(categoryId) }

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
    private var title: String = ""
    private val medicineAdapter = MedicineAdapter(this)
    private lateinit var dialogFilter: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt("id")
            title = it.getString("title") ?: ""
        }
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

        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = title

        initViews()
        initObservers()
        createDialogFilter()
    }

    private fun createDialogFilter() {
        val builder = AlertDialog.Builder(activity as MainActivity, R.style.Dialog)
        val bind = DataBindingUtil.inflate<DialogFilterBinding>(LayoutInflater.from(this.context), R.layout.dialog_filter,null, false)
        bind.lifecycleOwner = this
        val range = bind.root.findViewById<CrystalRangeSeekbar>(R.id.range_price_dialog)
        range.setMaxStartValue(medicineModel.filter.value?.priceTo?.toFloat()!!)
        range.setMinStartValue(medicineModel.filter.value?.priceFrom?.toFloat()!!)
        range.apply()
        range.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            val filter = medicineModel.filter.value?.copy(priceFrom = minValue.toInt(), priceTo = maxValue.toInt())
            medicineModel.filter.value = filter
        }

        bind.model = medicineModel
        builder.setView(bind.root)
        dialogFilter = builder.create()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        configureMenu(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showFilter() {
        dialogFilter.show()

        // configure window position (bottom full width)
        val window = dialogFilter.window
        val params = window?.attributes
        params?.gravity = Gravity.BOTTOM
        window?.attributes = params
        window?.setLayout(
            GridLayoutManager.LayoutParams.MATCH_PARENT,
            GridLayoutManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                showFilter()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        val layoutManager = CustomGridLayoutManager(context, 2)

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

    private fun initObservers() {
        medicineModel.medicines.observe(viewLifecycleOwner, Observer {
            medicineAdapter.submitList(it)
        })

        medicineModel.networkState?.observe(viewLifecycleOwner, Observer {
            medicineAdapter.updateNetworkState(it)
        })

        medicineModel.closeFilterDialog.observe(viewLifecycleOwner, Observer {
            dialogFilter.dismiss()
        })
    }

    private fun configureMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        searchItem.isVisible = categoryId == 0
        if (searchItem.isVisible) searchItem.expandActionView()

        val query = medicineModel.getCurrentQuery()
        val searchView = searchItem.actionView as SearchView

        if (query.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(query, false)
            searchView.clearFocus()
        }

        searchView.onTextChanged {
            medicineModel.getMedicinesByName(it!!)
        }

        menu.findItem(R.id.action_filter).isVisible = categoryId != 0
    }

    private fun updateUIData(size: Int, networkState: NetworkState?) {
        tv_state_medicine.visibility = View.GONE
        btn_refresh_medicine.visibility = View.GONE
        if (size == 0) {
            when (networkState) {
                NetworkState.SUCCESS -> {
                    tv_state_medicine.text = getString(R.string.empty)
                    tv_state_medicine.visibility = View.VISIBLE
                    btn_refresh_medicine.visibility = View.VISIBLE
                }
                NetworkState.FAILED -> {
                    tv_state_medicine.text = getString(R.string.error)
                    tv_state_medicine.visibility = View.VISIBLE
                    btn_refresh_medicine.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateUILoading(size: Int, networkState: NetworkState?) {
        tv_state_medicine.visibility = View.GONE
        btn_refresh_medicine.visibility = View.GONE
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