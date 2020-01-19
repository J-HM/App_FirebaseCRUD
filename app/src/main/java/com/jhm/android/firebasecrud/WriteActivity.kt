package com.jhm.android.firebasecrud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_write.*
import com.google.firebase.Timestamp


class WriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        button_write_write.setOnClickListener {
            val newPost = getPostDataFromActivity()
            postPost(newPost,
                onSuccess = { finish() }
            )
        }
        editText_write_content
    }

    private fun getPostDataFromActivity(): PostData {
        val title = editText_write_title.text.toString()
        val content = editText_write_content.text.toString()
        val time = Timestamp.now()
        return PostData(title, content, time)
    }

    private fun postPost(newPost: PostData, onSuccess: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("POST").add(newPost)
            .addOnSuccessListener { documentReference ->
                Log.d("jhmlog", "POST POST SUCCESS: ${documentReference.id}")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.w("jhmlog", "POST POST FAILURE:", exception)
            }
    }

}
