package com.pctcreative.booku

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.format.DateUtils
import android.text.format.DateUtils.DAY_IN_MILLIS
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.user_activity.*

/**
 * Created by Admin on 31/07/2018.
 */
class UserActivity : AppCompatActivity(){
    val mAuth = FirebaseAuth.getInstance()
    lateinit var database : DatabaseReference
    lateinit var Bookdatabase : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity)
        button2.setOnClickListener(View.OnClickListener {
            view ->
            val intent = Intent(this, BarcodeScanningActivity::class.java)
            startActivity(intent)
        })
        button8.setOnClickListener(View.OnClickListener {
            view ->
            val intent = Intent(this, BookListActivity::class.java)
            startActivity(intent)
        })
        database = FirebaseDatabase.getInstance().getReference("OfficialList")
        Bookdatabase = FirebaseDatabase.getInstance().getReference("BookList")

        val currentUser = mAuth!!.currentUser
        textView3.text = currentUser!!.uid

        database.child(currentUser.uid).child("Name").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                textView4.text = p0.getValue().toString()
            }
        })
        database.child(currentUser.uid).child("Status").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {

                if(p0.value.toString() != "None") {
                    Bookdatabase.child(p0.value.toString()).child("Title").addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(p1: DataSnapshot) {
                            Status.text = p1.getValue().toString()
                        }

                    })
                }
            }
        })
        database.child(currentUser.uid).child("Day").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.value.toString() != "0") {
                    val ago = DateUtils.getRelativeTimeSpanString(p0.value.toString().toLong(), System.currentTimeMillis(), MINUTE_IN_MILLIS)
                    val string = "Day Borrowed : " + ago
                    Day.text = string
                }else{
                    Day.text = "0"
                }
            }
        })

    }
}

