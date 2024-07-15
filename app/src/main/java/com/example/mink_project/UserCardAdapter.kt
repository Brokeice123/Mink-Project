package com.example.mink_project

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mink_project.model.User
import com.squareup.picasso.Picasso

class UserCardAdapter(
    private var users: List<User>,
    private val onLeftTap: (User) -> Unit,
    private val onRightTap: (User) -> Unit
) : RecyclerView.Adapter<UserCardAdapter.UserViewHolder>() {

    fun updateUsers(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_card, parent, false)
        return UserViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)

        holder.itemView.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val centerX = view.width / 2
                if (event.x < centerX) {
                    onLeftTap(user)
                } else {
                    onRightTap(user)
                }
                true
            } else {
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val phoneTextView: TextView = itemView.findViewById(R.id.phoneTextView)
        private val cityTextView: TextView = itemView.findViewById(R.id.cityTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val preferencesTextView: TextView = itemView.findViewById(R.id.preferencesTextView)
        private val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)

        fun bind(user: User) {
            nameTextView.text = user.name
            phoneTextView.text = user.phoneNumber
            cityTextView.text = user.city
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
}
