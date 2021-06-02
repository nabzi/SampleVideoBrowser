package ir.nabzi.samplevideobrowser.model


/**#code from  : gitHubBrowserSample
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val errorCode: Int? = 0,
    val hasMore : Boolean? = false
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(msg: String = "", data: T? = null, errorCode: Int? = 0): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                msg,
                errorCode,
                false
            )
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null,
                0,
                false
            )
        }
    }
}
enum class Status {
    SUCCESS, ERROR, LOADING
}
