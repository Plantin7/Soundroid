package fr.uge.soundroid.utils

import fr.uge.soundroid.models.SoundroidSearchable
import fr.uge.soundroid.repositories.AlbumRepository
import fr.uge.soundroid.repositories.ArtistRepository
import fr.uge.soundroid.repositories.PlaylistRepository
import fr.uge.soundroid.repositories.SoundtrackRepository

object SearchingService {

    fun search(filter: String): List<SoundroidSearchable> {
        val results = ArrayList<SoundroidSearchable>()

        results.addAll(AlbumRepository.findLike(mapOf(
            "name" to filter
        )))

        results.addAll(ArtistRepository.findLike(mapOf(
            "name" to filter
        )))

        results.addAll(PlaylistRepository.findLike(mapOf(
            "title" to filter
        )))

        results.addAll(SoundtrackRepository.findLike(mapOf(
            "title" to filter
        )))

        results.addAll(SoundtrackRepository.findLike(mapOf(
            "tags.name" to filter
        )))

        return results
    }

}