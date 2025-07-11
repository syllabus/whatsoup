package net.syllabus.whatsoup

import net.syllabus.whatsoup.WeekPlan.PairKey
import java.io.Serializable

data class MealList (
    val simples: MutableList<String>,
    val avec: MutableList<String>,
    val plats: MutableList<String>
) : Serializable {

    fun randomMeal(
        meal: Meal?,
        localSimples: MutableList<String>,
        localAvec: MutableList<String>,
        localPlats: MutableList<String>
    ): Meal {


        val selection = when (meal?.type) {
            null -> "Langue de boeuf"
            Meal.MealType.HARDCODED -> meal.name
            Meal.MealType.MEAL -> takeOne(localPlats)
            Meal.MealType.BUILT -> takeOne(localSimples) + " / " + takeOne(localAvec)
        }

        return Meal(Meal.MealType.HARDCODED, selection)
    }

    private fun takeOne(aList: MutableList<String>): String {
        if (aList == null || aList.size == 0) {
            return "langue ed boeuf"
        } else if (aList.size == 1) {
            return aList.get(0)
        } else {
            return aList.removeAt(0)
        }
    }

    fun randomWeek(template: WeekPlan):WeekPlan {
        val plan = WeekPlan()

        val localSimples = simples.toMutableList().shuffled().toMutableList()
        val localAvec = avec.toMutableList().shuffled().toMutableList()
        val localPlats = plats.toMutableList().shuffled().toMutableList()

        for(day in template.getDays()){
            for(lunch in listOf(true, false)) {
                val key = PairKey(day, lunch)
                plan.setMeal(key, randomMeal(template.getMeal(key), localSimples, localAvec, localPlats))
            }
        }

        return plan
    }


    companion object {
        fun default(): MealList {
            return MealList(
                mutableListOf(
                    "Knackies",
                    "Steaks hachés",
                    "Poissons Panés",
                    "Oeufs",
                    "Saucisses",
                    "Escalopes de poulet / dinde",
                    "Cordons bleus",
                    "Buns",
                    "Jambon"
                ),
                mutableListOf(
                    "Haricots verts",
                    "Petits pois / Carottes",
                    "Epinards / Poireaux surgelés",
                    "Galette de légumes",
                    "Ratatouille",
                    "Carottes rapées",
                    "Courgettes",
                    "Frites",
                    "Purée",
                    "Semoule",
                    "Quinoa",
                    "Polenta",
                    "Riz"
                ), mutableListOf(
                    "Paupiettes / Riz",
                    "Old El Paso",
                    "Semoule / Ratatouille",
                    "Boeuf bourguignon",
                    "Pot au feu",
                    "Quenelles / riz",
                    "Gratin (chou-fleur,endives-jambon,courge...)",
                    "Sushis, raclette, fondue, tartiflette",
                    "Choucroute",
                    "Saumon / Riz",
                    "Cassoulet",
                    "Piperade",
                    "Croques-monsieurs",
                    "Petit salé aux lentilles"
                )
            )
        }
    }
}