package com.zj.imcore.apis.group

import com.cf.im.db.domain.DialogBean
import com.zbl.api.BaseApi
import com.zj.imcore.apis.APIs
import retrofit2.HttpException

class CreateDialog {
    var type: String = "group"
    var team_id: String? = null
    var name: String? = null
    var members: List<String>? = null
}

object GroupApi {

    private fun get(): BaseApi<GroupApiService> {
        return APIs.getDefaultApi()
    }

    fun createDialog(
        dialog: CreateDialog,
        result: (Boolean, String?, exception: HttpException?) -> Unit
    ) {
        get().call({ it.createDialog(dialog) }, result)
    }

    fun queryDialog(
        dialogId: String,
        result: (Boolean, DialogBean?, exception: HttpException?) -> Unit
    ) {
        get().call({ it.queryDialog(dialogId) }, result)
    }

    /////杨吉 更新 讨论组信息
    fun updateDialog(
        dialogId: String,
        request: Map<String, Any>,
        result: (Boolean, String?, throwable: HttpException?) -> Unit
    ) {
        get().call({ it.update(dialogId, request) }, result)
    }

    fun removeUserToDialog(
        dialogId: String,
        teamId: String,
        result: (Boolean, String?, throwable: HttpException?) -> Unit
    ) {
        get().call({ it.removeUserToDialog(dialogId, teamId, "kick") }, result)
    }

    fun editUserToDialog(
        dialogId: String,
        teamId: String,
        result: (Boolean, String?, throwable: HttpException?) -> Unit
    ) {
        get().call({ it.removeUserToDialog(dialogId, teamId, "leave") }, result)
    }

    fun addUserToDialog(
        dialogId: String,
        map: Map<String, List<String>>,
        result: (Boolean, String?, throwable: HttpException?) -> Unit
    ) {
        get().call({ it.addUserToDialog(dialogId, map) }, result)
    }
}