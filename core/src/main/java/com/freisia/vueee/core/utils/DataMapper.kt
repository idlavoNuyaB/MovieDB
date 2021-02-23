package com.freisia.vueee.core.utils

import com.freisia.vueee.core.data.model.all.GenresResponse
import com.freisia.vueee.core.data.model.movie.*
import com.freisia.vueee.core.data.model.tv.*
import com.freisia.vueee.core.domain.model.all.GenresDomain
import com.freisia.vueee.core.domain.model.movie.*
import com.freisia.vueee.core.domain.model.tv.*
import com.freisia.vueee.core.presentation.model.all.Genres
import com.freisia.vueee.core.presentation.model.movie.*
import com.freisia.vueee.core.presentation.model.tv.*

object DataMapper {

    fun mapRatingTVResponseToDomain(input: RatingTVResponse) : RatingTVDomain {
        return RatingTVDomain(
            id = input.id,
            results = mapResultRatingTVResponseToDomain(input.results)
        )
    }

    private fun mapResultRatingTVResponseToDomain(input: ArrayList<ResultsRatingTVResponse>) : ArrayList<ResultsRatingTVDomain>{
        val listData = ArrayList<ResultsRatingTVDomain>()
        input.map{
            val data = ResultsRatingTVDomain(
                iso_3166_1 = it.iso_3166_1,
                rating = it.rating
            )
            listData.add(data)
        }
        return listData
    }

    fun mapResultTVResponseToDomain(input: ResultTVResponse) : ResultTVDomain {
        return ResultTVDomain(
            totalPages = input.totalPages,
            result = mapSearchTVResponseToDomain(input.result)
       )
    }

    private fun mapSearchTVResponseToDomain(input: ArrayList<SearchTVResponse>) : ArrayList<SearchTVDomain>{
        val searchList = ArrayList<SearchTVDomain>()
        input.map {
            val movie = SearchTVDomain(
                poster_path = it.poster_path,
                id = it.id,
                name = it.name,
                vote_average = it.vote_average
            )
            searchList.add(movie)
        }
        return searchList
    }

    fun mapRatingTVDomainToPresentation(input: RatingTVDomain) : RatingTV {
        return RatingTV(
            id = input.id,
            results = mapResultRatingTVDomainToPresentation(input.results)
        )
    }

    private fun mapResultRatingTVDomainToPresentation(input: ArrayList<ResultsRatingTVDomain>) : ArrayList<ResultsRatingTV>{
        val listData = ArrayList<ResultsRatingTV>()
        input.map{
            val data = ResultsRatingTV(
                iso_3166_1 = it.iso_3166_1,
                rating = it.rating
            )
            listData.add(data)
        }
        return listData
    }

    fun mapResultTVDomainToPresentation(input: ResultTVDomain) : ResultTV {
        return ResultTV(
            totalPages = input.totalPages,
            result = mapSearchTVDomainToPresentation(input.result)
        )
    }

    private fun mapSearchTVDomainToPresentation(input: ArrayList<SearchTVDomain>) : ArrayList<SearchTV>{
        val searchList = ArrayList<SearchTV>()
        input.map {
            val movie = SearchTV(
                poster_path = it.poster_path,
                id = it.id,
                name = it.name,
                vote_average = it.vote_average
            )
            searchList.add(movie)
        }
        return searchList
    }

    fun mapTVResponseToDomain(input: TVResponse) : TVDomain {
        return TVDomain(
            episode_run_time = input.episode_run_time,
            first_air_date = input.first_air_date,
            genres = mapGenreResponseToDomain(input.genres),
            id = input.id,
            name = input.name,
            overview = input.overview,
            poster_path = input.poster_path,
            vote_count = input.vote_count,
            vote_average = input.vote_average
        )
    }

    fun mapTVLocalResponseToDomain(input: List<TVResponse>) : List<TVDomain>{
        val listData : MutableList<TVDomain> = ArrayList()
        input.map {
            val data = TVDomain(
                episode_run_time = it.episode_run_time,
                first_air_date = it.first_air_date,
                genres = mapGenreResponseToDomain(it.genres),
                id = it.id,
                name = it.name,
                overview = it.overview,
                poster_path = it.poster_path,
                vote_count = it.vote_count,
                vote_average = it.vote_average
            )
            listData.add(data)
        }
        return listData
    }

