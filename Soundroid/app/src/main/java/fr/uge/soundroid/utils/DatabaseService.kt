package fr.uge.soundroid.utils

import android.util.Log
import fr.uge.soundroid.repositories.*
import java.lang.StringBuilder

object DatabaseService {

    fun printDatabase() {
        val sb = StringBuilder().apply {
            append("\n\n################################################################################\n" +
                    "                                   DATABASE                                     \n" +
                    "#################################################################################\n")

            append("SOUNDTRACKS  :\n")
            append("(id, title, path, note, seconds)\n")
            val soundtracks = SoundtrackRepository.findAll()
            for ( soundtrack in soundtracks ) {
                soundtrack.apply {
                    append("(${id}, ${title}, ${path}, ${note}, ${seconds})\n")

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

}