package com.zj.imcore.model

import com.google.gson.annotations.SerializedName

open class BaseApiMod {

    @SerializedName("message")
    var errorMessage: String = ""
}