package fr.uge.soundroid.utils

import fr.uge.soundroid.models.SoundroidSearchable
import fr.uge.soundroid.repositories.AlbumRepository
import fr.uge.soundroid.repositories.ArtistRepository
import fr.uge.soundroid.repositories.PlaylistRepository
import fr.uge.soundroid.repositories.SoundtrackRepository
import io.realm.Sort

object SearchingService {

    val ALPHABETICAL: Int = 0

    val NOTE: Int = 1

    val NUMBER: Int = 2

    fun search(filter: String, filterType: Int = 0): List<SoundroidSearchable> {
        val results = ArrayList<SoundroidSearchable>()
        var f = "title"
        var o = Sort.ASCENDING

        if ( filterType == NOTE) {
            f = "note"
            o = Sort.DESCENDING
        } else if ( filterType == NUMBER ) {
            f = "note"
            o = Sort.DESCENDING
        }

        results.addAll(SoundtrackRepository.findLike(mapOf(
            "title" to filter
        ), f, o))

        results.addAll(SoundtrackRepository.findLike(mapOf(
            "tags.name" to filter
        ), f, o))

        if ( filterType == ALPHABETICAL ) {
            results.addAll(AlbumRepository.findLike(mapOf(
                "name" to filter
            )))

            results.addAll(ArtistRepository.findLike(mapOf(
                "name" to filter
            )))

            results.addAll(PlaylistRepository.findLike(mapOf(
                "title" to filter
            )))
        }

        return results
    }

}