package com.media.iptvplayer

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.media.iptvplayer.model.Channel

class ChannelListActivity : AppCompatActivity() {

    private lateinit var listChannels: ListView
    private lateinit var searchBox: EditText
    private lateinit var groupContainer: LinearLayout

    private var allChannels =
        mutableListOf<Channel>()

    private var filteredChannels =
        mutableListOf<Channel>()

    private var selectedGroup = "Tümü"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_channel_list
        )

        listChannels =
            findViewById(R.id.listChannels)

        searchBox =
            findViewById(R.id.etSearch)

        groupContainer =
            findViewById(R.id.groupContainer)

        val category =
            intent.getStringExtra(
                "CATEGORY"
            ) ?: "LIVE"

        allChannels =
            ChannelRepository.channels
                .filter {
                    it.category == category
                }
                .toMutableList()

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

            startActivity(
                Intent(
                    this,
                    PlayerActivity::class.java
                )
                    .putExtra(
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

            allChannels.forEach {

                if (it.name ==
                    filteredChannels[position].name
                ) {

                    it.isFavorite =
                        filteredChannels[position].isFavorite
                }
            }

            applyFilter()

            true
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

            tv.setPadding(
                30,
                15,
                30,
                15
            )

            tv.textSize = 12f

            tv.setTextColor(Color.WHITE)

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

                tv.animate()
                    .scaleX(1.15f)
                    .scaleY(1.15f)
                    .setDuration(100)
                    .withEndAction {

                        tv.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                    }
                    .start()

                val vibrator =
                    getSystemService(
                        VIBRATOR_SERVICE
                    ) as Vibrator

                if (Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.O) {

                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            30,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )

                } else {

                    @Suppress("DEPRECATION")
                    vibrator.vibrate(30)
                }

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

                            it.group ==
                            selectedGroup ||

                            (
                                    selectedGroup ==
                                            "Favoriler"
                                            &&
                                            it.isFavorite
                                    )

                val searchOk =

                    it.name.lowercase()
                        .contains(search)

                groupOk && searchOk

            }

                .sortedByDescending {

                    it.isFavorite

                }

                .toMutableList()

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
