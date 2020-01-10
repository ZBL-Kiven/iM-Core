@file:Suppress("unused")

package com.zj.imcore

import android.util.Log
import android.view.View
import android.view.ViewGroup
import java.lang.IllegalArgumentException

fun makeSentParams(callId: String, localFilePath: String? = null): Map<String, Any> {
    val map = hashMapOf<String, Any>()
    val uid = "=bwNpr"
    map["call_id"] = callId
    map["local_created_ts"] = System.currentTimeMillis()
    if (localFilePath != null) map["localFilePath"] = localFilePath
    map["uid"] = uid
    return map
}

fun getGender(isLady: Boolean): String {
    return if (isLady) "lady" else "male"
}

fun genderIsLady(gender: String): Boolean {
    return when (gender) {
        "lady" -> true
        else -> false
    }
}

fun printViewTree(view: View) {
    fun printViews(view: View, sb: StringBuffer, i: Int = 0) {
        repeat((0 until i).count()) {
            sb.append(">")
        }
        sb.append(" ${view.javaClass.simpleName}").append("\n")

        val sb1 = StringBuffer()
        repeat((0 until i + 1).count()) {
            sb1.append(">")
        }

        (view as? ViewGroup)?.let {
            if (it.childCount == 0) {
                throw IllegalArgumentException("!!!")
            }
            (0 until it.childCount).forEach { i ->
                view.getChildAt(i)?.let { v ->
                    if (v is ViewGroup) {
                        printViews(v, sb, i + 1)
                    } else sb.append("${String(sb1)} ${v.javaClass.simpleName}\n")
                }
            }
        }
    }

    val sb = StringBuffer()
    printViews(view, sb)
    val s = sb.toString()
    Log.e("a-----", "-\n$s")

}