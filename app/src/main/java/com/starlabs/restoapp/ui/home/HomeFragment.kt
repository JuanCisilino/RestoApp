package com.starlabs.restoapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.starlabs.restoapp.databinding.FragmentHomeBinding
import com.starlabs.restoapp.ui.menu.MenuActivity

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setPrefs(requireContext())
        setVisibiliby()
        setButtons()
    }

    private fun setButtons() {
        binding.reservationButton.setOnClickListener {  }
        binding.menuButton.setOnClickListener { MenuActivity.start(requireActivity()) }

        binding.qrButton.setOnClickListener {  }
    }

    private fun setVisibiliby() {
        when (viewModel.getRol()) {
            "admin" -> {
                binding.adminLayout.visibility = View.VISIBLE
                binding.userLayout.visibility = View.GONE
            }
            else -> {
                binding.adminLayout.visibility = View.GONE
                binding.userLayout.visibility = View.VISIBLE
            }
        }
    }

}