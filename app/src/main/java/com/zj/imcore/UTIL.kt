package com.zj.imcore

fun makeSentParams(callId: String, localFilePath: String? = null): Map<String, Any> {
    val map = hashMapOf<String, Any>()
    val uid = "=bwNpr"
    map["call_id"] = callId
    map["local_created_ts"] = System.currentTimeMillis()
    if (localFilePath != null) map["localFilePath"] = localFilePath
    map["uid"] = uid
    return map
}