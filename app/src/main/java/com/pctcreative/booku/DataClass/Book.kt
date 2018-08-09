package com.pctcreative.booku.DataClass

/**
 * Created by Admin on 07/08/2018.
 */
class Book(val Title : String,val Description : String,val Status : String, val Url : String) {
    constructor() : this("","","","")
}