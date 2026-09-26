package com.fit2081.bryanzongsheng33523355.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {

    @GET("api/fruit/{fruitName}")
    suspend fun getFruitNutrition(
        @Path("fruitName") fruitName: String
    ): ResponseModel
    /**
     * Companion object to provide a factory method for creating an instance of the [APIService].
     */
    companion object {
        /**
         * The base URL for the FruityVice API.
         */
        val BASE_URL = "https://www.fruityvice.com/"

        /**
         * Creates an instance of [APIService] using Retrofit.
         * @return An implementation of [APIService] for making API calls.
         */
        fun create(): APIService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(APIService::class.java)
        }
    }
}