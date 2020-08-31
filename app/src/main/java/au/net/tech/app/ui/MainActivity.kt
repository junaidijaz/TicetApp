package au.net.tech.app.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import au.net.tech.app.R
import au.net.tech.app.ui.fragments.AddImageTicketFragment
import au.net.tech.app.ui.fragments.AddTicketFragment
import au.net.tech.app.ui.fragments.HomeFragment
import au.net.tech.app.ui.fragments.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity()   {

    val TAG = "MainActivity"
    var doubleBackToExitPressedOnce = false

    private val fm = supportFragmentManager
    private var fragmentSettings = SettingFragment()
    private var fragmentHome = HomeFragment()
    private var fragmentAddTicket = AddTicketFragment()
   

    private lateinit var visibleFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setBottomNavView()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun setBottomNavView() {

        fm.beginTransaction().add(R.id.flMain, fragmentSettings, "2").hide(fragmentSettings)
            .commit()
        fm.beginTransaction().add(R.id.flMain, fragmentAddTicket, "3").hide(fragmentAddTicket)
            .commit()
        fm.beginTransaction().add(R.id.flMain, fragmentHome, "1").commit()
        setToolBarTittle("Home", true)
        visibleFragment = fragmentHome

        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    Log.d(TAG, "setBottomNavView: ")
                    fm.beginTransaction().hide(visibleFragment).show(fragmentHome).commit()
                    visibleFragment = fragmentHome
                    setToolBarTittle("Home", true)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_add_ticket -> {
                    fm.beginTransaction().hide(visibleFragment).show(fragmentAddTicket).commit()
                    visibleFragment = fragmentAddTicket
                    setToolBarTittle("Add Ticket")
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_add_image_ticket -> {
                    startActivity(Intent(this, CameraActivity::class.java))
                    return@OnNavigationItemSelectedListener false
                }
                R.id.navigation_settings -> {
                    fm.beginTransaction().hide(visibleFragment).show(fragmentSettings).commit()
                    visibleFragment = fragmentSettings
                    setToolBarTittle("Settings")
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }


    private fun setToolBarTittle(tittle: String, enableFilerIcon: Boolean = false) {
        if (enableFilerIcon) ivFilters.visibility = View.VISIBLE
        else ivFilters.visibility = View.INVISIBLE
        tbTittle.text = tittle
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }




}