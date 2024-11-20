package com.mohamedrafat.real_timeschedulingapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mohamedrafat.real_timeschedulingapp.R
import com.mohamedrafat.real_timeschedulingapp.databinding.FragmentShowOutputStaticTableBinding
import com.mohamedrafat.real_timeschedulingapp.databinding.FragmentStaticTableInputsBinding

class ShowOutputStaticTableFragment : Fragment() {
    private lateinit var mNavController: NavController
    private var _binding: FragmentShowOutputStaticTableBinding? = null
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
        _binding = FragmentShowOutputStaticTableBinding.inflate(inflater, container, false)

        val eDeadline = arguments?.getString("eDeadline")
        val eDeadlineUnforced = arguments?.getString("eDeadlineUnforced")
        val fcfs = arguments?.getString("fcfs")

        binding.tvEarliestDeadline.text = eDeadline
        binding.tvEarliestDeadlineUnforced.text = eDeadlineUnforced
        binding.tvFcfs.text = fcfs

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}