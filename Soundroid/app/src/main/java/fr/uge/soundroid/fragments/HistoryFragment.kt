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
import fr.uge.soundroid.models.HistoryEntry
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.repositories.HistoryEntryRepository
import fr.uge.soundroid.adapters.SongListAdapter.ItemClickListener
import fr.uge.soundroid.listener.DefaultPopupSoundtrackMenuListener
import fr.uge.soundroid.repositories.PlaylistRepository
import io.realm.Realm
import io.realm.RealmChangeListener

class HistoryFragment : Fragment(), ItemClickListener {
    private val soundtrackHistoryList = ArrayList<Soundtrack>()
    private val soundtrackHistoryAdapter = SongListAdapter(soundtrackHistoryList)
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_song_list, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)

        soundtrackHistoryList.clear()
        soundtrackHistoryList.addAll(getAllSoundtracksFromHistory(HistoryEntryRepository.findAll()))
        soundtrackHistoryAdapter.notifyDataSetChanged()

        recyclerView.adapter = soundtrackHistoryAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        soundtrackHistoryAdapter.setClickListener(this)

        val realm = Realm.getDefaultInstance()
        val realmListener: RealmChangeListener<Realm> = RealmChangeListener {
            soundtrackHistoryList.clear()
            soundtrackHistoryList.addAll(getAllSoundtracksFromHistory(HistoryEntryRepository.findAll()))
            soundtrackHistoryAdapter.notifyDataSetChanged()
        }
        realm.addChangeListener(realmListener)

        return rootView
    }

    override fun onItemClick(view: View, song: Soundtrack) {
        val intent = Intent(context, PlayerActivity::class.java)
        val position = soundtrackHistoryList.indexOf(song)
        intent.putExtra("soundtrackId", song.id).putExtra("position", position)
        startActivity(intent)
    }

    override fun onMoreClick(view: View) {
        if ( view.tag == null ) return

        val index: Int = view.tag as Int
        val soundtrack = soundtrackHistoryList[index]

        val popupMenu = PopupMenu(context, view.findViewById(R.id.item_song_more))
        popupMenu.inflate(R.menu.popup_soundtrack_more)
        if ( context == null ) return
        popupMenu.setOnMenuItemClickListener(DefaultPopupSoundtrackMenuListener(soundtrack, soundtrackHistoryList, index, context!!, parentFragmentManager))
        popupMenu.show()
    }

    private fun getAllSoundtracksFromHistory(historyList:List<HistoryEntry>) : ArrayList<Soundtrack> {
        val soundtrackList = ArrayList<Soundtrack>()
        historyList.forEach { history ->
            history.soundtrack?.let { soundtrackList.add(it) }
        }
        return soundtrackList
    }

}