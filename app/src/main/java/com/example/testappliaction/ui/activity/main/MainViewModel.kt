package com.example.testappliaction.ui.activity.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testappliaction.data.model.ResultData
import com.example.testappliaction.data.network.ApiClient
import com.example.testappliaction.data.network.responses.ResponseResult
import com.example.testappliaction.util.ext.logD
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {
    var success = MutableLiveData<Boolean>()
    var responseResult = MutableLiveData<ResultData>()
    var error = MutableLiveData<Pair<Boolean, String>>()
    var progressBar =  MutableLiveData<Boolean>()

    suspend fun getProductList() = withContext(Dispatchers.Main){
        progressBar.value = true
        when (val result = ApiClient.getProductList()) {
            is ResponseResult.Loading -> {

            }
            is ResponseResult.Success -> {
                if (result.result.data != null) {
                    val jsonObject = result.result.data
                    logD("$jsonObject")
                    val response = Gson().fromJson(jsonObject.toString(), ResultData::class.java)
                    success.value = true
                    responseResult.value = response

                } else {
                    result.result.errorMsg?.let {
                        logD(it)
                        error.value = Pair(true, it)
                    }
                }
            }
            is ResponseResult.Error -> {
                error.value = Pair(true, result.msg.toString())
            }
            is ResponseResult.NoInternet -> {
                error.value = Pair(true, "No Internet connection. Please check your network settings.")
            }
            else -> error.value = Pair(true, "Something went wrong, Please try again.")
        }
    }

}