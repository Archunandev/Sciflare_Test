package com.greedy.sciflaretest.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.greedy.sciflaretest.app.SciflareTech
import com.greedy.sciflaretest.model.Users
import com.greedy.sciflaretest.repository.SciflareRepo
import com.greedy.sciflaretest.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

class SciflareViewModel(app: Application, val sciflareRepo: SciflareRepo) : AndroidViewModel(app) {

    val usersList: MutableLiveData<Resource<List<Users>>> = MutableLiveData()
    var usersResponse: List<Users>? = null

    init {
        getUsers()
    }

    fun getUsers() = viewModelScope.launch {
        usersCall()
    }

    fun saveUsers(users: List<Users>) = viewModelScope.launch {
        sciflareRepo.upsert(users)
    }

    fun getSavedUsers() = sciflareRepo.getSavedUsers()
    
    fun deleteUsers() = viewModelScope.launch { sciflareRepo.deleteuser() }


    private suspend fun usersCall() {
        usersList.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response = sciflareRepo.getUsersfromrepo()
                usersList.postValue(usersres(response))
            } else {
                usersList.postValue(Resource.Error("No internet connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> usersList.postValue(Resource.Error("Network Failure"))
                is UnknownHostException -> usersList.postValue(Resource.Error("Unknown host!"))
                else -> usersList.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun usersres(response: Response<List<Users>>): Resource<List<Users>>? {
        if (response.isSuccessful) {
            response.body().let { resultResponse ->
                usersResponse = resultResponse
                return (usersResponse ?: resultResponse)?.let { Resource.Success(it) }
            }
        }
        return Resource.Error(response.message())
        }


    private fun hasInternetConnection(): Boolean {

        val connectivityManager = getApplication<SciflareTech>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false

            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}