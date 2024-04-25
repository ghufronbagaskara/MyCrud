package com.example.mycrud

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.mycrud.adapter.UserAdapter
import com.example.mycrud.data.AppDatabase
import com.example.mycrud.data.entity.User
import com.example.mycrud.databinding.ActivityMainBinding
import com.example.mycrud.ui.editor.EditorActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var list = mutableListOf<User>()
    private lateinit var adapter: UserAdapter
    private lateinit var dataBase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        binding.etSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.isNotEmpty()){
                    searchData(s.toString())
                } else{
                    getData()
                }
            }
        })

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