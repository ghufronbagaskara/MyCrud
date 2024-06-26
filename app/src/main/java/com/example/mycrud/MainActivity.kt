package com.example.mycrud

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.mycrud.adapter.UserAdapter
import com.example.mycrud.data.AppDatabase
import com.example.mycrud.data.entity.User
import com.example.mycrud.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var list = mutableListOf<User>()
    private lateinit var adapter: UserAdapter
    private lateinit var dataBase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBase = AppDatabase.getInstance(applicationContext)
        adapter = UserAdapter(list)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext, VERTICAL, false)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(applicationContext, VERTICAL))

        binding.floatingActionButton.setOnClickListener{
            startActivity(Intent(this, EditorActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getData(){
        list.clear()
        list.addAll(dataBase.userDao().getAll())
        adapter.notifyDataSetChanged()
    }

}