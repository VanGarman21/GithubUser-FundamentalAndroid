package com.armand.githubuser

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.armand.githubuser.database.HelperFavorite
import com.armand.githubuser.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private val adapter = FavoriteAdapter()
    private lateinit var helper: HelperFavorite
    private var listFavorite: ArrayList<ResponseDetailUser> = ArrayList()

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.favorite)
            setDisplayHomeAsUpEnabled(true)
        }

        helper = HelperFavorite.getInstance(applicationContext)
        helper.open()

        setupRecyclerView()
        getDataFavorite()
    }

    private fun setupRecyclerView() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback { data -> selectedUser(data) }

        val layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(this, 2)
        } else {
            LinearLayoutManager(this)
        }
        binding.rvUser.layoutManager = layoutManager
        binding.rvUser.setHasFixedSize(true)
    }

    private fun selectedUser(user: ResponseDetailUser) {
        val message = "You chose ${user.login}"
        showToast(message)
        navigateToUserDetail(user.login)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToUserDetail(username: String?) {
        val intent = Intent(this, UserDetail::class.java)
        intent.putExtra(UserDetail.EXTRA_USER, username)
        startActivity(intent)
    }


    private fun getDataFavorite() {
        listFavorite = helper.queryAll()
        val isListNotEmpty = listFavorite.isNotEmpty()
        binding.rvUser.visibility = if (isListNotEmpty) View.VISIBLE else View.GONE
        binding.tvNotFound.visibility = if (isListNotEmpty) View.GONE else View.VISIBLE
        if (isListNotEmpty) {
            adapter.setData(listFavorite)
        }
    }


    override fun onResume() {
        super.onResume()
        getDataFavorite()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
