package ru.app.apteka.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_cart.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.app.apteka.R
import ru.app.apteka.databinding.FragmentCartBinding
import ru.app.apteka.models.MedicineCart
import ru.app.apteka.network.NetworkState
import ru.app.apteka.ui.activities.MainActivity
import ru.app.apteka.ui.adapters.CartAdapter
import ru.app.apteka.viewmodels.CartModel

class CartFragment : Fragment(), CartAdapter.OnClickListener {

    private val cartModel: CartModel by sharedViewModel()
    private var cartAdapter = CartAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCartBinding>(
            inflater,
            R.layout.fragment_cart,
            container,
            false
        )
        binding.cart = cartModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.cart)

        initViews()
        initObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.action_search).isVisible = false
        menu.findItem(R.id.action_filter).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initViews() {
        with(rv_list_cart) {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initObservers() {
        cartModel.getCartItems().observe(viewLifecycleOwner, Observer {
            cartModel.setTotal(it)

            progress_cart.visibility = View.GONE
            if (it.isEmpty()) {
                tv_state_cart.visibility = View.VISIBLE
                block_cart.visibility = View.GONE
            } else {
                tv_state_cart.visibility = View.GONE
                block_cart.visibility = View.VISIBLE
            }
            cartAdapter.updateData(it)
        })

        cartModel.networkState.observe(viewLifecycleOwner, Observer {
            if (it == NetworkState.RUNNING) {
                btn_order.visibility = View.GONE
                progress_cart_order.visibility = View.VISIBLE
            } else {
                btn_order.visibility = View.VISIBLE
                progress_cart_order.visibility = View.GONE
            }

            if (it == NetworkState.SUCCESS) {
                cartModel.deleteAll()
            }
        })

        cartModel.msg.observe(viewLifecycleOwner, Observer {
            if (it.second.isNotEmpty()) {
                var msg = it.second
                val color = when (it.first) {
                    1 -> {
                        resources.getColor(R.color.colorGreen)
                    }
                    2 -> {
                        msg = getString(R.string.warningSnackBarCity)
                        resources.getColor(R.color.colorPrimary)
                    }
                    3->{
                        msg = getString(R.string.errorSnackbar)
                        resources.getColor(R.color.colorRed)

                    }
                    else -> resources.getColor(R.color.colorPrimary)
                }
                showSnackBar(msg, color)
            }
        })
    }

    private fun showSnackBar(msg: String, color: Int) {
        Snackbar.make(block_cart, msg, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(color)
            .show()
        cartModel.clearMsgSnackBar()
    }

    override fun onClickCount(item: MedicineCart) {
        if (item.count.value == 0) cartModel.deleteCartItem(item)
        else cartModel.updateCartItem(item)
    }
}