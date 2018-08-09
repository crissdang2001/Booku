package com.pctcreative.booku

import android.os.Bundle
import android.renderscript.Sampler
import android.support.v7.app.AppCompatActivity
import android.text.format.DateUtils
import android.text.format.DateUtils.DAY_IN_MILLIS
import android.text.format.DateUtils.SECOND_IN_MILLIS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.scanning_result.*

/**
 * Created by Admin on 08/08/2018.
 */
class BarcodeScanningResultActivity : AppCompatActivity() {
    private lateinit var barcode : String
    private lateinit var BookDatabase : DatabaseReference
    private lateinit var UserDatabase : DatabaseReference
    var checkstatus : Boolean = false
    var checkvalidcode : Boolean = false
    var checkUserStatus : Boolean = false


    val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanning_result)

        barcode = intent.getStringExtra("bar_code")
        requireNotNull(barcode) { "no bar_code provided in Intent extras" }
        Barcode.text = barcode

        BookDatabase = FirebaseDatabase.getInstance().getReference("BookList")
        UserDatabase = FirebaseDatabase.getInstance().getReference("OfficialList")

        CheckStatus()
    }

    private fun CheckStatus(){
      UserDatabase.child(mAuth.currentUser!!.uid).child("Status").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                val status = p0.value.toString()
                if(!checkstatus) {
                    if (status == "None") {
                        LendBook()
                    } else {
                        GetBook()
                    }
                    checkstatus = true //stop listener
                }
            }
        })

    }
    private fun LendBook(){
        Permission.text = "No information"
        BookDatabase.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (!checkvalidcode) {
                    if (p0.hasChild(barcode)) {
                        //
                        BookDatabase.child(barcode).child("Title").addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p1: DataSnapshot) {
                                BookTitle.text = p1.value.toString()
                                Day.text = "0"
                                Permission.text = "Now you can take the book"
                                UserDatabase.child(mAuth.currentUser!!.uid).child("Status").setValue(barcode)

                                UserDatabase.child(mAuth.currentUser!!.uid).child("Day").setValue(System.currentTimeMillis())
                                BookDatabase.child(barcode).child("Status").setValue("Lend")
                            }
                        })
                        //
                    }
                    checkvalidcode = true //stop listener
                }
            }
        })


    }
    private fun GetBook(){
        UserDatabase.child(mAuth.currentUser!!.uid).child("Status").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if(!checkUserStatus) {
                    if (barcode == p0.value.toString()) {
                        Accept(barcode)
                    } else {
                        NotAccept(p0.value.toString())
                    }
                    checkUserStatus = true //stop listener
                }
            }
        })
    }
    private fun Accept(prev_code : String){
        BookDatabase.child(prev_code).child("Title").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                BookTitle.text = p0.value.toString()
                Permission.text = "You've returned this book! Thank you :)"
                UserDatabase.child(mAuth.currentUser!!.uid).child("Status").setValue("None")
                BookDatabase.child(prev_code).child("Status").setValue("None")
            }

        })
        UserDatabase.child(mAuth.currentUser!!.uid).child("Day").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                Day.text = p0.value.toString()

            }

        })
        UserDatabase.child(mAuth.currentUser!!.uid).child("Day").setValue(0)

    }
    private fun NotAccept(prev_code : String) {

        BookDatabase.child(prev_code).child("Title").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                BookTitle.text = p0.value.toString()

            }

        })
        UserDatabase.child(mAuth.currentUser!!.uid).child("Day").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                val string = DateUtils.getRelativeTimeSpanString(p0.value.toString().toLong(),System.currentTimeMillis(), DAY_IN_MILLIS)
                Day.text = string
            }

        })

        Permission.text = "You aren't allowed to take other book"
    }
}