@file:Suppress("unused")

package com.zj.im.emotionboard.data

open class Emoticon() {

    constructor(code: String?, uri: String?): this() {
        this.code = code
        this.uri = uri
    }

    var code: String? = null
    var uri: String? = null
}