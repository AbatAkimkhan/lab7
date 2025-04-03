package com.example.filereadwrite

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvData: TextView
    private lateinit var filePath: File // Теперь инициализируем внутри onCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvData = findViewById(R.id.tvData)

        // Теперь можно безопасно получить путь к файлу
        filePath = File(getExternalFilesDir(null), "test.txt")

        // Обработчик кнопки "WRITE TO FILE"
        val btnWriteFile = findViewById<Button>(R.id.btnWriteFile)
        btnWriteFile.setOnClickListener {
            writeFile("Hello: " + Date(System.currentTimeMillis()).toString()) // Запись в файл
        }

        // Обработчик кнопки "READ FROM FILE"
        val btnReadFile = findViewById<Button>(R.id.btnReadFile)
        btnReadFile.setOnClickListener {
            tvData.text = readFile() // Чтение из файла
        }

        // Чтение файла из assets
        readAssetFile()
    }

    private fun writeFile(data: String) {
        try {
            filePath.writeText(data)
            Toast.makeText(this, "File written: ${filePath.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error writing file: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun readFile(): String {
        return try {
            if (!filePath.exists()) "File not found" else filePath.readText()
        } catch (e: Exception) {
            e.printStackTrace()
            "Error reading file"
        }
    }

    private fun readAssetFile() {
        try {
            val inputStream = assets.open("test.txt")
            val bufferedReader = InputStreamReader(inputStream).buffered()
            tvData.text = bufferedReader.readText()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error reading file from assets", Toast.LENGTH_SHORT).show()
        }
    }
}
