package com.starlabs.restoapp.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.squareup.picasso.Picasso
import com.starlabs.restoapp.databinding.FragmentProfileBinding
import com.starlabs.restoapp.helpers.LoadState
import com.starlabs.restoapp.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setPrefs(requireContext())
        viewModel.getUser()
        subscribeToLiveData()
        setButton()
    }

    private fun setButton(){
        binding.logOutButton.setOnClickListener {
            viewModel.signOut()
            requireActivity().finish()
        }
    }

    private fun subscribeToLiveData() {
        viewModel.userLiveData.observe(viewLifecycleOwner) { setComponents(it) }
        viewModel.loadStateLiveData.observe(viewLifecycleOwner) { handleLoadingState(it) }
    }

    private fun handleLoadingState(state: LoadState) {
        when (state) {
            LoadState.Loading -> {  }
            LoadState.Success -> {  }
            else -> {  }
        }
    }

    private fun setComponents(user: User?) {
        user
            ?.let {
                binding.nameTextView.text = user.name?:""
                binding.emailTextView.text = user.email?:""
                Picasso.get().load(Uri.parse(user.photo)).into(binding.photoImageView)
            }
            ?:run {
                Toast.makeText(requireContext(), "No se pudo traer el usuario", Toast.LENGTH_LONG).show()
            }
    }
}