package com.bignerdranch.android.task04.ui.habitlist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.task04.ui.habit.HabitFragment
import com.bignerdranch.android.task04.R
import com.bignerdranch.android.task04.data.entity.Habit
import com.bignerdranch.android.task04.data.entity.HabitType
import com.bignerdranch.android.task04.viewmodels.habitlist.HabitListViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_habit_list.*
import kotlinx.android.synthetic.main.habit_list_item.*


class HabitListFragment : Fragment() {
    private var habitAdapter: HabitAdapter? = null
    private lateinit var habitType: HabitType

    private lateinit var navController: NavController

    private lateinit var habitListViewModel: HabitListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController()

        habitType = arguments!!.getSerializable(HABIT_TYPE) as HabitType

        habitListViewModel = ViewModelProvider(activity!!, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HabitListViewModel(activity!!.application) as T
            }
        }).get(HabitListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_habit_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habitListViewModel.habitMediatorLiveData[habitType]?.observe(viewLifecycleOwner, Observer {
                updateUI(it)
        })
    }

    private fun updateUI(habits: List<Habit>) {
        habitAdapter?.let {
            it.habits = habits
            habitRecyclerView.adapter = habitAdapter
            it.notifyDataSetChanged()
        } ?: run {
            habitAdapter =  HabitAdapter(habits)
            habitRecyclerView.adapter = habitAdapter
        }
    }

    private inner class HabitHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        View.OnClickListener,
        LayoutContainer
    {
        private var habit: Habit? = null

        init {
            containerView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val bundle = bundleOf(HabitFragment.EXTRA_HABIT_ID_KEY to habit?.uid )
            findNavController().navigate(
                R.id.action_habitViewPagerFragment_to_habitFragment,
                bundle
            )
        }

        fun bindCrime(habit: Habit) {
            this.habit = habit
            habitListItemName.text = habit.title
            habitListItemDescription.text = habit.description
            habitListItemPriority.text = habit.priority.name
            habitListItemType.text = habit.type.name
            habitListItemQuantity.text = getString(R.string.quantity, habit.count)
            habitListItemPeriodicity.text = getString(R.string.periodicity, habit.frequency)
            habitRelativeLayout.setBackgroundColor(ContextCompat.getColor(activity!!, habit.color.colorId))
        }
    }

    private inner class HabitAdapter(var habits: List<Habit>) :
        RecyclerView.Adapter<HabitHolder>()
    {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitHolder {
            val layoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater.inflate(R.layout.habit_list_item, parent, false)
            return HabitHolder(view)
        }

        override fun onBindViewHolder(holder: HabitHolder, position: Int) {
            val crime = habits[position]
            holder.bindCrime(crime)
        }

        override fun getItemCount(): Int {
            return habits.size
        }
    }

    companion object {
        private const val HABIT_TYPE = "habit_type"
        fun newInstance(habitType: HabitType) : HabitListFragment {
            val fragment =
                HabitListFragment()
            val bundle = Bundle()

            bundle.putSerializable(HABIT_TYPE, habitType)
            fragment.arguments = bundle

            return fragment
        }
    }
}
