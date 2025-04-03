package com.example.filereadwrite

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvData: TextView
    private val REQUEST_CODE_WRITE_PERM = 401
    private val filePath = Environment.getExternalStorageDirectory().toString() + "/test.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvData = findViewById(R.id.tvData)

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

        // Запрос разрешений при старте
        requestNeededPermission()

        // Чтение файла из папки assets
        try {
            val assetManager = assets
            val inputStream = assetManager.open("test.txt")  // Имя файла в папке assets
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()

            // Чтение файла построчно
            bufferedReader.forEachLine { stringBuilder.append(it).append("\n") }
            val fileContent = stringBuilder.toString()

            // Отображаем содержимое файла в TextView
            tvData.text = fileContent
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error reading file from assets", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestNeededPermission() {
        // Проверка, предоставлено ли разрешение на запись в хранилище
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Если разрешение не предоставлено, запрашиваем его
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_WRITE_PERM
            )
        } else {
            // Если разрешение уже предоставлено, уведомляем пользователя
            Toast.makeText(this, "Already have permission", Toast.LENGTH_SHORT).show()
        }
    }

    private fun writeFile(data: String) {
        try {
            val os = FileOutputStream(filePath)
            os.write(data.toByteArray())
            os.flush()
            os.close()

            // Показываем сообщение о успешной записи файла
            Toast.makeText(this, "File written: $filePath", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error writing file", Toast.LENGTH_LONG).show()
        }
    }

    // Метод для чтения файла
    private fun readFile(): String {
        return try {
            val file = File(filePath)
            if (!file.exists()) {
                return "File not found"
            }
            file.readText() // Чтение текста из файла
        } catch (e: Exception) {
            e.printStackTrace()
            "Error reading file" // Обработка ошибок чтения файла
        }
    }

    // Метод для обработки результата запроса разрешений
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_WRITE_PERM) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "WRITE_EXTERNAL_STORAGE permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "WRITE_EXTERNAL_STORAGE permission NOT granted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
