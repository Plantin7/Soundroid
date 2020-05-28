package fr.uge.soundroid.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import fr.uge.soundroid.R
import fr.uge.soundroid.models.Playlist
import fr.uge.soundroid.models.Soundtrack

class ConfirmPlaylistRemoveDialogFragment(val playlist: Playlist, val soundtrack: Soundtrack) : DialogFragment() {

    interface ConfirmRemoveListener {
        fun onDialogConfirmRemoveClick()
    }

    var listener: ConfirmRemoveListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(getString(R.string.remove_soundtrack_from_playlist_dialog_message, soundtrack.title, playlist.title))
            .setPositiveButton(R.string.remove_soundtrack_from_playlist_dialog_confirm) { _, _ ->
                listener?.onDialogConfirmRemoveClick()
            }
            .setNegativeButton(R.string.remove_soundtrack_from_playlist_dialog_cancel, null)

        return builder.create()
    }

}