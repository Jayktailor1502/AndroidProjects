package com.fs.antitheftsdk.constants
internal class FsServiceErrorUtils {

    enum class FSServiceError(var code: Int?) {
        internal_server_error( 500),
        unauthorized(401),
        not_implemented(501),
        invalid_input(400),
       not_found(404),
        not_allowed(403),
        no_access(403),
        already_exists(409),
        size_limit(413),
        input_too_large(413),
        rate_limit(429),


    }

    fun getEnumFromString(errorCode: Int?): FSServiceError? {
        for (errorEnum in FSServiceError.values()) {
            if (errorEnum.code== errorCode) {
                return errorEnum
            }
        }
        return FSServiceError.internal_server_error
    }
}