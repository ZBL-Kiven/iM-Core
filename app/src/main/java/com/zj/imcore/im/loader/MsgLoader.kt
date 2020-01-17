package com.zj.imcore.im.loader

object MsgLoader {


    fun load(info:LoaderInfo){
        if(info.isLoadNewer){
            loadNewer(info)
        }else{
            loadOlder(info)
        }
    }

    private fun loadNewer(info: LoaderInfo) {

    }

    private fun loadOlder(info: LoaderInfo) {

    }


}