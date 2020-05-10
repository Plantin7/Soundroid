package fr.uge.soundroid.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.adapters.SongListAdapter
import fr.uge.soundroid.models.Song
import fr.uge.soundroid.adapters.SongListAdapter.ItemClickListener
import fr.uge.soundroid.services.MusicPlayerService


class SongListFragment : Fragment(), ItemClickListener {

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
            //val id = songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            //var path = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songCursor.getLong(id))
            //var mediaSource = ProgressiveMediaSource()
            val path = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)) // TODO
            songModelData.add(Song("Y", titleSong, artisteSong, duration, path))
        }
        val songListAdapter = SongListAdapter(songModelData)
        recyclerView.adapter = songListAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        songListAdapter.setClickListener(this)

        return rootView
    }

    override fun onItemClick(view: View, song: Song) {
        Toast.makeText(context, song.songPath, Toast.LENGTH_SHORT).show()
        val intent = Intent(context, MusicPlayerService::class.java)
        intent.putExtra("song", song.songPath)
        context?.startService(intent)
    }
}