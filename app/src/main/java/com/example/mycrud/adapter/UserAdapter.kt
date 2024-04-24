package com.example.mycrud.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycrud.R
import com.example.mycrud.data.entity.User
import com.example.mycrud.databinding.RowUserBinding

class UserAdapter(var list: List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var dialog: Dialog

    interface Dialog {
        fun onClick(position: Int)
    }
    fun setDialog(dialog: Dialog){
        this.dialog = dialog
    }

    inner class ViewHolder(private val binding: RowUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: User){
            binding.tvFullName.text = user.fullName
            binding.tvEmail.text = user.email
            binding.tvPhone.text = user.phone
            binding.root.setOnClickListener {
                dialog.onClick(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        var view = LayoutInflater.from(parent.context).inflate(R.layout.row_user, parent, false)
//        return ViewHolder(view)
        val binding = RowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = list[position]
        return holder.bind(user)
    }
}