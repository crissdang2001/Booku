package com.pctcreative.booku

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.pctcreative.booku.DataClass.User
import kotlinx.android.synthetic.main.user_official.*

/**
 * Created by Admin on 09/08/2018.
 */
class UserInfoActivity : AppCompatActivity(){
    lateinit var user : User
    lateinit var title : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_official)

        val bundle = intent.getBundleExtra("user_info")
        user = bundle.getParcelable<User>("selected_user") as User
        title = bundle.getString("status")

       Name.text = user.Name
        Email.text = user.EmailAddress
        Home.text = user.HomeAddress
       Link.text = user.Link
        Phone.text = user.PhoneNumber
       Status.text = title
    }
}