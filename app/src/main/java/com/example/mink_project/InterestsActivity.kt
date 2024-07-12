package com.example.mink_project

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class InterestsActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    private lateinit var profileImage: ImageView
    private lateinit var backgroundImage: ImageView
    private lateinit var firstName: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var email: EditText
    private lateinit var city: EditText
    private lateinit var personalDescription: EditText
    private lateinit var chipGroup: ChipGroup
    private lateinit var saveButton: Button
    private lateinit var selectProfileImageButton: Button
    private lateinit var selectBackgroundImageButton: Button

    private var profileImageUri: Uri? = null
    private var backgroundImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interests)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        profileImage = findViewById(R.id.profileImage)
        backgroundImage = findViewById(R.id.backgroundImage)
        firstName = findViewById(R.id.fullName)
        phoneNumber = findViewById(R.id.phoneNumber)
        email = findViewById(R.id.email)
        city = findViewById(R.id.city)
        personalDescription = findViewById(R.id.personalDescription)
        chipGroup = findViewById(R.id.chipGroup)
        saveButton = findViewById(R.id.saveButton)
        selectProfileImageButton = findViewById(R.id.selectProfileImageButton)
        selectBackgroundImageButton = findViewById(R.id.selectBackgroundImageButton)

        selectProfileImageButton.setOnClickListener {
            selectImage(PROFILE_IMAGE_REQUEST_CODE)
        }

        selectBackgroundImageButton.setOnClickListener {
            selectImage(BACKGROUND_IMAGE_REQUEST_CODE)
        }

        saveButton.setOnClickListener {
            saveProfile()
        }
    }

    private fun selectImage(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                PROFILE_IMAGE_REQUEST_CODE -> {
                    profileImageUri = data.data
                    profileImage.setImageURI(profileImageUri)
                }
                BACKGROUND_IMAGE_REQUEST_CODE -> {
                    backgroundImageUri = data.data
                    backgroundImage.setImageURI(backgroundImageUri)
                }
            }
        }
    }

    private fun saveProfile() {
        val selectedOptions = mutableListOf<String>()

        for (i in 0 until chipGroup.childCount) {
            val chip: Chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedOptions.add(chip.text.toString())
            }
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            uploadImageToStorage(userId, profileImageUri, "profileImage")
            uploadImageToStorage(userId, backgroundImageUri, "backgroundImage")
            saveUserInfoToFirestore(userId, selectedOptions)
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImageToStorage(userId: String, imageUri: Uri?, imageType: String) {
        if (imageUri != null) {
            val storageRef = storage.reference.child("images/$userId/$imageType")
            storageRef.putFile(imageUri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        saveImageUrlToFirestore(userId, imageUrl, imageType)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to upload $imageType", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveImageUrlToFirestore(userId: String, imageUrl: String, imageType: String) {
        firestore.collection("users").document(userId)
            .update(imageType, imageUrl)
            .addOnSuccessListener {
                Toast.makeText(this, "$imageType URL saved to Firestore", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save $imageType URL", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserInfoToFirestore(userId: String, selectedOptions: List<String>) {
        val userPreferences = hashMapOf(
            "firstName" to firstName.text.toString(),
            "phoneNumber" to phoneNumber.text.toString(),
            "email" to email.text.toString(),
            "city" to city.text.toString(),
            "personalDescription" to personalDescription.text.toString(),
            "preferences" to selectedOptions
        )

        firestore.collection("users")
            .document(userId)
            .set(userPreferences)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save user info", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val PROFILE_IMAGE_REQUEST_CODE = 1
        private const val BACKGROUND_IMAGE_REQUEST_CODE = 2
    }
}
