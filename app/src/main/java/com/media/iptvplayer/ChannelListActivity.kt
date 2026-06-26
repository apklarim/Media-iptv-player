package com.media.iptvplayer

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.media.iptvplayer.model.Channel

class ChannelListActivity : AppCompatActivity() {

    private lateinit var listChannels: GridView
    private lateinit var searchBox: EditText
    private lateinit var groupContainer: LinearLayout

    private var allChannels = mutableListOf<Channel>()
    private var filteredChannels = mutableListOf<Channel>()

    private var selectedGroup = "Tümü"
    private var currentCategory = "LIVE"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_channel_list)

        listChannels = findViewById(R.id.listChannels)
        searchBox = findViewById(R.id.etSearch)
        groupContainer = findViewById(R.id.groupContainer)

        currentCategory =
            intent.getStringExtra("CATEGORY")
                ?: "LIVE"

        selectedGroup =
            ChannelPreferences
                .getLastGroup(
                    this,
                    currentCategory
                )

        if (currentCategory == "LIVE") {
            listChannels.numColumns = 1
        } else {
            listChannels.numColumns = 2
        }

        allChannels =
            ChannelRepository.channels
                .filter {
                    it.category == currentCategory
                }
                .toMutableList()

        if (allChannels.isEmpty()) {
            allChannels =
                ChannelRepository.channels
                    .toMutableList()
        }

        // ÖNEMLİ
        ChannelRepository.channels =
            allChannels.toMutableList()

        allChannels.forEach {
            it.isFavorite =
                FavoriteManager.isFavorite(
                    this,
                    it.name
                )
        }

        loadGroups()
        applyFilter()

        searchBox.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    applyFilter()
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {}
            }
        )

        listChannels.setOnItemClickListener {
                _, _, position, _ ->

            val channel =
                filteredChannels[position]

            // ÖNEMLİ
            ChannelRepository.channels =
                filteredChannels.toMutableList()

            startActivity(
                Intent(
                    this,
                    PlayerActivity::class.java
                ).putExtra(
                    "url",
                    channel.url
                )
            )
        }

        listChannels.setOnItemLongClickListener {
                _, _, position, _ ->

            FavoriteManager.toggleFavorite(
                this,
                filteredChannels[position].name
            )

            filteredChannels[position].isFavorite =
                !filteredChannels[position].isFavorite

            applyFilter()

            true
        }
    }

    // Buradan sonrası eski dosyanızdaki ile aynı kalacak

    private fun loadGroups() {
        // mevcut kodunuz aynı kalacak
    }

    private fun showPinDialog(group: String) {
        // mevcut kodunuz aynı kalacak
    }

    private fun openGroup(group: String) {
        // mevcut kodunuz aynı kalacak
    }

    private fun applyFilter() {
        // mevcut kodunuz aynı kalacak
    }

    private fun loadChannels() {

        listChannels.adapter =
            ChannelAdapter(
                this,
                filteredChannels,
                currentCategory
            )
    }
}
