package com.example.streetcat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streetcat.R
import com.example.streetcat.data.Cat
import kotlinx.android.synthetic.main.item_post_catname.view.*

class PostCatNameAdapter(private val nameList : ArrayList<Cat>) :
    RecyclerView.Adapter<PostCatNameAdapter.ViewHolder>() {

    interface ItemClickListener{
        fun onClick(view : View, position: Int)
    }

    private lateinit var itemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name : TextView = view.catName
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_post_catname, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = nameList[position].name
        viewHolder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount() = nameList.size
}