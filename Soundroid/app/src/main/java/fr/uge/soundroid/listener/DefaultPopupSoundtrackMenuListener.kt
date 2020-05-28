package fr.uge.soundroid.listener

import android.content.Context
import android.view.MenuItem
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import fr.uge.soundroid.R
import fr.uge.soundroid.fragments.AddToPlaylistDialogFragment
import fr.uge.soundroid.fragments.ConfirmSoundtrackDeleteDialogFragment
import fr.uge.soundroid.fragments.CreatePlaylistDialogFragment
import fr.uge.soundroid.models.Playlist
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.repositories.PlaylistRepository
import fr.uge.soundroid.repositories.SoundtrackRepository

open class DefaultPopupSoundtrackMenuListener(
    val soundtrack: Soundtrack,
    val soundtrackList: ArrayList<Soundtrack>,
    val index: Int,
    val context: Context,
    val fragmentManager: FragmentManager) : PopupMenu.OnMenuItemClickListener {

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when ( item.itemId ) {
            R.id.popup_soundtrack_more_add_to_playlist -> {
                val playlistList = PlaylistRepository.findAll()
                val fragment = AddToPlaylistDialogFragment(soundtrack, playlistList)
                fragment.listener = object : AddToPlaylistDialogFragment.AddToPlayListListener {
                    override fun onDialogConfirmAddClick() {
                        val currentIndex = fragment.currentIndex
                        val playlist = playlistList[currentIndex]
                        PlaylistRepository.realm.executeTransaction { realm ->
                            playlist.addSoundtrack(soundtrack)
                            realm.copyToRealmOrUpdate(playlist)
                        }
                    }
                }
                fragment.show(fragmentManager, "confirmAdd")
                return true
            }
            R.id.popup_soundtrack_more_create_playlist -> {
                val fragment = CreatePlaylistDialogFragment(soundtrack)
                fragment.listener = object : CreatePlaylistDialogFragment.ConfirmCreateListener {
                    override fun onDialogConfirmCreateClick() {
                        val editable = fragment.dialog?.findViewById<EditText>(R.id.dialog_create_playlist_edit_text)?.text
                            ?: return
                        val title = editable.toString()
                        val playlist = Playlist(null, title)

                        val existing = PlaylistRepository.findPlaylistById(playlist.hashCode())
                        if ( existing == null ) {
                            playlist.addSoundtrack(soundtrack)
                            playlist.initPrimaryKey()
                            PlaylistRepository.savePlaylist(playlist)
                            Toast.makeText(context, R.string.create_playlist_toast_success, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, R.string.create_playlist_toast_already_exist_error, Toast.LENGTH_LONG).show()
                        }

                    }
                }
                fragment.show(fragmentManager, "confirmCreate")
                return true
            }
            R.id.popup_soundtrack_more_delete -> {
                val fragment = ConfirmSoundtrackDeleteDialogFragment(soundtrack)
                fragment.listener = object : ConfirmSoundtrackDeleteDialogFragment.ConfirmDeleteListener {
                    override fun onDialogConfirmDeleteClick() {
                        // TODO : Delete the element of the phone
                        soundtrackList.removeAt(index)
                        SoundtrackRepository.deleteSoundtrack(soundtrack)
                        // Don't need to notify changes, th RealmChangeListener do already the job.
                    }
                }
                fragment.show(fragmentManager, "confirmDelete")
                return true
            }
            else -> return false
        }
    }
}