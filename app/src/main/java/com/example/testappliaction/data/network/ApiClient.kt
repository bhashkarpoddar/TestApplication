package com.example.testappliaction.data.network

import com.example.testappliaction.data.network.responses.ResponseResult
import com.example.testappliaction.data.network.responses.ResponseWrapper
import com.example.testappliaction.util.ext.logD
import com.squareup.okhttp.*
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit


object ApiClient {

    private var client: OkHttpClient = OkHttpClient()
    private val mediaTypeJson = MediaType.parse("application/json; charset=utf-8")

    private const val productList = "https://devfitser.com/PinkDelivery/dev/api/product/get"

    init {
        client.setReadTimeout(5, TimeUnit.MINUTES)
        client.setConnectTimeout(5, TimeUnit.MINUTES)
    }


    suspend fun getProductList(): ResponseResult<ResponseWrapper<JSONObject>> {
        val json = "{\"store_id\":\"3\",\"u_id\":\"\",\"access_type\":\"1\",\"source\":\"mob\"}"
        val requestBody = RequestBody.create(mediaTypeJson, json)
        val request = Request.Builder().url(productList)
            .post(requestBody)
            .build()
        try {
            val result = apiRequest(request)
            logD("Product Result: $result")
            return if (result != null) {
                val jsonObject = JSONObject(result)
                logD("Json: $jsonObject")
                val statusObject = jsonObject.getJSONObject("status")
                if (statusObject.getInt("error_code") == 0) {
                    val data = jsonObject.getJSONObject("result")
                    ResponseResult.Success(ResponseWrapper(data, "Success"))
                } else {
                    val msg = statusObject.getString("message")
                    ResponseResult.Success(ResponseWrapper(null, "Error: $msg"))
                }
            } else {
                ResponseResult.Error(ResponseWrapper(null, "Getting NULL as result"))
            }
        } catch (jsonException: JSONException) {
            logD("Product JSON exception::: \n ${jsonException.localizedMessage}")
            return ResponseResult.Error(ResponseWrapper(null, "Json Exception"))
        } catch (exception: Throwable) {
            logD("Network exception::: \n ${exception.localizedMessage}")
            return ResponseResult.NoInternet
        }
    }

    private suspend fun apiRequest(request: Request): String? = suspendCancellableCoroutine { cancellableContinuation ->
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                cancellableContinuation.resumeWith(Result.failure(Throwable("API failed.....${e?.localizedMessage}")))
            }

            override fun onResponse(response: Response?) {
                cancellableContinuation.resumeWith(Result.success(response?.body()?.string()))
            }
        })
    }
}