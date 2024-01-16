package com.michaelrichards.collectivechronicles.screens.registrationScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelrichards.collectivechronicles.dtos.requests.RegistrationRequest
import com.michaelrichards.collectivechronicles.repositories.auth.AuthRepository
import com.michaelrichards.collectivechronicles.repositories.results.AuthenticationResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(){

    private val resultChannel: Channel<AuthenticationResults<String>> = Channel()
    val authResults = resultChannel.receiveAsFlow()

    fun register(registrationRequest: RegistrationRequest){
        viewModelScope.launch {
            resultChannel.send(AuthenticationResults.Loading())
           val result = authRepository.register(registrationRequest = registrationRequest)
            resultChannel.send(result)
        }
    }

}