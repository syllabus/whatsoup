package net.syllabus.whatsoup.ui.week

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.syllabus.whatsoup.model.WeekTemplate

class WeekViewModel : ViewModel() {

    private val _sat1 = MutableLiveData<String>().apply {
        value = "Knackies"
    }
    val sat1: MutableLiveData<String> = _sat1

    private val _sat2 = MutableLiveData<String>().apply {
        value = "Tartiflette aux champignons"
    }
    val sat2: MutableLiveData<String> = _sat2

    private val _sun1 = MutableLiveData<String>().apply {
        value = "Knackies"
    }
    val sun1: MutableLiveData<String> = _sun1

    private val _sun2 = MutableLiveData<String>().apply {
        value = "Tartiflette aux champignons"
    }
    val sun2: MutableLiveData<String> = _sun2

    private val _mon1 = MutableLiveData<String>().apply {
        value = "Knackies"
    }
    val mon1: MutableLiveData<String> = _mon1

    private val _mon2 = MutableLiveData<String>().apply {
        value = "Tartiflette aux champignons"
    }
    val mon2: MutableLiveData<String> = _mon2

    private val _tue1 = MutableLiveData<String>().apply {
        value = "Knackies"
    }
    val tue1: MutableLiveData<String> = _tue1

    private val _tue2 = MutableLiveData<String>().apply {
        value = "Tartiflette aux champignons"
    }
    val tue2: MutableLiveData<String> = _tue2

    private val _wed1 = MutableLiveData<String>().apply {
        value = "Knackies"
    }
    val wed1: MutableLiveData<String> = _wed1

    private val _wed2 = MutableLiveData<String>().apply {
        value = "Tartiflette aux champignons"
    }
    val wed2: MutableLiveData<String> = _wed2

    private val _thu1 = MutableLiveData<String>().apply {
        value = "Knackies"
    }
    val thu1: MutableLiveData<String> = _thu1

    private val _thu2 = MutableLiveData<String>().apply {
        value = "Tartiflette aux champignons"
    }
    val thu2: MutableLiveData<String> = _thu2

    private val _fri1 = MutableLiveData<String>().apply {
        value = "Knackies"
    }
    val fri1: MutableLiveData<String> = _fri1

    private val _fri2 = MutableLiveData<String>().apply {
        value = "Tartiflette aux champignons"
    }
    val fri2: MutableLiveData<String> = _fri2

    fun fromString (string : String) {
        var i = 0;
        val fields =  arrayOf(_sat1, _sat2, _sun1, _sun2, _mon1, _mon2, _tue1, _tue2, _wed1, _wed2, _thu1, _thu2, _fri1, _fri2);
        for (plat in string.split("/*/")) {
            fields[i].postValue(plat)
            i++
        }
    }

    override fun toString () : String {
        var s = ""
        for (field in arrayOf(_sat1, _sat2, _sun1, _sun2, _mon1, _mon2, _tue1, _tue2, _wed1, _wed2, _thu1, _thu2, _fri1, _fri2)) {
            s = s + field.value
            if (field != _fri2) {
                s = s + "/*/"
            }
        }
        return s
    }

    fun onGenerate(simples : MutableList<String>, withs : MutableList<String>, meals : MutableList<String>, template : WeekTemplate) {

        simples.shuffle();
        withs.shuffle();
        meals.shuffle();

        var i = 0;
        for (field in arrayOf(_sat1, _sat2, _sun1, _sun2, _mon1, _mon2, _tue1, _tue2, _wed1, _wed2, _thu1, _thu2, _fri1, _fri2)) {
            val def = template.template.get(i)
            val type = def.type

            if (type == WeekTemplate.MEAL_TYPE.HARDCODED) {
                field.postValue(def.customisation)
            } else if (type == WeekTemplate.MEAL_TYPE.MEAL) {
                val meal = meals.removeAt(0)
                field.postValue(meal)
            } else if (type == WeekTemplate.MEAL_TYPE.BUILT) {
                val simple = simples.removeAt(0)
                val with = withs.removeAt(0)
                field.postValue(simple + " / " + with)
            } else {
                field.postValue("Langue de boeuf")
            }

            i++;
        }
    }
}