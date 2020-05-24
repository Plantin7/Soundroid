package fr.uge.soundroid.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import fr.uge.soundroid.R
import fr.uge.soundroid.models.Playlist
import fr.uge.soundroid.models.Soundtrack

class AddToPlaylistDialogFragment(val soundtrack: Soundtrack, private val playlistList : List<Playlist>) : DialogFragment() {

    var currentIndex: Int = 0

    interface AddToPlayListListener {
        fun onDialogConfirmAddClick()
    }

    var listener: AddToPlayListListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val nameList: List<String> = playlistList.map {
            it.title!!
        }
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.add_to_playlist_dialog_title))
        builder.setSingleChoiceItems(nameList.toTypedArray(), 0) { _, i ->
            currentIndex = i
        }

        if ( nameList.isEmpty() ) {
            builder.setMessage(getString(R.string.add_to_playlist_dialog_no_playlist))
        }

        builder.setPositiveButton(R.string.add_to_playlist_dialog_confirm) { _, _ ->
            listener?.onDialogConfirmAddClick()
        }

        builder.setNegativeButton(R.string.add_to_playlist_dialog_cancel, null)

        return builder.create()
    }

}