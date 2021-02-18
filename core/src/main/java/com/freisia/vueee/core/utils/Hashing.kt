package com.freisia.vueee.core.utils

import java.security.MessageDigest

object Hashing {
    fun getSha(data: String): String{
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(data.toByteArray())
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

}