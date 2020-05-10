package fr.uge.soundroid.fragments

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.adapters.SongListAdapter
import fr.uge.soundroid.models.Song


class SongListFragment : Fragment() {

    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_song_list, container, false)

        val recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)

        val songCursor: Cursor? = activity?.contentResolver?.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null)

        val songModelData: ArrayList<Song> = ArrayList()

        while(songCursor!=null && songCursor.moveToNext()) {
            val titleSong = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val artisteSong = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val duration = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
            songModelData.add(Song("Y", titleSong, artisteSong, duration))
        }
        val songListAdapter = SongListAdapter(songModelData)
        recyclerView.adapter = songListAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        return rootView
    }
}