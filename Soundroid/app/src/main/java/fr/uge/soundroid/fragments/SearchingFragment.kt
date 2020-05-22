package fr.uge.soundroid.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.adapters.SearchListAdapter
import fr.uge.soundroid.models.SoundroidSearchable
import fr.uge.soundroid.utils.SearchingService

class SearchingFragment : Fragment(), View.OnClickListener {

    private val searchList: ArrayList<SoundroidSearchable> = ArrayList()

    val adapter = SearchListAdapter(searchList, this)

    lateinit var recyclerView: RecyclerView

    class SearchTextChange(val adapter: SearchListAdapter, private val searchList: ArrayList<SoundroidSearchable>) : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            searchList.clear()

            if ( s!= null && s.length > 3 ) {
                val searchResult = SearchingService.search(s.toString())
                searchList.addAll(searchResult)
            }
            adapter.notifyDataSetChanged()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_searching, container, false)
        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.fragment_searching_autocomplete)

        recyclerView = view.findViewById(R.id.fragment_searching_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        autoCompleteTextView.addTextChangedListener(SearchTextChange(adapter, searchList))

        return view
    }

    override fun onClick(v: View) {
        if ( v.tag == null ) return

        val index = v.tag as Int
        val selectedSearch = searchList[index]

        // TODO : o the job when the use select an result
    }

}