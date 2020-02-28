package com.zj.imcore.utils.sender

import com.alibaba.fastjson.JSON
import com.zj.album.nModule.FileInfo
import com.zj.imcore.enums.MsgSubtype
import com.zj.imcore.enums.MsgType
import com.zj.imcore.gui.login.TeamManager
import com.zj.imcore.im.options.IMHelper
import com.zj.imcore.im.options.mod.BaseMod
import com.zj.model.mod.SendMessageBean
import java.util.*

object MsgSender {

    fun sendText(sessionId: String, text: String) {
        val callId = UUID.randomUUID().toString()
        val baseSendInfo = BaseMod()
        val m = SendMessageBean().apply {
            this.subtype = "normal"
            this.tmid = TeamManager.getTmId()
            this.team_id = TeamManager.getCurrentTeamId()
            this.dialog_id = sessionId
            this.localCreateTs = System.currentTimeMillis()
            this.text = text
            this.callId = callId
        }
        baseSendInfo.data = m
        baseSendInfo.callId = callId
        baseSendInfo.type = "create_message"
        val data = JSON.toJSONString(baseSendInfo)
        IMHelper.send(data, callId, 10000, false, false, null)
    }

    fun sendSticker(sessionId: String, path: String) {
        val callId = UUID.randomUUID().toString()
        val baseSendInfo = BaseMod()
        val m = SendMessageBean().apply {
            this.subtype = MsgType.STICKER.name
            this.tmid = TeamManager.getTmId()
            this.team_id = TeamManager.getCurrentTeamId()
            this.dialog_id = sessionId
            this.localCreateTs = System.currentTimeMillis()
            this.text = "[sticker]$path"
            this.callId = callId
        }
        baseSendInfo.data = m
        baseSendInfo.callId = callId
        baseSendInfo.type = "create_message"
        val data = JSON.toJSONString(baseSendInfo)
        IMHelper.send(data, callId, 10000, false, false, null)
    }

    fun sendImage(sessionId: String, info: FileInfo) {
        val callId = UUID.randomUUID().toString()
        val baseSendInfo = BaseMod()
        val m = SendMessageBean().apply {
            this.subtype = MsgType.FILE.name
            this.subtypeDetail = MsgSubtype.IMAGE.name
            this.tmid = TeamManager.getTmId()
            this.localCreateTs = System.currentTimeMillis()
            this.team_id = TeamManager.getCurrentTeamId()
            this.dialog_id = sessionId
            this.text = "[image]${info.path}"
            this.callId = callId
        }
        baseSendInfo.data = m
        baseSendInfo.callId = callId
        baseSendInfo.type = "create_message"
        val data = JSON.toJSONString(baseSendInfo)
        IMHelper.send(data, callId, 10000, false, false, null)
    }

    fun sendVideo(sessionId: String, info: FileInfo) {
        val callId = UUID.randomUUID().toString()
        val baseSendInfo = BaseMod()
        val m = SendMessageBean().apply {
            this.subtype = MsgType.FILE.name
            this.subtypeDetail = MsgSubtype.VIDEO.name
            this.tmid = TeamManager.getTmId()
            this.localCreateTs = System.currentTimeMillis()
            this.team_id = TeamManager.getCurrentTeamId()
            this.dialog_id = sessionId
            this.text = "[video]${info.path}"
            this.callId = callId
        }
        baseSendInfo.data = m
        baseSendInfo.callId = callId
        baseSendInfo.type = "create_message"
        val data = JSON.toJSONString(baseSendInfo)
        IMHelper.send(data, callId, 10000, false, false, null)
    }
}