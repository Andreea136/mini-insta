package com.university.ip.ui.editor

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.university.ip.R
import com.university.ip.model.Photo


class FiltersAdapter(context: Context, val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<FiltersAdapter.ViewHolder>() {

    //    var selections: MutableList<Photo> = mutableListOf()
    var selectMedia: (filter: String, Int) -> Unit =
        { filter, _ -> print(filter) }
    private val context = context

    private var list: List<String> = arrayListOf()
    private var inflater: LayoutInflater? = LayoutInflater.from(context)

    //getter and setter for the list
    fun getList(): List<String> {
        return list
    }

    fun setMediaList(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }

    //this is where create the inflater used to put items inside recycler
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater!!.inflate(R.layout.filter_item, parent, false)
        return ViewHolder(context, view, selectMedia)
    }

    //bind of the element with view item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(context: Context, view: View, val selectMedia: (String, Int) -> Unit) :
        RecyclerView.ViewHolder(view) {
        //this function should be changed when the view objects are changed
        fun bindView(item: String) {
            val textView: TextView = itemView.findViewById(R.id.filter_name)
            textView.text = item

            //click listener that will be implemented in view
            textView.setOnClickListener {
                selectMedia(item, adapterPosition)
                itemClickListener.onItemClick(item)
            }
        }
    }

    //click listener interface
    interface ItemClickListener {
        fun onItemClick(filter: String)
    }
}