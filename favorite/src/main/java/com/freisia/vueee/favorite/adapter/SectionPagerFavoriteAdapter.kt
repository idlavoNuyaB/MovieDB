package com.freisia.vueee.favorite.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.freisia.vueee.R
import com.freisia.vueee.favorite.movies.MoviesFavoriteFragment
import com.freisia.vueee.favorite.tvshow.TVShowsFavoriteFragment

class SectionPagerFavoriteAdapter(private val mContext: Context, fm : FragmentManager
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    @StringRes
    private val tabTitles = intArrayOf(R.string.tab1, R.string.tab2)
    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
        if(position ==0){
            fragmentList.add(position, MoviesFavoriteFragment())
        }
        else{
            fragmentList.add(position, TVShowsFavoriteFragment())
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