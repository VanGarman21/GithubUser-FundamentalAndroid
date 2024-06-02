package com.armand.githubuser

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.armand.githubuser.databinding.FollowersBinding

class Followers : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private val adapter = FollowAdapter()

    private var _binding: FollowersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getIsLoading.observe(viewLifecycleOwner, this::showLoading)
        viewModel.getFollowers.observe(viewLifecycleOwner) { followers ->
            if (followers.isNotEmpty()) {
                adapter.setData(followers)
            } else {
                Toast.makeText(requireContext(), "Followers Tidak Ditemukan", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.followers(UserDetail.username)
    }

    private fun setupRecyclerView() {
        binding.rvFollowers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollowers.setHasFixedSize(true)
        binding.rvFollowers.adapter = adapter

        adapter.setOnItemClickCallback { data -> navigateToUserDetail(data) }
    }

    private fun navigateToUserDetail(user: ResponseFollow) {
        Toast.makeText(requireContext(), "Selected ${user.login}", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireActivity(), UserDetail::class.java)
        intent.putExtra(UserDetail.EXTRA_USER, user.login)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

