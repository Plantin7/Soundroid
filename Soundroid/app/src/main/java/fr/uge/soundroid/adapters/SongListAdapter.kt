package fr.uge.soundroid.adapters

import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.models.Soundtrack
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException


/**
 * This class implement an RecyclerView to display song list and to know which song the user pressed
 * @author Vincent Agullo
 */

class SongListAdapter(private val songs: ArrayList<Soundtrack>) :
    RecyclerView.Adapter<SongListAdapter.ViewHolder>() {

    private lateinit var mClickListener: ItemClickListener

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val albumPicture: ImageView = itemView.findViewById(R.id.album_picture)
        private val titleSong: TextView = itemView.findViewById(R.id.title_song)
        private val artisteSong: TextView = itemView.findViewById(R.id.artiste_song)

        /** Init the clickListener*/
        init {
            itemView.setOnClickListener(this)
        }

        /** Update the state of the song */
        fun update(song: Soundtrack) {
            albumPicture.setImageBitmap(song.album?.getBitmap(itemView.context))
            titleSong.text = song.title
            artisteSong.text = song.artist?.name
        }

        /** A song has been clicked */
        override fun onClick(v: View) {
            mClickListener.onItemClick(v, getMusic(adapterPosition))
        }
    }

    /** */
    private fun getMusic(position: Int): Soundtrack {
        return songs[position]
    }

    /** Create a viewHolder*/
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.item_song,
                viewGroup,
                false
            )
        )
    }

    /** Binding soundtrack with the viewHolder */
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.update(songs[i])
    }

    /** get the number of cards */
    override fun getItemCount(): Int {
        return songs.size
    }

    /** allows clicks events to be caught */
    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener
    }

    /** parent activity will implement this method to respond to click events */
    interface ItemClickListener {
        fun onItemClick(view: View, song: Soundtrack)
    }

    /**-------------- Unused ------------------------------------------*/
    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }

    fun bitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

}