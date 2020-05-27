package fr.uge.soundroid.activities.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import fr.uge.soundroid.R
import fr.uge.soundroid.fragments.AlbumListFragment
import fr.uge.soundroid.fragments.HistoryFragment
import fr.uge.soundroid.fragments.PlaylistListFragment
import fr.uge.soundroid.fragments.SongListFragment


private val TAB_TITLES = arrayOf(
    R.string.Songs,
    R.string.Albums,
    R.string.Artistes,
    R.string.Playlists,
    R.string.History
)

/**
 * @author Vincent_Agullo
 */
class SectionsPagerAdapter(private val context: Context, private val fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            // Songs
            0 -> SongListFragment()
            // Albums
            1 -> AlbumListFragment()
            //Artists
            2 -> SongListFragment()
            // Playlists
            3 -> PlaylistListFragment()
            //History
            4 -> HistoryFragment()
            else -> SongListFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}