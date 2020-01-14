package com.jhm.android.firebasecrud.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.jhm.android.firebasecrud.PostAdapter
import com.jhm.android.firebasecrud.PostData
import com.jhm.android.firebasecrud.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val posts = ArrayList<PostData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_main_write.setOnClickListener {
            val intent = Intent(this, WriteActivity::class.java)
            startActivity(intent)
        }

        recycler_main_post.apply {
            this.layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = PostAdapter(posts)
        }
        connectPostsWithRecyclerView()
    }

    private fun connectPostsWithRecyclerView() {
        getPosts(
            onSuccess = { snapshot ->
                updateRecyclerView(snapshot)
            },
            onFailure = {

            }
        )
    }

    private fun getPosts(onSuccess: (QuerySnapshot) -> Unit, onFailure: () -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("POST")
            .orderBy("time")
            .addSnapshotListener { snapshot, exception ->
                exception?.let {
                    Log.w("jhmlog", "GET POST SUCCESS: ", it)
                    onFailure()
                    return@addSnapshotListener
                }
                snapshot?.let {
                    Log.d("jhmlog", "GET POST SUCCESS: ${it.size()} items")
                    onSuccess(it)
                    return@addSnapshotListener
                }

            }
    }

    private fun updateRecyclerView(snapshot: QuerySnapshot) {
        for (document in snapshot.documentChanges) {
            val title: String? = document.document.data["title"] as String?
            val content: String? = document.document.data["content"] as String?
            val updatedPost = PostData(title, content)

            when (document.type) {
                DocumentChange.Type.ADDED -> {
                    posts.add(updatedPost)
                    recycler_main_post.adapter?.notifyItemInserted(document.newIndex)
                }
                DocumentChange.Type.MODIFIED -> {
                    posts[document.newIndex] = updatedPost
                    recycler_main_post.adapter?.notifyItemChanged(document.newIndex)
                }
                DocumentChange.Type.REMOVED -> {
                    posts.removeAt(document.oldIndex)
                    recycler_main_post.adapter?.notifyItemRemoved(document.oldIndex)
                }
            }
        }
    }
}
