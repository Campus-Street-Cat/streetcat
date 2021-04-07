package com.example.streetcat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streetcat.Fragment.HomeFragment
import com.example.streetcat.databinding.ActivityCatMainBinding.bind
import com.example.streetcat.databinding.TestBinding.bind

class RecyclerViewAdapter(private val catList: ArrayList<list_cats>) :
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val imageView : ImageView

        init {
            textView = view.findViewById(R.id.name)
            imageView = view.findViewById(R.id.image)
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_recycler_view, viewGroup, false)

        return ViewHolder(view)
    }

    private lateinit var itemClickListner: OnItemClickListener

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = catList[position].name
        viewHolder.imageView.setImageResource(catList[position].img)

        viewHolder.itemView.setOnClickListener{
            itemClickListner.onItemClick(it, position)
        }
    }
    override fun getItemCount() = catList.size
}

/*class RecyclerViewAdapter(private val catList: ArrayList<list_cats>), val itemClick: (list_cats) -> Unit) :
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }



    class ViewHolder(view: View, itemClick : (list_cats) -> Unit) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val imageView : ImageView

        init {
            textView = view.findViewById(R.id.name)
            imageView = view.findViewById(R.id.image)
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_recycler_view, viewGroup, false)

        return ViewHolder(view, itemClick)
    }

    private lateinit var itemClickListner: OnItemClickListener

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = catList[position].name
        viewHolder.imageView.setImageResource(catList[position].img)

        viewHolder.itemView.setOnClickListener{
            itemClickListner.onItemClick(it, position)
        }
    }
    override fun getItemCount() = catList.size
}*/