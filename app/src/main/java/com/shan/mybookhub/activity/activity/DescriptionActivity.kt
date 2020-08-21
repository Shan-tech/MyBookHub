package com.shan.mybookhub.activity.activity

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shan.mybookhub.R
import com.shan.mybookhub.activity.database.BookDatabase
import com.shan.mybookhub.activity.database.BookEntity
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject


class DescriptionActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var imgBookImage:ImageView
    lateinit var txtBookName:TextView
    lateinit var txtAuthor:TextView
    lateinit var txtPrice:TextView
    lateinit var txtRatings:TextView
    lateinit var txtDescription:TextView
    lateinit var btnAddFav:Button
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    var bookId:String?="100"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        imgBookImage=findViewById(R.id.imgBookImage)
        txtAuthor=findViewById(R.id.txtAuthor)
        txtBookName=findViewById(R.id.txtBookName)
        txtPrice=findViewById(R.id.txtPrice)
        txtRatings=findViewById(R.id.txtRatings)
        txtDescription=findViewById(R.id.txtDescription)
        btnAddFav=findViewById(R.id.btnAddFav)
        progressBar=findViewById(R.id.progressBar)
        progressBar.visibility= View.VISIBLE
        progressLayout=findViewById(R.id.progressLayout)
        progressLayout.visibility=View.VISIBLE
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"


        if (intent!=null){
            bookId=intent.getStringExtra("book_id")
        }
        else{
            finish()
            Toast.makeText(this@DescriptionActivity,"Sm error has been occurred",Toast.LENGTH_SHORT)
                .show()
        }
        if (bookId=="100"){
            finish()
            Toast.makeText(this@DescriptionActivity,"Sm error has been occurred",Toast.LENGTH_SHORT)
                .show() }
        val queue= Volley.newRequestQueue(this@DescriptionActivity)
        val url="http://13.235.250.119/v1/book/get_book/"
        val jsonParams= JSONObject()
        jsonParams.put("book_id",bookId)
        val jsonRequest= object : JsonObjectRequest(Request.Method.POST,url,jsonParams,
            Response.Listener{
            try {
                val success=it.getBoolean("success")
               if (success){
                   progressLayout.visibility=View.GONE
                   val bookJsonObject=it.getJSONObject("book_data")
                   val imageUrl=bookJsonObject.getString("image")

                   Picasso.get().load(bookJsonObject.getString("image")).error(R.drawable.default_book_cover).into(imgBookImage)
                   txtBookName.text=bookJsonObject.getString("name")
                   txtAuthor.text=bookJsonObject.getString("author")
                   txtPrice.text=bookJsonObject.getString("price")
                   txtRatings.text=bookJsonObject.getString("rating")
                   txtDescription.text=bookJsonObject.getString("description")

                   val bookEntity=BookEntity(
                       bookId?.toInt() as Int,
                       txtBookName.text.toString(),
                       txtAuthor.text.toString(),
                       txtPrice.text.toString(),
                       txtRatings.text.toString(),
                       txtDescription.text.toString(),
                       imageUrl
                   )
                   val checkFav=dbAsync(applicationContext,bookEntity, 1).execute()
                   val isFav=checkFav.get()
                   if (isFav){
                       btnAddFav.text= "Remove from favourites"
                       val favColor=ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                       btnAddFav.setBackgroundColor(favColor)
                   }else{
                       btnAddFav.text= "Add to favourites"
                       val noFavColor=ContextCompat.getColor(applicationContext,R.color.design_default_color_primary)
                       btnAddFav.setBackgroundColor(noFavColor)
                   }
                   btnAddFav.setOnClickListener {
                       if (!dbAsync(applicationContext, bookEntity, 1).execute().get()) {
                          val async= dbAsync(applicationContext, bookEntity, 2).execute()
                           val result=async.get()
                           if (result){
                               Toast.makeText(applicationContext,"Book added to favourites",Toast.LENGTH_SHORT).show()
                               btnAddFav.text= "Remove from favourites"
                               val favColor=ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                               btnAddFav.setBackgroundColor(favColor)
                           }else{
                               Toast.makeText(applicationContext,"Some error has been occurred",Toast.LENGTH_SHORT).show()
                           }
                       }else{
                           val async=dbAsync(applicationContext, bookEntity, 3).execute()
                           val resut=async.get()
                           if (resut){
                               Toast.makeText(applicationContext,"Book removed from favourites",Toast.LENGTH_SHORT).show()
                               btnAddFav.text= "Add to favourites"
                               val noFavColor=ContextCompat.getColor(applicationContext,R.color.design_default_color_primary)
                               btnAddFav.setBackgroundColor(noFavColor)
                           }else{
                               Toast.makeText(applicationContext,"Some error has been occurred",Toast.LENGTH_SHORT).show()

                           }
                       }
                   }

               } else{
                   Toast.makeText(this@DescriptionActivity,"error",Toast.LENGTH_SHORT).show()
               }
           }
            catch (e: JSONException){
               Toast.makeText(this@DescriptionActivity,"Some unexpected error has been occurred !!",Toast.LENGTH_LONG )
                   .show()
           }
            }, Response.ErrorListener{

            //to handle errors
                Toast.makeText(this@DescriptionActivity,"Volley error may occurred!",Toast.LENGTH_LONG)
                    .show()
        })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers =HashMap<String,String>()
                headers["Content-type"]="application/json"
                headers["token"]="bcd19f3799475e"
                return headers
            }
        }
        queue.add(jsonRequest)
    }
    class dbAsync(context:Context,val bookEntity:BookEntity,val mode:Int):AsyncTask<Void,Void,Boolean>(){
       val db=Room.databaseBuilder(context, BookDatabase::class.java,"books-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1->{
                    //Check whether the book is in db r not
                    val book:BookEntity?=db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book!= null
                }
                2->{
                    //to add to fav
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3 ->{
                    //to del frm fav
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
                return false
            }
    }
}





