package fr.uge.soundroid.utils

import android.util.Log
import fr.uge.soundroid.models.*
import fr.uge.soundroid.repositories.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.StringBuilder

object DatabaseService {

    fun printDatabase() {
        val sb = StringBuilder().apply {
            append("\n\n################################################################################\n" +
                    "                                   DATABASE                                     \n" +
                    "#################################################################################\n")

            append("SOUNDTRACKS  :\n")
            append("(id, title, path, note, seconds, listeningNumber)\n")
            val soundtracks = SoundtrackRepository.findAll()
            for ( soundtrack in soundtracks ) {
                soundtrack.apply {
                    append("(${id}, ${title}, ${path}, ${note}, ${duration}, ${listeningNumber})\n")

                    append("Associated Album : ${album?.name}\n")
                    append("Associated Artist : ${artist?.name}\n")

                    append("Associated Tags : \n")

                    for ( tag in tags ) {
                        append("${tag?.name}, ")
                    }
                    append("\n")
                }
            }

            append("\n\nALBUMS  :\n")
            append("(id, name)\n")
            val albums = AlbumRepository.findAll()
            for ( album in albums ) {
                album.apply {
                    append("(${id}, ${name})\n")

                    append("Associated Artist : ${artist?.name}\n")

                    append("Associated Songs : \n")

                    for ( song in this.soundtracks ) {
                        append("${song.title}, ")
                    }
                    append("\n")
                }
            }

            append("\n\nARTISTS  :\n")
            append("(id, name)\n")
            val artists = ArtistRepository.findAll()
            for ( artist in artists ) {
                artist.apply {
                    append("(${id}, ${name})\n")
                }
            }

            append("\n\nPLAYLISTS  :\n")
            append("(id, title)\n")
            val playlists = PlaylistRepository.findAll()
            for ( playlist in playlists ) {
                playlist.apply {
                    append("(${id}, ${title})\n")

                    append("Associated Songs : \n")

                    for ( song in this.soundtracks ) {
                        append("${song.title}, ")
                    }
                    append("\n")
                }
            }

            append("\n\nTAGS  :\n")
            append("(id, name)\n")
            val tags = TagRepository.findAll()
            for ( tag in tags ) {
                tag.apply {
                    append("(${id}, ${name})\n")
                }
            }


        }
        Log.d(DatabaseService::class.java.name, sb.toString())
    }

    fun deleteAll() {
        AlbumRepository.deleteAllAlbums()
        ArtistRepository.deleteAllArtists()
        PlaylistRepository.deleteAllPlaylists()
        SoundtrackRepository.deleteAllSoundtracks()
        TagRepository.deleteAllTags()
    }

    fun exportToJson(): String {
        var s: String = "{" +
                "\"albums\": ["

        for ( album in AlbumRepository.findAll() ) {
            s += album.exportToJson() + ","
        }
        s += "]," +
                "\"artists\": ["

        for ( artist in ArtistRepository.findAll() ) {
            s += artist.exportToJson() + ","
        }
        s += "]," +
                "\"playlists\": ["

        for ( playlist in PlaylistRepository.findAll() ) {
            s += playlist.exportToJson() + ","
        }
        s += "]," +
                "\"soundtracks\": ["

        for ( soundtrack in SoundtrackRepository.findAll() ) {
            s += soundtrack.exportToJson() + ","
        }

        s += "]," +
                "\"tags\": ["
        for ( tag in TagRepository.findAll() ) {
            s += tag.exportToJson() + ","
        }
        s += "]}"

        return s
    }

