package com.bignerdranch.android.task04.ui.habitlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bignerdranch.android.task04.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.bottom_sheet_fragment.*
import kotlinx.android.synthetic.main.fragment_habit_view_pager.*


class HabitViewPagerFragment : Fragment() {
    private lateinit var sheetBehavior: BottomSheetBehavior<View>

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        ordersPagerViewPager.adapter =
            com.bignerdranch.android.task04.ui.habitlist.HabitListPagerAdapter(
                childFragmentManager
            )
        ordersPagerTabLayout.setupWithViewPager(ordersPagerViewPager)

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet)

        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
            override fun onStateChanged(view: View, newState: Int) {
            }
        })
        initFloatActionButton()
    }

    private fun initFloatActionButton() {
        floatingActionButton.setOnClickListener{
            navController.navigate(R.id.action_habitViewPagerFragment_to_habitFragment)
        }
    }
}
