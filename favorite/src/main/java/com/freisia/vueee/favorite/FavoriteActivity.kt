package com.freisia.vueee.favorite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
    }

    private fun initTabLayout(){
        val sectionPagerAdapter = SectionPagerFavoriteAdapter(
            this,
            supportFragmentManager
        )
        binding.viewPagers.adapter = sectionPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPagers)
    }
}