    fun mapTVDomainToResponse(input: TVDomain) : TVResponse {
        return TVResponse(
            episode_run_time = input.episode_run_time,
            first_air_date = input.first_air_date,
            genres = mapGenreDomainToResponse(input.genres),
            id = input.id,
            name = input.name,
            overview = input.overview,
            poster_path = input.poster_path,
            vote_count = input.vote_count,
            vote_average = input.vote_average
        )
    }

    fun mapTVLocalDomainToPresentation(input: List<TVDomain>) : List<TV> {
        val listData : MutableList<TV> = ArrayList()
        input.map {
            val data = TV(
                episode_run_time = it.episode_run_time,
                first_air_date = it.first_air_date,
                genres = mapGenreDomainToPresentation(it.genres),
                id = it.id,
                name = it.name,
                overview = it.overview,
                poster_path = it.poster_path,
                vote_count = it.vote_count,
                vote_average = it.vote_average
            )
            listData.add(data)
        }
        return listData
    }

    fun mapTVDomainToPresentation(input: TVDomain) : TV {
        return TV(
            episode_run_time = input.episode_run_time,
            first_air_date = input.first_air_date,
            genres = mapGenreDomainToPresentation(input.genres),
            id = input.id,
            name = input.name,
            overview = input.overview,
            poster_path = input.poster_path,
            vote_count = input.vote_count,
            vote_average = input.vote_average
        )
    }

    fun mapTVPresentationToDomain(input: TV) : TVDomain {
        return TVDomain(
            episode_run_time = input.episode_run_time,
            first_air_date = input.first_air_date,
            genres = mapGenrePresentationToDomain(input.genres),
            id = input.id,
            name = input.name,
            overview = input.overview,
            poster_path = input.poster_path,
            vote_count = input.vote_count,
            vote_average = input.vote_average
        )
    }

     fun mapSearchMovieResponseToDomain(input: SearchMovieResponse) : SearchMovieDomain{
        return SearchMovieDomain(
            poster_path = input.poster_path,
            id = input.id,
            backdrop_path = input.backdrop_path,
            title = input.title,
            vote_average = input.vote_average
        )
    }

     fun mapSearchMovieDomainToPresentation(input: SearchMovieDomain) : SearchMovie{
        return SearchMovie(
            poster_path = input.poster_path,
            id = input.id,
            backdrop_path = input.backdrop_path,
            title = input.title,
            vote_average = input.vote_average
        )
    }

    fun mapMovieRemoteResponseToDomain(input: MovieResponse) : MovieDomain {
        return MovieDomain(
            genres = mapGenreResponseToDomain(input.genres),
            releases = mapReleaseResponseToDomain(input.releases),
            release_date = input.release_date,
            overview = input.overview,
            id = input.id,
            runtime = input.runtime,
            poster_path = input.poster_path,
            title = input.title,
            vote_average = input.vote_average,
            vote_count = input.vote_count
        )
    }

    fun mapMovieLocalResponseToDomain(input: List<MovieResponse>) : List<MovieDomain>{
        val listData : MutableList<MovieDomain> = ArrayList()
        input.map {
            val data = MovieDomain(
                genres = mapGenreResponseToDomain(it.genres),
                releases = mapReleaseResponseToDomain(it.releases),
                release_date = it.release_date,
                overview = it.overview,
                id = it.id,
                runtime = it.runtime,
                poster_path = it.poster_path,
                title = it.title,
                vote_average = it.vote_average,
                vote_count = it.vote_count
            )
            listData.add(data)
        }
        return listData
    }

    private fun mapGenreResponseToDomain(input: ArrayList<GenresResponse>) : ArrayList<GenresDomain>{
        val listData = ArrayList<GenresDomain>()
        input.map{
            val data = GenresDomain(
                id = it.id,
                name = it.name
            )
            listData.add(data)
        }
        return listData
    }

    private fun mapReleaseResponseToDomain(input: ReleasesResponse) : ReleasesDomain {
        return ReleasesDomain(
            countries = mapCountriesResponseToDomain(input.countries)
        )
    }

    private fun mapCountriesResponseToDomain(input: ArrayList<CountriesResponse>) : ArrayList<CountriesDomain>{
        val listData = ArrayList<CountriesDomain>()
        input.map{
            val data = CountriesDomain(
                certification = it.certification,
                iso_3166_1 = it.iso_3166_1
            )
            listData.add(data)
        }
        return listData
    }

