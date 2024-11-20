package com.mohamedrafat.real_timeschedulingapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mohamedrafat.real_timeschedulingapp.databinding.FragmentAlgorithmsBinding


class AlgorithmsFragment : Fragment() {
    private lateinit var mNavController: NavController
    private var _binding: FragmentAlgorithmsBinding? = null
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
        _binding = FragmentAlgorithmsBinding.inflate(inflater, container, false)

        binding.staticTable.setOnClickListener {
            val action = AlgorithmsFragmentDirections.actionAlgorithmsFragmentToStaticTableInputsFragment("table")
            mNavController.navigate(action)
        }

        binding.staticPriority.setOnClickListener {
            val action = AlgorithmsFragmentDirections.actionAlgorithmsFragmentToStaticTableInputsFragment("priority")
            mNavController.navigate(action)
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


