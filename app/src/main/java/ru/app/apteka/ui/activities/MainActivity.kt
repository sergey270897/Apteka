package ru.app.apteka.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.app.apteka.R
import ru.app.apteka.ui.fragments.CatalogFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, CatalogFragment(), CatalogFragment::class.simpleName)
            .commit()
    }
}