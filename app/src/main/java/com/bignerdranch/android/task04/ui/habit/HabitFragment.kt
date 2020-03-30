package com.bignerdranch.android.task04.ui.habit

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.task04.R
import com.bignerdranch.android.task04.data.HabitRepository
import com.bignerdranch.android.task04.data.entity.Habit
import com.bignerdranch.android.task04.data.entity.HabitColor
import com.bignerdranch.android.task04.data.entity.HabitPriority
import com.bignerdranch.android.task04.data.entity.HabitType
import com.bignerdranch.android.task04.ui.CustomTextWatcher
import com.bignerdranch.android.task04.viewmodels.habit.HabitViewModel
import kotlinx.android.synthetic.main.fragment_habit.*
import java.util.*


class HabitFragment : Fragment() {
    private var habit: Habit? = null
    private var colorPickAdapter: ColorPickAdapter? = null
    private lateinit var habitViewModel: HabitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        habitViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val habitId: UUID? = arguments?.getSerializable(EXTRA_HABIT_ID_KEY)?.let {
                    return@let it as UUID
                } ?: run {
                    return@run null
                }
                return HabitViewModel(habitId) as T
            }
        }).get(HabitViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        habitViewModel.habit.observe(viewLifecycleOwner, Observer {
            habit = it
        })
        initUI(view)
    }

    private inner class ColorPickHolder(containerView: View) :
        RecyclerView.ViewHolder(containerView),
        View.OnClickListener {
        private lateinit var currentHabitColor: HabitColor
        private var colorPickButton: Button =
            containerView.findViewById(R.id.colorPickListItemBtn)
        private var colorPickWrapper: FrameLayout =
            containerView.findViewById(R.id.colorPickListItemWrapper)

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
                setStroke(1, ContextCompat.getColor(activity!!,
                    R.color.grey
                ))
            }

            colorPickButton.setOnClickListener(this)
            colorPickWrapper.background = frameLayoutBackground
        }

        override fun onClick(v: View?) {
            habit?.let {
                val currentColor = ContextCompat.getColor(activity!!, currentHabitColor.colorId)
                currentColorImg.setBackgroundColor(currentColor)
                it.color = currentHabitColor
                currentColorRgb.text = resources.getString(
                    R.string.rgb_current_color,
                    Color.red(ContextCompat.getColor(activity!!, it.color.colorId)),
                    Color.green(ContextCompat.getColor(activity!!, it.color.colorId)),
                    Color.blue(ContextCompat.getColor(activity!!, it.color.colorId))
                )

                val hsvArray = floatArrayOf(0f, 0f, 0f)
                Color.colorToHSV(ContextCompat.getColor(activity!!, it.color.colorId), hsvArray)
                currentColorHsv.text = resources.getString(
                    R.string.hsv_current_color,
                    hsvArray[0],
                    hsvArray[1],
                    hsvArray[2]
                )

            }
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

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(EXTRA_HABIT_NAME_KEY, habit.name)
//        outState.putString(EXTRA_HABIT_DESCRIPTION_KEY, habit.description)
//        outState.putSerializable(EXTRA_HABIT_TYPE_KEY, habit.type)
//        outState.putSerializable(EXTRA_HABIT_PRIORITY_KEY, habit.priority)
//        outState.putInt(EXTRA_HABIT_PERIODICITY_KEY, habit.periodicity)
//        outState.putInt(EXTRA_HABIT_QUANTITY_KEY, habit.quantity)
//        outState.putSerializable(EXTRA_HABIT_COLOR_KEY, habit.color)
//    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        if (savedInstanceState != null) {
//            habit.name = savedInstanceState.getString(EXTRA_HABIT_NAME_KEY)!!
//            habit.description = savedInstanceState.getString(EXTRA_HABIT_DESCRIPTION_KEY)!!
//            habit.type = savedInstanceState.getSerializable(EXTRA_HABIT_TYPE_KEY)!! as HabitType
//            habit.priority =
//                savedInstanceState.getSerializable(EXTRA_HABIT_PRIORITY_KEY)!! as HabitPriority
//            habit.quantity = savedInstanceState.getInt(EXTRA_HABIT_QUANTITY_KEY)
//            habit.periodicity = savedInstanceState.getInt(EXTRA_HABIT_PERIODICITY_KEY)
//            habit.color =
//                savedInstanceState.getSerializable(EXTRA_HABIT_COLOR_KEY)!! as HabitColor
//        }
//    }

    private fun initUI(view: View) {
        habitName.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit?.name = s.toString()
            }
        })
        habitDescription.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit?.description = s.toString()
            }
        })
        habit_quantity.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit?.quantity = s.toString().ifEmpty { "0" }.toInt()
            }
        })
        habitPeriodicity.addTextChangedListener(object : CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit?.periodicity = s.toString().ifEmpty { "0" }.toInt()
            }
        })

        initColorPicker()
        initPrioritySpinner()
        initTypeRadioGroup(view)
        initSaveButton()
    }

    private fun initColorPicker() {
        colorPickAdapter = ColorPickAdapter(HabitColor.values().toList())
        colorPickRecyclerView.adapter = colorPickAdapter
        val linearLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        colorPickRecyclerView.layoutManager = linearLayoutManager
    }

    private fun initPrioritySpinner() {
        val priorities = HabitPriority.values().map { it.name }
        val adapter = ArrayAdapter(
            activity!!.baseContext,
            android.R.layout.simple_spinner_item,
            priorities
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        habitPriority.adapter = adapter
        habitPriority.setSelection(0)
        habitPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                habit?.priority = HabitPriority.values()[position]
            }
        }
    }

    private fun initTypeRadioGroup(view: View) {
        HabitType.values().forEach {
            run {
                habitType.addView(
                    RadioButton(activity).apply { text = it.name }
                )
            }
        }

        habitType.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = view.findViewById<RadioButton>(checkedId)
            val typeIndex = HabitType.values().indexOfFirst { it.name == selectedRadioButton.text }
            habit?.type = HabitType.values()[typeIndex]
        }
    }

    private fun initSaveButton() {
        saveHabitButton.setOnClickListener {
            run {
                habit?.let {
                    HabitRepository.saveHabit(it)
                    activity!!.onBackPressed()
                } ?: run {
                    Toast.makeText(
                        activity!!,
                        getString(R.string.error_habit_save),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun updateUI() {
        habit?.let {
            colorPickRecyclerView.post {
                colorPickRecyclerView.smoothScrollToPosition(HabitColor.values().indexOf(it.color))
            }

            habitName.setText(it.name)
            habitDescription.setText(it.description)

            val priorityIndex = HabitPriority.values().indexOf(it.priority)
            habitPriority.setSelection(priorityIndex)

            val typeIndex = HabitType.values().indexOf(it.type)
            (habitType.getChildAt(typeIndex) as RadioButton).isChecked = true

            habit_quantity.setText(it.quantity.toString())
            habitPeriodicity.setText(it.periodicity.toString())

            updateColorPicker()
        }

    }

    private fun updateColorPicker() {
        habit?.let {
            currentColorImg.setBackgroundColor(
                ContextCompat.getColor(
                    activity!!,
                    it.color.colorId
                )
            )

            currentColorRgb.text = resources.getString(
                R.string.rgb_current_color,
                Color.red(ContextCompat.getColor(activity!!, it.color.colorId)),
                Color.green(ContextCompat.getColor(activity!!, it.color.colorId)),
                Color.blue(ContextCompat.getColor(activity!!, it.color.colorId))
            )

            val hsvArray = floatArrayOf(0f, 0f, 0f)
            Color.colorToHSV(ContextCompat.getColor(activity!!, it.color.colorId), hsvArray)
            currentColorHsv.text = resources.getString(
                R.string.hsv_current_color,
                hsvArray[0],
                hsvArray[1],
                hsvArray[2]
            )
        }
    }

    companion object {
        const val EXTRA_HABIT_ID_KEY = "extra_habit_id_key"
//        const val EXTRA_HABIT_NAME_KEY = "extra_habit_name_key"
//        const val EXTRA_HABIT_DESCRIPTION_KEY = "extra_habit_description_key"
//        const val EXTRA_HABIT_TYPE_KEY = "extra_habit_type_key"
//        const val EXTRA_HABIT_PRIORITY_KEY = "extra_habit_priority_key"
//        const val EXTRA_HABIT_QUANTITY_KEY = "extra_habit_quantity_key"
//        const val EXTRA_HABIT_PERIODICITY_KEY = "extra_habit_periodicity_key"
//        const val EXTRA_HABIT_COLOR_KEY = "extra_habit_color_key"
    }
}
