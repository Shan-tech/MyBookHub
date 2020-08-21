package com.shan.mybookhub.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shan.mybookhub.R
import com.shan.mybookhub.activity.database.BookEntity
import com.squareup.picasso.Picasso

class FavouritesRecyclerAdapter(val context: Context,val bookList: List<BookEntity>):
    RecyclerView.Adapter<FavouritesRecyclerAdapter.FavouritesViewHolder>() {
    class FavouritesViewHolder(view:View):RecyclerView.ViewHolder(view){
        val imgFavBookImage:ImageView=view.findViewById(R.id.imgFavBookImage)
        val txtFavBookName:TextView=view.findViewById(R.id.txtFavBookTitle)
        val txtFavBookPrice:TextView=view.findViewById(R.id.txtFavBookPrice)
        val txtFavBookRating:TextView=view.findViewById(R.id.txtFavBookRating)
        val txtFavBookAuthor:TextView=view.findViewById(R.id.txtFavBookAuthor)
        val llContent:LinearLayout=view.findViewById(R.id.llFavContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourite_single_row,parent,false)
        return FavouritesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val book=bookList[position]
        holder.txtFavBookName.text=book.bookName
        holder.txtFavBookAuthor.text=book.bookAuthor
        holder.txtFavBookPrice.text=book.bookPrice
        holder.txtFavBookRating.text=book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgFavBookImage)

    }
}