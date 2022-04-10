package com.example.relevelandroidproject.util

sealed class ResponseState<T>(val data :T?=null,val message:String?=null){
    class Loading<T>(data:T?=null):ResponseState<T>(data)
    class Success<T>(data:T):ResponseState<T>(data)
    class Error<T>(message:String,data:T?=null):ResponseState<T>(data,message)
}
