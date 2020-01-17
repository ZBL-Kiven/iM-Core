package com.zj.imcore.im.loader

data class LoaderInfo(val msgId: Long, val sessionId: Long, val uid: Long, val isLoadNewer: Boolean, val limit: Int)