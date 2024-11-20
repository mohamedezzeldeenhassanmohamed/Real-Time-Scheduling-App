package com.mohamedrafat.real_timeschedulingapp.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohamedrafat.real_timeschedulingapp.R
import com.mohamedrafat.real_timeschedulingapp.adapters.CustomAdapterStaticPriority
import com.mohamedrafat.real_timeschedulingapp.adapters.CustomAdapterStaticTable
import com.mohamedrafat.real_timeschedulingapp.databinding.FragmentStaticTableInputsBinding
import com.mohamedrafat.real_timeschedulingapp.model.AlgorithmRMS
import com.mohamedrafat.real_timeschedulingapp.model.StaticPriorityTasks
import com.mohamedrafat.real_timeschedulingapp.model.StaticTableTasks
import kotlinx.android.synthetic.main.custom_add_task_static_priority.view.*
import kotlinx.android.synthetic.main.custom_add_task_static_table.view.*
import kotlin.math.pow


class StaticTableInputsFragment : Fragment() {
    private lateinit var mNavController: NavController
    private var _binding: FragmentStaticTableInputsBinding? = null
    private val binding get() = _binding!!
    private lateinit var algorithmName:String

    private lateinit var listTableTasks: ArrayList<StaticTableTasks>
    private lateinit var sortedArrivalTime: ArrayList<StaticTableTasks>
    private lateinit var sortedStartingDeadline: ArrayList<StaticTableTasks>

    private var outputEarliestDeadline:String = ""
    private var outputEarliestDeadlineUnforced:String = ""
    private var outputFirstComeFirstServed:String = ""


    private lateinit var listPriorityTasks: ArrayList<StaticPriorityTasks>


    companion object {
        var numberTasks: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStaticTableInputsBinding.inflate(inflater, container, false)

        algorithmName = arguments?.getString("algorithmName")!!
        
        binding.fabAddTask.setOnClickListener {
            showDialodAddNotes()
        }


        listTableTasks = ArrayList()
        listPriorityTasks = ArrayList()


        if(algorithmName == "table"){
            val adapter = CustomAdapterStaticTable(requireContext() , listTableTasks)
            binding.rvTasks.layoutManager = LinearLayoutManager(activity)
            binding.rvTasks.adapter = adapter
            binding.rvTasks.setHasFixedSize(true)
        }else{
            val adapter = CustomAdapterStaticPriority(requireContext() , listPriorityTasks)
            binding.rvTasks.layoutManager = LinearLayoutManager(activity)
            binding.rvTasks.adapter = adapter
            binding.rvTasks.setHasFixedSize(true)
        }


        binding.fabNext.setOnClickListener {
            if(algorithmName == "table"){
                if(listTableTasks.size >= 2){
                    earliestDeadline()
                    earliestDeadlineUnforced()
                    firstComeFirstServed()

                    numberTasks = 0

                    val action = StaticTableInputsFragmentDirections.actionStaticTableInputsFragmentToShowOutputStaticTableFragment(outputEarliestDeadline , outputEarliestDeadlineUnforced , outputFirstComeFirstServed)
                    mNavController.navigate(action)
                }else{
                    Toast.makeText(activity, "please enter number task greater than one", Toast.LENGTH_SHORT).show()
                }
            }else{
                rateMonotonicScheduling()
            }

        }

        return binding.root
    }


