package com.zj.imcore.im.transfer

import com.zj.model.chat.DialogInfo
import com.zj.model.interfaces.DialogIn

object DialogTransfer {

    fun transform(i: Int): DialogInfo {
        return DialogInfo(object : DialogIn {
            override fun getId(): String {
                return "8589934605"
            }

            override fun getTitle(): String {
                return "test - $i"
            }

            override fun getSubDetail(): String {
                return "asdasdasd"
            }

            override fun getLatestTs(): Long {
                return i.toLong()
            }

            override fun getSelfReadTs(): Long {
                return 0
            }

            override fun getUnReadCount(): Int {
                return i
            }

            override fun getOtherReadTs(): Long {
                return 1
            }

            override fun getUserId(): String? {
                return "2"
            }

            override fun hasStar(): Boolean {
                return false
            }

            override fun getDraft(): String? {
                return "this is example draft text"
            }

            override fun isShown(): Boolean {
                return true
            }

            override fun sortTs(): Long {
                return i * 1L
            }

            override fun notification(): Boolean {
                return true
            }

            override fun hideTs(): Long {
                return 0
            }

            override fun getThumbUrl(): String {
                return "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3835973537,1602418292&fm=26&gp=0.jpg"
            }

            override fun isPin(): Boolean {
                return false
            }

            override fun isMute(): Boolean {
                return false
            }

            override fun isDelete(): Boolean {
                return false
            }
        })
    }

    fun getTestData(): ArrayList<DialogInfo> {
        return arrayListOf<DialogInfo>().apply {
            (0 until 1).forEach { i ->
                add(transform(i))
            }
        }
    }
}