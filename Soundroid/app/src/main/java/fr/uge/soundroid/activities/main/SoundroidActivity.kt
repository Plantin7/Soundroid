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
import fr.uge.soundroid.adapters.SongListAdapter.ItemClickListener
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
        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        // Request for permission to access external storage
        runWithPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            "This activity requires to access external storage, please grant the permission",
            Runnable { Toast.makeText(this, "Cannot accessed external storage we do not have the permission", Toast.LENGTH_LONG).show()} ,
            Runnable { })
    }
}

// Snackbar widget