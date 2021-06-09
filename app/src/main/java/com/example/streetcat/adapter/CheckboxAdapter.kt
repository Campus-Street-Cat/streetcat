package com.example.streetcat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streetcat.R
import com.example.streetcat.data.Cat
import com.example.streetcat.viewModel.PostViewModel
import kotlinx.android.synthetic.main.item_checkbox.view.*

// writePost에서 학교의 고양이를 선택하는 checkBox에 대한 어댑터
class CheckboxAdapter(private val cats: ArrayList<Cat>, private val postViewModel: PostViewModel, private val key : String) :
    RecyclerView.Adapter<CheckboxAdapter.ViewHolder>() {

    val selected = ArrayList<Cat>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val catname : TextView = view.cat
        val checkBox : CheckBox = view.check
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_checkbox, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.catname.text = cats[position].name

        viewHolder.checkBox.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{ // checkBox에서 선택 여부에 따라 저장하는 코드
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(isChecked){
                    if(!selected.contains(cats[position])){
                        selected.add(cats[position])
                    }
                }
                else{
                    if(selected.contains(cats[position])){
                        selected.remove(cats[position])
                    }
                }
                notifyDataSetChanged()
            }
        })
        postViewModel.addCats(key, selected)
    }

    override fun getItemCount() = cats.size
}