package com.dev.hare.webbasetemplatemodule

import org.junit.Test

class BasicTest {
    enum class Key(private val value: String) {
        Type("type");

        fun getValue() = value
    }

    @Test
    fun testEnum() {
        System.out.println(Key.Type)
        System.out.println(Key.Type.name)
        System.out.println(Key.Type.ordinal)
        System.out.println(Key.Type.toString())
        System.out.println(Key.Type.getValue())

    }
}