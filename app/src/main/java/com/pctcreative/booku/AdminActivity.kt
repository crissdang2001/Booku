package com.pctcreative.booku

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.admin_layout.*
import android.widget.AdapterView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pctcreative.booku.DataClass.User


/**
 * Created by Admin on 05/08/2018.
 */
class AdminActivity: AppCompatActivity() {
    var database : DatabaseReference
    var userDatabase : DatabaseReference
    var BookDatabase : DatabaseReference
    var waitingList : ArrayList<String>
    var userList : ArrayList<User>
    var currentTime : Long
    init{
        database = FirebaseDatabase.getInstance().getReference("WaitingList")
        userDatabase = FirebaseDatabase.getInstance().getReference("OfficialList")
        BookDatabase = FirebaseDatabase.getInstance().getReference("BookList")
        waitingList = ArrayList<String>()
        userList = ArrayList<User>()
        currentTime = System.currentTimeMillis()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_layout)
        //booklist
        booklist.setOnClickListener(View.OnClickListener {
            view ->
            val intent = Intent(this, BookListActivity::class.java)
            startActivity(intent)
        })
        //User list
        database.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val name : String = it.key.toString()
                    waitingList.add(name)
                }
                waitingListview.adapter = ArrayAdapter(this@AdminActivity,android.R.layout.simple_list_item_1, waitingList)
            }
        })
        userDatabase.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                userList.clear()
                p0.children.forEach {
                    val book = it.getValue(User::class.java)
                    userList.add(book!!)
                }

                for(i in userList.size-1 downTo 0){
                    if(!userList.get(i).Day.equals(0.toLong())) {
                        val diff = currentTime - userList.get(i).Day
                        if ( diff >= 3600000) { // 60 mins
                            userList.add(0, userList.get(i))
                            userList.removeAt(i+1)
                        }
                    }
                }
                userListview.adapter = UserListAdapter(this@AdminActivity,userList)
            }

        })



        waitingListview.setOnItemClickListener(object : AdapterView.OnItemClickListener {
             override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val item = adapterView.getItemAtPosition(i) as String
                UserDetail(item)
            }
        })
        userListview.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val item : User = userList.get(i)
                val status = item.Status
                val bundle = Bundle()
                if(status == "None"){
                   bundle.putString("status", status)
                   bundle.putParcelable("selected_user", item)
                    val intent = Intent(this@AdminActivity, UserInfoActivity::class.java)
                    intent.putExtra("user_info", bundle)
                    startActivity(intent)
                }else{
                    BookDatabase.child(status).child("Title").addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                        override fun onDataChange(p0: DataSnapshot) {
                            bundle.putString("status",p0.value.toString())
                           bundle.putParcelable("selected_user", item)
                            val intent = Intent(this@AdminActivity, UserInfoActivity::class.java)
                            intent.putExtra("user_info", bundle)
                            startActivity(intent)
                        }
                    })
                }
            }
        })
    }
    private fun UserDetail(item : String){
        val intent = Intent(this, UserWaitingInfoActivity::class.java)
        intent.putExtra("user_name", item)
        startActivity(intent)
    }
}