package tasos.grigoris.sharemyrecipe.ui.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.widget.Toast
import kotlinx.android.synthetic.main.main_activity.*
import tasos.grigoris.sharemyrecipe.MyApplication
import tasos.grigoris.sharemyrecipe.R
import tasos.grigoris.sharemyrecipe.SignInManager

class MainActivity : AppCompatActivity() {

    private lateinit var signInManager: SignInManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        signInManager = SignInManager(this)
        signInManager.initGSO()

        setSupportActionBar(main_activity_toolbar)

        supportFragmentManager.beginTransaction().replace(main_activity_frame.id, FRRecipes()).commit()

        bottom_nav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { position ->

            when (position.itemId) {

                R.id.menu_recipes -> supportFragmentManager.beginTransaction().replace(main_activity_frame.id, FRRecipes()).commit()
                R.id.menu_categories -> supportFragmentManager.beginTransaction().replace(main_activity_frame.id, FRCategories()).commit()
                R.id.menu_favorites -> supportFragmentManager.beginTransaction().replace(main_activity_frame.id, FRFavorites()).commit()
           //     R.id.menu_new -> supportFragmentManager.beginTransaction().replace(main_activity_frame.id, FRRecipes()).commit()
                R.id.menu_profile -> supportFragmentManager.beginTransaction().replace(main_activity_frame.id, FRProfile()).commit()

            }

            return@OnNavigationItemSelectedListener true

        })

        println("convert to fraction: ".plus(convertDecimalToFraction(0.5)))
        println("convert to fraction: ".plus(convertDecimalToFraction(0.9)))
        println("convert to fraction: ".plus(convertDecimalToFraction(0.3)))

        add_recipe.setOnClickListener {

            if (signInManager.getAccount() != null){

                val intent = Intent(this, AddRecipe::class.java);
                startActivity(intent)

            }else{

                Toast.makeText(this, "You are not logged in ...", Toast.LENGTH_SHORT).show()

            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        MyApplication.hasPeaked = false

    }

    private fun convertDecimalToFraction(x: Double): String {

        if (x < 0)
            return "-" + convertDecimalToFraction(-x)

        val tolerance = 1.0E-6
        var h1 = 1.0
        var h2 = 0.0
        var k1 = 0.0
        var k2 = 1.0
        var b = x
        do {
            val a = Math.floor(b)
            var aux = h1
            h1 = a * h1 + h2
            h2 = aux
            aux = k1
            k1 = a * k1 + k2
            k2 = aux
            b = 1 / (b - a)
        } while (Math.abs(x - h1 / k1) > x * tolerance)

        return h1.toString() + "/" + k1
    }

}