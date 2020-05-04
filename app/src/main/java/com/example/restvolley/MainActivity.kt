package com.example.restvolley

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    var listTaches = arrayListOf<Tache>()
    lateinit var adapter: TacheAdapter
    lateinit var layoutManager : LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter = TacheAdapter(this)
        recyclerView.adapter = adapter



        initData()
    }

    fun initData(){

        val urlCountriesData = "https://jsonplaceholder.typicode.com/posts"
        val jsonRequest = JsonArrayRequest(
            Request.Method.GET, urlCountriesData, null,
            Response.Listener { response ->
                for (i in 0 until response.length()){
                    val item = response.getJSONObject(i)
                    val userId = item.getInt("userId")
                    val id = item.getInt("id")
                    val titre = item.getString("title")
                    val body = item.getString("body")
                    val tache = Tache(id, userId, titre, body)
                    listTaches.add(tache)

                }
                adapter.notifyDataSetChanged()
            },
            Response.ErrorListener { Log.d("Error", "Request error") })
        RequestHandler.getInstance(this).addToRequestQueue(jsonRequest)
    }



    class TacheAdapter(val activity : MainActivity) : RecyclerView.Adapter<TacheAdapter.TacheViewHolder>(){
        class TacheViewHolder(v : View) : RecyclerView.ViewHolder(v){
            val titleTache = v.findViewById<TextView>(R.id.TacheTitle)
            val dateTache = v.findViewById<TextView>(R.id.TacheId)
            val layoutTache = v.findViewById<RelativeLayout>(R.id.itemLayout)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TacheViewHolder {
            return TacheViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_layout, parent, false))
        }

        override fun getItemCount(): Int {
            return activity.listTaches.size
        }

        override fun onBindViewHolder(holder: TacheViewHolder, position: Int) {
            val title = activity.listTaches[position].title
            val tacheId = activity.listTaches[position].id

            holder.titleTache.text = title
            holder.dateTache.text = tacheId.toString()

            holder.layoutTache.setOnClickListener {
                val intent = Intent(activity, TacheActivity::class.java)
                intent.putExtra("tache", activity.listTaches[position])
                activity.startActivity(intent)
            }
        }
    }
}
