package net.syllabus.whatsoup.ui.week

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeekViewModel : ViewModel() {

    private val _sat1 = MutableLiveData<String>().apply {
        value = "Knackies"
    }
    val sat1: LiveData<String> = _sat1

    private val _sat2 = MutableLiveData<String>().apply {
        value = "Tartiflette aux champignons"
    }
    val sat2: LiveData<String> = _sat2

    private val _sun1 = MutableLiveData<String>().apply {
        value = "Knackies"
    }
    val sun1: LiveData<String> = _sun1

    private val _sun2 = MutableLiveData<String>().apply {
        value = "Tartiflette aux champignons"
    }
    val sun2: LiveData<String> = _sun2

    private val _mon1 = MutableLiveData<String>().apply {
        value = "Knackies"
    }
    val mon1: LiveData<String> = _mon1

    private val _mon2 = MutableLiveData<String>().apply {
        value = "Tartiflette aux champignons"
    }
    val mon2: LiveData<String> = _mon2

    private val _tue1 = MutableLiveData<String>().apply {
        value = "Knackies"
    }
    val tue1: LiveData<String> = _tue1

    private val _tue2 = MutableLiveData<String>().apply {
        value = "Tartiflette aux champignons"
    }
    val tue2: LiveData<String> = _tue2

    private val _wed1 = MutableLiveData<String>().apply {
        value = "Knackies"
    }
    val wed1: LiveData<String> = _wed1

    private val _wed2 = MutableLiveData<String>().apply {
        value = "Tartiflette aux champignons"
    }
    val wed2: LiveData<String> = _wed2

    private val _thu1 = MutableLiveData<String>().apply {
        value = "Knackies"
    }
    val thu1: LiveData<String> = _thu1

    private val _thu2 = MutableLiveData<String>().apply {
        value = "Tartiflette aux champignons"
    }
    val thu2: LiveData<String> = _thu2

    private val _fri1 = MutableLiveData<String>().apply {
        value = "Knackies"
    }
    val fri1: LiveData<String> = _fri1

    private val _fri2 = MutableLiveData<String>().apply {
        value = "Tartiflette aux champignons"
    }
    val fri2: LiveData<String> = _fri2

}