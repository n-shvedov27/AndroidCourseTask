package com.bignerdranch.android.task04.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.task04.R
import com.bignerdranch.android.task04.ui.CustomTextWatcher
import com.bignerdranch.android.task04.viewmodels.habitlist.HabitListViewModel
import com.bignerdranch.android.task04.viewmodels.habitlist.SortType
import kotlinx.android.synthetic.main.bottom_sheet_fragment.*

@Suppress("UNCHECKED_CAST")
class BottomFragment: Fragment() {
    private lateinit var habitListViewModel: HabitListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        habitListViewModel = ViewModelProvider(activity!!, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HabitListViewModel() as T
            }
        }).get(HabitListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterEditText.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habitListViewModel.filterPrefix = s.toString()
            }
        })

        sortArrowDown.setOnClickListener { habitListViewModel.sortType = SortType.Descending }
        sortArrowUp.setOnClickListener { habitListViewModel.sortType = SortType.Ascending }
        simpleSort.setOnClickListener { habitListViewModel.sortType = SortType.None }
    }
}