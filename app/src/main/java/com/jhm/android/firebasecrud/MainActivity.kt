package com.jhm.android.firebasecrud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val posts = ArrayList<PostData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_main_post.apply {
            this.layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = PostAdapter(posts)
        }

        getPosts()
    }

    private fun getPosts() {
        val db = FirebaseFirestore.getInstance()
        db.collection("POST")
            .get()
            .addOnSuccessListener { result ->
                Log.d("jhmlog", "success: ${result.size()} items")
                for(document in result) {
                    val post = document.data
                    posts.add(PostData(post["title"] as String, post["content"] as String))
                }

                recycler_main_post.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("jhmlog", "failure", exception)
            }
    }
}