    fun mapMovieRemoteDomainToPresentation(input: MovieDomain) : Movie {
        return Movie(
            genres = mapGenreDomainToPresentation(input.genres),
            releases = mapReleaseDomainToPresentation(input.releases),
            release_date = input.release_date,
            overview = input.overview,
            id = input.id,
            runtime = input.runtime,
            poster_path = input.poster_path,
            title = input.title,
            vote_average = input.vote_average,
            vote_count = input.vote_count
        )
    }

    fun mapMovieLocalDomainToPresentation(input: List<MovieDomain>) : List<Movie>{
        val listData : MutableList<Movie> = ArrayList()
        input.map {
            val data = Movie(
                genres = mapGenreDomainToPresentation(it.genres),
                releases = mapReleaseDomainToPresentation(it.releases),
                release_date = it.release_date,
                overview = it.overview,
                id = it.id,
                runtime = it.runtime,
                poster_path = it.poster_path,
                title = it.title,
                vote_average = it.vote_average,
                vote_count = it.vote_count
            )
            listData.add(data)
        }
        return listData
    }

    private fun mapGenreDomainToPresentation(input: ArrayList<GenresDomain>) : ArrayList<Genres>{
        val listData = ArrayList<Genres>()
        input.map{
            val data = Genres(
                id = it.id,
                name = it.name
            )
            listData.add(data)
        }
        return listData
    }

    private fun mapReleaseDomainToPresentation(input: ReleasesDomain) : Releases {
        return Releases(
            countries = mapCountriesDomainToPresentation(input.countries)
        )
    }

    private fun mapCountriesDomainToPresentation(input: ArrayList<CountriesDomain>) : ArrayList<Countries>{
        val listData = ArrayList<Countries>()
        input.map{
            val data = Countries(
                certification = it.certification,
                iso_3166_1 = it.iso_3166_1
            )
            listData.add(data)
        }
        return listData
    }

    fun mapMovieRemoteDomainToResponse(input: MovieDomain) : MovieResponse {
        return MovieResponse(
            genres = mapGenreDomainToResponse(input.genres),
            releases = mapReleaseDomainToResponse(input.releases),
            release_date = input.release_date,
            overview = input.overview,
            id = input.id,
            runtime = input.runtime,
            poster_path = input.poster_path,
            title = input.title,
            vote_average = input.vote_average,
            vote_count = input.vote_count
        )
    }

    private fun mapGenreDomainToResponse(input: ArrayList<GenresDomain>) : ArrayList<GenresResponse>{
        val listData = ArrayList<GenresResponse>()
        input.map{
            val data = GenresResponse(
                id = it.id,
                name = it.name
            )
            listData.add(data)
        }
        return listData
    }

    private fun mapReleaseDomainToResponse(input: ReleasesDomain) : ReleasesResponse {
        return ReleasesResponse(
            countries = mapCountriesDomainToResponse(input.countries)
        )
    }

    private fun mapCountriesDomainToResponse(input: ArrayList<CountriesDomain>) : ArrayList<CountriesResponse>{
        val listData = ArrayList<CountriesResponse>()
        input.map{
            val data = CountriesResponse(
                certification = it.certification,
                iso_3166_1 = it.iso_3166_1
            )
            listData.add(data)
        }
        return listData
    }

    fun mapMovieRemotePresentationToDomain(input: Movie) : MovieDomain {
        return MovieDomain(
            genres = mapGenrePresentationToDomain(input.genres),
            releases = mapReleasePresentationToDomain(input.releases),
            release_date = input.release_date,
            overview = input.overview,
            id = input.id,
            runtime = input.runtime,
            poster_path = input.poster_path,
            title = input.title,
            vote_average = input.vote_average,
            vote_count = input.vote_count
        )
    }

    private fun mapGenrePresentationToDomain(input: ArrayList<Genres>) : ArrayList<GenresDomain>{
        val listData = ArrayList<GenresDomain>()
        input.map{
            val data = GenresDomain(
                id = it.id,
                name = it.name
            )
            listData.add(data)
        }
        return listData
    }

    private fun mapReleasePresentationToDomain(input: Releases) : ReleasesDomain {
        return ReleasesDomain(
            countries = mapCountriesDomainToDomain(input.countries)
        )
    }

    private fun mapCountriesDomainToDomain(input: ArrayList<Countries>) : ArrayList<CountriesDomain>{
        val listData = ArrayList<CountriesDomain>()
        input.map{
            val data = CountriesDomain(
                certification = it.certification,
                iso_3166_1 = it.iso_3166_1
            )
            listData.add(data)
        }
        return listData
    }

}