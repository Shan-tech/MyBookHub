package com.shan.mybookhub.activity.fragment

import android.app.Activity
import android.app.AlertDialog
import com.shan.mybookhub.activity.adapter.DashboardRecyclerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shan.mybookhub.R
import com.shan.mybookhub.activity.modle.Book
import com.shan.mybookhub.activity.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard:RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
   // lateinit var actionSort:Menu

    val bookInfoList= arrayListOf<Book>()
    var ratingComparator= Comparator<Book>{book1,book2 ->
        if (book1.bookRating==book2.bookRating){
            book1.bookName.compareTo(book2.bookName,true)
        }else{
            book1.bookRating.compareTo(book2.bookRating,true)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       val view= inflater.inflate(R.layout.fragment_dashboard, container, false)
        setHasOptionsMenu(true)
        recyclerDashboard=view.findViewById(R.id.recyclerDashboard)
        layoutManager=LinearLayoutManager(activity)
        progressBar=view.findViewById(R.id.progressBar)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressLayout.visibility=View.VISIBLE
        //actionSort=view.findViewById(R.id.action_sort)
        //Sending JSON requests
        val queue=Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v1/book/fetch_books/"
      if (ConnectionManager().checkConnectivity(activity as Context)){
              val jsonObjectRequest= object : JsonObjectRequest (Request.Method.GET,url, null,
                  Response.Listener{
                      //to handle response
                      // println("Response is $it")
                      try {
                          progressLayout.visibility=View.GONE
                          val success= it.getBoolean("success")
                          if (success){
                              val data =it.getJSONArray("data")
                              for (i in 0 until data.length()){
                                  val bookJSONObject=data.getJSONObject(i)
                                  val bookObject=Book(
                                      bookJSONObject.getString("book_id"),
                                      bookJSONObject.getString("name"),
                                      bookJSONObject.getString("author"),
                                      bookJSONObject.getString("rating"),
                                      bookJSONObject.getString("price"),
                                      bookJSONObject.getString("image")
                                  )
                                  bookInfoList.add(bookObject)
                                  recyclerAdapter= DashboardRecyclerAdapter(activity as Context, bookInfoList)
                                  //as is typecasting i.e changing the dataType of var
                                  recyclerDashboard.adapter=recyclerAdapter
                                  recyclerDashboard.layoutManager=layoutManager
                              }
                          }
                          else{
                              Toast.makeText(activity as Context,"error",Toast.LENGTH_SHORT).show()
                          }

                      } catch (e:JSONException){
                          Toast.makeText(activity as Context,"Some unexpected error has been occurred!!",Toast.LENGTH_LONG )
                              .show()
                      }

                  }, Response.ErrorListener{

                      //to handle errors
                      if (activity!=null){
                          Toast.makeText(activity as Context,"Volley error may occurred!",Toast.LENGTH_LONG)
                              .show()
                      }
                  }) {
                  override fun getHeaders(): MutableMap<String, String> {
                      val headers=HashMap<String,String>()
                      headers["Content-type"]="application/json"
                      headers["token"]="bcd19f3799475e"
                      return headers
                  }
              }
              queue.add(jsonObjectRequest)
          } else{
          val dialog=AlertDialog.Builder(activity as Context)
          dialog.setTitle("Error")
          dialog.setMessage("Internet connection error")
          dialog.setPositiveButton("Open settings"){ text,listner->
              //do
              val settings=Intent(Settings.ACTION_WIRELESS_SETTINGS)
              startActivity(settings)
              activity?.finish()
          }
          dialog.setNegativeButton("Exit"){text,listener->
              //do
              ActivityCompat.finishAffinity(activity as Activity)
          }
          dialog.create()
          dialog.show()
      }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if (id == R.id.action_sort) {
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
            recyclerAdapter.notifyDataSetChanged()
        }
        return super.onOptionsItemSelected(item)
    }
}

