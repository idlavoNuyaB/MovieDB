package com.freisia.vueee.presentation.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.freisia.vueee.R
import com.freisia.vueee.presentation.list.movies.MoviesFragment
import com.freisia.vueee.presentation.list.tvshow.TVShowsFragment

class SectionPagerAdapter(private val mContext: Context,fm : FragmentManager
                            ) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    @StringRes
    private val tabTitles = intArrayOf(R.string.tab1,R.string.tab2)
    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
        if(position ==0){
            fragmentList.add(position,MoviesFragment())
        }
        else{
            fragmentList.add(position,TVShowsFragment())
        }
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return tabTitles.size
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence {
        return mContext.resources.getString(tabTitles[position])
    }
}