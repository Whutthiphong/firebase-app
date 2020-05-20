package com.example.foodorderapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SplashScreenActivity :  BaseActivity() {
    private val handler: Handler? = null
    private val runnable: Runnable? = null
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen)

        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth
        Thread(Runnable {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
            }

            val currentUser = auth.currentUser
            if(currentUser!=null) {
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this@SplashScreenActivity, SigningActivity::class.java)
                startActivity(intent)
                finish()
            }
        }).start()
    }


    override fun onResume() {
        super.onResume()

        handler?.postDelayed(runnable, 3000)
    }

    override fun onStop() {
        super.onStop()
        handler?.removeCallbacks(runnable)
    }
}
