package com.yogendra.playapplication

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.yogendra.playapplication.databinding.MainActivityBinding
import com.yogendra.playapplication.utilities.ThemeManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.toolbar_switchicon.view.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
    HasAndroidInjector {

    val spreferences by lazy { getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE) }

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var themeSwitch: SwitchCompat

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun androidInjector(): AndroidInjector<Any> =
        dispatchingAndroidInjector as AndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: MainActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.main_activity)


        setSupportActionBar(binding.actMainToolbar)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginFragment
            )
        )


        setupActionBarWithNavController(navController, appBarConfiguration)

        init()
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {

        if (navController.currentDestination == navController.getBackStackEntry(R.id.loginFragment).destination) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        } else {
            onSupportNavigateUp()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.actionmenu, menu)
        val item = menu!!.findItem(R.id.myswitch)
        item.setActionView(R.layout.toolbar_switchicon)

        themeSwitch = item.actionView.switchCompat
        val selectedOption = spreferences.getString(getString(R.string.preference_key_theme), "")
        selectedOption?.let { setSwitch(it) }

        themeSwitch.setOnCheckedChangeListener { p0, isChecked ->
            val themePreferenceKey = getString(R.string.preference_key_theme)
            val editor = spreferences.edit()

            if (isChecked) {
                editor.putString(themePreferenceKey, getString(R.string.dark_theme_value))
                ThemeManager.applyTheme(getString(R.string.dark_theme_value))

            } else {
                editor.putString(themePreferenceKey, getString(R.string.follow_system_value))
                ThemeManager.applyTheme(getString(R.string.follow_system_value))

            }

            editor.apply()
            editor.commit()
        }
        return true

    }


/*
    private fun applyTheme(selectedOption: String) {

        when (selectedOption) {
            getString(R.string.light_theme_value) -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )

                delegate.applyDayNight()
            }


            getString(R.string.dark_theme_value) -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
                delegate.applyDayNight()

            }

            getString(R.string.auto_battery_value) -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                )
                delegate.applyDayNight()

            }
            getString(R.string.follow_system_value) -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
                delegate.applyDayNight()

            }
        }
    }
*/

    private fun init() {
        val themePreferenceKey = getString(R.string.preference_key_theme)
        val selectedOption = spreferences.getString(themePreferenceKey, "")
        selectedOption?.let {
            ThemeManager.applyTheme(selectedOption)
        }
        Log.i("Applied theme", "$selectedOption")
    }

    fun setSwitch(selectedOption: String) {
        themeSwitch.isChecked = selectedOption == getString(R.string.dark_theme_value)
    }

}
