package fr.uge.soundroid.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import fr.uge.soundroid.models.Soundtrack

class Database(context: Context) : SQLiteOpenHelper(context, "soundroid.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(
            "CREATE TABLE artists (id INT PRIMARY KEY, name VARCHAR(255) NOT NULL)"
        )

        db?.execSQL(
            "CREATE TABLE albums (id INT PRIMARY KEY, name VARCHAR(255) NOT NULL, album_picture VARCHAR(255), artist INT NOT NULL, FOREIGN KEY (artist) REFERENCES artists(id))"
        )

        db?.execSQL(
            "CREATE TABLE soundtracks (id INT PRIMARY KEY, hash VARCHAR(255) NOT NULL, title VARCHAR(255) NOT NULL, path VARCHAR(255) NOT NULL, seconds INT NOT NULL, artist INT, album INT, FOREIGN KEY (artist) REFERENCES artists(id), FOREIGN KEY (album) REFERENCES albums(id))"
        )

        db?.execSQL(
            "CREATE TABLE playlists (id INT PRIMARY KEY, title VARCHAR(255) NOT NULL)"
        )

        db?.execSQL(
            "CREATE TABLE playlists_soundtracks (id_soundtrack INT NOT NULL, id_playlist INT NOT NULL, FOREIGN KEY (id_soundtrack) REFERENCES soundtracks(id), FOREIGN KEY (id_playlist) REFERENCES playlists(id))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    /**
     * This function insert the given soundtrack in the database.
     *
     * @param soundtrack The soundtrack object.
     */
    fun saveSoundtrack(soundtrack: Soundtrack) {

        /* Save normal part */
        val values = ContentValues()
        values.put("title", soundtrack.title)
        values.put("path", soundtrack.path)
        values.put("seconds", soundtrack.seconds)
        writableDatabase.insert("soundtracks", null, values)
    }

}