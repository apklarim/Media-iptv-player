package com.media.iptvplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
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

        // Performans için sabit logo

        logo.setImageResource(
            R.drawable.ic_media_logo
        )

        // TV / TV Box odak animasyonu

        view.isFocusable = true
        view.isFocusableInTouchMode = true

        view.setOnFocusChangeListener {
                v, hasFocus ->

            if (hasFocus) {

                v.startAnimation(
                    AnimationUtils.loadAnimation(
                        context,
                        R.anim.focus_scale
                    )
                )

                v.elevation = 20f

            } else {

                v.startAnimation(
                    AnimationUtils.loadAnimation(
                        context,
                        R.anim.focus_unscale
                    )
                )

                v.elevation = 0f
            }
        }

        return view
    }
}
