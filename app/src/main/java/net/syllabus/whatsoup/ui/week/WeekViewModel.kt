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

    fun onGenerate(menus : List<String>) {
        _sat1.postValue(menus[0])
        _sat2.postValue(menus[1])
        _sun1.postValue(menus[2])
        _sun2.postValue(menus[3])
        _mon1.postValue(menus[4])
        _mon2.postValue(menus[5])
        _tue1.postValue(menus[6])
        _tue2.postValue(menus[7])
        _wed1.postValue(menus[8])
        _wed2.postValue(menus[9])
        _thu1.postValue(menus[10])
        _thu2.postValue(menus[11])
        _fri1.postValue(menus[12])
        _fri2.postValue(menus[13])
    }
}