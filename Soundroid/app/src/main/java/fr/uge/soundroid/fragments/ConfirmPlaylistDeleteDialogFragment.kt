package fr.uge.soundroid.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import fr.uge.soundroid.R
import fr.uge.soundroid.models.Playlist
import fr.uge.soundroid.models.Soundtrack

class ConfirmPlaylistDeleteDialogFragment(val playlist: Playlist) : DialogFragment() {

    interface ConfirmDeleteListener {
        fun onDialogConfirmDeleteClick()
    }

    var listener: ConfirmDeleteListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(getString(R.string.remove_playlist_dialog_message, playlist.title))
            .setPositiveButton(R.string.remove_soundtrack_dialog_confirm) { _, _ ->
                listener?.onDialogConfirmDeleteClick()
            }
            .setNegativeButton(R.string.remove_soundtrack_dialog_cancel, null)

        return builder.create()
    }

}