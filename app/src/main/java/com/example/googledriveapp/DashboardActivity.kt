package com.example.googledriveapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

class DashboardActivity : AppCompatActivity() {

    private lateinit var startImageView: ImageView
    private lateinit var uploadPhotosButton: Button
    private lateinit var uploadVideosButton: Button
    private lateinit var uploadDocsButton: Button
    private lateinit var viewFilesButton: Button
    private lateinit var deleteFilesButton: Button

    private val pickPhotoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { saveFile(it, "photo") }
    }

    private val pickVideoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { saveFile(it, "video") }
    }

    private val pickDocLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { saveFile(it, "document") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        startImageView = findViewById(R.id.startImageView)
        uploadPhotosButton = findViewById(R.id.uploadPhotosButton)
        uploadVideosButton = findViewById(R.id.uploadVideosButton)
        uploadDocsButton = findViewById(R.id.uploadDocsButton)
        viewFilesButton = findViewById(R.id.viewFilesButton)
        deleteFilesButton = findViewById(R.id.deleteFilesButton)

        checkPermissions()

        uploadPhotosButton.setOnClickListener {
            pickPhotoLauncher.launch("image/*")
        }

        uploadVideosButton.setOnClickListener {
            pickVideoLauncher.launch("video/*")
        }

        uploadDocsButton.setOnClickListener {
            pickDocLauncher.launch("application/pdf")
        }

        viewFilesButton.setOnClickListener {
            startActivity(Intent(this, activityfilelist::class.java))
        }

        deleteFilesButton.setOnClickListener {
            deleteLastFile()
        }
    }

    private fun saveFile(uri: Uri, type: String) {
        try {
            val destinationFile = File(filesDir, "${type}_${System.currentTimeMillis()}")
            contentResolver.openInputStream(uri)?.use { inputStream ->
                destinationFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Toast.makeText(this, "Archivo guardado: ${destinationFile.name}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al guardar el archivo: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteLastFile() {
        val files = filesDir.listFiles()
        if (files != null && files.isNotEmpty()) {
            val lastFile = files.last()
            lastFile.delete()
            Toast.makeText(this, "Archivo eliminado: ${lastFile.name}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No hay archivos para eliminar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)
        }
    }

    companion object {
        private const val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 100
    }
}
