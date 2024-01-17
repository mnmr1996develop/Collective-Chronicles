package com.michaelrichards.collectivechronicles.screens.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaelrichards.collectivechronicles.repositories.auth.AuthRepository
import com.michaelrichards.collectivechronicles.repositories.results.AuthenticationResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel(){

    private val resultChannel: Channel<AuthenticationResults<String>> = Channel()
    val authResults = resultChannel.receiveAsFlow()

    init {
        authenticate()
    }

    fun authenticate() {
        viewModelScope.launch {
            val result = repository.authenticate()
            resultChannel.send(result)
        }
    }
}