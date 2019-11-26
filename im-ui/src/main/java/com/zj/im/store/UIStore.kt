package com.zj.im.store

import java.util.*

class UIStore<DATA> {

    var uiQueue: PriorityQueue<DATA>? = null
        get() {
            if (field == null) field = PriorityQueue()
            return field
        }



}