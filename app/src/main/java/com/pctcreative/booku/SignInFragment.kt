package com.pctcreative.booku

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.signin_fragment.*

/**
 * Created by Admin on 31/07/2018.
 */
class SignInFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.signin_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button.setOnClickListener(View.OnClickListener {
            view -> SignIn()
        })
    }

    private fun SignIn(){
        var email = Email.text.toString()
        var pass = Password.text.toString()

        if(!email.isEmpty() && !pass.isEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pass).addOnCompleteListener(activity, OnCompleteListener{ task ->
                if(task.isSuccessful)
                {
                    if(email == "tkien2703@gmail.com" && pass == "270302") {
                        Toast.makeText(activity, "Welcome Admin :)", Toast.LENGTH_LONG).show()
                        val intent = Intent(activity,AdminActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(activity, "Welcome Boi", Toast.LENGTH_LONG).show()
                        val intent = Intent(activity, UserActivity::class.java)
                        startActivity(intent)
                    }
                }else {
                    Toast.makeText(activity,"Fuck out",Toast.LENGTH_LONG).show()
                }
            })
        }
        else {
         Toast.makeText(activity,"Please fill up",Toast.LENGTH_SHORT).show()
        }
    }
}