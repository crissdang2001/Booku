package com.pctcreative.booku

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.book_adding.*
import java.io.IOException
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener



/**
 * Created by Admin on 07/08/2018.
 */
class BookAddingActivity : AppCompatActivity(), View.OnClickListener {
    private val PICK_IMAGE_REQUEST = 123


    private var filePath: Uri? = null

    internal var storage:FirebaseStorage?=null
    internal var storageReference:StorageReference?=null
    internal var database:DatabaseReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_adding)

        //init Firebase
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        database = FirebaseDatabase.getInstance().getReference("BookList")

        UploadImg.setOnClickListener(this)
        Submit.setOnClickListener(this)

    }
    override fun onClick(p0: View) {
        if(p0 === UploadImg)
            showFileChooser()
        else if(p0 === Submit)
            Upload()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data!=null){
            filePath = data.data
            try{
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filePath)
                Thumbnail!!.setImageBitmap(bitmap)
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }
    private fun Upload() {
        val title = Title.text.toString()
        val des = Description.text.toString()
        val code = Barcode.text.toString()
        if(!title.isEmpty() && !des.isEmpty() && !code.isEmpty()) {
            if (filePath != null) {
                val imageRef = storageReference!!.child("images/" + code)
                imageRef.putFile(filePath!!)
                        .addOnSuccessListener { taskSnapshot ->
                            database?.child(code)?.child("Title")?.setValue(title)
                            database?.child(code)?.child("Description")?.setValue(des)
                            database?.child(code)?.child("Status")?.setValue("None")
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                database?.child(code)?.child("Url")?.setValue(uri.toString())

                                Toast.makeText(this, "Submit Successfully", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to Submit", Toast.LENGTH_SHORT).show()
                        }

            }
        }else{
            Toast.makeText(this,"Please fill all",Toast.LENGTH_SHORT).show()
        }
    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"SELECT A PICTURE"),PICK_IMAGE_REQUEST)
    }
}