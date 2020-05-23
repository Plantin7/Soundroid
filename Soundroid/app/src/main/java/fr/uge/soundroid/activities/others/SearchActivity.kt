package fr.uge.soundroid.activities.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.uge.soundroid.R
import fr.uge.soundroid.fragments.SearchingFragment

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val fragment = SearchingFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.activity_search_container, fragment)
            .commit()
    }


}
