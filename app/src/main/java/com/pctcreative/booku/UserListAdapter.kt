package com.pctcreative.booku

import android.content.Context
import android.graphics.Typeface
import android.text.format.DateUtils
import android.text.format.DateUtils.DAY_IN_MILLIS
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.pctcreative.booku.DataClass.User

/**
 * Created by Admin on 09/08/2018.
 */
class UserListAdapter(var context : Context, var userArray : ArrayList<User>) : BaseAdapter(){
    class ViewHolder(row : View){
        var Name : TextView
        var Status : TextView
        var currentTime : Long
        init {
            Name = row.findViewById<TextView>(R.id.Name)
            Status = row.findViewById<TextView>(R.id.Status)
            currentTime = System.currentTimeMillis()
        }
    }
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view : View?
        var viewholder : ViewHolder
        if(p1 == null){
            var layoutinflater : LayoutInflater = LayoutInflater.from(context)
            view = layoutinflater.inflate(R.layout.user_line, null)
            viewholder = ViewHolder(view!!)
            view.tag = viewholder

        } else {
            view = p1
            viewholder = p1.tag as ViewHolder
        }
        val user : User = getItem(p0) as User
        viewholder.Name.text = user.Name
        viewholder.Status.text = "0"
        val starttime  = user.Day
        if(starttime.equals(0.toLong()))
        {

        }else {
            val ago = DateUtils.getRelativeTimeSpanString(starttime, viewholder.currentTime, MINUTE_IN_MILLIS)
            viewholder.Status.text = ago
            val diff = viewholder.currentTime - starttime
            if (diff >= 3600000){
                viewholder.Status.typeface = Typeface.DEFAULT_BOLD
            }
        }
        return view
    }

    override fun getItem(p0: Int): Any {
        return userArray.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return userArray.size
    }

}