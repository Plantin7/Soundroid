package fr.uge.soundroid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.models.Song

/**
 * This class implement an RecyclerView to display song list and to know which song the user pressed
 * @author Vincent Agullo
 */

class SongListAdapter(private val songs:List<Song>) : RecyclerView.Adapter<SongListAdapter.ViewHolder>() {

    private lateinit var mClickListener: ItemClickListener

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val albumPicture:ImageView = itemView.findViewById(R.id.album_picture)
        private val titleSong:TextView = itemView.findViewById(R.id.title_song)
        private val artisteSong:TextView = itemView.findViewById(R.id.artiste_song)

        /** Init the clickListener*/
        init {
            itemView.setOnClickListener(this)
        }

        /** Update the state of the song */
        fun update(song: Song) {
            //albumPicture.setImageBitmap(song.getBitmap(albumPicture.context)) TODO
            titleSong.text = song.title
            artisteSong.text = song.artiste
        }

        /** A song has been clicked */
        override fun onClick(v: View) {
            mClickListener.onItemClick(v, getMusic(adapterPosition))
        }
    }

    /** */
    private fun getMusic(position:Int): Song {
        return songs[position]
    }

    /** Create a viewHolder*/
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_song, viewGroup,false))
    }

    /** Binding card with the viewHolder */
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.update(songs[i])
    }

    /** get the number of cards */
    override fun getItemCount(): Int {
        return songs.size
    }

    /** parent activity will implement this method to respond to click events */
    interface ItemClickListener {
        fun onItemClick(view: View, song:Song)
    }
}