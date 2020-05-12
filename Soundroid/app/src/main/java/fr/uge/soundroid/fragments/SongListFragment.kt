package fr.uge.soundroid.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.getIntent
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import fr.uge.soundroid.adapters.SongListAdapter.ItemClickListener
import fr.uge.soundroid.models.Song
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.repositories.SoundtrackRepository
import fr.uge.soundroid.services.MusicPlayerService


class SongListFragment : Fragment(), ItemClickListener {

    //private lateinit var songModelData:ArrayList<Song>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_song_list, container, false)
        //val songs = arguments?.getParcelableArrayList<Song>("song_model_data")
        val songModelData = ArrayList(SoundtrackRepository.findSoundtracksList(HashMap()))
        Log.d("Testy", songModelData.size.toString())
        val recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)
        val songListAdapter = SongListAdapter(songModelData)
        recyclerView.adapter = songListAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        songListAdapter.setClickListener(this)

        return rootView
    }

    override fun onItemClick(view: View, song: Soundtrack) {
        Toast.makeText(context, song.path, Toast.LENGTH_SHORT).show()
        val intent = Intent(context, MusicPlayerService::class.java)
        intent.putExtra("song", song.path)
        context?.startService(intent)
    }
}