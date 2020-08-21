package com.shan.mybookhub.activity.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import androidx.room.Room
import com.shan.mybookhub.R
import com.shan.mybookhub.activity.adapter.FavouritesRecyclerAdapter
import com.shan.mybookhub.activity.database.BookDatabase
import com.shan.mybookhub.activity.database.BookEntity


class FavouritesFragment : Fragment() {
    lateinit var recyclerFavourite: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var recyclerAdapter: FavouritesRecyclerAdapter
    lateinit var layoutManager:RecyclerView.LayoutManager
    var dbBookList=listOf<BookEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favoutites, container, false)
        recyclerFavourite = view.findViewById(R.id.RecyclerFavourite)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)
        layoutManager = GridLayoutManager(activity as Context, 2)
        dbBookList=RetrieveFavourites(activity as Context).execute().get()

        if (activity!=null){
            progressLayout.visibility=View.GONE
            recyclerAdapter=FavouritesRecyclerAdapter(activity as Context,dbBookList)
            recyclerFavourite.adapter=recyclerAdapter
            recyclerFavourite.layoutManager=layoutManager
        }
        return view
    }
    class RetrieveFavourites(val context:Context):AsyncTask<Void, Void,List<BookEntity>>(){
            override fun doInBackground(vararg params: Void?): List<BookEntity> {
                val db= Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
                return db.bookDao().getAllBooks()
            }
        }
}
