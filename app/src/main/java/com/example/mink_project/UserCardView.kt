package com.example.mink_project

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import com.example.mink_project.model.User
import com.squareup.picasso.Picasso

class UserCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val nameTextView: TextView
    private val descriptionTextView: TextView
    private val preferencesTextView: TextView
    private val profileImageView: ImageView
    val radioButton: RadioButton

    init {
        inflate(context, R.layout.user_card, this)
        nameTextView = findViewById(R.id.nameTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        preferencesTextView = findViewById(R.id.preferencesTextView)
        profileImageView = findViewById(R.id.profileImageView)
        radioButton = findViewById(R.id.radioButton)
    }

    fun bind(user: User) {
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
    }
}
