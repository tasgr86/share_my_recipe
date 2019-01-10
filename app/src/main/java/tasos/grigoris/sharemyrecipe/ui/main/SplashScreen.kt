package tasos.grigoris.sharemyrecipe.ui.main

import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.splash_screen.*
import tasos.grigoris.sharemyrecipe.MyPrefs
import tasos.grigoris.sharemyrecipe.R
import tasos.grigoris.sharemyrecipe.Utils

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val utils = Utils(this)
        val prefs = MyPrefs(this)

        if (utils.shouldShowSplashScreen()) {

            Handler().postDelayed({

                splash_label1.animateText(getString(R.string.splash_label1))

            }, 100)

            Handler().postDelayed({

                splash_label2.animateText(getString(R.string.splash_label2))

            }, 800)

            Handler().postDelayed({

                splash_label3.animateText(getString(R.string.splash_label3))

            }, 1500)

            Handler().postDelayed({

                splash_label4.animateText(getString(R.string.splash_label4))

            }, 2200)


            Handler().postDelayed({

                prefs.storeSplashDay()
                startActivity()

            }, 2600)

        } else
            startActivity()

    }

    private fun startActivity(){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}