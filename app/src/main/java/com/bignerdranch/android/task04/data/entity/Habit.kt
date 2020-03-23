
import java.util.*

class Habit() {
    var name: String = "New habit"
    var description: String = "description"
    var priority: HabitPriority = HabitPriority.LOW
    var type: HabitType = HabitType.Good
    var quantity: Int = 0
    var periodicity: Int = 0
    var color: HabitColor = HabitColor.AQUA
    var id: UUID = UUID.randomUUID()

    constructor(id: UUID) : this() {
        this.id = id
    }
}