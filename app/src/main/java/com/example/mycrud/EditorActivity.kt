package com.example.mycrud

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mycrud.data.AppDatabase
import com.example.mycrud.data.entity.User
import com.example.mycrud.databinding.ActivityEditorBinding

class EditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditorBinding
    private lateinit var dataBase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBase = AppDatabase.getInstance(applicationContext)

        binding.btnSave.setOnClickListener() {
            if (binding.etFullName.text.isNotEmpty() && binding.etEmail.text.isNotEmpty() && binding.etPhone.text.isNotEmpty()) {
                Log.d("tes", "tes terbaca")
                dataBase.userDao().insertAll(
                    User(
                        null,
                        binding.etFullName.text.toString(),
                        binding.etEmail.text.toString(),
                        binding.etPhone.text.toString()
                    )
                )
                finish()
            } else {
                Toast.makeText(applicationContext, "Silahkan isi semua data", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}