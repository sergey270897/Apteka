package ru.app.apteka.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.app.apteka.R
import ru.app.apteka.ui.fragments.CatalogFragment

class MainActivity : AppCompatActivity() {

    interface OnBackPressed {
        fun onBackPressed(): Boolean
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fragment = supportFragmentManager.findFragmentByTag(CatalogFragment::class.simpleName)

        if(fragment == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.container, CatalogFragment(), CatalogFragment::class.simpleName)
                .commit()
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(CatalogFragment::class.simpleName)
        if (fragment == null || !fragment.isVisible || (fragment as OnBackPressed).onBackPressed())
            super.onBackPressed()
    }
}