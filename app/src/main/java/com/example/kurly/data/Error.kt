package com.example.kurly.data

import com.google.gson.Gson
import retrofit2.HttpException
import timber.log.Timber

/**
 * BCMProject
 * Class: Error
 * Created by 박수연 on 2021-04-19.
 *
 * Description: 서버 return시 Error 처리
 */

enum class E_API_CODE constructor(val code: Int) {
    SERVER_SUCCESS(200),
    CLIENT_ERROR(400),
    TOKEN_ERROR(401),
    SERVER_ERROR(500),
    SERVICE_INSPECT(503),
    UNEXPECTED_ERROR(999)
}

data class Error(
    val message: String,    //에러 메세지
    val status: Int         //에러 코드(200, 401, 500...etc)
)

/**
 * Exception 처리를 위한 확장 함수
 */
fun Exception?.toError(): Error? {

    var error: Error? = null

    if (this != null) {
        Timber.d("this() = $this")
        if (this is HttpException) {
            try {
                Timber.d("response() = ${response()}")
                val body = response()?.errorBody()?.string()
                Timber.d("body() = $body")
                error = Gson().fromJson(body, Error::class.java)
            } catch (e: java.lang.Exception) {
                Timber.e(e)
                e.message?.let {
                    error = Error(it, E_API_CODE.UNEXPECTED_ERROR.code)
                }
            }
        } else if (!message.isNullOrEmpty()) {
            error = if (this.toString()
                    .contains(E_API_CODE.TOKEN_ERROR.code.toString())
            ) {     //refreshTokenExpireTime 만료시 에러처리 추가
                Error("", E_API_CODE.TOKEN_ERROR.code)
            } else {
                Error(message!!, E_API_CODE.UNEXPECTED_ERROR.code)
            }
        }
    }

    return error
}