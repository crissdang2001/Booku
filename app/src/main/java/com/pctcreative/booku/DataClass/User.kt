package com.pctcreative.booku.DataClass

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Admin on 09/08/2018.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class User(val Name : String,val EmailAddress : String,val HomeAddress : String,val Link : String,val PhoneNumber : String,val Password : String,val Status : String,val Day : Long) : Parcelable{
    constructor() : this("","","","","","","",0)
}