package com.example.onlineexamportal

import com.google.android.gms.common.api.Response
import retrofit2.http.GET
import retrofit2.http.Query

// BookApiService.kt
interface BookApiService {
    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String
    ): BookResponse
}
