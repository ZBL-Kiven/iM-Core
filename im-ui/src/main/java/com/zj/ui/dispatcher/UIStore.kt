package com.zj.ui.dispatcher

import com.zj.ui.debugLog
import com.zj.ui.log

object UIStore {

    private var msgObservers: HashSet<UIOptions<*, *>>? = null
        get() {
            if (field == null) field = hashSetOf()
            return field
        }

    internal fun putAnObserver(option: UIOptions<*, *>) {
        msgObservers?.add(option)
    }

    internal fun removeAnObserver(option: UIOptions<*, *>) {
        msgObservers?.remove(option)
    }

    /**
     * post a data into msg processor ,
     *
     * only supported type Data or List<Data>
     * */
    fun postData(data: Any?, payload: String? = null) {
        if (data == null) {
            log("why are you post a null object?")
            return
        }
        var isUsed = false
        msgObservers?.forEach {
            if (it.post(data, payload)) {
                isUsed = true
                debugLog("the observer names ${it.getUniquen()} and subscirbe of ${it.getSubscirbeClassName()}.class successful and received the data")
            }
        }
        if (!isUsed) debugLog("the data ${data.javaClass.simpleName}.class has been abandon with none consumer")
    }
}