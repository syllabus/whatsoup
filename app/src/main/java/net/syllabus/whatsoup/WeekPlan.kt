package net.syllabus.whatsoup

import java.io.Serializable

data class WeekPlan(val meals: LinkedHashMap <PairKey, Meal?> = LinkedHashMap()) : Serializable {

    data class PairKey(val day: String, val lunch: Boolean) : Serializable

    fun getMeal(key: PairKey) : Meal?{
        return meals[key]
    }

    fun setMeal(key: PairKey, meal: Meal?){
        meals[key] = meal
    }

    fun getDays(): List<String> {
        val days = ArrayList<String>()
        var prev: String? = null

        meals.keys.forEach(){
            if (prev != it.day) {
                days.add(it.day)
            }
            prev = it.day
        }

        return days
    }

    companion object{
        fun defaultTemplate(): WeekPlan{
            val template = WeekPlan()

            template.setMeal(PairKey("Samedi", true), Meal(Meal.MealType.HARDCODED, "Restes"))
            template.setMeal(PairKey("Samedi", false), Meal(Meal.MealType.MEAL))
            template.setMeal(PairKey("Dimanche", true), Meal(Meal.MealType.MEAL))
            template.setMeal(PairKey("Dimanche", false), Meal(Meal.MealType.HARDCODED, "Soupe/Gaspat' / Frometon"))
            template.setMeal(PairKey("Lundi", true), Meal(Meal.MealType.BUILT))
            template.setMeal(PairKey("Lundi", false), Meal(Meal.MealType.HARDCODED, "Salade"))
            template.setMeal(PairKey("Mardi", true), Meal(Meal.MealType.BUILT))
            template.setMeal(PairKey("Mardi", false), Meal(Meal.MealType.HARDCODED, "Pâtes"))
            template.setMeal(PairKey("Mercredi", true), Meal(Meal.MealType.BUILT))
            template.setMeal(PairKey("Mercredi", false), Meal(Meal.MealType.HARDCODED, "Quiche"))
            template.setMeal(PairKey("Jeudi", true), Meal(Meal.MealType.BUILT))
            template.setMeal(PairKey("Jeudi", false), Meal(Meal.MealType.HARDCODED, "Marie cuisine"))
            template.setMeal(PairKey("Vendredi", true), Meal(Meal.MealType.BUILT))
            template.setMeal(PairKey("Vendredi", false), Meal(Meal.MealType.MEAL))

            return template
        }
    }

}