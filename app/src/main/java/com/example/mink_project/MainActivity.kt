package com.example.mink_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.mink_project.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private lateinit var userContainer: LinearLayout
    private lateinit var proceedButton: Button
    private lateinit var selectedUsers: MutableList<User>
    private lateinit var allUsers: MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectedUsers = mutableListOf()
        allUsers = mutableListOf()
        userContainer = findViewById(R.id.userContainer)
        proceedButton = findViewById(R.id.proceedButton)
        proceedButton.setOnClickListener { navigateToLikedUsers() }

        fetchUsers()
    }

    private fun fetchUsers() {
        FirebaseFirestore.getInstance().collection("users")
            .get()
            .addOnSuccessListener { documents ->
                val users = documents.map { document -> document.toUser() }
                allUsers.addAll(users)
                displayUsers(users)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to fetch users: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun displayUsers(users: List<User>) {
        for (user in users) {
            val userCardView = layoutInflater.inflate(R.layout.user_card, userContainer, false) as CardView

            val nameTextView: TextView = userCardView.findViewById(R.id.nameTextView)
            val descriptionTextView: TextView = userCardView.findViewById(R.id.descriptionTextView)
            val preferencesTextView: TextView = userCardView.findViewById(R.id.preferencesTextView)
            val profileImageView: ImageView = userCardView.findViewById(R.id.profileImageView)
            val radioButton: RadioButton = userCardView.findViewById(R.id.radioButton)

            nameTextView.text = user.name
            descriptionTextView.text = user.description
            preferencesTextView.text = user.preferences.joinToString(", ")
            if (user.profileImageUrl.isNotEmpty()) {
                Picasso.get()
                    .load(user.profileImageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(profileImageView)
            } else {
                profileImageView.setImageResource(R.drawable.placeholder_image)
            }

            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedUsers.add(user)
                } else {
                    selectedUsers.remove(user)
                }
                updateProceedButton()
            }
            userContainer.addView(userCardView)
        }
    }

    private fun updateProceedButton() {
        proceedButton.text = "Proceed (${selectedUsers.size})"
        proceedButton.isEnabled = selectedUsers.isNotEmpty()
    }

    private fun navigateToLikedUsers() {
        val intent = Intent(this, LikedUsers::class.java).apply {
            putParcelableArrayListExtra("likedUsers", ArrayList(selectedUsers))
        }
        startActivity(intent)
    }

    private fun QueryDocumentSnapshot.toUser(): User {
        return User(
            name = getString("name") ?: "",
            phoneNumber = getString("phoneNumber") ?: "",
            city = getString("city") ?: "",
            description = getString("description") ?: "",
            preferences = get("preferences") as? List<String> ?: emptyList(),
            profileImageUrl = getString("profileImageUrl") ?: ""
        )
    }
}
