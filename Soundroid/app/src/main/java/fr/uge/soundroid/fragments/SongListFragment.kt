package fr.uge.soundroid.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.activities.others.PlayerActivity
import fr.uge.soundroid.adapters.SongListAdapter
import fr.uge.soundroid.adapters.SongListAdapter.ItemClickListener
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.repositories.SoundtrackRepository
import io.realm.Realm
import io.realm.RealmChangeListener


class SongListFragment : Fragment(), ItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var songListAdapter: SongListAdapter
    private lateinit var realm: Realm
    private lateinit var realmListener:RealmChangeListener<Realm>
    private lateinit var soundtrackModelData : ArrayList<Soundtrack>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_song_list, container, false)
        soundtrackModelData = ArrayList(SoundtrackRepository.findSoundtracksList(HashMap()))
        recyclerView = rootView.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)
        songListAdapter = SongListAdapter(soundtrackModelData)
        recyclerView.adapter = songListAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        songListAdapter.setClickListener(this)

        /*Realm.getDefaultInstance().use {
            it.executeTransactionAsync {
                val ne = ArrayList(SoundtrackRepository.findSoundtracksList(HashMap()))
                songListAdapter = SongListAdapter(ne)
                recyclerView.adapter = songListAdapter
                songListAdapter.setClickListener(this)
            }
        }*/
        // TODO
        realm = Realm.getDefaultInstance()
        realmListener = RealmChangeListener {
            val ne = ArrayList(SoundtrackRepository.findSoundtracksList(HashMap()))
            songListAdapter = SongListAdapter(ne)
            recyclerView.adapter = songListAdapter
            songListAdapter.setClickListener(this)
        }
        realm.addChangeListener(realmListener)

        return rootView
    }

    override fun onItemClick(view: View, song: Soundtrack) {
        val intent = Intent(context, PlayerActivity::class.java)
        val position = soundtrackModelData.indexOf(song)
        intent.putExtra("soundtrackId", song.id).putExtra("position", position)
        startActivity(intent)
    }
}