package com.app.trustlink.extension


object Constant {
    const val BASE_URL_PASSIVE = "passive_liveness_url"
    const val BASE_URL_OBTAIN = "obtain"
    const val BASE_URL_SERVICES_TASK = "services_task"
    const val BASE_URL_ENCRYPTION = "user_encryption"
    const val BASE_URL_OCR = "ocr_url"
    const val BASE_URL_DUKCAPIL = "dukcapil_url"
    const val BASE_URL_DUKCAPIL_TOKEN = "dukcapil_token_url"

    const val END_POINT_OCR = "endpoint_ocr"
    const val END_POINT_DUKCAPIL_TOKEN = "endpoint_dokcapil_token"

//    const val API_KEY_APIGEE = "Qkm1VtCzB3Ooe39BMqd9Jw2gpmmUpO2heZweTMxjAKqKpt81"
//    const val API_SECREET_APIGEE = "n2sX42YeZmgc2HAyNQrnThBuDA0giLdU2PRQatcBAobv3dFhTnTRYsIdoEnR5h1Q"
//    const val API_KEY_OCR = "iBHIezhL2oCpel0ABKsj2iiFvyVGGFrQUFolhobB"

    const val API_KEY_APIGEE = "api_key_apigee"
    const val API_SECREET_APIGEE = "api_secreet_apigee"
    const val API_KEY_OCR = "api_key_ocr"

    const val ACCESS_TOKEN = "access_token"

    const val TITLE_OCR = "ocr"
    const val TITLE_PASSIVE_LIVENESS = "pasive_liveness"
    const val TITLE_OBTAIN = "obtain"
    const val TITLE_SERVICES_TASK = "services_task"
    const val TITLE_ENCRYPTION = "user_encryption"
    const val TITLE_DUKCAPIL_TOKEN = "dukcapil_token"

    const val ID_KTP = "id_ktp"

    interface HEADER {
        companion object {
            const val ACCEPT = "Accept: application/json"
            const val ACCEPT_ALL = "Accept: */*"
            const val CONTENT_TYPE = "Content-Type: application/json"
            const val CONTENT_TYPE_MULTIPART = "Content-Type: multipart/form-data "
            const val APP_ID = "App-ID"
            const val API_KEY = "API-Key"
        }
    }

    interface PREFERENCE {
        companion object {
            const val DATA_KTP = "data_ktp"
            const val USER_LOGIN = "user_login"
        }
    }

}