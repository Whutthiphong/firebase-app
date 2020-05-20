package com.example.foodorderapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.foodorderapplication.databinding.ActivityNewEventBinding
import com.example.foodorderapplication.models.Events
import com.example.foodorderapplication.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class NewEventActivity : BaseActivity(), View.OnClickListener {

    // [START declare_database_ref]
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityNewEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewEventBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setProgressBar(binding.progressBar!!)


        database = Firebase.database.reference

        binding.saveEvent.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.saveEvent -> {
                saveNewEvent()
            }
        }
    }

    private fun saveNewEvent() {
        val title = binding.tvTitle.text.toString()
        val desc = binding.tvDescription.text.toString()
        val userId = uid
        database.child("users").child(userId).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get user value
                    val user = dataSnapshot.getValue<User>()

                    if (user == null) {
                        // User is null, error out
                        Log.e(TAG, "User $userId is unexpectedly null")
                        Toast.makeText(
                            baseContext,
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        writeNewEvent(userId, title, desc)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "getUser:onCancelled", databaseError.toException())
                }
            })
        // [END single_value_read]
    }

    private fun writeNewEvent(userId: String, title: String?, desc: String?) {
        val eventID = UUID.randomUUID().toString()
        var event = Events(userId = userId, title = title!!, description = desc!!, eventId = eventID)
        database.child("events").child(eventID).setValue(event)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Save Success ", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {

                Toast.makeText(applicationContext, "Save Failed ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    companion object {
        private const val TAG = "NewEventActivity"
    }
}
