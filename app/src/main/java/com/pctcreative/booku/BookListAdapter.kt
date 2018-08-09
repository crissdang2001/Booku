package com.pctcreative.booku

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.pctcreative.booku.DataClass.Book
import com.squareup.picasso.Picasso

/**
 * Created by Admin on 07/08/2018.
 */
class BookListAdapter(var context : Context, var bookArray : ArrayList<Book>) : BaseAdapter() {
    class ViewHolder(row : View){
        var title : TextView
        var descrip : TextView
        var status : TextView
        var image : ImageView
        init {
            title = row.findViewById<TextView>(R.id.Title)
            descrip = row.findViewById<TextView>(R.id.Description)
            status = row.findViewById<TextView>(R.id.Status)
            image = row.findViewById<ImageView>(R.id.imageView)
        }
    }
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view : View?
        var viewholder : ViewHolder
        if(p1 == null){
            var layoutinflater : LayoutInflater= LayoutInflater.from(context)
            view = layoutinflater.inflate(R.layout.book_line, null)
            viewholder = ViewHolder(view!!)
            view.tag = viewholder

        } else {
            view = p1
            viewholder = p1.tag as ViewHolder
        }
        var book : Book = getItem(p0) as Book
        viewholder.title.text = book.Title
        viewholder.descrip.text = book.Description
        viewholder.status.text = book.Status
        Picasso.with(context).load(book.Url).into(viewholder.image)

        return view
    }

    override fun getItem(p0: Int): Any {
        return bookArray.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
       return bookArray.size
    }
}