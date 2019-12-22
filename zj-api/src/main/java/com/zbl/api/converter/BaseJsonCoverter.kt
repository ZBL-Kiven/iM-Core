package com.zbl.api.converter

import com.google.gson.Gson
import retrofit2.Converter

abstract class BaseJsonConverter : Converter.Factory() {

    open val gson = Gson()


}