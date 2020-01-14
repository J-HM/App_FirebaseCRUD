package com.jhm.android.firebasecrud

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_post.view.*

class PostAdapter(private val posts: ArrayList<PostData>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_post,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.text_post_title.text = posts[position].title
        holder.view.text_post_content.text = posts[position].content
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
