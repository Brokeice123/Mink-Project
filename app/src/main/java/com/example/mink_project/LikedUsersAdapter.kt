package com.example.mink_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mink_project.model.User
import com.squareup.picasso.Picasso

class LikedUsersAdapter(private val likedUsers: List<User>) :
    RecyclerView.Adapter<LikedUsersAdapter.LikedUserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_liked_user, parent, false)
        return LikedUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: LikedUserViewHolder, position: Int) {
        val user = likedUsers[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return likedUsers.size
    }

    class LikedUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
