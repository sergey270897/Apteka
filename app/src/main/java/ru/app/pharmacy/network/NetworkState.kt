package ru.app.pharmacy.network

enum class NetworkState {
    RUNNING,
    SUCCESS,
    FAILED,
    WRONG_DATA;

    var code:Int? = null
}