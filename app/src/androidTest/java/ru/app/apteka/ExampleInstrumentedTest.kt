package ru.app.apteka

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import ru.app.apteka.utils.DataGenerator
import ru.app.apteka.utils.Utils

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ru.app.apteka", appContext.packageName)
    }

    @Test
    fun test(){
        val json = DataGenerator.getCategoriesApi()
        val res = Utils.json2Category(json)
        Log.d("M__ExampleInstrumentedTest",res.toString())
    }
}
