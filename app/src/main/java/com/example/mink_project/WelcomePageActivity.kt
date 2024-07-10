package com.example.mink_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WelcomePageActivity : AppCompatActivity() {
    private lateinit var btn_sign_in: Button
    private lateinit var btn_sign_up: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_sign_in = findViewById(R.id.BtnSignIn)
        btn_sign_up = findViewById(R.id.BtnSignUp)

        btn_sign_in.setOnClickListener {
            var gotosignin = Intent(this, SigninActivity::class.java)
            startActivity(gotosignin)
            finish()
        }

        btn_sign_up.setOnClickListener {
            var gotosignup = Intent(this, SignUpActivity::class.java)
            startActivity(gotosignup)
            finish()
        }
    }
}