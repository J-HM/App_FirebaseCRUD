package com.jhm.android.firebasecrud

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
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
            this.adapter = PostAdapter(posts) { position ->
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("id", posts[position].id)
                intent.putExtra("title", posts[position].title)
                intent.putExtra("content", posts[position].content)
                startActivity(intent)
            }
        }

        linkPostsToRecycler()
    }

    private fun linkPostsToRecycler() {
        getPosts(
            onSuccess = {updateRecycler(it)},
            onFailure = {}
        )
    }

    private fun getPosts(onSuccess: (QuerySnapshot) -> Unit, onFailure: () -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("POST")
            .orderBy("time")
            .addSnapshotListener { snapshot, exception ->
                exception?.let {
                    Log.w("jhmlog", "GET POST FAILURE: ", it)
                    onFailure()
                    return@addSnapshotListener
                }
                snapshot?.let {
                    Log.w("jhmlog", "GET POST SUCCESS: ${snapshot.size()}")
                    onSuccess(it)
                    return@addSnapshotListener
                }
            }
    }

    private fun updateRecycler(snapshot: QuerySnapshot) {
        for (document in snapshot.documentChanges) {
            val title: String? = document.document.data["title"] as String?
            val content: String? = document.document.data["content"] as String?
            val time: Timestamp? = document.document.data["time"] as Timestamp?
            val id: String = document.document.id

            val updatedPost = PostData(title, content, time, id)

            when (document.type) {
                DocumentChange.Type.ADDED -> addPost(updatedPost, document.newIndex)
                DocumentChange.Type.MODIFIED -> {
                    if (document.oldIndex != document.newIndex) {
                        removePost(document.oldIndex)
                        addPost(updatedPost, document.newIndex)
                    } else modifyPost(updatedPost, document.newIndex)
                }
                DocumentChange.Type.REMOVED -> removePost(document.oldIndex)
            }
        }
    }

    private fun addPost(updatedPost: PostData, IndexToBeAdd: Int) {
        posts.add(IndexToBeAdd, updatedPost)
        recycler_main_post.adapter?.notifyItemInserted(IndexToBeAdd)
        recycler_main_post.scrollToPosition(IndexToBeAdd)
    }

    private fun modifyPost(updatedPost: PostData, IndexToBeModify: Int) {
        posts[IndexToBeModify] = updatedPost
        recycler_main_post.adapter?.notifyItemChanged(IndexToBeModify)
        recycler_main_post.scrollToPosition(IndexToBeModify)
    }

    private fun removePost(IndexToBeRemoved: Int) {
        posts.removeAt(IndexToBeRemoved)
        val adapter = recycler_main_post.adapter
        adapter?.notifyItemRemoved(IndexToBeRemoved)
        adapter?.notifyItemRangeChanged(IndexToBeRemoved, posts.size - IndexToBeRemoved)
        recycler_main_post.scrollToPosition(IndexToBeRemoved)
    }

}
