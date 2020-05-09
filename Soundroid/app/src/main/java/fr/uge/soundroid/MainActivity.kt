package fr.uge.soundroid

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.adapters.SongListAdapter
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import fr.uge.soundroid.adapters.SongListAdapter.ItemClickListener
import fr.uge.soundroid.models.*
import fr.uge.soundroid.repositories.ArtistRepository
import fr.uge.soundroid.repositories.PlaylistRepository
import fr.uge.soundroid.repositories.SoundtrackRepository

class MainActivity : AppCompatActivity(), ItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private val songModelData: ArrayList<Song> = ArrayList()
    private lateinit var songListAdapter:SongListAdapter

    companion object {
        val PERMISSION_REQUEST_CODE = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)

        /* Guillaume tests : */
        val artist1 = Artist().apply {
            name = "artist 1"
        }

        val album1 = Album().apply {
            name = "album 1"
            artist = artist1
        }

        val soundtrack1 = Soundtrack().apply {
            title = "soundtrack1"
            artist = artist1
            album = album1
            path = "test/path"
            seconds = 10
        }

        val playlist = Playlist().apply {
            title = "playlist1"
            soundtracks.add(soundtrack1)
        }

        // to save :
        PlaylistRepository.savePlaylist(playlist)

        val getSoundtrack = SoundtrackRepository.findSingleSoundtrack(mapOf("title" to "soundtrack1"))
        Log.i("GUILLAUME", "soundtrack = $getSoundtrack")
        Log.i("GUILLAUME", "$getSoundtrack artist = ${getSoundtrack?.artist?.name}")
        Log.i("GUILLAUME", "PLAYLIST = $playlist FIRSTSONG = ${playlist.soundtracks.first()?.title}")
        /* End guillaume tests*/

        if(ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
        else {
            loadData()
        }
    }

    fun loadData() {
        // TODO Change the position of this code
        var songCursor:Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null)

        while(songCursor!=null && songCursor.moveToNext()) {
            var titleSong = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            var artisteSong = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            var duration = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
            songModelData.add(Song("Y", titleSong, artisteSong, duration))
        }
        songListAdapter = SongListAdapter(songModelData)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = songListAdapter
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT).show()
                loadData()
            }
        }

    }

    override fun onItemClick(view: View, song: Song) {
    }
}
