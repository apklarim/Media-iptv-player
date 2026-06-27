package com.media.iptvplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.media.iptvplayer.model.Channel

class ChannelAdapter(
    context: Context,
    private val channels: List<Channel>,
    private val category: String
) : ArrayAdapter<Channel>(
    context,
    0,
    channels
) {

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        val view = convertView ?: LayoutInflater
            .from(context)
            .inflate(
                R.layout.item_channel,
                parent,
                false
            )

        val channel = channels[position]

        val logo =
            view.findViewById<ImageView>(
                R.id.imgLogo
            )

        val name =
            view.findViewById<TextView>(
                R.id.txtChannelName
            )

        name.text =
            if (channel.isFavorite)
                "⭐ ${channel.name}"
            else
                channel.name

        if (channel.logo.isNotEmpty()) {

            logo.load(channel.logo) {

                crossfade(false)

                placeholder(
                    R.drawable.ic_media_logo
                )

                error(
                    R.drawable.ic_media_logo
                )

                allowHardware(true)
            }

        } else {

            logo.setImageResource(
                R.drawable.ic_media_logo
            )
        }

        return view
    }
}
