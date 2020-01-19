package com.jhm.android.firebasecrud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_post.view.*


class PostAdapter(private val posts: ArrayList<PostData>, private val postClicked: (Int) -> Unit) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

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
        val title = posts[position].title
        val content = posts[position].content

        if(title.isNullOrBlank())
            holder.view.text_post_title.text = "제목없음"
        else
            holder.view.text_post_title.text = posts[position].title

        if(content.isNullOrBlank())
            holder.view.text_post_content.text = "내용없음"
        else
            holder.view.text_post_content.text = posts[position].content

        holder.view.setOnClickListener {
            postClicked(position)
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}