    fun importFromJson(json: String) {
        deleteAll()
        val jsonObject = JSONObject(json)

        val jsonArtistArray = jsonObject.getJSONArray("artists")
        for (index in 0 until jsonArtistArray.length()-1) {
            val jsonArtistObject = jsonArtistArray.getJSONObject(index)
            val artist = Artist(jsonArtistObject.getInt("id"), jsonArtistObject.getString("name"))
            ArtistRepository.saveArtist(artist)
        }

        val jsonAlbumsArray = jsonObject.getJSONArray("albums")
        for (index in 0 until jsonAlbumsArray.length()-1) {
            val jsonAlbumObject = jsonAlbumsArray.getJSONObject(index)
            val artist = ArtistRepository.findArtistById(jsonAlbumObject.getInt("artist"))
            val album = Album(jsonAlbumObject.getInt("id"), jsonAlbumObject.getString("name"), jsonAlbumObject.getString("albumPicture"), artist)
            AlbumRepository.saveAlbum(album)
        }

        val jsonPlaylistsArray = jsonObject.getJSONArray("playlists")
        for (index in 0 until jsonPlaylistsArray.length()-1) {
            val jsonPlaylistObject = jsonPlaylistsArray.getJSONObject(index)
            val playlist = Playlist(jsonPlaylistObject.getInt("id"), jsonPlaylistObject.getString("title"))
            PlaylistRepository.savePlaylist(playlist)
        }

        val jsonTagsArray = jsonObject.getJSONArray("tags")
        for (index in 0 until jsonTagsArray.length()-1) {
            val jsonTagObject = jsonTagsArray.getJSONObject(index)
            val tag = Tag(jsonTagObject.getInt("id"), jsonTagObject.getString("name"))
            tag.initPrimaryKey()
            TagRepository.saveTag(tag)
        }

        val jsonSoundtrackArray = jsonObject.getJSONArray("soundtracks")
        for (index in 0 until jsonSoundtrackArray.length()-1) {
            val jsonSoundtrackObject = jsonSoundtrackArray.getJSONObject(index)
            val artist = ArtistRepository.findArtistById(jsonSoundtrackObject.getInt("artist"))
            val album = AlbumRepository.findAlbumById(jsonSoundtrackObject.getInt("album"))
            val note: Float?
            if ( jsonSoundtrackObject.isNull("note" ) ) {
                note = null
            } else {
                note = jsonSoundtrackObject.getDouble("note").toFloat()
            }

            val soundtrack = Soundtrack(
                jsonSoundtrackObject.getInt("id"),
                jsonSoundtrackObject.getString("title"),
                jsonSoundtrackObject.getString("path"),
                jsonSoundtrackObject.getInt("duration"),
                note,
                artist,
                album
            )
            soundtrack.initPrimaryKey()
            SoundtrackRepository.saveSoundtrack(soundtrack)
        }

        AlbumRepository.realm.executeTransaction {
            for (index in 0 until jsonAlbumsArray.length() - 1) {
                val jsonAlbumObject = jsonAlbumsArray.getJSONObject(index)
                val album = AlbumRepository.findAlbumById(jsonAlbumObject.getInt("id"))

                val jsonSoundtracksAlbum = jsonAlbumObject.getJSONArray("soundtracks")
                for (j in 0 until jsonSoundtracksAlbum.length() - 1) {
                    val jsonSoundtrackObject = jsonSoundtracksAlbum.getJSONObject(j)
                    val soundtrack =
                        SoundtrackRepository.findSoundtrackById(jsonSoundtrackObject.getInt("id"))
                    if (soundtrack != null) {
                        album?.addSoundtrack(soundtrack)
                    }
                }
                it.copyToRealmOrUpdate(album!!)
            }

            for (index in 0 until jsonPlaylistsArray.length() - 1) {
                val jsonPlaylistObject = jsonPlaylistsArray.getJSONObject(index)
                val playlist = PlaylistRepository.findPlaylistById(jsonPlaylistObject.getInt("id"))

                val jsonSoundtracksPlaylist = jsonPlaylistObject.getJSONArray("soundtracks")
                for (j in 0 until jsonSoundtracksPlaylist.length() - 1) {
                    val jsonSoundtrackObject = jsonSoundtracksPlaylist.getJSONObject(j)
                    val soundtrack =
                        SoundtrackRepository.findSoundtrackById(jsonSoundtrackObject.getInt("id"))
                    if (soundtrack != null) {
                        playlist?.addSoundtrack(soundtrack)
                    }
                }
                it.copyToRealmOrUpdate(playlist!!)
            }
        }
    }

}