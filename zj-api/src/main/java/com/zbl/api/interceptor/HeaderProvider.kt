package com.zbl.api.interceptor

interface HeaderProvider {

    fun headers(): Map<String, String>

}