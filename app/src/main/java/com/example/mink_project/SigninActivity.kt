package com.example.mink_project

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SigninActivity : AppCompatActivity() {
    private lateinit var log_edt_email: EditText
    private lateinit var log_edt_password: EditText
    private lateinit var log_btn_log: TextView
    private lateinit var loadingBarEmailPassword: ProgressDialog
    private lateinit var btn_back: ImageView

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadingBarEmailPassword = ProgressDialog(this)

        FirebaseApp.initializeApp(this)

        log_edt_email = findViewById(R.id.edtLogemail)
        log_edt_password = findViewById(R.id.edtLogpassword)
        log_btn_log = findViewById(R.id.btnLog)
        btn_back = findViewById(R.id.btn_back_signin)

        auth = FirebaseAuth.getInstance()

        log_btn_log.setOnClickListener {
            // Show loading bar
            showLoadingBarEmailPassword()

            var email = log_edt_email.text.toString().trim()
            var password = log_edt_password.text.toString().trim()

            //Validate Input
            if (email.isEmpty() || password.isEmpty()) {
                // Hide loading bar
                hideLoadingBarEmailPassword()

                Toast.makeText(this, "One of the inputs is empty", Toast.LENGTH_SHORT).show()
            } else{
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                    // Hide loading bar
                    hideLoadingBarEmailPassword()

                    if (it.isSuccessful){
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        var gotomain = Intent(this, MainActivity::class.java)
                        startActivity(gotomain)
                        finish()
                    } else{
                        Toast.makeText(this, "Login Failed. Kindly check your email or password", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        btn_back.setOnClickListener {
            val intent = Intent(this, WelcomePageActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showLoadingBarEmailPassword() {
        loadingBarEmailPassword.setMessage("Signing in...")
        loadingBarEmailPassword.setCancelable(false)
        loadingBarEmailPassword.show()
    }

    private fun hideLoadingBarEmailPassword() {
        loadingBarEmailPassword.dismiss()
    }


}