package org.sopt24.dshyun0226.androidseminar.Network.POST

data class PostSignupResponse(
     val status: Int,
     val success: Boolean,
     val message: String,
     val data  : String?
)