package com.media.iptvplayer

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HiddenGroupsActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_hidden_groups
        )

        listView =
            findViewById(R.id.listHiddenGroups)

        val btnRestoreAll =
            findViewById<Button>(
                R.id.btnRestoreAll
            )

        loadGroups()

        btnRestoreAll.setOnClickListener {

            HiddenGroupsManager
                .clearHiddenGroups(this)

            Toast.makeText(
                this,
                "Tüm gruplar geri getirildi",
                Toast.LENGTH_SHORT
            ).show()

            loadGroups()
        }
    }

    private fun loadGroups() {

        val groups =
            HiddenGroupsManager
                .getHiddenGroups(this)
                .toList()
                .sorted()

        listView.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                groups
            )

        listView.setOnItemClickListener {
                _, _, position, _ ->

            val group =
                groups[position]

            HiddenGroupsManager
                .restoreGroup(
                    this,
                    group
                )

            Toast.makeText(
                this,
                "$group geri getirildi",
                Toast.LENGTH_SHORT
            ).show()

            loadGroups()
        }
    }
}
