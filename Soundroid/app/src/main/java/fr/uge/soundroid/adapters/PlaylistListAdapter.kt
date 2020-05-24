package fr.uge.soundroid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.models.Playlist

class PlaylistListAdapter(val playlistList: ArrayList<Playlist>,
                          private val itemClickListener: ItemClickListener
    ) : RecyclerView.Adapter<PlaylistListAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(view: View, index: Int)
        fun onMoreClick(view: View, index: Int)
    }

    class ViewHolder(itemView: View) :  RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.item_playlist_card_view)
        val textTitle = itemView.findViewById<TextView>(R.id.item_playlist_text_view_title)
        val textNumber = itemView.findViewById<TextView>(R.id.item_playlist_text_view_song_number)
        val moreButton = itemView.findViewById<ImageButton>(R.id.item_playlist_more)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.item_playlist, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlist = playlistList[position]

        holder.textTitle.text = playlist.title
        holder.textNumber.text =  "${playlist.soundtracks.size} musiques"
        holder.cardView.tag = position
        holder.cardView.setOnClickListener {
            itemClickListener.onItemClick(it, position)
        }
        holder.moreButton.setOnClickListener {
            itemClickListener.onMoreClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return playlistList.size
    }
}