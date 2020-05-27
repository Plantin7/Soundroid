package fr.uge.soundroid.listener

import android.content.Context
import android.view.MenuItem
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import fr.uge.soundroid.R
import fr.uge.soundroid.fragments.AddToPlaylistDialogFragment
import fr.uge.soundroid.fragments.ConfirmPlaylistDeleteDialogFragment
import fr.uge.soundroid.fragments.ConfirmSoundtrackDeleteDialogFragment
import fr.uge.soundroid.fragments.CreatePlaylistDialogFragment
import fr.uge.soundroid.models.Playlist
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.repositories.PlaylistRepository
import fr.uge.soundroid.repositories.SoundtrackRepository

class DefaultPopupPlaylistMenuListener(
    val playlist: Playlist,
    val playlistList: ArrayList<Playlist>,
    val index: Int,
    val context: Context,
    private val fragmentManager: FragmentManager) : PopupMenu.OnMenuItemClickListener {

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when ( item.itemId ) {
            R.id.popup_playlist_more_delete -> {
                val fragment = ConfirmPlaylistDeleteDialogFragment(playlist)
                fragment.listener = object : ConfirmPlaylistDeleteDialogFragment.ConfirmDeleteListener {
                    override fun onDialogConfirmDeleteClick() {
                        playlistList.removeAt(index)
                        PlaylistRepository.deletePlaylist(playlist)
                        // Don't need to notify changes, th RealmChangeListener do already the job.
                    }
                }
                fragment.show(fragmentManager, "confirmDelete")
                true
            }
            else -> false
        }
    }
}