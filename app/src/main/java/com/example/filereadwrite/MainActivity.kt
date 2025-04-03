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
import java.io.File
import java.io.FileOutputStream
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvData: TextView
    private val REQUEST_CODE_WRITE_PERM = 401
    private val filePath = Environment.getExternalStorageDirectory().toString() + "/test.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvData = findViewById(R.id.tvData)

        val btnWriteFile = findViewById<Button>(R.id.btnWriteFile)
        btnWriteFile.setOnClickListener {
            writeFile("Hello: " + Date(System.currentTimeMillis()).toString())
        }

        val btnReadFile = findViewById<Button>(R.id.btnReadFile)
        btnReadFile.setOnClickListener {
            tvData.text = readFile()
        }

        requestNeededPermission() // Запрос разрешений при старте
    }

    private fun writeFile(data: String) {
        try {
            val os = FileOutputStream(filePath)
            os.write(data.toByteArray())
            os.flush()
            os.close()
            Toast.makeText(this, "File written: $filePath", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error writing file", Toast.LENGTH_LONG).show()
        }
    }

    private fun readFile(): String {
        return try {
            val file = File(filePath)
            if (!file.exists()) {
                return "File not found"
            }
            file.readText()
        } catch (e: Exception) {
            e.printStackTrace()
            "Error reading file"
        }
    }

    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                Toast.makeText(this, "I need it for File", Toast.LENGTH_SHORT).show()
            }

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_WRITE_PERM
            )
        } else {
            Toast.makeText(this, "Already have permission", Toast.LENGTH_SHORT).show()
        }
    }

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
