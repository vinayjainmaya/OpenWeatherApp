package com.example.openweatherapp.utils

enum class ResponseState {
    LOADING, SUCCESS, API_ERROR, NOT_FOUND, NO_INTERNET
}

enum class BookMarkState {
    LOADING, BM_EMPTY, BM_ADDED, BM_REMOVED, BM_PRESENT, BM_NOT_PRESENT, SQL_ERROR
}