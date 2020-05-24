package fr.uge.soundroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.adapters.PlaylistListAdapter
import fr.uge.soundroid.listener.DefaultPopupPlaylistMenuListener
import fr.uge.soundroid.listener.DefaultPopupSoundtrackMenuListener
import fr.uge.soundroid.models.Playlist
import fr.uge.soundroid.repositories.PlaylistRepository
import io.realm.Realm
import io.realm.RealmChangeListener

class PlaylistListFragment : Fragment(), PlaylistListAdapter.ItemClickListener {

    private val playlistList: ArrayList<Playlist> = ArrayList()

    private val adapter = PlaylistListAdapter(playlistList, this)

    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_playlist_list, container, false)

        playlistList.clear()
        playlistList.addAll(PlaylistRepository.findAll())

        recyclerView = view.findViewById(R.id.fragment_playlist_list_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val realm = Realm.getDefaultInstance()
        val realmListener: RealmChangeListener<Realm> = RealmChangeListener {
            playlistList.clear()
            playlistList.addAll(PlaylistRepository.findAll())
            adapter.notifyDataSetChanged()
        }
        realm.addChangeListener(realmListener)

        return view
    }

    override fun onItemClick(view: View, index: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMoreClick(view: View, index: Int) {
        val playlist = playlistList[index]

        val popupMenu = PopupMenu(context, view.findViewById(R.id.item_playlist_more))
        popupMenu.inflate(R.menu.popup_playlist_more)
        if ( context == null ) return
        popupMenu.setOnMenuItemClickListener(DefaultPopupPlaylistMenuListener(playlist, playlistList, index, context!!, parentFragmentManager))
        popupMenu.show()
    }

}