package ru.app.apteka.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.app.apteka.R
import ru.app.apteka.ui.fragments.CatalogFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fragment = supportFragmentManager.findFragmentByTag(CatalogFragment::class.simpleName)

        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.container,
                    CatalogFragment.getInstance("group", getString(R.string.app_name), 0),
                    CatalogFragment::class.simpleName
                )
                .commit()
        }

        toolbar.setNavigationOnClickListener {
            supportFragmentManager.popBackStack()
        }
    }
}