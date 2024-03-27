package com.example.submissiononevtwo.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissiononevtwo.data.response.GithubResponse
import com.example.submissiononevtwo.data.response.ItemsItem
import com.example.submissiononevtwo.data.retrofit.ApiConfig
import com.example.submissiononevtwo.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var reviewAdapter: ReviewAdapter

    companion object {
        private const val TAG = "MainActivity"
        private const val query = "Nijika"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        reviewAdapter = ReviewAdapter()
        binding.rvReview.adapter = reviewAdapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        getUsers()
    }

    private fun getUsers() {
        showLoading(true)
        val client = ApiConfig.getApiService().getUsers(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        setUserList(it.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }


            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUserList(userList: List<ItemsItem>?) {
        userList?.let { reviewAdapter.submitList(it) }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else  {
            binding.progressBar.visibility = View.GONE
        }
    }
}