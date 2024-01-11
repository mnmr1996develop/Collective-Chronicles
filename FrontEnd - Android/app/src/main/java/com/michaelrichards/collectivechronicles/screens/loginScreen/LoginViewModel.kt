package com.michaelrichards.collectivechronicles.screens.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelrichards.collectivechronicles.dtos.requests.AuthenticationRequest
import com.michaelrichards.collectivechronicles.repositories.auth.AuthRepository
import com.michaelrichards.collectivechronicles.repositories.results.ApiSuccessFailState
import com.michaelrichards.collectivechronicles.repositories.results.AuthenticationResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
        private val repository: AuthRepository
) : ViewModel() {

        private val resultChannel = Channel<AuthenticationResults<String>>()
        val authResults = resultChannel.receiveAsFlow()

        fun login(authenticationRequest: AuthenticationRequest){
              viewModelScope.launch {

                      resultChannel.send(AuthenticationResults.Loading())

                      val result= repository.login(
                              authenticationRequest = authenticationRequest
                      )

                      resultChannel.send(result)

              }
        }

}