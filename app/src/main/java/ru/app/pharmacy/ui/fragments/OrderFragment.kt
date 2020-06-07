package ru.app.pharmacy.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.app.pharmacy.R
import ru.app.pharmacy.network.NetworkState
import ru.app.pharmacy.ui.activities.MainActivity
import ru.app.pharmacy.ui.adapters.OrderAdapter
import ru.app.pharmacy.viewmodels.OrderModel

class OrderFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val orderModel: OrderModel by viewModel()
    private val orderAdapter = OrderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.action_filter).isVisible = false
        menu.findItem(R.id.action_search).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).supportActionBar?.title = getString(R.string.orders)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        initView()
        initObservers()
    }

    private fun initObservers() {
        orderModel.orders.observe(viewLifecycleOwner, Observer {
            orderAdapter.updateData(it)
        })

        orderModel.networkState.observe(viewLifecycleOwner, Observer {
            uploadUILoading(it)
            if (it != NetworkState.RUNNING) {
                updateUIData(it)
            }
        })
    }

    private fun initView() {
        with(rv_list_order) {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(context)
        }

        btn_refresh_order.setOnClickListener {
            orderModel.loadOrders()
        }

        swipe.setOnRefreshListener(this)
    }

    private fun uploadUILoading(networkState: NetworkState?) {
        tv_state_order.visibility = View.GONE
        btn_refresh_order.visibility = View.GONE
        rv_list_order.visibility = View.GONE

        progress_order.visibility =
            if (networkState == NetworkState.RUNNING) View.VISIBLE else View.GONE
    }

    private fun updateUIData(networkState: NetworkState?) {
        tv_state_order.visibility = View.GONE
        btn_refresh_order.visibility = View.GONE
        rv_list_order.visibility = View.GONE

        when (networkState) {
            NetworkState.SUCCESS -> {
                if (orderAdapter.itemCount == 0) {
                    tv_state_order.text = getString(R.string.empty)
                    tv_state_order.visibility = View.VISIBLE
                    btn_refresh_order.visibility = View.VISIBLE
                } else {
                    rv_list_order.visibility = View.VISIBLE
                }
            }
            else -> {
                tv_state_order.text = getString(R.string.error)
                tv_state_order.visibility = View.VISIBLE
                btn_refresh_order.visibility = View.VISIBLE
            }
        }
    }

    override fun onRefresh() {
        Handler().postDelayed(
            {
                orderModel.refresh()
                swipe.isRefreshing = false
            }, 1500
        )
    }
}