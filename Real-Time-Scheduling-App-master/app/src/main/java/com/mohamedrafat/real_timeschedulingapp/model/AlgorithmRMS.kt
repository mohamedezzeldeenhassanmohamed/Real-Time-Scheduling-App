package com.mohamedrafat.real_timeschedulingapp.model

import android.os.Build
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

class AlgorithmRMS {
    companion object{
        var outputEquation:String = ""
        var outputTasks:String = ""
    }

    private fun gcd(a: Int, b: Int): Int {
        var a = a
        var b = b
        while (b > 0) {
            val temp = b
            b = a % b // % is remainder
            a = temp
        }
        return a
    }

    private fun lcm(a: Int, b: Int): Int {
        return a * (b / gcd(a, b))
    }

    private fun lcm(input: IntArray): Int {
        var result = input[0]
        for (i in 1 until input.size) result = lcm(result, input[i])
        return result
    }

    private fun lhs(taskList: LinkedList<Task>): Double {
        var returnValue = 0.00
        for (eachTask in taskList) {
            returnValue += eachTask.c.toDouble() / eachTask.p.toDouble()
        }
        return returnValue
    }

    private fun rhs(n: Int): Double {
        return n.toDouble() * (2.0.pow(1 / n.toDouble()) - 1.0)
    }

    ////////////////////////////////////////////////////////////////////
    
    fun startingAlgorithm(tasks:ArrayList<StaticPriorityTasks>) {
        val periodicTaskList = LinkedList<Task>()
        val ReadyQueue: ReadyQueue = AlgorithmRMS().ReadyQueue()
        
        try {
            val totalTask = tasks.size     /// Enter size
            val periodList = ArrayList<Int>()

            for (i in 0 until totalTask) {
                val period = tasks[i].period     /// Enter period
                periodList.add(period)
                val cTime = tasks[i].processingTime  /// Enter processing time
                val tempTask: Task = AlgorithmRMS().Task(period, cTime, i+1)
                periodicTaskList.add(tempTask)
            }
            
            val isFailure: Boolean = lhs(periodicTaskList) > rhs(periodicTaskList.size)
            
            if (isFailure) {
                outputEquation += "▪ LHS : ${lhs(periodicTaskList)} \n  RHS : ${rhs(periodicTaskList.size)} \n\n "
                outputEquation += "▪ The Tasks deadline misses"
            } else {
                outputEquation += "▪ LHS : ${lhs(periodicTaskList)} \n  RHS : ${rhs(periodicTaskList.size)} \n\n "
                outputEquation += "▪ The Tasks is definitely Scheduling by RMS"
            }
            
            var periodArray = IntArray(0)
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                periodArray = periodList.stream().mapToInt { i: Int? -> i!! }.toArray()
            }
            val periodLCM = lcm(periodArray)
            
            var i = 0
            while (true) {
                for (individualTask in periodicTaskList) {
                    if (i % individualTask.p == 0) {
                        ReadyQueue.addNewTask(
                            AlgorithmRMS().Task(
                                individualTask.p,
                                individualTask.c,
                                individualTask.id )
                        )
                    }
                }

                //Check cycle complete or not
                if (i != 0 && i % periodLCM == 0) {
                    var tempVar = 0
                    if (ReadyQueue.theQueue.size == periodicTaskList.size) {
                        for (k in ReadyQueue.theQueue.indices) {
                            if (ReadyQueue.theQueue[k].r != ReadyQueue.theQueue[k].c) {
                                tempVar = 1
                                break
                            }
                        }
                    }
                    if (tempVar == 0) {
                        outputTasks += "End of one complete cycle 👌\n"
                        return
                    }
                }

                //Execute Once!
                val output = ReadyQueue.executeOneUnit()
                if (output == -1) {
                    return
                }
                i++
            }
        } catch (e: Exception) {
            Log.d("error","Some error occurred. Program will now terminate: $e")
        }
    }

    inner class Task internal constructor(//period
        var p: Int, //computational time
        var c: Int, identification: Int ) {
        var r:Int = c  //remaining computational time after preemption
        var entryTime = 0
        var id: Int

        init {
            id = identification
        }
    }
    

    inner class ReadyQueue internal constructor() {
        var lastExecutedTask: Task?
        var theQueue:LinkedList<Task>
        var timeLapsed: Int

        init {
            theQueue = LinkedList()
            timeLapsed = 0
            lastExecutedTask = null
        }

        //0 -> Nothing to do
        //1 -> Everything went normally
        //-1 -> Deadline Missed!
        //2 -> Process completely executed without missing the deadline
        @Throws(Exception::class)
        fun executeOneUnit(): Int {
            if (theQueue.isEmpty()) {
                timeLapsed++
                return 0
            }
            timeLapsed++
            val T = theQueue.first
            //Somehow remaining time became negative, or is 0 from first
            if (T.r <= 0) {
                throw Exception()
            }
            T.r--
            if (T.r + 1 == T.c || T !== lastExecutedTask && lastExecutedTask != null) {
                outputTasks += "🔸 At time " + (timeLapsed - 1) + ", task " + T.id + " has started execution \n\n"
            }
            lastExecutedTask = T
            //After running if remaining time becomes zero, i.e. process is completely executed
            return if (T.r == 0) {
                if (T.entryTime + T.p >= timeLapsed) {
                    outputTasks += "🔹 At time " + timeLapsed + ", task " + T.id + " has been completely executed \n\n"
                    theQueue.pollFirst()
                    2
                } else {
                    outputTasks += "🔻 Task ${theQueue.first.id} finished at time $timeLapsed thus, \n   Missing it's deadline of time ${(theQueue.first.entryTime + theQueue.first.p)} \n\n"
                    theQueue.pollFirst()
                    -1
                }
            } else 99
        }

        //Added task in empty queue -> 0
        //Added identical task to the first task -> 1
        //Added the most prioritized task
        //Previous Process Not Pre-Empted -> 2
        //Previous Process Pre-Empted -> 3
        //Added the second task with less priority -> 4
        //Added task somewhere in the middle -> 5
        //Added the least prioritized task in a list with size more than 2 -> 6
        //Impossible -> 7
        fun addNewTask(T: Task): Int {
            if (theQueue.isEmpty()) {
                theQueue.addFirst(T)
                T.entryTime = timeLapsed
                return 0
            }
            if (T.p == theQueue.first.p) {
                theQueue.add(1, T)
                T.entryTime = timeLapsed
                return 1
            }
            if (T.p < theQueue.first.p) {
                val tFlag = theQueue.first.c == theQueue.first.r
                theQueue.addFirst(T)
                T.entryTime = timeLapsed
                return if (tFlag) 2 else {
                    outputTasks += "🔺 At time  $timeLapsed , task ${theQueue[1].id} has been preempted \n\n"
                    3
                }
            }

            if (T.p > theQueue.first.p) {
                if (theQueue.size == 1) {
                    theQueue.add(T)
                    T.entryTime = timeLapsed
                    return 4
                }
                for (i in 1 until theQueue.size) {
                    if (T.p < theQueue[i].p) {
                        theQueue.add(i, T)
                        T.entryTime = timeLapsed
                        return 5  }
                    if (T.p > theQueue[i].p) {
                        if (i == theQueue.size - 1) {
                            theQueue.add(T)
                            T.entryTime = timeLapsed
                            return 6
                        }
                    }
                }
            }
            return 7
        }

    }
}
