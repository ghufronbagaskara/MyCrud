package com.example.mycrud

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
        adapter.setDialog(object: UserAdapter.Dialog{
            override fun onClick(position: Int) {
                // create a dialog view
                val dialog = AlertDialog.Builder(this@MainActivity)
                dialog.setTitle(list[position].fullName)
                dialog.setItems(R.array.items_option, DialogInterface.OnClickListener{ dialog, which->
                    when(which){
                        0 ->{ //edit
                            val intent = Intent(this@MainActivity, EditorActivity::class.java)
                            intent.putExtra("id", list[position].uid)
                            startActivity(intent)
                        }
                        1 -> { //delete
                            dataBase.userDao().delete(list[position])
                            getData()
                        }
                        2-> { //cancel
                            dialog.dismiss()
                        }
                    }
                })
                // show dialog
                val dialogView = dialog.create()
                dialogView.show()
            }

        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext, VERTICAL, false)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(applicationContext, VERTICAL))

        binding.floatingActionButton.setOnClickListener{
            startActivity(Intent(this, EditorActivity::class.java))
        }

        binding.btnSearch.setOnClickListener{
            if (binding.etSearch.text.isNotEmpty()){
                searchData(binding.etSearch.text.toString())
            }else{
                getData()
                Toast.makeText(applicationContext, "Silahkan isi terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        binding.etSearch.setOnKeyListener{ v, keyCode, event ->
            if (binding.etSearch.text.isNotEmpty()){
                searchData(binding.etSearch.text.toString())
            } else{
                getData()
            }
            true
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

    @SuppressLint("NotifyDataSetChanged")
    fun searchData(search: String){
        list.clear()
        list.addAll(dataBase.userDao().searchByName(search))
        adapter.notifyDataSetChanged()
    }

}