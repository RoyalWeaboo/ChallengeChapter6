package com.malikazizali.challengechapter6.viewmodel

import com.malikazizali.challengechapter6.model.FavoritesResponseItem
import com.malikazizali.challengechapter6.model.MovieResponse
import com.malikazizali.challengechapter6.network.RestfulApiFavorites
import com.malikazizali.challengechapter6.network.RestfulApiMovie
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Call

class MovieViewModelTest {
    lateinit var serviceMovie: RestfulApiMovie
    lateinit var serviceFavorites: RestfulApiFavorites

    @Before
    fun setUp() {
        serviceMovie = mockk()
        serviceFavorites = mockk()
    }

    @Test
    fun getMovieTest(): Unit = runBlocking {
        //mocking GIVEN
        val respAllMovie = mockk<Call<MovieResponse>>()

        every {
            runBlocking {
                serviceMovie.getAllMovie()
            }
        } returns respAllMovie

        // System Under Test (WHEN)
        val result = serviceMovie.getAllMovie()

        verify {
            runBlocking { serviceMovie.getAllMovie() }
        }
        assertEquals(result, respAllMovie)

    }

    @Test
    fun getFavoritesTest(): Unit = runBlocking {
        //mocking GIVEN
        val respAllFav = mockk<Call<List<FavoritesResponseItem>>>()

        every {
            runBlocking {
                serviceFavorites.getAllMovie()
            }
        } returns respAllFav

        // System Under Test (WHEN)
        val result = serviceFavorites.getAllMovie()

        verify {
            runBlocking { serviceFavorites.getAllMovie() }
        }
        assertEquals(result, respAllFav)

    }

    @Test
    fun addFavoritesTest(): Unit = runBlocking {
        //mocking GIVEN
        val respAddFav = mockk<Call<FavoritesResponseItem>>()

        every {
            runBlocking {
                serviceFavorites.addNewMovie(FavoritesResponseItem("","lang","title","poster","date","vote","overview"))
            }
        } returns respAddFav

        // System Under Test (WHEN)
        val result = serviceFavorites.addNewMovie(FavoritesResponseItem("","lang","title","poster","date","vote","overview"))

        verify {
            runBlocking { serviceFavorites.addNewMovie(FavoritesResponseItem("","lang","title","poster","date","vote","overview"))
            }
        }
        assertEquals(result, respAddFav)

    }

    @Test
    fun deleteFavoritesTest(): Unit = runBlocking {
        //mocking GIVEN
        val respDelFav = mockk<Call<Int>>()

        every {
            runBlocking {
                serviceFavorites.deleteMovie(1)
            }
        } returns respDelFav

        // System Under Test (WHEN)
        val result = serviceFavorites.deleteMovie(1)

        verify {
            runBlocking { serviceFavorites.deleteMovie(1) }
        }
        assertEquals(result, respDelFav)

    }


}