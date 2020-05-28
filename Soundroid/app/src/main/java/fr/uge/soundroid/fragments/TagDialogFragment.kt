package fr.uge.soundroid.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.adapters.TagListAdapter
import fr.uge.soundroid.adapters.TagListAdapter.RemoveTagListener
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.models.Tag
import fr.uge.soundroid.repositories.SoundtrackRepository
import fr.uge.soundroid.repositories.TagRepository
import io.realm.Realm


class TagDialogFragment : DialogFragment(), RemoveTagListener {

    //Recyclerview & Adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tagListAdapter: TagListAdapter
    private lateinit var tags: ArrayList<Tag>
    private var soundtrack: Soundtrack? = null

    //widgets
    private lateinit var mTagInput: EditText
    private lateinit var mActionBack: Button
    private lateinit var mActionAdd: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        val view = inflater.inflate(R.layout.fragment_tag_dialog, container, false)
        val soundtrackId = arguments?.getInt("soundtrackId")
        soundtrack = soundtrackId?.let { SoundtrackRepository.findSoundtrackById(it) }
        tags = ArrayList(soundtrack!!.tags)

        recyclerView = view.findViewById(R.id.tag_dialog_recyclerview) as RecyclerView
        recyclerView.setHasFixedSize(true)
        tagListAdapter = TagListAdapter(tags)
        recyclerView.adapter = tagListAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        tagListAdapter.setRemoveTagListener(this)

        mTagInput = view.findViewById(R.id.tag_dialog_input_text)
        mTagInput.requestFocus()
        mActionAdd = view.findViewById(R.id.tag_dialog_add_button)
        mActionBack = view.findViewById(R.id.tag_dialog_back_button)

        mActionBack.setOnClickListener {
            dialog?.dismiss()
        }

        mActionAdd.setOnClickListener {
            val tag = mTagInput.text.toString()
            if (tag.isNotEmpty() && tag.length < 11 && !tagContains(tag)) {
                addNewTag(tag)
            } else {
                Toast.makeText(context, "Tag Error", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    companion object {
        fun createTagDialogFragment(soundtrackId: Int): TagDialogFragment {
            val tagDialogFragment = TagDialogFragment()
            val bundle = Bundle()
            bundle.putInt("soundtrackId", soundtrackId)
            tagDialogFragment.arguments = bundle
            return tagDialogFragment
        }
    }

    private fun addNewTag(tagName: String) {
        val newTag = Tag().apply { name = tagName; initPrimaryKey() }

        Realm.getDefaultInstance().executeTransaction { realm: Realm ->
            soundtrack?.addTag(newTag)
            realm.copyToRealmOrUpdate(soundtrack!!)
        }

        tags.add(newTag)
        tagListAdapter.notifyItemInserted(tags.size - 1)
    }

    override fun removeTag(tag: Tag) {
        val positionTag = tags.indexOf(tag)
        Realm.getDefaultInstance().executeTransaction { realm: Realm ->
            soundtrack?.tags?.remove(tag)
            realm.copyToRealmOrUpdate(soundtrack!!)
        }
        tags.remove(tag)
        tagListAdapter.notifyItemRemoved(positionTag)
        Toast.makeText(context, "Tag " + tag.name + " has been removed !", Toast.LENGTH_SHORT)
            .show()
    }

    private fun tagContains(tagName: String): Boolean {
        return tags.find { tag -> tag.name == tagName } != null
    }

}