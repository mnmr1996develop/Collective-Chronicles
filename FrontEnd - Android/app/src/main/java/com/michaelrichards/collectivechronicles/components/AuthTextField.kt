package com.michaelrichards.collectivechronicles.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    textValueState: MutableState<String>,
    label: String,
    onlyLetters: Boolean = false,
    usernameCharactersOnly: Boolean = true,
    maxCharacters: Int,
    enabled: Boolean = true,
    isError: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable () -> Unit = {}

    ) {
    OutlinedTextField(
        modifier = modifier,
        value = textValueState.value,
        onValueChange = {
            if (it.length <= maxCharacters) {
                textValueState.value =
                    if (usernameCharactersOnly) it.usernameCharacters()
                    else if (onlyLetters) it.letters()
                    else it.trim()
            }
        },
        label = { Text(text = label, style = MaterialTheme.typography.labelMedium) },
        enabled = enabled,
        isError = isError,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.onError
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        trailingIcon = {trailingIcon()}


    )
}

private fun String.letters() = filter { it.isLetter() }.trim()

private fun String.usernameCharacters() =
    filter { it.isLetterOrDigit() || it == '_' || it == '-' }.trim()