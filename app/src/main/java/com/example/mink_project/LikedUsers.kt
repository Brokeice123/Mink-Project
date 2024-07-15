package com.example.mink_project

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mink_project.model.User
import com.squareup.picasso.Picasso

class LikedUsers : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var likedUsersAdapter: LikedUsersAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked_users)

        val likedUsers: List<User> = intent.getParcelableArrayListExtra("likedUsers") ?: emptyList()

        recyclerView = findViewById(R.id.recyclerViewLikedUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        likedUsersAdapter = LikedUsersAdapter(likedUsers)
        recyclerView.adapter = likedUsersAdapter

    }
}