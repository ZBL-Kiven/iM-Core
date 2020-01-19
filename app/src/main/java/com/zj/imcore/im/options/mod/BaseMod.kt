package com.zj.imcore.im.options.mod

import com.alibaba.fastjson.annotation.JSONField

class BaseMod {
    var type: String = ""
    @JSONField(name = "call_id")
    var callId: String = ""
    var data: Any? = null
}