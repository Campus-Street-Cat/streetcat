package com.example.streetcat.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.streetcat.adapter.HomeRecyclerViewAdapter
import com.example.streetcat.data.ListCats
import com.example.streetcat.R
import com.example.streetcat.activity.CatAdd
import com.example.streetcat.activity.CatInfo
import com.example.streetcat.viewModel.FbViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private val mainViewModel: FbViewModel by viewModels()
    private lateinit var storage : FirebaseStorage
    private lateinit var storageRef : StorageReference
    private lateinit var database : FirebaseDatabase
    private lateinit var dataRef : DatabaseReference

    val cats = ArrayList<ListCats>()
    var cnt = 0
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        var rootView =  inflater.inflate(R.layout.fragment_home, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_btn.setOnClickListener {
            val intent = Intent(context, CatAdd::class.java)
            startActivity(intent)
        }

        database = FirebaseDatabase.getInstance()
        dataRef = database.getReference().child("cats")
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference.child("폼폼이").child("pictures")

        getData()

    }

    private fun getData()
    {
        val adapter = HomeRecyclerViewAdapter(cats)
        val listAllTask: Task<ListResult> = storageRef.listAll()
        var cats_url = ArrayList<Uri>()

        listAllTask.addOnCompleteListener { result ->
            val items: List<StorageReference> = result.result!!.items
            items.forEachIndexed { index, item ->
                item.downloadUrl.addOnSuccessListener {
                    cats_url.add(it)
                }.addOnCompleteListener {
                    univ_cats_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    univ_cats_view.adapter = HomeRecyclerViewAdapter(cats)
                }
            }
        }

        dataRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(p0: DataSnapshot) {
                //var cats = ArrayList<list_cats>()
                for(data in p0.children){
                    //cats.add(list_cats(Uri.parse(data.child("picture").getValue().toString()), data.child("name").getValue().toString()))
                    cats.add(ListCats(cats_url[cnt++], data.child("name").getValue().toString()))
                }

                //var adapter = RecyclerViewAdapter(cats)
                univ_cats_view.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                univ_cats_view.adapter = adapter

                adapter.setItemClickListener(object : HomeRecyclerViewAdapter.ItemClickListener{
                    override fun onClick(view : View, position : Int){
                        if(position == 0) {
                            val intent = Intent(context, CatInfo::class.java)
                            startActivity(intent)
                        }
                        else if(position == 5){
                            val intent = Intent(context, CatAdd::class.java)
                            startActivity(intent)
                        }
                    }
                })
            }
        })
    }

}