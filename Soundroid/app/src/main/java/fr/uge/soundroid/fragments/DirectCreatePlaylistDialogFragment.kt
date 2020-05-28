package fr.uge.soundroid.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import fr.uge.soundroid.R
import fr.uge.soundroid.models.Soundtrack

class DirectCreatePlaylistDialogFragment() : DialogFragment() {

    interface ConfirmCreateListener {
        fun onDialogConfirmCreateClick()
    }

    var listener: ConfirmCreateListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val activity = activity ?: return builder.create()

        val inflater = activity.layoutInflater

        builder.setMessage(getString(R.string.direct_create_playlist_dialog_message))

        builder.setView(inflater.inflate(R.layout.dialog_direct_create_playlist, null))

        builder.setPositiveButton(R.string.create_playlist_dialog_confirm) { _, _ ->
            listener?.onDialogConfirmCreateClick()
        }

        builder.setNegativeButton(R.string.create_playlist_dialog_cancel, null)
        return builder.create()
    }

}