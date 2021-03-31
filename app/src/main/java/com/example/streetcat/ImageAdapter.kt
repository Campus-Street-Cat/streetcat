package com.example.streetcat

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class ImageAdapter(private val context: Context) : BaseAdapter() {

    private val images = arrayOf( // 여기를 DB랑 연동해서 사진을 가져오면 되는건가
            R.drawable.p1, R.drawable.p2, R.drawable.p3,
            R.drawable.p4, R.drawable.pompom1, R.drawable.p6,
            R.drawable.p1, R.drawable.p2, R.drawable.p3,
            R.drawable.p4, R.drawable.pompom1, R.drawable.p6,)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView:ImageView

        if(convertView == null){
            imageView = ImageView(context)
            imageView.run{
                layoutParams = ViewGroup.LayoutParams(350,350)
                scaleType = ImageView.ScaleType.CENTER_CROP
                setPadding(10,10,10,10)
            }
        }else{
            imageView = convertView as ImageView
        }

        imageView.setImageResource(images[position])
        return imageView
    }

    override fun getItem(position: Int): Any = images[position]

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = images.size
}