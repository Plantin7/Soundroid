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
import fr.uge.soundroid.adapters.SongListAdapter.ItemClickListener
import fr.uge.soundroid.listener.DefaultPopupSoundtrackMenuListener
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.repositories.SoundtrackRepository
import io.realm.Realm
import io.realm.RealmChangeListener


class SongListFragment : Fragment(), ItemClickListener {
    private val soundtrackList = ArrayList<Soundtrack>()
    private val songListAdapter = SongListAdapter(soundtrackList)
    private lateinit var recyclerView: RecyclerView
    private lateinit var realm: Realm
    private lateinit var realmListener:RealmChangeListener<Realm>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_song_list, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)

        soundtrackList.clear()
        soundtrackList.addAll(SoundtrackRepository.findAll())
        songListAdapter.notifyDataSetChanged()

        recyclerView.adapter = songListAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        songListAdapter.setClickListener(this)

        realm = Realm.getDefaultInstance()
        realmListener = RealmChangeListener {
            soundtrackList.clear()
            soundtrackList.addAll(SoundtrackRepository.findAll())
            songListAdapter.notifyDataSetChanged()
        }
        realm.addChangeListener(realmListener)

        return rootView
    }

    override fun onItemClick(view: View, song: Soundtrack) {
        val intent = Intent(context, PlayerActivity::class.java)
        val position = soundtrackList.indexOf(song)
        intent.putExtra("soundtrackId", song.id).putExtra("position", position)
        startActivity(intent)
    }

    override fun onMoreClick(view: View) {
        if ( view.tag == null ) return

        val index: Int = view.tag as Int
        val soundtrack = soundtrackList[index]

        val popupMenu = PopupMenu(context, view.findViewById(R.id.item_song_more))
        popupMenu.inflate(R.menu.popup_soundtrack_more)
        if ( context == null ) return
        popupMenu.setOnMenuItemClickListener(DefaultPopupSoundtrackMenuListener(soundtrack, soundtrackList, index, context!!, parentFragmentManager))
        popupMenu.show()
    }
}