package com.bignerdranch.android.task04.ui.habitlist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bignerdranch.android.task04.data.entity.HabitType

class HabitListPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment
            = HabitListFragment.newInstance(HabitType.values()[position])

    override fun getCount(): Int
            = HabitType.values().size

    override fun getPageTitle(position: Int): CharSequence?
            = "${HabitType.values()[position].name} habits"
}