package net.syllabus.whatsoup

import java.io.Serializable

data class Meal(val type: MealType, val name: String = "") : Serializable {

    enum class MealType {
        HARDCODED, MEAL, BUILT
    }

    override fun toString(): String {
        return if (type==MealType.HARDCODED) name else type.toString()
    }
}
