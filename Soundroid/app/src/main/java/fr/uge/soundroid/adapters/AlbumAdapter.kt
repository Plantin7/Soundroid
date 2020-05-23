package fr.uge.soundroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.models.Album

class AlbumAdapter(private val albumList: ArrayList<Album>,
                   private val itemClickListener: View.OnClickListener)
    : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        val text = itemView.findViewById<TextView>(R.id.item_album_name)
        val albumPicture = itemView.findViewById<ImageView>(R.id.item_album_picture)
        val artist = itemView.findViewById<TextView>(R.id.item_album_artist_name)
        val cardView = itemView.findViewById<CardView>(R.id.item_album_card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.item_album, parent, false)
        return ViewHolder(viewItem, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albumList[position]
        holder.text.text = album.getNameForSearch()
        holder.artist.text = album.artist?.name
        holder.cardView.tag = position
        holder.cardView.setOnClickListener(itemClickListener)
        holder.albumPicture.setImageBitmap(album.getBitmap(holder.context))
    }

    override fun getItemCount(): Int {
        return albumList.size
    }
}