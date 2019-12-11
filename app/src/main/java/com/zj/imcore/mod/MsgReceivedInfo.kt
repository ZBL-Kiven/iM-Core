package com.zj.imcore.mod

data class MsgReceivedInfo(var msgInfo: MsgInfo? = null, var isSelf: Boolean = false, var createTs: Long)
