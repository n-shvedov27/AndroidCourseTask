package com.bignerdranch.android.task04.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity
class Habit {

    @SerializedName("uid")
    @PrimaryKey
    @TypeConverters(UUIDConverter::class)
    @NotNull
    var uid: UUID?= null

    @SerializedName("date")
    @TypeConverters(DateConverter::class)
    var date: Date = Date()

    @SerializedName("title")
    var title: String = "New habit"

    @SerializedName("description")
    var description: String = "description"

    @TypeConverters(HabitPriorityConverter::class)
    @SerializedName("priority")
    var priority: HabitPriority = HabitPriority.LOW

    @TypeConverters(HabitTypeConverter::class)
    @SerializedName("type")
    var type: HabitType = HabitType.Good

    @SerializedName("count")
    var count: Int = 0

    @SerializedName("frequency")
    var frequency: Int = 0

    @TypeConverters(HabitColorConverter::class)
    @SerializedName("color")
    var color: HabitColor = HabitColor.RED
}

class HabitTypeAdapter : TypeAdapter<Habit>() {
    override fun write(jsonWriter: JsonWriter, habit: Habit) {
        jsonWriter.beginObject();

        habit.uid?.let {
            jsonWriter.name("uid");
            jsonWriter.value(it.toString())
        }

        jsonWriter.name("date");
        jsonWriter.value(habit.date.time);

        jsonWriter.name("title");
        jsonWriter.value(habit.title);

        jsonWriter.name("description");
        jsonWriter.value(habit.description);

        jsonWriter.name("priority");
        jsonWriter.value(habit.priority.ordinal);

        jsonWriter.name("type");
        jsonWriter.value(habit.type.ordinal);

        jsonWriter.name("count");
        jsonWriter.value(habit.count);

        jsonWriter.name("frequency");
        jsonWriter.value(habit.frequency);

        jsonWriter.name("color");
        jsonWriter.value(habit.color.ordinal);

        jsonWriter.endObject();
    }

    override fun read(jsonReader: JsonReader): Habit {
        val habit = Habit()
        jsonReader.beginObject()
        var fieldname: String? = null

        while (jsonReader.hasNext()) {
            var token: JsonToken = jsonReader.peek()
            if (token.equals(JsonToken.NAME)) { //get the current token
                fieldname = jsonReader.nextName()
            }

            when (fieldname) {
                "uid" -> {
                    token = jsonReader.peek()
                    habit.uid = UUID.fromString(jsonReader.nextString())
                }
                "date" -> {
                    token = jsonReader.peek()
                    habit.date = Date(jsonReader.nextLong())
                }
                "title" -> {
                    token = jsonReader.peek()
                    habit.title = jsonReader.nextString()
                }

                "description" -> {
                    token = jsonReader.peek()
                    habit.description = jsonReader.nextString()
                }

                "priority" -> {
                    token = jsonReader.peek()
                    habit.priority = HabitPriority.values()[jsonReader.nextInt()]
                }

                "type" -> {
                    token = jsonReader.peek()
                    habit.type = HabitType.values()[jsonReader.nextInt()]
                }

                "count" -> {
                    token = jsonReader.peek()
                    habit.count = jsonReader.nextInt()
                }

                "frequency" -> {
                    token = jsonReader.peek()
                    habit.frequency = jsonReader.nextInt()
                }

                "color" -> {
                    token = jsonReader.peek()
                    habit.color = HabitColor.values()[jsonReader.nextInt()]
                }
            }
        }
        jsonReader.endObject()
        return habit
    }
}

class UUIDConverter {

    @TypeConverter
    fun fromColor(value: UUID): String = value.toString()

    @TypeConverter
    fun toColor(value: String): UUID = UUID.fromString(value)
}