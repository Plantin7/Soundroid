package fr.uge.soundroid.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.uge.soundroid.R
import fr.uge.soundroid.activities.others.PlaylistActivity
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

        val floatingActionButton = view.findViewById<FloatingActionButton>(R.id.fragment_playlist_list_add)
        floatingActionButton.setOnClickListener {
            val fragment = DirectCreatePlaylistDialogFragment()
            fragment.listener = object : DirectCreatePlaylistDialogFragment.ConfirmCreateListener {
                override fun onDialogConfirmCreateClick() {
                    val editable = fragment.dialog?.findViewById<EditText>(R.id.dialog_direct_create_playlist_edit_text)?.text
                        ?: return
                    val title = editable.toString()
                    val playlist = Playlist(null, title)

                    val existing = PlaylistRepository.findPlaylistById(playlist.hashCode())
                    if ( existing == null ) {
                        playlist.initPrimaryKey()
                        PlaylistRepository.savePlaylist(playlist)
                        Toast.makeText(context, R.string.create_playlist_toast_success, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, R.string.create_playlist_toast_already_exist_error, Toast.LENGTH_LONG).show()
                    }

                }
            }
            fragment.show(parentFragmentManager, "confirmCreate")
        }

        return view
    }

    override fun onItemClick(view: View, index: Int) {
        val intent = Intent(context, PlaylistActivity::class.java)
        intent.putExtra("playlistId", playlistList[index].id)
        startActivity(intent)
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