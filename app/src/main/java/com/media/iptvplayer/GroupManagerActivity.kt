package com.media.iptvplayer

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GroupManagerActivity : AppCompatActivity() {

    private lateinit var listGroups: ListView
    private lateinit var btnSelectAll: Button
    private lateinit var btnClearSelection: Button
    private lateinit var btnHideSelected: Button

    private lateinit var groups: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_group_manager
        )

        listGroups =
            findViewById(R.id.listGroups)

        btnSelectAll =
            findViewById(R.id.btnSelectAll)

        btnClearSelection =
            findViewById(R.id.btnClearSelection)

        btnHideSelected =
            findViewById(R.id.btnHideSelected)

        groups =
            ChannelRepository.channels
                .map { it.group }
                .distinct()
                .filter { it.isNotEmpty() }
                .sorted()

        listGroups.choiceMode =
            ListView.CHOICE_MODE_MULTIPLE

        listGroups.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                groups
            )

        btnSelectAll.setOnClickListener {

            for (i in groups.indices) {
                listGroups.setItemChecked(i, true)
            }
        }

        btnClearSelection.setOnClickListener {

            for (i in groups.indices) {
                listGroups.setItemChecked(i, false)
            }
        }

        btnHideSelected
