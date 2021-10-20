package com.ak.otaku_kun.utils


/**
 * Exception class for empty response from the server.
 *
 * @param message the detail message string.
 * @param cause the cause of this throwable.
 * **/
class EmptyDataException(message: String = "No data to be displayed", cause: Throwable? = null) : Exception(message, cause) {

    /**
     * Throwable Class for empty search response result.
     *
     * @param searchQuery the query to be searched for.
     * **/
    class SearchResultThrowable(searchQuery: String) : Throwable(message = "There is no data for $searchQuery were found")

    /**
     * Throwable Class for empty media browse filter response result.
     * **/
    class MediaFilterThrowable : Throwable("No media were found for given filters")
}