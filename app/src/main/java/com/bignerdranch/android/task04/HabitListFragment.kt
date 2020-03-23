package com.bignerdranch.android.task04

import Habit
import HabitType
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_habit_list.*


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

        val crimeRecyclerView = view.findViewById(R.id.habit_recycler_view) as RecyclerView
        crimeRecyclerView.setLayoutManager(LinearLayoutManager(activity))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

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
        habit_recycler_view.adapter = habitAdapter
    }

    private inner class HabitHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val linearLayout: RelativeLayout = itemView.findViewById(R.id.habit_relative_layout)
        private val name: TextView = itemView.findViewById(R.id.habit_list_item_name)
        private val description: TextView = itemView.findViewById(R.id.habit_list_item_description)
        private val priority: TextView = itemView.findViewById(R.id.habit_list_item_priority)
        private val type: TextView = itemView.findViewById(R.id.habit_list_item_type)
        private val quantity: TextView = itemView.findViewById(R.id.habit_list_item_quantity)
        private val periodicity: TextView = itemView.findViewById(R.id.habit_list_item_periodicity)
        private var habit: Habit? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val bundle = bundleOf(HabitFragment.EXTRA_HABIT_ID_KEY to habit?.id )
            navController.navigate(
                R.id.action_habitViewPagerFragment_to_habitFragment,
                bundle
            )
        }

        fun bindCrime(habit: Habit) {
            this.habit = habit
            name.text = habit.name
            description.text = habit.description
            priority.text = habit.priority?.name
            type.text = habit.type?.name
            quantity.text = getString(R.string.quantity) + habit.quantity.toString()
            periodicity.text = getString(R.string.periodicity) + habit.periodicity.toString()
            linearLayout.setBackgroundColor(resources.getColor(habit.color.colorId))
        }
    }

    private inner class HabitAdapter(var habits: List<Habit>) : RecyclerView.Adapter<HabitHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitHolder {
            val layoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater.inflate(R.layout.list_item_habit, parent, false)
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
            val fragment = HabitListFragment()
            val bundle = Bundle()

            bundle.putSerializable(HABIT_TYPE, habitType)
            fragment.arguments = bundle

            return fragment
        }
    }
}
