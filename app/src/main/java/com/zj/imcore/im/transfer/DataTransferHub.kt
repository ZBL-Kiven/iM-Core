package com.zj.imcore.im.transfer

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zj.im.UIHelper
import com.zj.im.chat.enums.SendMsgState
import com.zj.imcore.im.options.mod.BaseMod
import com.zj.imcore.utils.unity.DateUtils
import com.zj.model.chat.DialogInfo
import com.zj.model.chat.ProgressInfo
import com.zj.model.interfaces.DialogIn
import com.zj.model.mod.MessageBean
import java.util.*

object DataTransferHub {

    //todo 临时拆分
    fun onSocketDataReceived(data: String?, callId: String?, sendingState: SendMsgState?, onFinish: () -> Unit) {
//        val d = Gson().fromJson(data, JsonObject::class.java)
//        val msg = Gson().fromJson(d.get("data").toString(), MessageBean::class.java)
//        mCacheMsgs.firstOrNull { it == msg }?.let {
//            msg.callId = it.callId
//            msg.localCreateTs = System.currentTimeMillis()
//        } ?: {
//            msg.callId = if (callId.isNullOrEmpty()) d.get("call_id").asString else callId
//            msg.sendMsgState = sendingState?.type ?: 0
//            //            val createDate: Date? = DateUtils.getDate("yyyy-MM-dd'T'HH:mm:ss'Z'", msg.created)
//            msg.localCreateTs = System.currentTimeMillis()
//        }.invoke()
//        mCacheMsgs.add(msg)
//        val s = mCacheMsgs.joinToString { "${it.text} \n" }
        println("----- $data")
        //        UIHelper.postReceiveData(msg)
        onFinish()
    }

    fun onSendingProgressChanged(process: Int, callId: String) {
        UIHelper.postReceiveData(ProgressInfo(callId, process))
    }

    fun queryDialogInDb() {
        val dialogs = arrayListOf<DialogInfo>().apply {
            (0..10).forEach { i ->
                add(DialogInfo(object : DialogIn {
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
                        return ""
                    }

                    override fun isShown(): Boolean {
                        return true
                    }

                    override fun sortTs(): Long {
                        return 0
                    }

                    override fun notification(): Boolean {
                        return true
                    }

                    override fun hideTs(): Long {
                        return 0
                    }

                    override fun getThumbUrl(): String {
                        return "http://img4.imgtn.bdimg.com/it/u=2853553659,1775735885&fm=26&gp=0.jpg"
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
                }))
            }
        }
        UIHelper.postReceiveData(dialogs)
    }

    fun queryMsgInDb(uid: String, dialogId: String) {
        UIHelper.postReceiveData(mCacheMsgs)
    }

    val mCacheMsgs = mutableListOf<MessageBean>()

}