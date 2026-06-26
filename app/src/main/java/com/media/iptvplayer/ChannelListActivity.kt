package com.media.iptvplayer

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.GridView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.media.iptvplayer.model.Channel

class ChannelListActivity : AppCompatActivity() {

    private lateinit var listChannels: GridView
    private lateinit var searchBox: EditText
    private lateinit var groupContainer: LinearLayout

    private var allChannels = mutableListOf<Channel>()
    private var filteredChannels = mutableListOf<Channel>()

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

        listChannels.numColumns =
            if (currentCategory == "LIVE") 1 else 2

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

        filteredChannels =
            allChannels.toMutableList()

        loadChannels()

        searchBox.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

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
                ) {
                }
            }
        )

        listChannels.setOnItemClickListener {
                _, _, position, _ ->

            // Tüm kategori listesini koru
            ChannelRepository.setChannels(
                allChannels
            )

            startActivity(
                Intent(
                    this,
                    PlayerActivity::class.java
                ).putExtra(
                    "url",
                    filteredChannels[position].url
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

            loadChannels()

            true
        }
    }

    private fun applyFilter() {

        val search =
            searchBox.text.toString()
                .lowercase()

        filteredChannels =
            allChannels.filter {

                it.name.lowercase()
                    .contains(search)

            }.toMutableList()

        loadChannels()
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
