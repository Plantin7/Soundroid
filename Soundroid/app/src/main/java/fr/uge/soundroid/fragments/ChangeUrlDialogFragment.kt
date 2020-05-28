package fr.uge.soundroid.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import fr.uge.soundroid.R
import fr.uge.soundroid.models.Soundtrack
import kotlinx.android.synthetic.main.fragment_dialog_change_website.*

class ChangeUrlDialogFragment(val currentUrl: String) : DialogFragment() {

    interface ConfirmChangeListener {
        fun onDialogConfirmChangeClick()
    }

    var listener: ConfirmChangeListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val activity = activity ?: return builder.create()

        val inflater = activity.layoutInflater
        val inflated = inflater.inflate(R.layout.fragment_dialog_change_website, null)
        val editText = inflated.findViewById<EditText>(R.id.fragment_dialog_change_website_edit_text)
        editText.text.insert(0, currentUrl)

        builder.setView(inflated)


        builder.setPositiveButton(R.string.create_playlist_dialog_confirm) { _, _ ->
            listener?.onDialogConfirmChangeClick()
        }

        builder.setNegativeButton(R.string.create_playlist_dialog_cancel, null)
        return builder.create()
    }

}