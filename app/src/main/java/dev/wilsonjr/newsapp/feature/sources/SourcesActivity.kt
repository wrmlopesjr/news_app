package dev.wilsonjr.newsapp.feature.sources

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View.*
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import dev.wilsonjr.newsapp.R
import kotlinx.android.synthetic.main.activity_sources.*
import android.widget.AutoCompleteTextView
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import dev.wilsonjr.newsapp.api.Category
import dev.wilsonjr.newsapp.api.Country
import java.util.*


class SourcesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sources)
        setupView()
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        app_bar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(layout: AppBarLayout?, verticalOffset: Int) {
//                val diff = Math.abs(verticalOffset / app_bar.totalScrollRange)
//                if(diff==0) configure.visibility = INVISIBLE
//                else configure.visibility = VISIBLE

//                configure.alpha = Math.abs(verticalOffset / app_bar.totalScrollRange.toFloat())
            }

        })

        configureSpinners()

    }

    private fun configureSpinners() {
        country_select.setAdapter(CustomArrayAdapter(this, R.layout.select_item, Country.values().toMutableList()))
        country_select.keyListener = null
        country_select.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                (v as AutoCompleteTextView).showDropDown()
                return false
            }
        })


        category_select.setAdapter(CustomArrayAdapter(this, R.layout.select_item, Category.values().toMutableList()))
        category_select.keyListener = null
        category_select.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                (v as AutoCompleteTextView).showDropDown()
                return false
            }
        })
    }
}
