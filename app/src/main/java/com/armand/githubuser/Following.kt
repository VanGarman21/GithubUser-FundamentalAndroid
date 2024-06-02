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
import com.armand.githubuser.databinding.FollowingBinding

class Following : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private val adapter = FollowAdapter()
    private lateinit var binding: FollowingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeFollowingData()
        viewModel.following(UserDetail.username)
        viewModel.getIsLoading.observe(viewLifecycleOwner, this::showLoading)
    }

    private fun observeFollowingData() {
        viewModel.getFollowing.observe(viewLifecycleOwner) { following ->
            if (following.isNotEmpty()) {
                adapter.setData(following)
            } else {
                Toast.makeText(requireContext(), "Following Tidak Ada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvFollowing.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = this@Following.adapter
        }

        adapter.setOnItemClickCallback { data -> selectedUser(data) }
    }

    private fun selectedUser(user: ResponseFollow) {
        Toast.makeText(requireContext(), "Selected ${user.login}", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireActivity(), UserDetail::class.java)
        intent.putExtra(UserDetail.EXTRA_USER, user.login)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
