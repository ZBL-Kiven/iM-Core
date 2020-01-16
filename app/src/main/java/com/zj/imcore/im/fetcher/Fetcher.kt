package com.zj.imcore.im.fetcher

import com.cf.im.db.repositorys.MemberRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zbl.api.base.BaseRetrofit
import com.zj.base.utils.storage.sp.SPUtils_Proxy
import com.zj.im.dispatcher.UIStore
import com.zj.im.log
import com.zj.imcore.apis.fetcher.FetcherApi
import com.zj.imcore.apis.members.MemberApi
import com.zj.imcore.model.member.MembersEventMod

class Fetcher(private var isCompleted: ((Boolean) -> Unit)?) {

    companion object {
        private const val FETCH_DIALOGT_CODE = 441
        private const val FETCH_MEMBERS_CODE = 442
    }

    private var compons: HashMap<Int, BaseRetrofit.RequestCompo> = hashMapOf()

    private var isDialogsFetched = false
    private var isMessagesFetched = false
    private var isMembersFetched = false

    private var fetchStep: ((Boolean) -> Unit)? = null

    init {
        fetchDialogs()
        fetchMembers()
        fetchStep = {
            if (!it) {
                shutdown()
                isCompleted?.invoke(false)
            } else if (isDialogsFetched && isMembersFetched) {
                isCompleted?.invoke(true)
            }
        }
    }

    private fun fetchDialogs() {
        val cop = FetcherApi.syncDialogs { b, httpException ->
            if (b) {
                compons.remove(FETCH_DIALOGT_CODE)
                isDialogsFetched = true
            } else {
                log("fetch dialogs failed ,case: ${httpException?.response()?.errorBody()?.string()}")
            }
            fetchStep?.invoke(b)
        }
        compons[FETCH_DIALOGT_CODE] = cop
    }

    private fun fetchMembers() {
        val since = SPUtils_Proxy.getMemberSyncSince(0)
        val cop = MemberApi.fetchMembers(since) { isSuccess, data, throwable ->
            val obj = Gson().fromJson(data, JsonObject::class.java)
            if (isSuccess && obj != null) {
                @Suppress("CAST_NEVER_SUCCEEDS") val nextTs = obj["next_ts"] as Long
                val d = obj["members"].toString()
                SPUtils_Proxy.setMemberSyncSince(nextTs)
                MemberRepository.insertOrUpdateAll(d) {
                    UIStore.postData(MembersEventMod("fetch members fetchStep"))
                    isMembersFetched = true
                    fetchStep?.invoke(true)
                }
            } else {
                fetchStep?.invoke(false)
                log("fetch dialogs failed ,case: ${throwable?.response()?.errorBody()?.string()}")
            }
        }
        compons[FETCH_MEMBERS_CODE] = cop
    }

    fun shutdown() {
        isCompleted = null
        isDialogsFetched = false
        isMessagesFetched = false
        isMembersFetched = false
        compons.forEach { (k, v) ->
            v.cancel()
            compons.remove(k)
        }
    }
}