package com.sample.core.business.entity

import com.google.gson.annotations.SerializedName
import com.sample.core.extensions.empty

data class EncryptedResponse (
    @SerializedName("iv") val iv: String = String.empty,
    @SerializedName("encryptedData") val encryptedData: String = String.empty
): BaseResponse()