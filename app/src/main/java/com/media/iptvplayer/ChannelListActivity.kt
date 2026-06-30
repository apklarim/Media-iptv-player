package com.media.iptvplayer

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.media.iptvplayer.model.Channel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChannelListActivity : AppCompatActivity() {

    private lateinit var listChannels: GridView
    private lateinit var searchBox: EditText
    private lateinit var groupContainer: LinearLayout
    private lateinit var loadingLayout: LinearLayout

    private var allChannels = mutableListOf<Channel>()
    private var filteredChannels = mutableListOf<Channel>()
    private var visibleChannels = mutableListOf<Channel>()

    private var currentCategory = "LIVE"
    private val PAGE_SIZE = 300
    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_list)

        listChannels = findViewById(R.id.listChannels)
        searchBox = findViewById(R.id.etSearch)
        groupContainer = findViewById(R.id.groupContainer)
        loadingLayout = findViewById(R.id.loadingLayout)

        currentCategory = intent.getStringExtra("CATEGORY") ?: "LIVE"
        listChannels.numColumns = 1

        loadingLayout.visibility = View.VISIBLE

        lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                allChannels = ChannelRepository.channels
                    .filter { it.category == currentCategory }
                    .toMutableList()

                if (allChannels.isEmpty())
                    allChannels = ChannelRepository.channels.toMutableList()

                val hidden = HiddenGroupsManager.getHiddenGroups(this@ChannelListActivity)

                allChannels = allChannels.filter {
                    !hidden.contains(it.group)
                }.toMutableList()

                filteredChannels = allChannels.toMutableList()
            }

            loadGroups()
            loadChannels()
            loadingLayout.visibility = View.GONE
        }

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                applyFilter()
            }
        })

        listChannels.setOnItemClickListener { _, _, position, _ ->
            ChannelRepository.setChannels(filteredChannels)
            startActivity(Intent(this, PlayerActivity::class.java)
                .putExtra("url", visibleChannels[position].url))
        }
    }

    private fun applyFilter() {
        val search = searchBox.text.toString().lowercase()
        filteredChannels = allChannels.filter {
            it.name.lowercase().contains(search)
        }.toMutableList()
        loadChannels()
    }

    private fun loadChannels() {
        val endIndex = minOf(PAGE_SIZE, filteredChannels.size)
        visibleChannels = filteredChannels.subList(0, endIndex).toMutableList()
        listChannels.adapter = ChannelAdapter(this, visibleChannels, currentCategory)
    }

    private fun loadGroups() {
        groupContainer.removeAllViews()

        val groups = allChannels.map { it.group }
            .distinct()
            .filter { it.isNotEmpty() }
            .sorted()

        groups.forEach { group ->
            addGroupButton(group) {
                filteredChannels = allChannels.filter {
                    it.group == group
                }.toMutableList()
                loadChannels()
            }
        }
    }

    private fun addGroupButton(title: String, action: () -> Unit) {

        val button = Button(this)
        button.text = title
        button.setBackgroundResource(R.drawable.focus_selector)
        button.setTextColor(Color.WHITE)

        button.setOnClickListener { action() }

        button.setOnLongClickListener {

            val groups = allChannels.map { it.group }
                .distinct()
                .filter { it.isNotEmpty() }
                .sorted()

            val checked = BooleanArray(groups.size)

            AlertDialog.Builder(this)
                .setTitle("Grupları Gizle")
                .setMultiChoiceItems(groups.toTypedArray(), checked) { _, which, isChecked ->
                    checked[which] = isChecked
                }
                .setPositiveButton("Kaydet") { _, _ ->
                    HiddenGroupsManager.clearHiddenGroups(this)

                    for (i in groups.indices) {
                        if (checked[i]) {
                            HiddenGroupsManager.hideGroup(this, groups[i])
                        }
                    }
                    recreate()
                }
                .setNeutralButton("Tümünü Göster") { _, _ ->
                    HiddenGroupsManager.clearHiddenGroups(this)
                    recreate()
                }
                .setNegativeButton("İptal", null)
                .show()

            true
        }

        groupContainer.addView(button)
    }
}
