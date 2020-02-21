package com.zj.imcore.apis.group

import com.zbl.api.BaseApi
import com.zj.imcore.apis.APIs
import retrofit2.HttpException

class CreateDialog(
    var type: String,
    var team_id: String,
    var name: String,
    var members: List<String>
)

object GroupApi {

    private fun get(): BaseApi<GroupApiService> {
        return APIs.getDefaultApi()
    }

    fun create(
        dialog: CreateDialog,
        result: (Boolean, String?, exception: HttpException?) -> Unit
    ) {
        get().call({ it.createDialog(dialog) }, result)
    }

    fun queryDialog(
        dialogId: String,
        result: (Boolean, String?, exception: HttpException?) -> Unit
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
        userId: String,
        result: (Boolean, String?, throwable: HttpException?) -> Unit
    ) {
        get().call({ it.removeUserToDialog(dialogId, teamId, userId) }, result)
    }
}