    private fun earliestDeadline() {
            var timeLine = listTableTasks.maxBy { it.executionTime }.executionTime + listTableTasks.maxBy { it.startingDeadline }.startingDeadline

            var temp = listTableTasks.sortedWith(compareBy { it.arrivalTime })
            sortedArrivalTime = ArrayList()

            for (i in temp) {
                sortedArrivalTime.add(i)
            }

            var currentPoint = sortedArrivalTime[0].arrivalTime
            val currentArrivalTasks:ArrayList<StaticTableTasks> = ArrayList()

            if(currentPoint > 0){
                outputEarliestDeadline += "Delay IS : $currentPoint -> [ 0 : $currentPoint ] \n"
//                Toast.makeText(activity, "Delay IS : $currentPoint" , Toast.LENGTH_SHORT).show()
            }

            var size = 0
            while(currentPoint < timeLine){
                size = sortedArrivalTime.size

                for (task in 0 until size){
                    if (currentPoint > sortedArrivalTime[task].startingDeadline.toInt()) {
                        outputEarliestDeadline += "Task: "+sortedArrivalTime[task].numTask+" Is Missed At [ ${sortedArrivalTime[task].startingDeadline} ] \n"
//                        Toast.makeText(activity, "task"+sortedArrivalTime[task].numTask+" is miss" , Toast.LENGTH_SHORT).show()
                        sortedArrivalTime.removeAt(task)
                        break
                    }else if (currentPoint >= sortedArrivalTime[task].arrivalTime){
                            currentArrivalTasks.add(sortedArrivalTime[task])

                    }else if(currentPoint < sortedArrivalTime[task].arrivalTime && currentArrivalTasks.size == 0){
                        outputEarliestDeadline += "Delay Is : ${sortedArrivalTime[task].arrivalTime - currentPoint} -> [ $currentPoint : ${sortedArrivalTime[task].arrivalTime} ] \n"
//                        Toast.makeText(activity, "Delay Is : ${sortedArrivalTime[task].arrivalTime - currentPoint}", Toast.LENGTH_SHORT).show()
                        currentPoint = sortedArrivalTime[task].arrivalTime
                        break
                    }
                }

                if (currentArrivalTasks.size == 0)
                    continue
                else{
                    val sortedTasks = currentArrivalTasks.sortedWith(compareBy { it.startingDeadline })

                    outputEarliestDeadline += "Task: "+sortedTasks[0].numTask+" Is Completed -> [ $currentPoint : ${currentPoint+sortedTasks[0].executionTime} ] \n"
//                    Toast.makeText(activity, "Task"+sortedTasks[0].numTask+" Is complete", Toast.LENGTH_SHORT).show()

                    currentPoint += sortedTasks[0].executionTime

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        sortedArrivalTime.removeIf { it ->  it.numTask == sortedTasks[0].numTask}
                    }

                }

                if(currentArrivalTasks.size > 0)
                    currentArrivalTasks.clear()

                if(sortedArrivalTime.size == 0){
                    break
                }
            }
    }
    
    private fun earliestDeadlineUnforced(){
            var timeLine = listTableTasks.maxBy { it.executionTime }.executionTime + listTableTasks.maxBy { it.startingDeadline }.startingDeadline

            var temp = listTableTasks.sortedWith(compareBy { it.startingDeadline })
            sortedStartingDeadline = ArrayList()

            for (i in temp) {
                sortedStartingDeadline.add(i)
            }


            var currentPoint = sortedStartingDeadline[0].arrivalTime
            val currentArrivalTasks:ArrayList<StaticTableTasks> = ArrayList()

            if(currentPoint > 0){
                outputEarliestDeadlineUnforced += "Delay IS : $currentPoint -> [ 0 : $currentPoint ] \n"
//                Toast.makeText(activity, "Delay IS : $currentPoint" , Toast.LENGTH_SHORT).show()
            }

            var size = 0
            while(currentPoint < timeLine){
                size = sortedStartingDeadline.size

                for (task in 0 until size){
                    if (currentPoint > sortedStartingDeadline[task].startingDeadline.toInt()) {
                        outputEarliestDeadlineUnforced += "Task: "+sortedStartingDeadline[task].numTask+" Is Missed At [ ${sortedStartingDeadline[task].startingDeadline} ]  \n"
//                        Toast.makeText(activity, "task"+sortedStartingDeadline[task].numTask+" is miss" , Toast.LENGTH_SHORT).show()
                        sortedStartingDeadline.removeAt(task)
                        break
                    }else if(currentPoint < sortedStartingDeadline[task].arrivalTime && currentArrivalTasks.size == 0){
                        outputEarliestDeadlineUnforced += "Delay Is : ${sortedStartingDeadline[task].arrivalTime - currentPoint} -> [ $currentPoint : ${sortedStartingDeadline[task].arrivalTime} ] \n"
//                        Toast.makeText(activity, "Delay Is : ${sortedStartingDeadline[task].arrivalTime - currentPoint}", Toast.LENGTH_SHORT).show()
                        currentPoint = sortedStartingDeadline[task].arrivalTime
                        break
                    } else if ( task < size-1 && sortedStartingDeadline[task].startingDeadline == sortedStartingDeadline[task+1].startingDeadline ){
                        currentArrivalTasks.add(sortedStartingDeadline[task])
                    }else if(size>1 && task == size-1 && sortedStartingDeadline[task].startingDeadline == sortedStartingDeadline[task-1].startingDeadline){
                        currentArrivalTasks.add(sortedStartingDeadline[task])
                        break
                    } else{

                        outputEarliestDeadlineUnforced += "Task: "+sortedStartingDeadline[0].numTask+" Is Completed -> [ $currentPoint : ${currentPoint+sortedStartingDeadline[0].executionTime} ] \n"
//                        Toast.makeText(activity, "Task"+sortedStartingDeadline[0].numTask+" Is complete", Toast.LENGTH_SHORT).show()

                        currentPoint += sortedStartingDeadline[0].executionTime

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            sortedStartingDeadline.removeIf { it ->  it.numTask == sortedStartingDeadline[0].numTask}
                        }
                        break
                    }
                }

                if(sortedStartingDeadline.size == 0){
                    break
                }

                if (currentArrivalTasks.size == 0)
                    continue
                else{
                    val sortedTasks = currentArrivalTasks.sortedWith(compareBy { it.arrivalTime })

                    outputEarliestDeadlineUnforced += "Task: "+sortedTasks[0].numTask+" Is Completed -> [ $currentPoint : ${currentPoint+sortedTasks[0].executionTime} ] \n"
//                    Toast.makeText(activity, "Task"+sortedTasks[0].numTask+" Is completed", Toast.LENGTH_SHORT).show()

                    currentPoint += sortedTasks[0].executionTime


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        sortedStartingDeadline.removeIf { it ->  it.numTask == sortedTasks[0].numTask}
                    }

                }

                if(currentArrivalTasks.size > 0)
                    currentArrivalTasks.clear()
            }

    }
    
    private fun firstComeFirstServed(){
            var temp = listTableTasks.sortedWith(compareBy { it.arrivalTime })
            sortedArrivalTime = ArrayList()

            for (i in temp) {
                sortedArrivalTime.add(i)
            }

            var currentPoint = sortedArrivalTime[0].arrivalTime
            if(currentPoint > 0){
                outputFirstComeFirstServed+= "Delay IS : $currentPoint -> [ 0 : $currentPoint ] \n"
//                Toast.makeText(activity, "Delay IS : $currentPoint" , Toast.LENGTH_SHORT).show()
            }

            var size = 0
            while(sortedArrivalTime.size>0){
                size = sortedArrivalTime.size

                for (task in 0 until size){
                    if (currentPoint > sortedArrivalTime[task].startingDeadline.toInt()) {
                        outputFirstComeFirstServed += "Task: "+sortedArrivalTime[task].numTask+" Is Missed At [ ${sortedArrivalTime[task].startingDeadline} ] \n"
//                        Toast.makeText(activity, "task"+sortedArrivalTime[task].numTask+" is miss" , Toast.LENGTH_SHORT).show()
                        sortedArrivalTime.removeAt(task)
                        break
                    }else if(currentPoint < sortedArrivalTime[task].arrivalTime ){
                        outputFirstComeFirstServed += "Delay Is : ${sortedArrivalTime[task].arrivalTime - currentPoint} -> [ $currentPoint : ${sortedArrivalTime[task].arrivalTime} ] \n"
//                        Toast.makeText(activity, "Delay Is : ${sortedArrivalTime[task].arrivalTime - currentPoint}", Toast.LENGTH_SHORT).show()
                        currentPoint = sortedArrivalTime[task].arrivalTime
                        break
                    }else{

                        outputFirstComeFirstServed += "Task: "+sortedArrivalTime[0].numTask+" Is Completed -> [ $currentPoint : ${currentPoint+sortedArrivalTime[0].executionTime} ] \n"
//                        Toast.makeText(activity, "Task"+sortedArrivalTime[0].numTask+" Is complete", Toast.LENGTH_SHORT).show()

                        currentPoint += sortedArrivalTime[0].executionTime

                        sortedArrivalTime.removeAt(task)
                        break
                    }
                }
            }

    }
    


    private fun rateMonotonicScheduling(){
        var size = listPriorityTasks.size
        if( size >= 2) {

            val rms = AlgorithmRMS()
            rms.startingAlgorithm(listPriorityTasks)
//            Toast.makeText(activity, ""+AlgorithmRMS.outputEquation , Toast.LENGTH_SHORT).show()
//            Toast.makeText(activity, ""+AlgorithmRMS.outputTasks , Toast.LENGTH_SHORT).show()

            val action = StaticTableInputsFragmentDirections.actionStaticTableInputsFragmentToShowOutputStaticPriorityFragment(AlgorithmRMS.outputEquation , AlgorithmRMS.outputTasks )
            mNavController.navigate(action)

            AlgorithmRMS.outputEquation = ""
            AlgorithmRMS.outputTasks = ""
        }else{
            Toast.makeText(activity, "please enter number task greater than one", Toast.LENGTH_SHORT).show()
        }
    }


    fun showDialodAddNotes() {
        if(algorithmName == "table"){
            val view = layoutInflater.inflate(R.layout.custom_add_task_static_table, null, false)

            val dialog = AlertDialog.Builder(requireContext())
            dialog.setView(view)
            dialog.setTitle("Add New Task")
            val alert = dialog.create()
            alert.show()

            view.btn_add_task.setOnClickListener {
                val arrivalTime = view.et_arrivalTime.text.toString()
                val executionTime = view.et_executionTime.text.toString()
                val startingDeadline = view.et_startingDeadline.text.toString()

                if (arrivalTime.isNotEmpty() && executionTime.isNotEmpty() && startingDeadline.isNotEmpty()) {
                    if(arrivalTime.toInt() <= startingDeadline.toInt()){
                        numberTasks++
                        listTableTasks.add(StaticTableTasks(
                            numberTasks,
                            arrivalTime.toInt(),
                            executionTime.toInt(),
                            startingDeadline.toInt() ) )

                        alert.dismiss()
                    }else{
                        Toast.makeText(activity, "Must enter the Arrival Time less than Starting Deadline", Toast.LENGTH_LONG).show()
                    }

                } else
                    Toast.makeText(activity, "Please Enter valid Data", Toast.LENGTH_LONG).show()
            }
        }else {
            val view = layoutInflater.inflate(R.layout.custom_add_task_static_priority, null, false)

            val dialog = AlertDialog.Builder(requireContext())
            dialog.setView(view)
            dialog.setTitle("Add New Task")
            val alert = dialog.create()
            alert.show()

            view.btn_add_task_priority.setOnClickListener {
                val period = view.et_period.text.toString()
                val processingTime = view.et_processingTime.text.toString()

                if (period.isNotEmpty() && processingTime.isNotEmpty()) {
                    numberTasks++
                    listPriorityTasks.add(StaticPriorityTasks(numberTasks, period.toInt(), processingTime.toInt() ) )

                    alert.dismiss()
                } else
                    Toast.makeText(activity, "Please Enter valid Data", Toast.LENGTH_LONG).show()
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}