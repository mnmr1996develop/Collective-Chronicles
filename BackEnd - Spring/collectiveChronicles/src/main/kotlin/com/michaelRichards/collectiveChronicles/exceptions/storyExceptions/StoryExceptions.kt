package com.michaelRichards.collectiveChronicles.exceptions.storyExceptions


sealed class StoryExceptions(message: String): RuntimeException(message) {
    class IndexOutOfBound(index: Int, variableName: String) : StoryExceptions("$variableName: $index is out of bounds")
    class MaxStoriesReached(storiesInCanon: Int, storyTitle: String): StoryExceptions("$storyTitle has already reached the max limit of $storiesInCanon")
    class NotFoundException(title: String): StoryExceptions("$title is not found")
}