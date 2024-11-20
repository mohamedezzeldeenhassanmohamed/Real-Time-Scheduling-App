package com.mohamedrafat.real_timeschedulingapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohamedrafat.real_timeschedulingapp.R
import com.mohamedrafat.real_timeschedulingapp.model.StaticPriorityTasks
import com.mohamedrafat.real_timeschedulingapp.model.StaticTableTasks
import kotlinx.android.synthetic.main.custom_list_static_priority.view.*
import kotlinx.android.synthetic.main.custom_list_static_table.view.*

class CustomAdapterStaticPriority (private val context: Context, private val myList: ArrayList<StaticPriorityTasks>) :
    RecyclerView.Adapter<CustomAdapterStaticPriority.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_list_static_priority , parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = myList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return myList.size
    }


    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numberTask = itemView.tv_numTaskPriority
        val period = itemView.tv_period
        val processingTime = itemView.tv_ProcessingTime


        fun bind(task: StaticPriorityTasks) {
            numberTask.text = "Task ${task.numTask}"
            period.text = "Period: ${task.period}"
            processingTime.text = "ProcessingTime: ${task.processingTime}"
        }

    }

}