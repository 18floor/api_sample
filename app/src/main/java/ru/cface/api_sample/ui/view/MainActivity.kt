package ru.cface.api_sample.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.cface.api_sample.R
import ru.cface.api_sample.data.api.ApiHelper
import ru.cface.api_sample.data.api.ApiServiceImpl
import ru.cface.api_sample.data.model.User
import ru.cface.api_sample.ui.adapter.MainAdapter
import ru.cface.api_sample.ui.base.ViewModelFactory
import ru.cface.api_sample.ui.viewmodel.MainViewModel
import ru.cface.api_sample.utils.Status

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        setupUI()
        setupViewModel()
        setupObserver()
    }

    private fun setupUI() {
        rvUsersView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        rvUsersView.addItemDecoration(
            DividerItemDecoration(
                rvUsersView.context,
                (rvUsersView.layoutManager as LinearLayoutManager).orientation
            )
        )
        rvUsersView.adapter = adapter
    }

    private fun setupObserver() {
        mainViewModel.getUsers().observe(this, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    progressView.visibility = View.GONE
                    it.data?.let { users ->
                        renderList(users)
                        rvUsersView.visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> {
                    progressView.visibility = View.VISIBLE
                    rvUsersView.visibility = View.GONE
                }
                Status.ERROR -> {
                    progressView.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun renderList(users: List<User>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(MainViewModel::class.java)
    }
}