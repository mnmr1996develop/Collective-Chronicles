package com.michaelRichards.collectiveChronicles.exceptions

sealed class CustomExceptions(message: String): Exception(message) {
    class UnAuthorizedAction(message: String): CustomExceptions(message)
    class IndexOutOfBound(index: Int, variableName: String) : CustomExceptions("$variableName: $index is out of bounds")


}