package com.dev.hare.apputilitymodule.exception

import com.dev.hare.apputilitymodule.exception.interfaces.ExceptionHandler

/**
 * 에러가 발생 시 재 실행 하기 위한 클래스
 * targetCount : 최대 재 실행 횟수, OnExecute : 재 실행할 함수
 *
 * @Author : Hare
 * @Created : 19.3.27
 */
class ExceptionUtil(private val targetCount: Int, private val onExecute: OnExecute) :
    ExceptionHandler {
    private var currentCount = 1

    /**
     * 에러가 발생할 경우 최대 재 실행 횟수 만큼 재 실행합니다
     *
     * @Author : Hare
     * @Updated : 19.3.27
     */
    override fun <T> handleException(def: T?): T? {
        var result = def
        try {
            result = onExecute.execute()
        } catch (e: Exception) {
            e.printStackTrace()
            if (targetCount > currentCount) {
                currentCount++
                return handleException(def)
            }
        }
        return result
    }

    interface OnExecute {
        @Throws(Exception::class)
        fun <T> execute(): T?
    }

}
