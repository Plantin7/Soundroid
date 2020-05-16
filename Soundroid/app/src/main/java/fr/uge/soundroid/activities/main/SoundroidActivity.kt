package fr.uge.soundroid.activities.main

import android.Manifest
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import fr.uge.soundroid.R
import fr.uge.soundroid.models.Album
import fr.uge.soundroid.models.Artist
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.repositories.SoundtrackRepository
import fr.uge.soundroid.utils.RequiringPermissionActivity
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException


/**
 * Main activity
 * @author Vincent_Agullo
 */
class SoundroidActivity : RequiringPermissionActivity() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soundroid)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        /** Request for permission to access external storage */
        runWithPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            "This activity requires to access external storage, please grant the permission",
            Runnable {
                Toast.makeText(
                    this,
                    "Cannot accessed external storage we do not have the permission",
                    Toast.LENGTH_LONG
                ).show()
            },
            Runnable {
                val songModelData = getSoundtracks()
                SoundtrackRepository.saveSoundtrackList(songModelData)
            })
    }

    /** Find and retrieve music stored on the phone*/
    private fun getSoundtracks(): ArrayList<Soundtrack> {
        val songCursor: Cursor? = contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val songModelData: ArrayList<Soundtrack> = ArrayList()

        while (songCursor != null && songCursor.moveToNext()) {
            val title =
                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val path = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                songCursor.getLong(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
            ).toString()
            val duration =
                songCursor.getInt(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
            val artist =
                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val album =
                songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
            val albumUri = ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"),
                songCursor.getLong(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
            ).toString()

            // Adding soundtrack into a list
            songModelData.add(createSoundtrack(title, path, duration, artist, album, albumUri))
        }
        songCursor?.close()
        return songModelData
    }

    /** Create soundtrack */
    private fun createSoundtrack( title: String, path: String, duration: Int, artistName: String, albumName: String, albumPicture: String ): Soundtrack {
        return Soundtrack().apply {
            this.title = title
            this.path = path
            this.duration = duration

            val newArtist = createArtist(artistName)
            /** Artist of the soundtrack */
            artist = newArtist

            /** Album of the soundtrack */
            album = createAlbum(albumName, albumPicture, newArtist)
            initPrimaryKey()
        }
    }

    /** Create Artist */
    private fun createArtist(name: String): Artist {
        return Artist().apply {
            this.name = name
            initPrimaryKey()
        }
    }

    /** Create Album*/
    private fun createAlbum(name: String, albumPicture: String, artist: Artist): Album {
        return Album().apply {
            this.name = name
            this.albumPicture = albumPicture
            this.artist = artist
            initPrimaryKey()
        }
    }
}