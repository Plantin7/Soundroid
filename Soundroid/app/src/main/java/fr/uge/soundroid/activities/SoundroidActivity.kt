package fr.uge.soundroid.activities

import android.Manifest
import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import fr.uge.soundroid.R
import fr.uge.soundroid.activities.ui.main.SectionsPagerAdapter
import fr.uge.soundroid.adapters.SongListAdapter
import fr.uge.soundroid.adapters.SongListAdapter.ItemClickListener
import fr.uge.soundroid.models.Song
import fr.uge.soundroid.utils.RequiringPermissionActivity
import kotlinx.android.synthetic.main.item_song.*


/**
 * Main activity
 * @author Vincent_Agullo
 */
// @ActivityMetadata(permissions={ Manifest.permission.SEND_SMS})
class SoundroidActivity : RequiringPermissionActivity(), ItemClickListener {

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
            Runnable { loadData()})

    }

    private fun loadData() {
        val songCursor: Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null)

        while(songCursor!=null && songCursor.moveToNext()) {
            var titleSong = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            var artisteSong = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            var duration = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
            //songModelData.add(Song("Y", titleSong, artisteSong, duration))
            Log.d("Testy", titleSong)
        }

        //songListAdapter = SongListAdapter(songModelData)
        //recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        //recyclerView.adapter = songListAdapter
    }

    override fun onItemClick(view: View, song: Song) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

// Snackbar widget