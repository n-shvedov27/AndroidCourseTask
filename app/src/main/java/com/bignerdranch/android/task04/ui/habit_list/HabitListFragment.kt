package com.bignerdranch.android.task04.ui.habit_list

import com.bignerdranch.android.task04.data.entity.Habit
import com.bignerdranch.android.task04.data.entity.HabitType
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.task04.ui.habit_fragment.HabitFragment
import com.bignerdranch.android.task04.R
import com.bignerdranch.android.task04.data.HabitRepository
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_habit_list.*
import kotlinx.android.synthetic.main.habit_list_item.*


class HabitListFragment : Fragment() {
    private var habitAdapter: HabitAdapter? = null
    private var habitType: HabitType? = null
    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_habit_list, container, false)
        val crimeRecyclerView = view.findViewById(R.id.habitRecyclerView) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(activity)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        arguments?.let {
            habitType = it.getSerializable(HABIT_TYPE) as HabitType
        }
    }
    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val habits = HabitRepository.habitList.filter { habit -> habit.type == habitType }

        habitAdapter = HabitAdapter(habits)
        habitRecyclerView.adapter = habitAdapter
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
            val bundle = bundleOf(HabitFragment.EXTRA_HABIT_ID_KEY to habit?.id )
            findNavController().navigate(
                R.id.action_habitViewPagerFragment_to_habitFragment,
                bundle
            )
        }

        fun bindCrime(habit: Habit) {
            this.habit = habit
            habitListItemName.text = habit.name
            habitListItemDescription.text = habit.description
            habitListItemPriority.text = habit.priority.name
            habitListItemType.text = habit.type.name
            habitListItemQuantity.text = getString(R.string.quantity, habit.quantity)
            habitListItemPeriodicity.text = getString(R.string.periodicity, habit.periodicity)
            habitRelativeLayout.setBackgroundColor(ContextCompat.getColor(activity!!, habit.color.colorId))
        }
    }

    private inner class HabitAdapter(var habits: List<Habit>) : RecyclerView.Adapter<HabitHolder>() {

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
