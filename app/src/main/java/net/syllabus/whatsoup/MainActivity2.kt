package net.syllabus.whatsoup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.GsonBuilder
import net.syllabus.whatsoup.databinding.ActivityMain2Binding
import java.io.File
import java.net.URL


class MainActivity2 : AppCompatActivity() {

    private val sharedPrefName = "meal_prefs"
    private val planName = "week_plan"
    private val nextplanName = "nextweek_plan"
    private val templateName = "week_template"

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main2) as NavHostFragment
        val navController = navHostFragment.navController
        //val navController = findNavController(R.id.nav_host_fragment_activity_main2)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_week, R.id.navigation_nextweek, R.id.navigation_template, R.id.navigation_lists
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        handleshare()
    }

    override fun onResume() {
        super.onResume()
        handleshare()
    }

    fun handleshare() {
        Log.e("WHATSOUP", "===============   HANDLE SHARE   ====================")
        Log.e("WHATSOUP","onSharedIntent: receivedIntent: " + intent)
        onSharedIntent()
        when {
            intent?.action == Intent.ACTION_SEND -> {
                try {
                    handleSendUrl(intent)
                } catch (e: Exception) {
                    Log.e("WHATSOUP", "share pb", e)
                }
                try {
                    handleSendText(intent)
                } catch (e: Exception) {
                    Log.e("WHATSOUP", "share pb", e)
                }
            }
            intent?.action == Intent.ACTION_VIEW -> {
                try {
                    handleSendUrl(intent)
                } catch (e: Exception) {
                    Log.e("WHATSOUP", "share pb", e)
                }
                try {
                    handleSendText(intent)
                } catch (e: Exception) {
                    Log.e("WHATSOUP", "share pb", e)
                }
            }
        }
        Log.e("WHATSOUP", "=====================================================")
    }

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            readUri(Uri.parse(it))
        }
    }

    private fun handleSendUrl(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            readUri(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun readUri(uri: Uri) {
            Thread() {
                run {
                    var s: String? = intent.getStringExtra(Intent.EXTRA_TEXT)
                    if (s == null) {
                        try {
                            val file = createTempFile()
                            uri?.let { this.contentResolver.openInputStream(uri) }.use { input ->
                                file.outputStream().use { output ->
                                    input?.copyTo(output)
                                }
                            }
                            s = file.readText()
                            file.delete()

                        } catch (e: Exception) {
                            s = URL(uri.toString()).openStream().readAllBytes().decodeToString()
                        }
                    }
                    Log.e("WHATSOUP", s!!)
                    saveData(s)
                    finish();
                    val openMainActivity = Intent(
                        applicationContext,
                        MainActivity2::class.java
                    )
                    openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(openMainActivity)
                }
            }.start()
    }

    private fun saveData(jsonWeekPlan: String){

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val type = WeekPlan::class.java
        val weekPlan = gson.fromJson(jsonWeekPlan, type)

        var destination = nextplanName
        for (day in weekPlan.getDays()) {
            for (lunch in arrayOf(true, false)) {
                val key = WeekPlan.PairKey(day, lunch)
                val meal = weekPlan.getMeal(key)
                if (meal == null) {
                    weekPlan.setMeal(key, Meal(Meal.MealType.HARDCODED, "pain / eau"))
                } else {
                    if (meal.type != Meal.MealType.HARDCODED) {
                        destination = templateName
                    }
                }
            }
        }

        val jsonWeekPlanFixed = gson.toJson(weekPlan)

        val sharedPref = this.getSharedPreferences(sharedPrefName, MODE_PRIVATE)
        val editor = sharedPref?.edit()

        editor?.putString(destination, jsonWeekPlanFixed)
        Log.d("WHATSOUP", "saving "+destination+" " + jsonWeekPlanFixed)

        editor?.apply()
    }

    private fun onSharedIntent() {
        try {
            val receivedIntent = intent
            val receivedAction = receivedIntent.action
            val receivedType = receivedIntent.type

            Log.e("WHATSOUP","onSharedIntent: receivedIntent: " + receivedIntent)
            Log.e("WHATSOUP","onSharedIntent: receivedAction: " + receivedAction)
            Log.e("WHATSOUP","onSharedIntent: receivedType: " + receivedType)
            Log.e("WHATSOUP", "data: " + receivedIntent.dataString)
            Log.e("WHATSOUP", "dataddd: " + receivedIntent.data)
            Log.e("WHATSOUP", "extra_text: " + receivedIntent.getStringExtra(Intent.EXTRA_TEXT))
            Log.e("WHATSOUP", "extra_stream: " + receivedIntent.extras)

        }catch (e: Exception) {
            Log.e("WHATSOUP", "share pb", e)
        }
    }
    fun share(): String? {
        val ctx: Activity = this
        val intent = ctx.intent
        val action = intent.action
        val type = intent.type
        if (Intent.ACTION_SEND == action && type != null) {
            if (type.startsWith("text/")) {
                try {
                    val uri = intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as Uri?
                    if (uri != null) {
                        val filePath = uri.path
                        var sharedFile: File? = null
                        val projection = arrayOf(MediaStore.MediaColumns.DATA)
                        val pathCursor =
                            ctx.contentResolver.query(uri, projection, null, null, null)
                        // Check for a valid cursor
                        if (pathCursor != null &&
                            pathCursor.moveToFirst()
                        ) {
                            // Get the column index in the Cursor
                            val filenameIndex = pathCursor.getColumnIndex(
                                MediaStore.MediaColumns.DATA
                            )
                            // Get the full file name including path
                            val fileName = pathCursor.getString(filenameIndex)
                            pathCursor.close()
                            // Create a File object for the filename
                            sharedFile = File(fileName)
                        } else {
                            sharedFile = File(uri.path)
                        }
                        return sharedFile.path
                    }
                } catch (e: Throwable) {
                    return e.message
                }
            }
        }
        return null
    }
}