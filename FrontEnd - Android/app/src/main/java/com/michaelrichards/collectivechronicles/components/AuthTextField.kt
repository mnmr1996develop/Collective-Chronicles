package com.michaelrichards.collectivechronicles.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.michaelrichards.collectivechronicles.R


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
            errorBorderColor = MaterialTheme.colorScheme.errorContainer,
            errorLabelColor = MaterialTheme.colorScheme.error,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        trailingIcon = {
            Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
                if (textValueState.value.isNotEmpty()) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.clickable { textValueState.value = "" }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(id = R.string.clear_text),
                            modifier = Modifier
                                .size(15.dp))
                    }

                    Spacer(modifier = Modifier.width(5.dp))
                }

                trailingIcon()
            }
        }


    )
}

private fun String.letters() = filter { it.isLetter() }.trim()

private fun String.usernameCharacters() =
    filter { it.isLetterOrDigit() || it == '_' || it == '-' }.trim()