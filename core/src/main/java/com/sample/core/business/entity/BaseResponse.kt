package com.sample.core.business.entity

import com.google.gson.annotations.SerializedName
import com.sample.core.extensions.empty

open class BaseResponse {

    @SerializedName("status")
    val status: Boolean = false

    @SerializedName("message")
    var message: String = String.empty
}