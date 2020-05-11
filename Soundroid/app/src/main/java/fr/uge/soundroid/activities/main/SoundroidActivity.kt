package fr.uge.soundroid.activities.main

import android.Manifest
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import fr.uge.soundroid.R
import fr.uge.soundroid.adapters.SongListAdapter
import fr.uge.soundroid.adapters.SongListAdapter.ItemClickListener
import fr.uge.soundroid.fragments.SongListFragment
import fr.uge.soundroid.models.Song
import fr.uge.soundroid.utils.RequiringPermissionActivity


/**
 * Main activity
 * @author Vincent_Agullo
 */
// @ActivityMetadata(permissions={ Manifest.permission.SEND_SMS})
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

                //val songModelData = getSongs()
                //val ft = supportFragmentManager.beginTransaction()
                //ft.replace(R.id.songs_container, SongListFragment.create(songModelData))
                //ft.commit()
            })
    }

    // should we put this method elsewhere
    private fun getSongs(): ArrayList<Song> {
        val songCursor: Cursor? = contentResolver?.query( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null )
        val songModelData: ArrayList<Song> = ArrayList()
        while (songCursor != null && songCursor.moveToNext()) {
            val titleSong = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val artisteSong = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val duration = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
            val path = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)) // TODO
            songModelData.add(Song("Y", titleSong, artisteSong, duration, path))
        }
        return songModelData
    }
}

// test to avoid deprecated DATA
//val id = songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
//var path = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songCursor.getLong(id))
//var mediaSource = ProgressiveMediaSource()
