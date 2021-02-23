package com.freisia.vueee.core.data.remote

import com.freisia.vueee.core.data.model.movie.MovieResponse
import com.freisia.vueee.core.data.model.movie.ResultMovieResponse
import com.freisia.vueee.core.data.model.tv.RatingTVResponse
import com.freisia.vueee.core.data.model.tv.ResultTVResponse
import com.freisia.vueee.core.data.model.tv.TVResponse
import com.freisia.vueee.core.utils.Constant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET(Constant.GET_MOVIE)
    suspend fun getMovies(@Query("page") page: Int,
                          @Query("api_key") api_key: String = Constant.TOKEN_API,
                          @Query("language") language: String = Constant.LANGUAGE): Response<ResultMovieResponse>

    @GET(Constant.GET_NOW_PLAYING_MOVIE)
    suspend fun getNowPlayingMovies(@Query("page") page: Int,
                                    @Query("api_key") api_key: String = Constant.TOKEN_API,
                                    @Query("language") language: String = Constant.LANGUAGE): Response<ResultMovieResponse>

    @GET(Constant.GET_TOP_RATED_MOVIE)
    suspend fun getTopRatedMovies(@Query("page") page: Int,
                                  @Query("api_key") api_key: String = Constant.TOKEN_API,
                                  @Query("language") language: String = Constant.LANGUAGE): Response<ResultMovieResponse>

    @GET(Constant.GET_TV)
    suspend fun getTV(@Query("page") page: Int,
                      @Query("api_key") api_key: String = Constant.TOKEN_API,
                      @Query("language") language: String = Constant.LANGUAGE): Response<ResultTVResponse>

    @GET(Constant.GET_ON_AIR_TV)
    suspend fun getOnAirTV(@Query("page") page: Int,
                           @Query("api_key") api_key: String = Constant.TOKEN_API,
                           @Query("language") language: String = Constant.LANGUAGE): Response<ResultTVResponse>

    @GET(Constant.GET_TOP_RATED_TV)
    suspend fun getTopRatedTV(@Query("page") page: Int,
                              @Query("api_key") api_key: String = Constant.TOKEN_API,
                              @Query("language") language: String = Constant.LANGUAGE): Response<ResultTVResponse>

    @GET(Constant.GET_TV_RATING)
    suspend fun getRatingTV(@Path("tv_id") tv_id: Int,
                            @Query("api_key") api_key: String = Constant.TOKEN_API,
                            @Query("language") language: String = Constant.LANGUAGE): Response<RatingTVResponse>

    @GET(Constant.GET_MOVIE_DETAIL)
    suspend fun getMovieDetail(@Path("movie_id") movie_id: Int,
                               @Query("api_key") api_key: String = Constant.TOKEN_API,
                               @Query("language") language: String = Constant.LANGUAGE,
                               @Query("append_to_response") append_to_response: String = Constant.RELEASE): Response<MovieResponse>

    @GET(Constant.GET_TV_DETAIL)
    suspend fun getTVDetail(@Path("tv_id") tv_id: Int,
                            @Query("api_key") api_key: String = Constant.TOKEN_API,
                            @Query("language") language: String = Constant.LANGUAGE) : Response<TVResponse>
}