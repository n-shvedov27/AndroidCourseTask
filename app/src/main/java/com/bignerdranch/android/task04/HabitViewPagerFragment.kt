package com.bignerdranch.android.task04

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_habit_view_pager.*


class HabitViewPagerFragment : Fragment() {

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        initFloatActionButton(view)

        ordersPagerViewPager.adapter =
            HabitListPagerAdapter(
                childFragmentManager
            )
        ordersPagerTabLayout.setupWithViewPager(ordersPagerViewPager)
    }

    private fun initFloatActionButton(view: View) {
        val floatingActionButton =
            view.findViewById(R.id.floating_action_button) as FloatingActionButton
        floatingActionButton.setOnClickListener{
            navController.navigate(R.id.action_habitViewPagerFragment_to_habitFragment)
        }
    }
}
