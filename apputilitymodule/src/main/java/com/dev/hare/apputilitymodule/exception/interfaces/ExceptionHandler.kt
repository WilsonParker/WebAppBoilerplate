package com.dev.hare.apputilitymodule.exception.interfaces

interface ExceptionHandler {
    fun <T> handleException(def: T?): T?
}
