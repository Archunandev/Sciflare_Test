package com.greedy.sciflaretest.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.greedy.sciflaretest.R
import com.greedy.sciflaretest.databinding.ActivityMainBinding
import com.greedy.sciflaretest.db.UsersDatabase
import com.greedy.sciflaretest.model.Users
import com.greedy.sciflaretest.repository.SciflareRepo
import com.greedy.sciflaretest.util.Resource
import com.greedy.sciflaretest.viewmodel.SciflareFactory
import com.greedy.sciflaretest.viewmodel.SciflareViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    lateinit var viewModel: SciflareViewModel
    lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val newsRepository = SciflareRepo(UsersDatabase(this))
        val viewModelProviderFactory = SciflareFactory(application, newsRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(SciflareViewModel::class.java)

        getUsersList()

    }

    private fun getUsersList() {

        viewModel.usersList.observe(this, Observer { response ->

            when (response) {

                is Resource.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }

                is Resource.Success -> {
                    viewModel.deleteUsers()
                    response.data?.let {
                        setUpRv(it)
                        viewModel.saveUsers(it)
                    }
                }

                is Resource.Error -> {
                    response.message.let { message ->
                        if (message.equals("No internet connection")) {
                            getFromDb()
                        } else {
                            Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }


    private fun getFromDb() {
        viewModel.getSavedUsers().observe(this, Observer { repoList ->
            if (!repoList.isNullOrEmpty()) {
                setUpRv(it = repoList)
            } else {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUpRv(it: List<Users>) {
        usersAdapter = UsersAdapter(it)
        activityMainBinding.apply {
            userListRv.adapter = usersAdapter
        }
    }
}