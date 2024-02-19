package com.starlabs.restoapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.starlabs.restoapp.databinding.FragmentAdminBinding
import com.starlabs.restoapp.ui.abm.ABMActivity
import com.starlabs.restoapp.ui.menu.MenuActivity
import dagger.hilt.android.AndroidEntryPoint

class AdminFragment : Fragment() {

    private val viewModel by viewModels<AdminViewModel>()
    private lateinit var binding: FragmentAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setComponents()
    }

    private fun setComponents() {
        binding.editMenuButton.setOnClickListener { MenuActivity.start(requireActivity()) }
        binding.createButton.setOnClickListener { ABMActivity.start(requireActivity()) }
    }
}