package com.zj.imcore.im.options.mod

import com.google.gson.annotations.SerializedName

class BaseMod {
    var type: String = ""
    @SerializedName("call_id")
    var callId: String = ""
    var data: Any? = null
}