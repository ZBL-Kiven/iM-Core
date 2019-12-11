package com.zj.imcore

import java.util.*

fun makeSentParams(callId: String, localFilePath: String? = null): Map<String, Any> {
    val map = hashMapOf<String, Any>()
    val uid = "=bwNpr"
    map["call_id"] = callId
    map["local_created_ts"] = System.currentTimeMillis()
    if (localFilePath != null) map["localFilePath"] = localFilePath
    map["uid"] = uid
    return map
}

val userId = "aaa"

fun msgIsSelf(uid: String?): Boolean {
    return uid == userId
}