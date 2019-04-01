package dev.wilsonjr.newsapp.feature.sources

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.VERTICAL
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import dev.wilsonjr.faire.base.delegates.LoadingDelegate
import dev.wilsonjr.faire.base.delegates.NewsLoadingDelegate
import dev.wilsonjr.newsapp.R
import dev.wilsonjr.newsapp.api.model.Source
import dev.wilsonjr.newsapp.api.model.enums.Category
import dev.wilsonjr.newsapp.api.model.enums.Country
import dev.wilsonjr.newsapp.base.NetworkState
import dev.wilsonjr.newsapp.feature.news.NEWS_ACTIVITY_SOURCE
import dev.wilsonjr.newsapp.feature.news.NewsActivity
import dev.wilsonjr.newsapp.feature.sources.adapter.SourcesListAdapter
import kotlinx.android.synthetic.main.activity_sources.*
import org.koin.android.ext.android.inject


class SourcesActivity : AppCompatActivity(), SourcesListAdapter.SourceListAdapterItemListener,
    LoadingDelegate by NewsLoadingDelegate() {

    val sourcesViewModel: SourcesViewModel by inject()

    private var viewAdapter: SourcesListAdapter = SourcesListAdapter(this)
    private var viewManager: RecyclerView.LayoutManager = GridLayoutManager(this, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sources)
        setupView()
        setupList()
        loadSources()
        configureOrientation(resources.configuration.orientation)
    }

    private fun setupList() {
        sources_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun loadSources() {
        sourcesViewModel.sources.observe(this, Observer {
            viewAdapter.apply {
                clear()
                notifyDataSetChanged()
                add(it)
                notifyDataSetChanged()
                sources_list.scrollToPosition(0)
                app_bar.setExpanded(true)
            }
        })

        sourcesViewModel.networkState.observe(this, Observer {
            if (NetworkState.RUNNING == it) {
                showLoading(this)
            } else {
                hideLoading()
            }
        })

        sourcesViewModel.loadSources()
    }

    private fun setupView() {
        configureAutocompletes()

    }

    private fun configureAutocompletes() {
        country_select.setAdapter(CustomArrayAdapter(this, R.layout.select_item, Country.values().toMutableList()))
        country_select.keyListener = null
        country_select.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                (v as AutoCompleteTextView).showDropDown()
                return false
            }
        })
        country_select.setOnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position)
            if (item is Country) {
                sourcesViewModel.changeCountry(item)
            }
        }


        category_select.setAdapter(CustomArrayAdapter(this, R.layout.select_item, Category.values().toMutableList()))
        category_select.keyListener = null
        category_select.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                (v as AutoCompleteTextView).showDropDown()
                return false
            }
        })
        category_select.setOnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position)
            if (item is Category) {
                sourcesViewModel.changeCategory(item)
            }
        }
    }

    override fun onClick(source: Source) {
        val intent = Intent(this, NewsActivity::class.java)
        intent.putExtra(NEWS_ACTIVITY_SOURCE, source)
        startActivity(intent)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        newConfig?.orientation?.let {
            configureOrientation(it)
        }
    }

    private fun configureOrientation(orientation: Int) {
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> setupLandscape()
            else -> setupPortrait()
        }
    }

    private fun setupPortrait() {
        setListColumns(1)
        sources_filters.orientation = VERTICAL
        configureFilterLayoutParams(country_select_layout, MATCH_PARENT, 0f)
        configureFilterLayoutParams(category_select_layout, MATCH_PARENT, 0f)
    }

    private fun setupLandscape() {
        setListColumns(2)
        sources_filters.orientation = HORIZONTAL
        configureFilterLayoutParams(country_select_layout, 0, 1f)
        configureFilterLayoutParams(category_select_layout, 0, 1f)
    }

    private fun configureFilterLayoutParams(textInput: TextInputLayout, width: Int, weight: Float) {
        val layoutParams = textInput.layoutParams
        if (layoutParams is LinearLayout.LayoutParams) {
            layoutParams.width = width
            layoutParams.weight = weight
        }
    }

    private fun setListColumns(columns: Int) {
        val layoutManager = sources_list.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanCount = columns
            viewAdapter.notifyDataSetChanged()
        }
    }
}
