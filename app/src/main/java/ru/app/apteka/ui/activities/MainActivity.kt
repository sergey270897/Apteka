package ru.app.apteka.ui.activities

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*
import ru.app.apteka.R
import ru.app.apteka.ui.fragments.CatalogFragment
import ru.app.apteka.ui.fragments.MedicineListFragment
import ru.app.apteka.utils.dpToPx
import ru.app.apteka.utils.extensions.startFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()

        val fragment = supportFragmentManager.findFragmentByTag(CatalogFragment::class.simpleName)
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.container,
                    CatalogFragment.getInstance(
                        CatalogFragment.TypeCatalog.GROUP,
                        getString(R.string.app_name),
                        0
                    ),
                    CatalogFragment::class.simpleName
                )
                .commit()
        }
    }

    private fun openSearchView() {
        startFragment(
            R.id.container,
            MedicineListFragment.getInstance(0, ""),
            MedicineListFragment::class.simpleName!!
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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
                return true
            }
        })
        return true
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            supportFragmentManager.popBackStack()
        }
    }
}