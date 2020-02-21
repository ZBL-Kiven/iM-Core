package com.zj.imcore.ui.main.contact

import com.cf.im.db.repositorys.DialogRepository
import com.zj.imcore.im.transfer.DialogTransfer
import com.zj.model.chat.DialogInfo
import com.zj.ui.mainHandler

interface DialogsVisitor {
    fun onGot(m: List<DialogInfo>?)
}

object DialogsProvider {

    fun getDialogsFromLocalOrServer(vistor: DialogsVisitor) {
        DialogRepository.queryDialog {
            if (!it.isNullOrEmpty()) {
                mainHandler.post {
                    vistor.onGot(DialogTransfer.transform(it))
                }
            }
        }
    }
}