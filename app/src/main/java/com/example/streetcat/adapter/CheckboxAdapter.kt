package com.example.streetcat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streetcat.R
import kotlinx.android.synthetic.main.item_checkbox.view.*

class CheckboxAdapter(private val cats: ArrayList<String>) :
    RecyclerView.Adapter<CheckboxAdapter.ViewHolder>() {

//    private var ck = 0
//    private var checkboxList = ArrayList<checkboxData>()
//
//
//    inner class Holder(view: View?) : RecyclerView.ViewHolder(view!!) {
//        //var thumbnail: ImageView = itemView!!.findViewById<ImageView>(R.id.thumbnail_img)
//        var checkbox: CheckBox = view!!.check
//
//        fun bind(data: String, num: Int) {
//
////            if (ck == 1) {
////                checkbox.visibility = View.VISIBLE
////            } else
////                checkbox.visibility = View.GONE
//
//
//            checkbox.isChecked = checkboxList[num].checked
//            checkboxList[num].catname = data
//
//            checkbox.setOnClickListener {
//                checkboxList[num].checked = checkbox.isChecked
//            }
//        }
//
//    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val catname : TextView = view.cat
        val checkBox : CheckBox = view.check
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_checkbox, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.catname.text = cats[position]
        //viewHolder.bind(cats[position], position)
    }

    override fun getItemCount() = cats.size

//    class checkboxData(
//        var catname : String,
//        var checked : Boolean
//    )

//    companion object { // 체크박스 처리하는 부분
//        class ItemHolder(var binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root),
//            CompoundButton.OnCheckedChangeListener {
//            var item: ItemModel.ItemEntity? = null
//
//            init {
//                binding.checkBox.setOnCheckedChangeListener(this)
//            }
//
//            fun bind(item: ItemModel.ItemEntity?) {
//                item?.let {
//                    this.item = item
//                    binding.checkBox.isChecked = it.isChecked
//                    binding.textTitle.text = it.title
//                    binding.textContents.text = it.contents
//                }
//            }
//
//
//            override fun onCheckedChanged(
//                buttonView: CompoundButton?,
//                isChecked: Boolean
//            ) { //체크 리스너 등록
//                item?.let {
//                    it.isChecked = it.isChecked.not()
//                    Log.d("checkState", "${it.isChecked}")
//                }
//            }
//        }
//    }
}