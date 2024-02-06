package com.starlabs.restoapp.model

data class ErrorResponse(
    var isError: Boolean?=false,
    var errorMessage: String?= null
)
