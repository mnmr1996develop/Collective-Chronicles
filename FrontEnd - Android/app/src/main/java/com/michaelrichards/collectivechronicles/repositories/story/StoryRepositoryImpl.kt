package com.michaelrichards.collectivechronicles.repositories.story

import android.content.SharedPreferences
import com.michaelrichards.collectivechronicles.dtos.requests.StoryPieceRequest
import com.michaelrichards.collectivechronicles.dtos.requests.StoryRequest
import com.michaelrichards.collectivechronicles.dtos.responses.StoryResponse
import com.michaelrichards.collectivechronicles.network.StoryAPI
import com.michaelrichards.collectivechronicles.repositories.results.ApiSuccessFailState
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

class StoryRepositoryImpl(private val api: StoryAPI,private val prefs: SharedPreferences) : StoryRepository {

    companion object{
        private const val ACCESS_TOKEN = "accessToken"
        private const val TIME_OUT_VAL = 10000L
    }

    override suspend fun startStory(storyRequest: StoryRequest): ApiSuccessFailState<StoryResponse> {
        return try {
            withTimeout(TIME_OUT_VAL){
                val token = prefs.getString(ACCESS_TOKEN, null)
                if (token == null) {
                    ApiSuccessFailState.UnAuthorized()
                } else {
                    val data = api.startStory(token = token, storyRequest = storyRequest)
                    ApiSuccessFailState.Success(data = data)
                }
            }
        } catch (e: TimeoutCancellationException){
            ApiSuccessFailState.TimeOut()
        } catch (e: Exception) {
            ApiSuccessFailState.BadRequest()
        }
    }

    override suspend fun getStoryData(storyId: Long): ApiSuccessFailState<StoryResponse> {
        return try {
            withTimeout(TIME_OUT_VAL){
                val token = prefs.getString(ACCESS_TOKEN, null)
                if (token == null) {
                    ApiSuccessFailState.UnAuthorized()
                } else {
                    val data = api.getStoryDetails(token = token, storyId = storyId)
                    ApiSuccessFailState.Success(data = data)
                }
            }
        } catch (e: TimeoutCancellationException){
            ApiSuccessFailState.TimeOut()
        } catch (e: Exception) {
            ApiSuccessFailState.BadRequest()
        }
    }

    override suspend fun sendStoryRequest(
        storyId: Long,
        storyPieceRequest: StoryPieceRequest
    ): ApiSuccessFailState<Unit> {
        TODO("Not yet implemented")
    }


}
