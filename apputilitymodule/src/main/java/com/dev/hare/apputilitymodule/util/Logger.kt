package com.dev.hare.apputilitymodule.util

import android.util.Log

object Logger {
    var IS_DEBUG = true
    private const val HORIZON =
        "----------------------------------------------------------------------------------------------------\n"

    enum class LogType {
        INFO, DEBUG, VERB, WARN, ERROR
    }

    fun log(logType: LogType, message: String = "", e: Exception) {
        val log = buildLogMsg(message + e.message)
        log(logType, log[0], log[1])
        e.printStackTrace()
    }

    fun log(logType: LogType, message: String = "", t: Throwable) {
        val log = buildLogMsg("$message ${t.message}")
        log(logType, log[0], log[1])
        t.printStackTrace()
    }

    fun log(logType: Logger.LogType, vararg messages: String) {
        val tag = buildLogMsg(messages[0])[0]
        execute(logType, tag, HORIZON)
        messages.forEach {
            val log = buildLogMsg(it)
            execute(logType, log[0], log[1])
        }
        execute(logType, tag, HORIZON)
    }

    fun log(logType: Logger.LogType, message: String) {
        val log = buildLogMsg(message)
        log(logType, log[0], log[1])
    }

    private fun log(logType: LogType, tag: String, message: String) {
        execute(logType, tag, HORIZON)
        execute(logType, tag, message)
        execute(logType, tag, HORIZON)
    }

    private fun execute(logType: LogType, tag: String, message: String) {
        if (IS_DEBUG) {
            when (logType) {
                Logger.LogType.INFO -> Log.i(tag, message)
                Logger.LogType.DEBUG -> Log.d(tag, message)
                Logger.LogType.VERB -> Log.v(tag, message)
                Logger.LogType.WARN -> Log.w(tag, message)
                Logger.LogType.ERROR -> Log.e(tag, message)
            }
        }
    }

    private fun buildLogMsg(message: String): Array<String> {
        val ste = Thread.currentThread().stackTrace[4]
        return arrayOf(ste.fileName.replace(".java", ""), "[ ${ste.methodName} ] : $message")
    }
}