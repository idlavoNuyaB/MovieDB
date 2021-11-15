package com.freisia.vueee.presentation.list


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.freisia.vueee.R
import com.freisia.vueee.core.utils.EspressoIdlingResource
import com.freisia.vueee.databinding.ActivityListBinding
import com.freisia.vueee.presentation.adapter.SectionPagerAdapter
import java.util.*

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initTabLayout()
    }

    private fun initToolbar(){
        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setDisplayShowCustomEnabled(true)
            setCustomView(R.layout.toolbar)
            elevation = 0f
        }
        val actionBar = supportActionBar
        val toolbar = actionBar?.customView?.parent as Toolbar
        toolbar.setContentInsetsAbsolute(0, 0)
        toolbar.contentInsetEnd
        toolbar.setPadding(0, 0, 0, 0)
        val view = toolbar.getChildAt(0)
        view.setOnClickListener {
            val intent = Intent(this@ListActivity,ListActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun initTabLayout(){
        val sectionPagerAdapter = SectionPagerAdapter(
            this,
            supportFragmentManager
        )
        binding.viewPager.adapter = sectionPagerAdapter
        binding.viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener(){

            override fun onPageScrollStateChanged(state: Int) {
                if(state == ViewPager.SCROLL_STATE_DRAGGING){
                    EspressoIdlingResource.increment()
                } else if(state == ViewPager.SCROLL_STATE_IDLE) {
                    if(!EspressoIdlingResource.getEspressoIdlingResourceForMainActivity().isIdleNow){
                        EspressoIdlingResource.decrement()
                    }
                }
                super.onPageScrollStateChanged(state)
            }

        })
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite -> {
                val uri = Uri.parse("vueee://favorite")
                val intent = Intent(Intent.ACTION_VIEW,uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
