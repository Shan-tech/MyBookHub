package com.shan.mybookhub.activity.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.shan.mybookhub.R
import com.shan.mybookhub.activity.activity.DescriptionActivity
import com.shan.mybookhub.activity.modle.Book
import com.squareup.picasso.Picasso



class DashboardRecyclerAdapter (val context: Context, val itemList:ArrayList<Book>):
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){
//responsible for creating the initial 10 viewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return DashboardViewHolder(view)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.bookName.text =book.bookName
        holder.bookAuthor.text=book.bookAuthor
        holder.bookRatings.text=book.bookRating
        holder.bookPrice.text=book.bookPrice
       // holder.bookImage.setImageResource(book.bookImage)
       // Picasso.get().load(book.bookImage).into(holder.BookImage)  // Picasso-TO get img
       Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.bookImage) //toSetDefaultCoverInCaseOfPoorConnection

        holder.llContent.setOnClickListener{
            val intent=Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
        }
    }
    class DashboardViewHolder(view: View):RecyclerView.ViewHolder(view){

        val bookName: TextView =view.findViewById(R.id.txtBookName)
        val bookAuthor:TextView=view.findViewById(R.id.txtAuthor)
        val bookRatings:TextView=view.findViewById(R.id.txtRatings)
        val bookPrice:TextView=view.findViewById(R.id.txtPrice)
        val bookImage:ImageView=view.findViewById(R.id.imgBookImage)
        val llContent:LinearLayout=view.findViewById(R.id.llContent)
    }
}


