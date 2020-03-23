package com.bignerdranch.android.task04

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class HabitListPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return HabitListFragment.newInstance(
            HabitType.values()[position]
        )
    }

    override fun getCount(): Int = HabitType.values().size

    override fun getPageTitle(position: Int): CharSequence? {
        return "${HabitType.values()[position].name} habits"
    }
}