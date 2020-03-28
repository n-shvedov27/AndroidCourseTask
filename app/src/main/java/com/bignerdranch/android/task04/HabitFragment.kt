package com.bignerdranch.android.task04

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.task04.data.HabitRepository
import com.bignerdranch.android.task04.data.entity.Habit
import com.bignerdranch.android.task04.data.entity.HabitColor
import com.bignerdranch.android.task04.data.entity.HabitPriority
import com.bignerdranch.android.task04.data.entity.HabitType
import kotlinx.android.synthetic.main.fragment_habit.*
import java.util.*


class HabitFragment : Fragment() {
    private lateinit var habit: Habit
    private var colorPickAdapter: ColorPickAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        habit = getHabit()
        initUI(view)
    }

    private inner class ColorPickHolder(containerView: View) :
        RecyclerView.ViewHolder(containerView),
        View.OnClickListener {
        private lateinit var currentHabitColor: HabitColor
        private var colorPickButton: Button =
            containerView.findViewById(R.id.color_pick_list_item_btn)
        private var colorPickWrapper: FrameLayout =
            containerView.findViewById(R.id.color_pick_list_item_wrapper)

        fun bindColorPick(
            previousHabitColor: HabitColor,
            currentHabitColor: HabitColor,
            nextHabitColor: HabitColor
        ) {
            this.currentHabitColor = currentHabitColor
            val previousColor: Int = ContextCompat.getColor(activity!!, previousHabitColor.colorId)
            val currentColor: Int = ContextCompat.getColor(activity!!, currentHabitColor.colorId)
            val nextColor: Int = ContextCompat.getColor(activity!!, nextHabitColor.colorId)

            val frameLayoutBackground = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(
                    ColorUtils.blendARGB(previousColor, currentColor, 0.5f),
                    currentColor,
                    ColorUtils.blendARGB(currentColor, nextColor, 0.5f)
                )
            ).apply {
                shape = GradientDrawable.RECTANGLE
                gradientType = GradientDrawable.LINEAR_GRADIENT
            }

            colorPickButton.background = GradientDrawable().apply {
                setColor(currentColor)
                cornerRadius = 5f
                setStroke(1, ContextCompat.getColor(activity!!, R.color.grey))
            }

            colorPickButton.setOnClickListener(this)
            colorPickWrapper.background = frameLayoutBackground
        }

        override fun onClick(v: View?) {
            val currentColor = ContextCompat.getColor(activity!!, currentHabitColor.colorId)
            current_color_img.setBackgroundColor(currentColor)
            habit.color = currentHabitColor
            current_color_rgb.text = resources.getString(
                R.string.rgb_current_color,
                Color.red(ContextCompat.getColor(activity!!, habit.color.colorId)),
                Color.green(ContextCompat.getColor(activity!!, habit.color.colorId)),
                Color.blue(ContextCompat.getColor(activity!!, habit.color.colorId))
            )

            val hsvArray = floatArrayOf(0f, 0f, 0f)
            Color.colorToHSV(ContextCompat.getColor(activity!!, habit.color.colorId), hsvArray)
            current_color_hsv.text = resources.getString(
                R.string.hsv_current_color,
                hsvArray[0],
                hsvArray[1],
                hsvArray[2]
            )
        }
    }

    private inner class ColorPickAdapter(var colors: List<HabitColor>) :
        RecyclerView.Adapter<ColorPickHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorPickHolder {
            val layoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater.inflate(R.layout.color_pick_list_item, parent, false)
            return ColorPickHolder(view)
        }

        override fun getItemCount(): Int {
            return colors.size
        }

        override fun onBindViewHolder(holder: ColorPickHolder, position: Int) {
            val previousColor: HabitColor = when (position) {
                0 -> colors[position]
                else -> colors[position - 1]
            }
            val nextColor: HabitColor = when (position) {
                HabitColor.values().size - 1 -> colors[position]
                else -> colors[position + 1]
            }

            holder.bindColorPick(previousColor, colors[position], nextColor)
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putString(EXTRA_HABIT_NAME_KEY, habit.name)
//        outState.putString(EXTRA_HABIT_DESCRIPTION_KEY, habit.description)
//        outState.putSerializable(EXTRA_HABIT_TYPE_KEY, habit.type)
//        outState.putSerializable(EXTRA_HABIT_PRIORITY_KEY, habit.priority)
//        outState.putInt(EXTRA_HABIT_PERIODICITY_KEY, habit.periodicity)
//        outState.putInt(EXTRA_HABIT_QUANTITY_KEY, habit.quantity)
//        outState.putSerializable(EXTRA_HABIT_COLOR_KEY, habit.color)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
//            habit.name = savedInstanceState.getString(EXTRA_HABIT_NAME_KEY)!!
//            habit.description = savedInstanceState.getString(EXTRA_HABIT_DESCRIPTION_KEY)!!
//            habit.type = savedInstanceState.getSerializable(EXTRA_HABIT_TYPE_KEY)!! as HabitType
//            habit.priority =
//                savedInstanceState.getSerializable(EXTRA_HABIT_PRIORITY_KEY)!! as HabitPriority
//            habit.quantity = savedInstanceState.getInt(EXTRA_HABIT_QUANTITY_KEY)
//            habit.periodicity = savedInstanceState.getInt(EXTRA_HABIT_PERIODICITY_KEY)
//            habit.color =
//                savedInstanceState.getSerializable(EXTRA_HABIT_COLOR_KEY)!! as HabitColor
        }
    }

    private fun initUI(view: View) {
        habit_name.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit.name = s.toString()
            }
        })
        habit_description.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit.description = s.toString()
            }
        })
        habit_quantity.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit.quantity = s.toString().ifEmpty { "0" }.toInt()
            }
        })
        habit_periodicity.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit.periodicity = s.toString().ifEmpty { "0" }.toInt()
            }
        })

        initColorPicker()
        initPrioritySpinner()
        initTypeRadioGroup(view)
        initSaveButton()
    }

    private fun initColorPicker() {
        colorPickAdapter = ColorPickAdapter(HabitColor.values().toList())
        color_pick_recycler_view.adapter = colorPickAdapter
        val linearLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        color_pick_recycler_view.layoutManager = linearLayoutManager
    }

    private fun initPrioritySpinner() {
        val priorities = HabitPriority.values().map { it.name }
        val adapter = ArrayAdapter<String>(
            activity!!.baseContext,
            android.R.layout.simple_spinner_item,
            priorities
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        habit_priority.adapter = adapter
        habit_priority.setSelection(0)
        habit_priority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                habit.priority = HabitPriority.values()[position]
            }
        }
    }

    private fun initTypeRadioGroup(view: View) {
        HabitType.values().forEach {
            run {
                habit_type.addView(
                    RadioButton(activity).apply { text = it.name }
                )
            }
        }

        habit_type.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = view.findViewById<RadioButton>(checkedId)
            val typeIndex = HabitType.values().indexOfFirst { it.name == selectedRadioButton.text }
            habit.type = HabitType.values()[typeIndex]
        }
    }

    private fun initSaveButton() {
        save_habit_button.setOnClickListener {
            run {
                HabitRepository.saveHabit(habit)
                activity!!.onBackPressed()
            }
        }
    }

    private fun getHabit(): Habit {
        return if (arguments == null) {
            Habit()
        } else {
            val habitId = arguments!!.getSerializable(EXTRA_HABIT_ID_KEY) as UUID
            HabitRepository.getHabit(habitId)
        }
    }

    private fun updateUI() {
        color_pick_recycler_view.post {
            color_pick_recycler_view.smoothScrollToPosition(HabitColor.values().indexOf(habit.color))
        }

        habit_name.setText(habit.name)
        habit_description.setText(habit.description)

        val priorityIndex = HabitPriority.values().indexOf(habit.priority)
        habit_priority.setSelection(priorityIndex)

        val typeIndex = HabitType.values().indexOf(habit.type)
        (habit_type.getChildAt(typeIndex) as RadioButton).isChecked = true

        habit_quantity.setText(habit.quantity.toString())
        habit_periodicity.setText(habit.periodicity.toString())

        updateColorPicker()
    }

    private fun updateColorPicker() {
        current_color_img.setBackgroundColor(
            ContextCompat.getColor(
                activity!!,
                habit.color.colorId
            )
        )

        current_color_rgb.text = resources.getString(
            R.string.rgb_current_color,
            Color.red(ContextCompat.getColor(activity!!, habit.color.colorId)),
            Color.green(ContextCompat.getColor(activity!!, habit.color.colorId)),
            Color.blue(ContextCompat.getColor(activity!!, habit.color.colorId))
        )

        val hsvArray = floatArrayOf(0f, 0f, 0f)
        Color.colorToHSV(ContextCompat.getColor(activity!!, habit.color.colorId), hsvArray)
        current_color_hsv.text = resources.getString(
            R.string.hsv_current_color,
            hsvArray[0],
            hsvArray[1],
            hsvArray[2]
        )
    }

    companion object {
        const val EXTRA_HABIT_ID_KEY = "extra_habit_id_key"
        const val EXTRA_HABIT_NAME_KEY = "extra_habit_name_key"
        const val EXTRA_HABIT_DESCRIPTION_KEY = "extra_habit_description_key"
        const val EXTRA_HABIT_TYPE_KEY = "extra_habit_type_key"
        const val EXTRA_HABIT_PRIORITY_KEY = "extra_habit_priority_key"
        const val EXTRA_HABIT_QUANTITY_KEY = "extra_habit_quantity_key"
        const val EXTRA_HABIT_PERIODICITY_KEY = "extra_habit_periodicity_key"
        const val EXTRA_HABIT_COLOR_KEY = "extra_habit_color_key"
    }
}
