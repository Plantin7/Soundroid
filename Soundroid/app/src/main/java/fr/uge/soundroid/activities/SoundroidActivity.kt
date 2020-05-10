package fr.uge.soundroid.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import fr.uge.soundroid.R
import fr.uge.soundroid.activities.ui.main.SectionsPagerAdapter
import fr.uge.soundroid.adapters.SongListAdapter.ItemClickListener
import fr.uge.soundroid.models.Song


/**
 * Main activity
 * @author Vincent_Agullo
 */
class SoundroidActivity : AppCompatActivity(), ItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soundroid)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

    }

    override fun onItemClick(view: View, song: Song) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

// Snackbar widget