package ru.app.apteka.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.app.apteka.R
import ru.app.apteka.models.Medicine
import ru.app.apteka.ui.fragments.CartFragment
import ru.app.apteka.ui.fragments.CatalogFragment
import ru.app.apteka.ui.fragments.InfoFragment
import ru.app.apteka.ui.fragments.MedicineListFragment
import ru.app.apteka.ui.fragments.OrderFragment
import ru.app.apteka.ui.fragments.ProfileFragment
import ru.app.apteka.utils.dpToPx
import ru.app.apteka.utils.extensions.startFragment
import ru.app.apteka.viewmodels.CartModel

class MainActivity : AppCompatActivity() {

    private val cartModel: CartModel by viewModel()

    private val CODE_REQUEST_AUTH = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initToolbar()
        initBottomMenu()
        initObservers()
        initBackStackListener()
    }

    private fun initBackStackListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            setSelectedMenuItem()
        }
    }

    private fun initObservers() {
        cartModel.count.observe(this, Observer {
            showBadgeCount(it)
        })

        cartModel.startActivityAuth.observe(this, Observer {
            if(it)startAuthActivity()
        })
    }

    private fun initBottomMenu() {
        bottom_menu.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.catalog -> openCatalog()
                R.id.cart -> openCart()
                R.id.profile -> openProfile()
                R.id.order->openOrder()
            }
            true
        }
        bottom_menu.selectedItemId = R.id.catalog
    }

    private fun showBadgeCount(count: Int) {
        val badge = bottom_menu.getOrCreateBadge(R.id.cart)
        badge.number = count
        badge.backgroundColor = getColor(R.color.colorRed)
        badge.isVisible = badge.number != 0
    }

    private fun openOrder(){
        startFragment(
            R.id.container,
            OrderFragment(),
            OrderFragment::class.simpleName!!
        )
    }

    private fun openProfile() {
        if (cartModel.checkLogin()) {
            startFragment(
                R.id.container,
                ProfileFragment(),
                ProfileFragment::class.simpleName!!
            )
        }
    }

    private fun openCart() {
        startFragment(
            R.id.container,
            CartFragment(),
            CartFragment::class.simpleName!!
        )
    }

    private fun openCatalog() {
        startFragment(
            R.id.container,
            CatalogFragment.getInstance(
                CatalogFragment.TypeCatalog.GROUP,
                getString(R.string.app_name),
                0
            ),
            CatalogFragment::class.simpleName!!,
            supportFragmentManager.backStackEntryCount != 0
        )
    }

    private fun openSearchView() {
        startFragment(
            R.id.container,
            MedicineListFragment.getInstance(0, ""),
            MedicineListFragment::class.simpleName!!
        )
    }

    fun openMedicineInfo(item:Medicine) {
        startFragment(
            R.id.container,
            InfoFragment.getInstance(item),
            InfoFragment::class.simpleName!!
        )
    }

    private fun initMenu(menu: Menu?) {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.queryHint = getString(R.string.search_hint)

        val searchPlate = searchView?.findViewById<View>(androidx.appcompat.R.id.search_plate)
        searchPlate?.setBackgroundResource(R.drawable.bg_round_4dp)

        val searchEditText =
            searchView?.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText?.setTextColor(resources.getColor(R.color.colorPrimaryDark))
        searchEditText?.setHintTextColor(resources.getColor(R.color.colorGray))
        searchEditText?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

        val searchCloseButton =
            searchView?.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        searchCloseButton?.setImageResource(R.drawable.ic_close_black_24dp)
        searchCloseButton?.setColorFilter(
            resources.getColor(R.color.colorPrimaryDark),
            PorterDuff.Mode.SRC_IN
        )

        val padding = dpToPx(this, 8)
        val search = searchView?.findViewById<View>(androidx.appcompat.R.id.search_edit_frame)
        search?.setPadding(0, padding, 0, padding)

        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                openSearchView()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                supportFragmentManager.popBackStack()
                supportFragmentManager.popBackStack()
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        initMenu(menu)
        return true
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //setSelectedMenuItem()
    }

    private fun setSelectedMenuItem(){
        val index = supportFragmentManager.backStackEntryCount - 1
        val tag: String =
            if (index < 0) CatalogFragment::class.simpleName.toString() else supportFragmentManager.getBackStackEntryAt(
                index
            ).name.toString()
        when {
            tag.contains(CatalogFragment::class.simpleName.toString()) ->
                bottom_menu.menu.findItem(R.id.catalog).isChecked = true
            tag.contains(MedicineListFragment::class.simpleName.toString()) ->
                bottom_menu.menu.findItem(R.id.catalog).isChecked = true
            tag.contains(CartFragment::class.simpleName.toString()) ->
                bottom_menu.menu.findItem(R.id.cart).isChecked = true
            tag.contains(ProfileFragment::class.simpleName.toString()) ->
                bottom_menu.menu.findItem(R.id.profile).isChecked = true
            tag.contains(OrderFragment::class.simpleName.toString()) ->
                bottom_menu.menu.findItem(R.id.order).isChecked = true
        }
    }

    private fun startAuthActivity() {
        cartModel.startActivityAuth.value = false
        val intent = Intent(this, AuthActivity::class.java)
        startActivityForResult(intent, CODE_REQUEST_AUTH)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_REQUEST_AUTH && resultCode == Activity.RESULT_OK) {
            Log.d("M__MainActivity", "auth_ok")
        } else {
            Snackbar
                .make(bottom_menu, "Для продолжения необходимо авторизоваться", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.colorPrimary))
                .show()
        }
    }
}