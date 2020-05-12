package fr.uge.soundroid.activities.main

import android.Manifest
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import fr.uge.soundroid.R
import fr.uge.soundroid.models.Artist
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.repositories.SoundtrackRepository
import fr.uge.soundroid.utils.RequiringPermissionActivity


/**
 * Main activity
 * @author Vincent_Agullo
 */
class SoundroidActivity : RequiringPermissionActivity(){

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soundroid)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        // Request for permission to access external storage
        runWithPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            "This activity requires to access external storage, please grant the permission",
            Runnable { Toast.makeText(this, "Cannot accessed external storage we do not have the permission", Toast.LENGTH_LONG).show()} ,
            Runnable {
                val songModelData = getSongs()
                SoundtrackRepository.saveSoundtrackList(songModelData)
            })
    }

    // should we put this method elsewhere
    private fun getSongs(): ArrayList<Soundtrack> {
        val songCursor: Cursor? = contentResolver?.query( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null )
        val songModelData: ArrayList<Soundtrack> = ArrayList()
        //var changeId = 1 //TODO
        while (songCursor != null && songCursor.moveToNext()) {
            val titleSong = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val artisteSong = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val duration = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
            val pathSong = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)) // TODO

            songModelData.add(Soundtrack().apply {
                title = titleSong
                artist = Artist().apply { name = artisteSong }
                seconds = duration.toInt()
                path = pathSong
                //album = Album().apply { name= }
            })
        }
        Log.d("Testy", "Activity :" + songModelData.size)
        return songModelData
    }
}

// test to avoid deprecated DATA
//val id = songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
//var path = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songCursor.getLong(id))
//var mediaSource = ProgressiveMediaSource()

//val ft = supportFragmentManager.beginTransaction()
//ft.replace(R.id.songs_container, SongListFragment.create(songModelData))
//ft.commit()
