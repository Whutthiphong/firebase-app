package com.example.foodorderapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapplication.adapters.ListEventAdapter
import com.example.foodorderapplication.databinding.ActivityMainBinding
import com.example.foodorderapplication.interfaces.IOnItemClickListener
import com.example.foodorderapplication.models.Events
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : BaseActivity(), View.OnClickListener, IOnItemClickListener {

    var currentUser: FirebaseUser? = null
    lateinit var eventAdapter: ListEventAdapter
    var listEvent: MutableList<Events> = ArrayList()
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setProgressBar(binding.progressBar)
        binding.btnSignOut.setOnClickListener(this)
        binding.btnNewEvent.setOnClickListener(this)

        database = Firebase.database.reference

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this@MainActivity, SigningActivity::class.java)
            startActivity(intent)
        } else {
            getListEvent()
            updateList()
        }
    }

    private fun updateList() {
        listEvent.clear()
        var rootRef =
            database.child("events").orderByChild("userId").equalTo("${currentUser!!.uid}")
        rootRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val chil = p0.children
                chil.forEach{
//                        var item = it.getValue<Events>()
                    Log.d(TAG,it.getValue().toString())
                    it.getValue<Events>()?.let { it1 -> listEvent.add(it1) }
                }
                eventAdapter.setListItem(listEvent)

            }
        })
    }

    private fun getListEvent() {
        eventAdapter = ListEventAdapter(applicationContext, iOnItemClickListener = this)
        binding.rvListEvent.apply {
            adapter = eventAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.btnSignOut -> signOut()
            R.id.btnNewEvent -> newEvent()

        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }
    override fun onItemCLick(position: Int) {
        super.onItemCLick(position)
    }

    private fun newEvent() {
        val intent = Intent(this@MainActivity, NewEventActivity::class.java).apply {

        }
        startActivity(intent)
    }


    private fun signOut() {
        // Firebase sign out
        auth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressBar()
        if (user != null) {

        } else {

            val intent = Intent(this@MainActivity, SigningActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
