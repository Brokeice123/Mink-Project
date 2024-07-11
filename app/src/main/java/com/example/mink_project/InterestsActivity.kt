package com.example.mink_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InterestsActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interests)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val chipGroup: ChipGroup = findViewById(R.id.chipGroup)
        val saveButton: Button = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            Log.d("InterestsActivity", "Save button clicked")
            val selectedOptions = mutableListOf<String>()

            for (i in 0 until chipGroup.childCount) {
                val chip: Chip = chipGroup.getChildAt(i) as Chip
                if (chip.isChecked) {
                    selectedOptions.add(chip.text.toString())
                }
            }

            val currentUser = auth.currentUser
            if (currentUser != null) {
                saveSelectedOptionsToFirestore(currentUser.uid, selectedOptions)
            } else {
                Log.e("InterestsActivity", "User not authenticated")
                // Handle the case when the user is not authenticated
            }
        }
    }

    private fun saveSelectedOptionsToFirestore(userId: String, selectedOptions: List<String>) {
        Log.d("InterestsActivity", "Saving selected options to Firestore: $selectedOptions")
        val userPreferences = hashMapOf(
            "preferences" to selectedOptions
        )

        firestore.collection("users")
            .document(userId)
            .set(userPreferences)
            .addOnSuccessListener {
                Log.d("InterestsActivity", "Preferences saved successfully")
                val gotoMain = Intent(this, MainActivity::class.java)
                startActivity(gotoMain)
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("InterestsActivity", "Error saving preferences", e)
                // Handle failure
            }
    }
}
