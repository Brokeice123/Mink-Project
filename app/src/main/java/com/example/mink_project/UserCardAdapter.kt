package com.example.mink_project

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mink_project.model.User
import com.google.firebase.auth.FirebaseAuth

class UserCardAdapter(val context: Context, val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserCardAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.user_card, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        holder.txtName.text = currentUser.name

        // Generate initials bitmap and set it to the image view
        val initialsBitmap = generateInitialsBitmap(currentUser.name!!)
        holder.imgInitials.setImageBitmap(initialsBitmap)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)
            context.startActivity(intent)
        }
    }

    private fun generateInitialsBitmap(name: String): Bitmap {
        val width = context.resources.getDimensionPixelSize(R.dimen.profile_image_size)
        val height = context.resources.getDimensionPixelSize(R.dimen.profile_image_size)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Draw background circle
        val paint = Paint().apply {
            isAntiAlias = true
            color = ContextCompat.getColor(context, R.color.gradient_start)
        }
        val radius = width / 2.toFloat()
        canvas.drawCircle(radius, radius, radius, paint)

        // Draw text (initials) in the center
        paint.apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = context.resources.getDimensionPixelSize(R.dimen.profile_image_text_size).toFloat()
        }
        val textBounds = Rect()
        paint.getTextBounds(name, 0, 1, textBounds)
        val x = width / 2.toFloat()
        val y = (height + textBounds.height()) / 2.toFloat()
        canvas.drawText(name.substring(0, 1).toUpperCase(), x, y, paint)

        return bitmap
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txt_name)
        val imgInitials: ImageView = itemView.findViewById(R.id.image_initials)
    }
}
