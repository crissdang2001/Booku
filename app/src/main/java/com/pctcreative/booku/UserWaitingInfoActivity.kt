package com.pctcreative.booku

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.user_unofficial.*

/**
 * Created by Admin on 06/08/2018.
 */
class UserWaitingInfoActivity : AppCompatActivity() {
    var userName : String =""
    var database : DatabaseReference
    var newdatabase : DatabaseReference
    val mAuth = FirebaseAuth.getInstance();
    init {
        database = FirebaseDatabase.getInstance().getReference("WaitingList")
        newdatabase = FirebaseDatabase.getInstance().getReference("OfficialList")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_unofficial)

        userName = intent.getStringExtra("user_name")
        requireNotNull(userName) { "no user_name provided in Intent extras" }

        //Name
        Name.text = userName
        //phone
        getDB("PhoneNumber", Phone)
        //email
        getDB("EmailAddress",Email)
        //Link
        getDB("Link", Name)
        //HomeAddress
        getDB("HomeAddress",HomeAddress)
        //Password
        database.child(userName).child("PassWord").setValue("123456")
        getDB("PassWord", Password)

        //we dun't have btn 1 and 2
        button3.setOnClickListener(View.OnClickListener {
            view -> EditInfo("PhoneNumber")
        })
        button4.setOnClickListener(View.OnClickListener {
            view -> EditInfo("EmailAddress")
        })
        button5.setOnClickListener(View.OnClickListener {
            view -> EditInfo("Link")
        })
        button6.setOnClickListener(View.OnClickListener {
            view -> EditInfo("HomeAddress")
        })
        button7.setOnClickListener(View.OnClickListener {
            view -> EditInfo("PassWord")
        })
        Accept.setOnClickListener(View.OnClickListener {
            view ->
            mAuth.createUserWithEmailAndPassword(Email.text.toString(),Password.text.toString()).addOnCompleteListener(this, OnCompleteListener { task->
                if(task.isSuccessful)
                {
                    val user = mAuth.currentUser
                    val uid = user!!.uid
                    newdatabase.child(uid).child("PhoneNumber").setValue(Phone.text.toString())
                    newdatabase.child(uid).child("Name").setValue(userName)
                    newdatabase.child(uid).child("Link").setValue(Name.text.toString())
                    newdatabase.child(uid).child("EmailAddress").setValue(Email.text.toString())
                    newdatabase.child(uid).child("HomeAddress").setValue(HomeAddress.text.toString())
                    newdatabase.child(uid).child("Password").setValue(Password.text.toString())
                    newdatabase.child(uid).child("Status").setValue("None")
                    newdatabase.child(uid).child("Day").setValue(0)
                    Toast.makeText(this,"ACCEPTED", Toast.LENGTH_LONG).show()
                    database.child(userName).child("PhoneNumber").setValue(null)
                    database.child(userName).child("EmailAddress").setValue(null)
                    database.child(userName).child("HomeAddress").setValue(null)
                    database.child(userName).child("Link").setValue(null)
                    database.child(userName).child("PassWord").setValue(null)
                    Toast.makeText(this,"Clean user Information", Toast.LENGTH_SHORT).show()
                }else {

                }
            })

        })
        Deny.setOnClickListener(View.OnClickListener {
            view ->
            database.child(userName).child("PhoneNumber").setValue(null)
            database.child(userName).child("EmailAddress").setValue(null)
            database.child(userName).child("HomeAddress").setValue(null)
            database.child(userName).child("Link").setValue(null)
            database.child(userName).child("PassWord").setValue(null)
            Toast.makeText(this,"DENIED", Toast.LENGTH_LONG).show()
        })
    }
    private fun getDB(child : String, textview : TextView){
        database.child(userName).child(child).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                textview.text = p0.getValue().toString()
            }


        })
    }
    private fun EditInfo(info : String){
        editText.visibility = View.VISIBLE
        Confirm.visibility = View.VISIBLE

        Confirm.setOnClickListener(View.OnClickListener {
            view ->
            val text = editText.text.toString()
            database.child(userName).child(info).setValue(text)
            editText.visibility = View.GONE
            Confirm.visibility = View.GONE
        })
    }


}