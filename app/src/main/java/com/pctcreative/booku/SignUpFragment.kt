package com.pctcreative.booku

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.signup_fragment.*

/**
 * Created by Admin on 31/07/2018.
 */
class SignUpFragment : Fragment() {
    lateinit var mDatabase : DatabaseReference
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.signup_fragment, container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDatabase = FirebaseDatabase.getInstance().getReference("WaitingList")
        button.setOnClickListener(View.OnClickListener {
            view -> SignUp()
        })
    }
    private fun SignUp() {
        var name =  Name.text.toString()
        var phone = PhoneNumber.text.toString()
        var home = Address.text.toString()
        var email = MailAdress.text.toString()
        var link = FbLink.text.toString()
        if(email.isEmpty() || link.isEmpty() || name.isEmpty() || phone.isEmpty() || home.isEmpty()) {
            Toast.makeText(activity,"Please fill all", Toast.LENGTH_SHORT).show()
        }
        else {
            mDatabase.child(name).child("PhoneNumber").setValue(phone)
            mDatabase.child(name).child("HomeAddress").setValue(home)
            mDatabase.child(name).child("EmailAddress").setValue(email)
            mDatabase.child(name).child("Link").setValue(link)
            Toast.makeText(activity,"Submit Success! Wait for Admin to accept your request", Toast.LENGTH_LONG).show()
        }
    }
}
