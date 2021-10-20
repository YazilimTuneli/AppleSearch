package com.enes.appleapi.api

import com.google.gson.JsonElement
import rx.Observable

class AppleService(var protocol: ServiceProtocol) {

    fun postRequest( url: String, queryMap: HashMap<String, Any?>): Observable<JsonElement> {
        return protocol.postRequest(url, queryMap)
    }

    fun getRequest(url: String,queryMap: HashMap<String, Any?>): Observable<JsonElement> {
        return protocol.getRequest(url, queryMap)
    }

}