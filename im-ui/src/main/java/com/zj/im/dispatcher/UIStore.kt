package com.zj.im.dispatcher

import com.zj.im.log

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
    fun postData(data: Any?) {
        if (data == null) {
            log("why are you post a null object?")
            return
        }
        msgObservers?.forEach {
            if (it.post(data)) {
                log("the observer names ${it.getUniquen()} and subscirbe of ${it.getSubscirbeClassName()}.class successful and received the data")
            } else {
                log("invalid observer names ${it.getUniquen()} and subscirbe of ${it.getSubscirbeClassName()}.class has abandon the data ${data.javaClass.simpleName}.class")
            }
        }
    }
}