package fr.uge.soundroid.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.uge.soundroid.R
import fr.uge.soundroid.activities.others.AlbumActivity
import fr.uge.soundroid.activities.others.PlayerActivity
import fr.uge.soundroid.activities.others.PlaylistActivity
import fr.uge.soundroid.adapters.SearchListAdapter
import fr.uge.soundroid.models.Album
import fr.uge.soundroid.models.Playlist
import fr.uge.soundroid.models.SoundroidSearchable
import fr.uge.soundroid.models.Soundtrack
import fr.uge.soundroid.utils.SearchingService


class SearchingFragment() : Fragment(), View.OnClickListener {

    private val searchList: ArrayList<SoundroidSearchable> = ArrayList()

    val adapter = SearchListAdapter(searchList, this)

    lateinit var recyclerView: RecyclerView

    lateinit var radioGroup: RadioGroup

    lateinit var autoCompleteTextView: AutoCompleteTextView

    var mode: Int = SearchingService.ALPHABETICAL

    class SearchTextChange(val adapter: SearchListAdapter, private val searchList: ArrayList<SoundroidSearchable>, val fragment: SearchingFragment) : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            searchList.clear()

            if ( s!= null && s.length > 0 ) {
                val searchResult = SearchingService.search(s.toString(), fragment.mode)
                searchList.addAll(searchResult)
            }
            adapter.notifyDataSetChanged()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_searching, container, false)
        autoCompleteTextView = view.findViewById(R.id.fragment_searching_autocomplete)

        recyclerView = view.findViewById(R.id.fragment_searching_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        autoCompleteTextView.addTextChangedListener(SearchTextChange(adapter, searchList, this))

        radioGroup = view.findViewById(R.id.fragment_searching_radio_group)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group.findViewById(checkedId) as RadioButton
            val isChecked = checkedRadioButton.isChecked

            if (isChecked) {
                searchList.clear()

                if ( checkedId == R.id.fragment_searching_radio_alpha ) {
                    mode = SearchingService.ALPHABETICAL
                } else if ( checkedId == R.id.fragment_searching_radio_listening_number ) {
                    mode = SearchingService.NUMBER
                } else if ( checkedId == R.id.fragment_searching_radio_note ) {
                    mode = SearchingService.NOTE
                }

                searchList.addAll(SearchingService.search(autoCompleteTextView.text.toString(), mode))
            }
            adapter.notifyDataSetChanged()
        }

        return view
    }

    override fun onClick(v: View) {
        if ( v.tag == null ) return

        val index = v.tag as Int
        val selectedSearch = searchList[index]

        when ( selectedSearch ) {
            is Soundtrack -> {
                val intent = Intent(context, PlayerActivity::class.java)
                intent.putExtra("soundtrackId", selectedSearch.id)
                startActivity(intent)
            }
            is Playlist -> {
                val intent = Intent(context, PlaylistActivity::class.java)
                intent.putExtra("playlistId", selectedSearch.id)
                startActivity(intent)
            }
            is Album -> {
                val intent = Intent(context, AlbumActivity::class.java)
                intent.putExtra("albumId", selectedSearch.id)
                startActivity(intent)
            }
        }
    }

}