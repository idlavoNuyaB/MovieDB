package com.freisia.vueee.favorite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.freisia.vueee.core.utils.EspressoIdlingResource
import com.freisia.vueee.favorite.adapter.SectionPagerFavoriteAdapter
import com.freisia.vueee.favorite.databinding.ActivityFavoriteBinding
import com.freisia.vueee.favorite.di.favoriteModule
import com.freisia.vueee.presentation.list.ListActivity
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import com.freisia.vueee.favorite.R as T

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val loadModules by lazy{loadKoinModules(favoriteModule)}
    private fun injectFeatures() = loadModules

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initTabLayout()
        unloadKoinModules(favoriteModule)
        injectFeatures()
    }

    private fun initToolbar() {
        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setDisplayShowCustomEnabled(true)
            setCustomView(T.layout.toolbar)
            elevation = 0f
        }
        val actionBar = supportActionBar
        val toolbar = actionBar?.customView?.parent as Toolbar
        toolbar.setContentInsetsAbsolute(0, 0)
        toolbar.contentInsetEnd
        toolbar.setPadding(0, 0, 0, 0)
        val view = toolbar.getChildAt(0)
        view.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun initTabLayout(){
        val sectionPagerAdapter = SectionPagerFavoriteAdapter(
            this,
            supportFragmentManager
        )
        binding.viewPagers.adapter = sectionPagerAdapter
        binding.viewPagers.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    EspressoIdlingResource.increment()
                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow) {
                        EspressoIdlingResource.decrement()
                    }
                }
                super.onPageScrollStateChanged(state)
            }
        })
        binding.tabs.setupWithViewPager(binding.viewPagers)
    }
}