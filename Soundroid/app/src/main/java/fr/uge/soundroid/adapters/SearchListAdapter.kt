package fr.uge.soundroid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.models.SoundroidSearchable

class SearchListAdapter(private val searchList: List<SoundroidSearchable>) : RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text = itemView.findViewById<TextView>(R.id.item_search_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem = inflater.inflate(R.layout.item_search, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val search = searchList[position]
        holder.text.text = search.getNameForSearch()
    }

    override fun getItemCount(): Int {
        return searchList.size
    }
}