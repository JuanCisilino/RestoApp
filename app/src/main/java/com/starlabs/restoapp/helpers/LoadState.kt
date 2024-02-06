package com.starlabs.restoapp.helpers

sealed class LoadState {

    object Loading : LoadState()
    object Success : LoadState()
    object Error : LoadState()
}