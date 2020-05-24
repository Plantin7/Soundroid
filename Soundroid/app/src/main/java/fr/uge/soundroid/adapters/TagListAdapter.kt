package fr.uge.soundroid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.fragments.TagDialogFragment
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.models.Tag
import fr.uge.soundroid.repositories.SoundtrackRepository
import fr.uge.soundroid.repositories.TagRepository

class TagListAdapter (private val tags: ArrayList<Tag>) :
    RecyclerView.Adapter<TagListAdapter.ViewHolder>() {

    private lateinit var mRemoveTagListener: RemoveTagListener

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val tagText: TextView = itemView.findViewById(R.id.tag_text)
        private val deleteTag : ImageButton = itemView.findViewById(R.id.tag_cross)

        /** Update the state of the song */
        fun update(tag: Tag) {
            tagText.text = tag.name
            deleteTag.setOnClickListener{
                mRemoveTagListener.removeTag(getTag(adapterPosition))
            }
        }

        override fun onClick(v: View) {
            //
        }
    }

    private fun getTag(position: Int): Tag {
        return tags[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false))
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(tags[position])
    }

    /** allows clicks events to be caught */
    fun setRemoveTagListener(removeTagListener: RemoveTagListener) {
        mRemoveTagListener = removeTagListener
    }

    public interface RemoveTagListener {
        fun removeTag(tag:Tag)
    }
}