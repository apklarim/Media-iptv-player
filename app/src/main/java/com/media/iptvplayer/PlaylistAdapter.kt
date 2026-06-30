package com.media.iptvplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.media.iptvplayer.model.Playlist

class PlaylistAdapter(
    context: Context,
    private val playlists: List<Playlist>
) : ArrayAdapter<Playlist>(
    context,
    0,
    playlists
) {

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        val view = convertView ?: LayoutInflater
            .from(context)
            .inflate(
                R.layout.item_playlist,
                parent,
                false
            )

        val playlist = playlists[position]

        val txtName =
            view.findViewById<TextView>(
                R.id.txtPlaylistName
            )

        val txtType =
            view.findViewById<TextView>(
                R.id.txtPlaylistType
            )

        val txtExpire =
            view.findViewById<TextView>(
                R.id.txtPlaylistExpire
            )

        txtName.text = playlist.name
        txtType.text = "Tür : ${playlist.type}"

        txtExpire.text =
            if (playlist.type == "XTREAM")
                "Bitiş : Bilinmiyor"
            else
                "Bitiş : Yok"

        return view
    }
}
