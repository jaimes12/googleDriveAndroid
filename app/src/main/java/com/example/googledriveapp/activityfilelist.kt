package com.example.googledriveapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class activityfilelist : AppCompatActivity() {
    private lateinit var fileListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activityfilelist)

        fileListView = findViewById(R.id.fileListView)

        val files = filesDir.listFiles()
        val fileNames = files?.map { it.name } ?: listOf()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, fileNames)
        fileListView.adapter = adapter
    }
}