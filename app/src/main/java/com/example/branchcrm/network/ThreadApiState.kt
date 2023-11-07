package com.example.branchcrm.network

data class ThreadApiState<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): ThreadApiState<T> {
            return ThreadApiState(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String): ThreadApiState<T> {
            return ThreadApiState(Status.ERROR, null, msg)
        }

        fun <T> loading(): ThreadApiState<T> {
            return ThreadApiState(Status.LOADING, null, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}
