package com.mohamedrafat.real_timeschedulingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mohamedrafat.real_timeschedulingapp.databinding.FragmentShowOutputStaticPriorityBinding
import com.mohamedrafat.real_timeschedulingapp.databinding.FragmentStaticTableInputsBinding


class ShowOutputStaticPriorityFragment : Fragment() {
    private lateinit var mNavController: NavController
    private var _binding: FragmentShowOutputStaticPriorityBinding ? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNavController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShowOutputStaticPriorityBinding.inflate(inflater, container, false)

        val resultEquation = arguments?.getString("resultEquation")
        val resultTasks = arguments?.getString("outputTasks")

        binding.tvResultRms.text = resultEquation
        binding.tvPriorityTasks.text = resultTasks

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}