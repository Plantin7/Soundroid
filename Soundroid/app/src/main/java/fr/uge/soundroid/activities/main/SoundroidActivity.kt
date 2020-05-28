package fr.uge.soundroid.activities.main

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import fr.uge.soundroid.R
import fr.uge.soundroid.activities.others.SearchActivity
import fr.uge.soundroid.fragments.ChangeUrlDialogFragment
import fr.uge.soundroid.fragments.ConfirmPlaylistDeleteDialogFragment
import fr.uge.soundroid.models.Album
import fr.uge.soundroid.models.Artist
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.repositories.AlbumRepository
import fr.uge.soundroid.repositories.PlaylistRepository
import fr.uge.soundroid.repositories.SoundtrackRepository
import fr.uge.soundroid.utils.DatabaseService
import fr.uge.soundroid.utils.RequiringPermissionActivity
import fr.uge.soundroid.utils.WebsiteService
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets


/**
 * Main activity
 * @author Vincent_Agullo
 */
class SoundroidActivity : RequiringPermissionActivity() {

    private val EXPORT_INTENT_CODE: Int = 1

    private val IMPORT_INTENT_CODE: Int = 2

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soundroid)

        // Init the queue for the HTTP requests (Partage musical)
        WebsiteService.initQueue(this)

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
                val currentSoundtrackList = SoundtrackRepository.findAll()
                val songModelData = getSoundtracks()
                val diffList = ArrayList<Soundtrack>()
                for(songModel in songModelData) {
                    if(currentSoundtrackList.find {it.hashCode() == songModel.hashCode()} == null) {
                        diffList.add(songModel)
                    }
                }
                Log.d("Testy", diffList.toString())
                SoundtrackRepository.saveSoundtrackList(diffList)
                // TODO Remove removed soundtrack
                
                SoundtrackRepository.realm.executeTransaction {
                    for ( soundtrack in SoundtrackRepository.findAll() ) {
                        val album = soundtrack.album ?: continue
                        album.addSoundtrack(soundtrack)
                        it.copyToRealmOrUpdate(album)
                    }
                }
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
            newArtist.addSoundtrack(this)
            /** Artist of the soundtrack */
            artist = newArtist

            /** Album of the soundtrack */
            val newAlbum = createAlbum(albumName, albumPicture, newArtist)
            album = newAlbum
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

    /**
     * Add the menu to the main activity.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
     * Click event on the menus
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when ( item.itemId ) {
            R.id.main_menu_item_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
            R.id.main_menu_export_database -> {
                var chooseFile = Intent(Intent.ACTION_CREATE_DOCUMENT)
                chooseFile.type = "*/*"
                chooseFile = Intent.createChooser(chooseFile, "Select directory a file")
                startActivityForResult(chooseFile, EXPORT_INTENT_CODE)
                Log.i(SoundroidActivity::class.java.name, "Database dump saved.")
            }
            R.id.main_menu_import_database -> {
                var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
                chooseFile.type = "*/*"
                chooseFile = Intent.createChooser(chooseFile, "Select the file")
                startActivityForResult(chooseFile, IMPORT_INTENT_CODE)
            }
            R.id.main_menu_change_website -> {
                val fragment = ChangeUrlDialogFragment(WebsiteService.url)
                fragment.listener = object : ChangeUrlDialogFragment.ConfirmChangeListener {
                    override fun onDialogConfirmChangeClick() {
                        val editable = fragment.dialog?.findViewById<EditText>(R.id.fragment_dialog_change_website_edit_text)?.text
                            ?: return
                        WebsiteService.url = editable.toString()
                    }
                }

                fragment.show(supportFragmentManager, "confirmDelete")
            }
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            EXPORT_INTENT_CODE -> {
                if (resultCode == -1) {
                    val fileUri = data?.data
                    if ( fileUri != null ) {
                        val json = DatabaseService.exportToJson()
                        val os = contentResolver.openOutputStream(fileUri)
                        os?.write(json.toByteArray(Charset.forName("UTF-8")))
                        os?.close()
                    }

                }
            }
            IMPORT_INTENT_CODE -> {
                if (resultCode == -1) {
                    val fileUri = data?.data
                    if ( fileUri != null ) {
                        val ist = contentResolver.openInputStream(fileUri)
                        val json = ist?.reader(Charset.forName("UTF-8"))?.readText()
                        ist?.close()
                        if ( json != null ) {
                            DatabaseService.importFromJson(json)
                        }
                    }
                }
            }
        }
    }
}