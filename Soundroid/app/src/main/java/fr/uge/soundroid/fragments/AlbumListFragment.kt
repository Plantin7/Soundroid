package fr.uge.soundroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.adapters.AlbumAdapter
import fr.uge.soundroid.models.Album
import fr.uge.soundroid.repositories.AlbumRepository

class AlbumListFragment  : Fragment(), View.OnClickListener  {

    private val albumList: ArrayList<Album> = ArrayList()

    private val adapter = AlbumAdapter(albumList, this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_album_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_album_recycler_view)

        albumList.addAll(AlbumRepository.findAll())

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter

        return view
    }

    override fun onClick(v: View) {
        if ( v.tag == null ) return

        val index = v.tag as Int
        val selectedAlbum = albumList[index]

        // TODO
    }

}