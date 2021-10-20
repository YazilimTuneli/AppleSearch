package com.enes.appleapi.api

import com.google.gson.JsonElement
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url
import rx.Observable

interface ServiceProtocol {
    @POST
    fun postRequest(@Url url: String, @QueryMap queryMap: HashMap<String, Any?>): Observable<JsonElement>

    @GET
    fun getRequest(@Url url: String, @QueryMap queryMap: HashMap<String, Any?>): Observable<JsonElement>

}