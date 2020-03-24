package com.bignerdranch.android.task04

import com.bignerdranch.android.task04.data.entity.Habit
import com.bignerdranch.android.task04.data.entity.HabitColor
import com.bignerdranch.android.task04.data.entity.HabitPriority
import com.bignerdranch.android.task04.data.entity.HabitType
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.ColorUtils
import com.bignerdranch.android.task04.data.HabitRepository
import kotlinx.android.synthetic.main.fragment_habit.*
import java.util.*


class HabitFragment : Fragment() {
    private lateinit var habit: Habit

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


    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_HABIT_NAME_KEY, habit!!.name)
        outState.putString(EXTRA_HABIT_DESCRIPTION_KEY, habit!!.description)
        outState.putSerializable(EXTRA_HABIT_TYPE_KEY, habit!!.type)
        outState.putSerializable(EXTRA_HABIT_PRIORITY_KEY, habit!!.priority)
        outState.putInt(EXTRA_HABIT_PERIODICITY_KEY, habit!!.periodicity)
        outState.putInt(EXTRA_HABIT_QUANTITY_KEY, habit!!.quantity)
        outState.putSerializable(EXTRA_HABIT_COLOR_KEY, habit!!.color)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState!= null) {
            habit!!.name = savedInstanceState.getString(EXTRA_HABIT_NAME_KEY)!!
            habit!!.description = savedInstanceState.getString(EXTRA_HABIT_DESCRIPTION_KEY)!!
            habit!!.type = savedInstanceState.getSerializable(EXTRA_HABIT_TYPE_KEY)!! as HabitType
            habit!!.priority = savedInstanceState.getSerializable(EXTRA_HABIT_PRIORITY_KEY)!! as HabitPriority
            habit!!.quantity = savedInstanceState.getInt(EXTRA_HABIT_QUANTITY_KEY)
            habit!!.periodicity = savedInstanceState.getInt(EXTRA_HABIT_PERIODICITY_KEY)
            habit!!.color = savedInstanceState.getSerializable(EXTRA_HABIT_COLOR_KEY)!! as HabitColor
        }
    }

    private fun initUI(view: View) {
        habit_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit!!.name = s.toString()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        habit_description.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit!!.description = s.toString()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        habit_quantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit!!.quantity = s.toString().ifEmpty { "0" }.toInt()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        habit_periodicity.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit!!.periodicity = s.toString().ifEmpty { "0" }.toInt()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        initPrioritySpinner()
        initTypeRadioGroup(view)
        initColorPicker()
        initSaveButton()
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
                habit!!.priority = HabitPriority.values()[position]
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
            habit!!.type = HabitType.values()[typeIndex]
        }
    }

    private fun initColorPicker() {
        HabitColor.values().forEachIndexed { index, habitColor ->
            run {
                val previousColorId: Int = when (index) {
                    0 -> habitColor.colorId
                    else -> {
                        HabitColor.values()[index - 1].colorId
                    }
                }
                val nextColorId = when (index) {
                    HabitColor.values().size - 1 -> habitColor.colorId
                    else -> {
                        HabitColor.values()[index + 1].colorId
                    }
                }

                val previousColor: Int = resources.getColor(previousColorId)
                val nextColor: Int = resources.getColor(nextColorId)
                val currentColor: Int = resources.getColor(habitColor.colorId)

                val drawable = GradientDrawable(
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

                val colorButton = Button(activity).apply {
                    background = GradientDrawable().apply {
                        setColor(currentColor)
                        cornerRadius = 5f
                        setStroke(1, resources.getColor(R.color.grey))
                    }
                    val scale: Float = resources.displayMetrics.density

                    val pxWidth: Int = (70 * scale + 0.5f).toInt()
                    val pxHeight: Int = (70 * scale + 0.5f).toInt()

                    layoutParams = LinearLayout.LayoutParams(pxWidth, pxHeight)
                    setOnClickListener {
                        current_color_img.setBackgroundColor(currentColor)
                        habit!!.color = habitColor
                        current_color_rgb.text =
                            "r: ${Color.red(currentColor)};\ng: ${Color.green(currentColor)};\nb: ${Color.blue(
                                currentColor
                            )};"

                        val hsvArray = floatArrayOf(0f, 0f, 0f)
                        Color.colorToHSV(currentColor, hsvArray)
                        current_color_hsv.text =
                            "h: ${hsvArray[0]};\ns: ${hsvArray[1]};\nv: ${hsvArray[2]};"
                    }
                }

                val linearLayout: LinearLayout = LinearLayout(activity).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER
                    val scale: Float = resources.displayMetrics.density

                    val dpWidth: Int = (100 * scale + 0.5f).toInt()
                    val dpHeight: Int = (100 * scale + 0.5f).toInt()
                    layoutParams = LinearLayout.LayoutParams(dpWidth, dpHeight)
                    background = drawable
                    addView(colorButton)
                }

                color_picker.addView(linearLayout)
            }
        }
    }

    private fun initSaveButton() {
        save_habit_button.setOnClickListener {
            run {
                habit?.let {
                    HabitRepository.saveHabit(habit!!)
                }
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
        habit_name.setText(habit!!.name)
        habit_description.setText(habit!!.description)

        val priorityIndex = HabitPriority.values().indexOf(habit!!.priority)
        habit_priority.setSelection(priorityIndex)

        val typeIndex = HabitType.values().indexOf(habit!!.type)
        (habit_type.getChildAt(typeIndex) as RadioButton).isChecked = true

        habit_quantity.setText(habit!!.quantity.toString())
        habit_periodicity.setText(habit!!.periodicity.toString())

        current_color_img.setBackgroundColor(resources.getColor(habit!!.color.colorId))

        current_color_rgb.text =
            "r: ${Color.red(resources.getColor(habit!!.color.colorId))};\ng: ${Color.green(
                resources.getColor(habit!!.color.colorId)
            )};\nb: ${Color.blue(resources.getColor(habit!!.color.colorId))};"

        val hsvArray = floatArrayOf(0f, 0f, 0f)
        Color.colorToHSV(resources.getColor(habit!!.color.colorId), hsvArray)
        current_color_hsv.text = "h: ${hsvArray[0]};\ns: ${hsvArray[1]};\nv: ${hsvArray[2]};"
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

        fun newInstance(habitId: UUID?): HabitFragment {
            val fragment = HabitFragment()

            habitId?.let {
                val bundle = Bundle()
                bundle.putSerializable(EXTRA_HABIT_ID_KEY, it)
                fragment.arguments = bundle
            }

            return fragment
        }
    }
}
