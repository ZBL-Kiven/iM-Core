package com.zj.imcore.ui.main.contact

import com.alibaba.fastjson.JSON
import com.cf.im.db.domain.MemberBean
import com.cf.im.db.repositorys.MemberRepository
import com.zj.base.utils.storage.sp.SPUtils_Proxy
//import com.zj.imcore.apis.members.MemberApi
import com.zj.imcore.model.member.MembersEventMod
import com.zj.ui.dispatcher.UIStore
import com.zj.ui.mainHandler

interface MembersVisitor {
    fun onGot(m: List<MemberBean>?)
}

object MembersProvider {

    private var isContactLoading = false
    fun getMembersFromLocalOrServer(isFirst: Boolean, vistor: MembersVisitor) {
        MemberRepository.queryAll {
            if (it.isNullOrEmpty()) {
                if (isContactLoading) {
                    return@queryAll
                }
                if (isFirst) getData()
                else vistor.onGot(null)
            } else {
                mainHandler.post {
                    vistor.onGot(it)
                }
            }
        }
    }

    private fun getData() {
        isContactLoading = true
//        val since = SPUtils_Proxy.getMemberSyncSince(0)
//        MemberApi.fetchMembers(since) { isSuccess, data, _ ->
//            if (isSuccess) {
//                val obj = JSON.parseObject(data?.string())
//                if (obj.isNullOrEmpty()) {
//                    @Suppress("CAST_NEVER_SUCCEEDS") val nextTs = obj["next_ts"] as Long
//                    val d = obj["members"].toString()
//                    MemberRepository.insertOrUpdateAll(d) {
//                        if (!it.isNullOrEmpty()) {
//                            UIStore.postData(MembersEventMod("get members with null local"))
//                        }
//                    }
//                }
//            }
//            isContactLoading = false
//        }
    }

}