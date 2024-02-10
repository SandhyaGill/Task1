package com.example.task1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class BaseAdapterClass(var arrayList: ArrayList<ToDoEntity>): BaseAdapter() {
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return "Any Value"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       var initView = LayoutInflater.from(parent?.context).inflate(R.layout.item_adapter,parent,false)
        var tvDate = initView.findViewById<TextView>(R.id.tvDate)
        var tvTask = initView.findViewById<TextView>(R.id.tvTask)

        tvDate.setText(arrayList[position].id.toString())
        tvTask.setText(arrayList[position].todoItem)

        return initView
    }
}