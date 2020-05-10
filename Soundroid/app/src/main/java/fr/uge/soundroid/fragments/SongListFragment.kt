package fr.uge.soundroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.adapters.SongListAdapter
import fr.uge.soundroid.models.Song


class SongListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_song_list, container, false)

        val rv = rootView.findViewById(R.id.recyclerView) as RecyclerView
        rv.setHasFixedSize(true)
        val adapter = SongListAdapter(listOf(Song("A", "B", "C", "D")))
        rv.adapter = adapter

        val llm = LinearLayoutManager(activity)
        rv.layoutManager = llm

        return rootView
    }
}