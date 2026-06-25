package com.media.iptvplayer

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.media.iptvplayer.model.Channel

class ChannelListActivity : AppCompatActivity() {

    private lateinit var listChannels: ListView
    private lateinit var searchBox: EditText
    private lateinit var groupContainer: LinearLayout

    private var allChannels = mutableListOf<Channel>()
    private var filteredChannels = mutableListOf<Channel>()

    private var selectedGroup = "Tümü"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_channel_list)

        listChannels = findViewById(R.id.listChannels)
        searchBox = findViewById(R.id.etSearch)
        groupContainer = findViewById(R.id.groupContainer)

        allChannels = ChannelRepository.channels.toMutableList()

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

        // KANAL TIKLAMA TESTİ

        listChannels.setOnItemClickListener {
                _, _, position, _ ->

            val channel =
                filteredChannels[position]

            Toast.makeText(
                this,
                "Açılıyor: ${channel.name}",
                Toast.LENGTH_SHORT
            ).show()

            val intent =
                Intent(
                    this,
                    PlayerActivity::class.java
                )

            intent.putExtra(
                "url",
                channel.url
            )

            startActivity(intent)
        }
    }

    private fun loadGroups() {

        groupContainer.removeAllViews()

        val groups =
            mutableListOf(
                "Tümü",
                "Favoriler"
            )

        groups.addAll(
            allChannels.map {
                it.group.ifBlank {
                    "Diğer"
                }
            }.distinct().sorted()
        )

        groups.distinct().forEach { group ->

            val tv = TextView(this)

            tv.text = group
            tv.textSize = 12f
            tv.setTextColor(Color.WHITE)

            tv.setPadding(
                30,
                15,
                30,
                15
            )

            if (group == selectedGroup) {
                tv.setBackgroundColor(
                    Color.parseColor("#FF9800")
                )
            } else {
                tv.setBackgroundColor(
                    Color.parseColor("#18C7D1")
                )
            }

            val params =
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

            params.setMargins(
                8,
                8,
                8,
                8
            )

            tv.layoutParams = params

            tv.setOnClickListener {

                selectedGroup = group

                loadGroups()
                applyFilter()
            }

            groupContainer.addView(tv)
        }
    }

    private fun applyFilter() {

        val search =
            searchBox.text.toString()
                .lowercase()

        filteredChannels =
            allChannels.filter {

                val groupOk =
                    selectedGroup == "Tümü" ||
                            (
                                    selectedGroup ==
                                            "Favoriler"
                                            &&
                                            it.isFavorite
                                    ) ||
                            it.group == selectedGroup

                val searchOk =
                    it.name.lowercase()
                        .contains(search)

                groupOk && searchOk

            }.sortedByDescending {

                it.isFavorite

            }.toMutableList()

        loadChannels()
    }

    private fun loadChannels() {

        listChannels.adapter =
            ChannelAdapter(
                this,
                filteredChannels
            )
    }
}
