package com.jhm.android.firebasecrud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val id = intent.getStringExtra("id")

        editText_detail_title.setText(title)
        editText_detail_content.setText(content)


        button_detail_delete.setOnClickListener {
            deletePost(id) {
                finish()
            }
        }

        button_detail_update.setOnClickListener {
            updatePost(id) {
                finish()
            }
        }
    }

    private fun deletePost(documentId: String?, onSuccess: () -> Unit) {
        if(documentId == null) { return }
        val db = FirebaseFirestore.getInstance()
        db.collection("POST").document(documentId)
            .delete()
            .addOnSuccessListener {
                Log.d("jhmlog", "DELETE SUCCESS")
                onSuccess()
            }
            .addOnFailureListener {
                Log.w("jhmlog", "DELETE FAILURE: ", it)
            }
    }

    private fun updatePost(documentId: String?, onSuccess: () -> Unit) {
        if(documentId == null) { return }
        val db = FirebaseFirestore.getInstance()
        db.collection("POST").document(documentId)
            .update(mapOf(
                "title" to editText_detail_title.text.toString(),
                "content" to editText_detail_content.text.toString()
            ))
            .addOnSuccessListener {
                Log.d("jhmlog", "UPDATE SUCCESS:")
                onSuccess()
            }
            .addOnFailureListener {
                Log.w("jhmlog", "UPDATE FAILURE: ", it)
            }
    }
}
