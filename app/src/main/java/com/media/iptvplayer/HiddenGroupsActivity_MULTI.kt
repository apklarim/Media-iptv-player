package com.media.iptvplayer

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HiddenGroupsActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var btnRestoreAll: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_hidden_groups)

        listView = findViewById(R.id.listHiddenGroups)
        btnRestoreAll = findViewById(R.id.btnRestoreAll)

        loadGroups()

        btnRestoreAll.setOnClickListener {

            HiddenGroupsManager.clearHiddenGroups(this)

            Toast.makeText(
                this,
                "Tüm gruplar geri getirildi",
                Toast.LENGTH_SHORT
            ).show()

            loadGroups()
        }

        // Uzun basınca tüm grupları geri getir
        listView.setOnItemLongClickListener { _, _, _, _ ->

            val groups =
                HiddenGroupsManager
                    .getHiddenGroups(this)
                    .toList()

            if (groups.isEmpty()) {
                Toast.makeText(
                    this,
                    "Gizli grup bulunamadı",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnItemLongClickListener true
            }

            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Tümünü Seç")
                .setMessage(
                    "Tüm gizli gruplar geri getirilsin mi?"
                )
                .setPositiveButton("Evet") { _, _ ->

                    HiddenGroupsManager
                        .clearHiddenGroups(this)

                    Toast.makeText(
                        this,
                        "Tüm gruplar geri getirildi",
                        Toast.LENGTH_SHORT
                    ).show()

                    loadGroups()
                }
                .setNegativeButton("Hayır", null)
                .show()

            true
        }
    }

    private fun loadGroups() {

        val groups =
            HiddenGroupsManager
                .getHiddenGroups(this)
                .toList()
                .sorted()

        listView.choiceMode =
            ListView.CHOICE_MODE_MULTIPLE

        listView.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                groups
            )

        // Tek dokununca geri getir
        listView.setOnItemClickListener { _, _, position, _ ->

            val group = groups[position]

            HiddenGroupsManager.restoreGroup(
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
