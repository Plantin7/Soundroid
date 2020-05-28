package fr.uge.soundroid.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.activities.others.PlayerActivity
import fr.uge.soundroid.adapters.SongListAdapter
import fr.uge.soundroid.listener.PlaylistExtendedSoundtrackMenuListener
import fr.uge.soundroid.models.Playlist
import fr.uge.soundroid.models.Soundtrack
import io.realm.Realm
import io.realm.RealmChangeListener

class PlaylistSoundtrackListFragment(private val playlist: Playlist) : Fragment(), SongListAdapter.ItemClickListener {

    private val soundtrackList: ArrayList<Soundtrack> = ArrayList()

    private val adapter = SongListAdapter(soundtrackList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_playlist_soundtrack_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_playlist_soundtrack_list_recycler_view)
        adapter.setClickListener(this)

        soundtrackList.clear()
        soundtrackList.addAll(playlist.soundtracks)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val realm = Realm.getDefaultInstance()
        val realmListener: RealmChangeListener<Realm> = RealmChangeListener {
            soundtrackList.clear()
            soundtrackList.addAll(playlist.soundtracks)
            adapter.notifyDataSetChanged()
        }
        realm.addChangeListener(realmListener)

        return view
    }

    override fun onItemClick(view: View, song: Soundtrack) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("soundtrackId", song.id)
        intent.putExtra("playlistId", playlist.id)
        startActivity(intent)
    }

    override fun onMoreClick(view: View) {
        if ( view.tag == null ) return

        val index: Int = view.tag as Int
        val soundtrack = soundtrackList[index]

        val popupMenu = PopupMenu(context, view.findViewById(R.id.item_song_more))
        popupMenu.inflate(R.menu.playlist_extended_popup_soundtrack_more)
        if ( context == null ) return
        popupMenu.setOnMenuItemClickListener(PlaylistExtendedSoundtrackMenuListener(
            playlist, soundtrack, soundtrackList, index, context!!, parentFragmentManager
        ))
        popupMenu.show()
    }


}