package com.zj.imcore.im.fetcher

import com.alibaba.fastjson.JSON
import com.cf.im.db.repositorys.MemberRepository
import com.zbl.api.base.BaseRetrofit
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.ui.dispatcher.UIStore
import com.zj.ui.log
import com.zj.imcore.apis.fetcher.FetcherApi
import com.zj.imcore.apis.members.MemberApi
import com.zj.imcore.im.fetcher.interfaces.FetcherDialogsIn
import com.zj.imcore.im.fetcher.interfaces.FetcherMembersIn
import com.zj.imcore.model.member.MembersEventMod
import java.util.concurrent.ConcurrentHashMap

class Fetcher(private var isCompleted: ((String, Boolean) -> Unit)?) {

    companion object {
        private const val FETCH_MSG_CODE = 440
        private const val FETCH_DIALOGT_CODE = 441
        private const val FETCH_MEMBERS_CODE = 442
    }

    private var compons: ConcurrentHashMap<Int, BaseRetrofit.RequestCompo> = ConcurrentHashMap()

    private var isDialogsFetched = false
    private var isMessagesFetched = false
    private var isCanceled = false

    init {
        fetchMembers(FetcherMembersIn { name, isSuccess ->
            if (!isCanceled) if (!isSuccess) {
                onFetchFailed(name)
            } else {
                onFetchDialogs()
            }
        })
    }

    private fun onFetchDialogs() {
        fetchDialogs(FetcherDialogsIn { name, isSuccess ->
            if (!isCanceled) if (!isSuccess) {
                onFetchFailed(name)
            } else {
                onFetchMessages()
            }
        })
    }

    private fun onFetchMessages() {
        //        fetchMessages(FetcherMsgIn { name, isSuccess ->
        //            if (!isCanceled) if (!isSuccess) {
        //                onFetchFailed(name)
        //            } else {
        onFetchSuccessed(/***name*/
            "")
        //            }
        //        })
    }

    private fun fetchMembers(fetcherMembersIn: FetcherMembersIn) {
        val since = SPUtils_Proxy.getMemberSyncSince(0)
        val cop = MemberApi.fetchMembers(since) { isSuccess, data, throwable ->
            val obj = JSON.parseObject(data?.string())
            if (isSuccess && !obj.isNullOrEmpty()) {
                @Suppress("CAST_NEVER_SUCCEEDS") val nextTs = obj["next_ts"].toString().toLong()
                val d = obj["members"].toString()
                SPUtils_Proxy.setMemberSyncSince(nextTs)
                MemberRepository.insertOrUpdateAll(d) {
                    if (!it.isNullOrEmpty()) {
                        UIStore.postData(MembersEventMod("fetch members fetchStep"))
                    }
                    fetcherMembersIn.onResult("members", true)
                }
            } else {
                fetcherMembersIn.onResult("members", false)
                log("fetch members failed ,case: ${throwable?.response()?.errorBody()?.string()}")
            }
        }
        compons[FETCH_MEMBERS_CODE] = cop
    }

    private fun fetchDialogs(fetcherDialogsIn: FetcherDialogsIn) {
        val cop = FetcherApi.syncDialogs { b, httpException ->
            if (b) {
                compons.remove(FETCH_DIALOGT_CODE)
                isDialogsFetched = true
            } else {
                log("fetch dialogs failed ,case: ${httpException?.response()?.errorBody()?.string()}")
            }
            fetcherDialogsIn.onResult("dialogs", b)
        }
        compons[FETCH_DIALOGT_CODE] = cop
    }

    //    private fun fetchMessages(dialogId: String, msgId: String, fetcherMsgIn: FetcherMsgIn) {
    //        val cop = FetcherApi.syncMessages(dialogId, msgId, 20, true) { b, httpException ->
    //            if (b) {
    //                compons.remove(FETCH_DIALOGT_CODE)
    //                isMessagesFetched = true
    //            } else {
    //                log("fetch messages failed ,case: ${httpException?.response()?.errorBody()?.string()}")
    //            }
    //            fetcherMsgIn.onResult("messages", b)
    //        }
    //        compons[FETCH_MSG_CODE] = cop
    //    }

    private fun onFetchFailed(name: String) {
        isCompleted?.invoke(name, false)
        shutdown()
    }

    private fun onFetchSuccessed(name: String) {
        isCompleted?.invoke(name, true)
        shutdown()
    }

    fun shutdown() {
        isCompleted = null
        isDialogsFetched = false
        isMessagesFetched = false
        compons.forEach { (k, v) ->
            v.cancel()
            compons.remove(k)
        }
    }
}