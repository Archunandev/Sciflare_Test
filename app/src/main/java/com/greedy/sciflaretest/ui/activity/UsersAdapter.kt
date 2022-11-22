package com.greedy.sciflaretest.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.greedy.sciflaretest.databinding.ItemUsersListBinding
import com.greedy.sciflaretest.model.Users

class UsersAdapter(private var data: List<Users>) :
    RecyclerView.Adapter<UsersAdapter.ListViewHolder>() {

    class ListViewHolder(val binding: ItemUsersListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ItemUsersListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var repoList = data[position]
        holder.binding.apply {
            name.text = repoList.name
            email.text = repoList.age
            gender.text = repoList.color
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


}