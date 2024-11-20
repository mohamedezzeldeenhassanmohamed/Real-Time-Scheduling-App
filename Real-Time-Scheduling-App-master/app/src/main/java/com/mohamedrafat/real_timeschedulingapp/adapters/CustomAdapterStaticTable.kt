package com.mohamedrafat.real_timeschedulingapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohamedrafat.real_timeschedulingapp.R
import com.mohamedrafat.real_timeschedulingapp.model.StaticTableTasks
import kotlinx.android.synthetic.main.custom_list_static_table.view.*
import kotlin.collections.ArrayList

class CustomAdapterStaticTable(private val context: Context, private val myList: ArrayList<StaticTableTasks>) :
    RecyclerView.Adapter<CustomAdapterStaticTable.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_list_static_table, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = myList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return myList.size
    }


    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numberTask = itemView.tv_numTask
        val arrivalTime = itemView.tv_arrivalTime
        val executionTime = itemView.tv_executionTime
        val startingDeadline = itemView.tv_startingDeadline


        fun bind(task: StaticTableTasks) {
            numberTask.text = "Task ${task.numTask}"
            arrivalTime.text = "ArrivalTime: ${task.arrivalTime}"
            executionTime.text = "ExecutionTime: ${task.executionTime}"
            startingDeadline.text = "StartingDeadline: ${task.startingDeadline}"
        }

    }

}