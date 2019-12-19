package com.zj.imcore.enums

enum class MsgSubtype(private val subTypeName: String) {

    IMAGE("image"), VIDEO("video"), FILE("file");

    fun eq(name: String?): Boolean {
        return name.equals(this.subTypeName, true)
    }

    override fun toString(): String {
        return subTypeName
    }
}