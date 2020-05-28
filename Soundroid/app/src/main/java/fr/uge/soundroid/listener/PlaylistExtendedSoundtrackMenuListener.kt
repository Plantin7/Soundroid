package fr.uge.soundroid.listener

import android.content.Context
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import fr.uge.soundroid.R
import fr.uge.soundroid.fragments.ConfirmPlaylistRemoveDialogFragment
import fr.uge.soundroid.models.Playlist
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.repositories.PlaylistRepository

class PlaylistExtendedSoundtrackMenuListener(
    val playlist: Playlist,
    soundtrack: Soundtrack,
    soundtrackList: ArrayList<Soundtrack>,
    index: Int,
    context: Context,
    fragmentManager: FragmentManager
) : DefaultPopupSoundtrackMenuListener(
    soundtrack,
    soundtrackList,
    index,
    context,
    fragmentManager
) {


    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when( item.itemId ) {
            R.id.popup_soundtrack_more_remove_from_playlist -> {
                val fragment = ConfirmPlaylistRemoveDialogFragment(playlist, soundtrack)
                fragment.listener = object : ConfirmPlaylistRemoveDialogFragment.ConfirmRemoveListener {
                    override fun onDialogConfirmRemoveClick() {
                        PlaylistRepository.realm.executeTransaction {
                            playlist.soundtracks.remove(soundtrack)
                            //soundtrackList.remove(soundtrack)
                            //it.copyToRealmOrUpdate(playlist)
                        }
                        // Don't need to notify changes, th RealmChangeListener do already the job.
                    }
                }
                fragment.show(fragmentManager, "confirmRemove")
                true
            }
            else -> {
                super.onMenuItemClick(item)
            }
        }
    }


}