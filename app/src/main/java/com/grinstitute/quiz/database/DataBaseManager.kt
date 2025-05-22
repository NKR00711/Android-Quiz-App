package com.grinstitute.quiz.database

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataBaseManager() {
    private val database = FirebaseDatabase.getInstance()

    init {
        database.setPersistenceEnabled(true)
    }

    fun getSnapShot(context: Context, dbName: String,callback: (DataSnapshot?) -> Unit) {
        database.getReference(dbName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                callback(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                callback(null)
            }
        })
    }

}