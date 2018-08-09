package com.pctcreative.booku

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val manager = supportFragmentManager


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_signin -> {
                createSigninFrag()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_signup -> {
                createSignupFrag()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//initial fragment
        createSigninFrag()

        //


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
    fun createSigninFrag(){
        val transaction = manager.beginTransaction()
        val fragment = SignInFragment()
        transaction.replace(R.id.fragmentZone,fragment)
        transaction.addToBackStack(null);
        transaction.commit()

    }
    fun createSignupFrag(){
        val transaction = manager.beginTransaction()
        val fragment = SignUpFragment()
        transaction.replace(R.id.fragmentZone,fragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }
}

