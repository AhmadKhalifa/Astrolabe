package com.khalifa.astrolabe.data.repository

import com.khalifa.astrolabe.util.NoInternetConnectionException
import com.khalifa.astrolabe.util.BaseConnectionChecker
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Ahmad Khalifa
 */

interface Repository

open class BaseLocalRepository

open class BaseRemoteRepository(
        private val connectionChecker: BaseConnectionChecker?,
        private val baseUrl: String?
) {

    private val serviceMap: MutableMap<Class<*>, Any> = HashMap()

    @Suppress("UNCHECKED_CAST")
    protected fun <T> create(clazz: Class<T>): T {
        val service: T
        if (serviceMap.containsKey(clazz)) {
            service = serviceMap[clazz] as T
        } else {
            service = retrofit().create(clazz)
            serviceMap.put(clazz, service!!)
        }
        return service
    }

    @Throws(Throwable::class)
    protected fun <T> execute(call: Call<T>): T {
        if (!isNetworkAvailable()) {
            throw NoInternetConnectionException()
        }
        val response: Response<T> = call.execute()
        if (!response.isSuccessful) {
            throw Throwable(response.message())
        }
        return response.body()!!
    }

    private fun retrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl!!)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                    OkHttpClient.Builder()
                            .readTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .build()
            ).build()

    protected fun <R>requireInternet(action: () -> R): R {
        if (isNetworkAvailable()) {
            return action()
        } else throw NoInternetConnectionException()
    }

    protected fun isNetworkAvailable() = connectionChecker?.isNetworkAvailable() ?: false
}