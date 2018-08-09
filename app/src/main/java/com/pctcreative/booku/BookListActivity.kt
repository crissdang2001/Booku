package com.pctcreative.booku

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pctcreative.booku.DataClass.Book
import kotlinx.android.synthetic.main.book_list.*


/**
 * Created by Admin on 07/08/2018.
 */
class BookListActivity : AppCompatActivity(){
    var database : DatabaseReference
    var bookArray : ArrayList<Book> = ArrayList()
    val mAuth = FirebaseAuth.getInstance()
    init {
        database = FirebaseDatabase.getInstance().getReference("BookList")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_list)


        database.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                bookArray.clear()
                p0.children.forEach {
                    val book = it.getValue(Book::class.java)
                    bookArray.add(book!!)
                }
                listview.adapter = BookListAdapter(this@BookListActivity,bookArray)
            }

        })
        val adUid = mAuth.currentUser!!.uid
        if(adUid == "JIoAJqBDjIPIPo0SW0TzcuG1n2Y2") {
            Add.setOnClickListener(View.OnClickListener { view ->
                val intent = Intent(this, BookAddingActivity::class.java)
                startActivity(intent)
            })
        }else{
            Add.visibility = View.GONE
        }
    }
}