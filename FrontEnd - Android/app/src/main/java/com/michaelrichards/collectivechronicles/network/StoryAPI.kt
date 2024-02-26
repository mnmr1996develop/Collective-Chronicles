package com.michaelrichards.collectivechronicles.network

import com.michaelrichards.collectivechronicles.dtos.requests.AuthenticationRequest
import com.michaelrichards.collectivechronicles.dtos.requests.StoryRequest
import com.michaelrichards.collectivechronicles.dtos.responses.StoryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface StoryAPI {
    companion object {
        private const val AUTHORIZATION = "Authorization"
    }


    @POST("story")
    suspend fun startStory(
        @Header(AUTHORIZATION) token: String,
        @Body storyRequest: StoryRequest
    ): StoryResponse

    @GET("story/{storyId]")
    suspend fun getStoryDetails(
        @Header(AUTHORIZATION) token: String,
        @Path("storyId") storyId: Long
    ): StoryResponse

}