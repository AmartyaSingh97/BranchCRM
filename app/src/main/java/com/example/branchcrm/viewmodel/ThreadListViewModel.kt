package com.example.branchcrm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.branchcrm.API_FAILED
import com.example.branchcrm.AUTH
import com.example.branchcrm.network.AppObjectController
import com.example.branchcrm.responseModel.GetMessageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ThreadListViewModel(application: Application): AndroidViewModel(application)   {

    var apiStatus = MutableSharedFlow<String?>(replay = 0)

    val messages = MutableSharedFlow<GetMessageResponse?>(replay = 0)

    fun getMessages(){
        viewModelScope.launch(Dispatchers.IO) {
            val authToken = AppObjectController.read(getApplication<Application>().applicationContext, AUTH)
            val resp = authToken?.let { AppObjectController.service.getMessages(it) }
            if (resp != null) {
                if (resp.isSuccessful){
                    messages.emit(resp.body())
                }else{
                    apiStatus.emit(API_FAILED)
                }
            }
        }
    }

    fun reset() {
        viewModelScope.launch(Dispatchers.IO) {
            AppObjectController.remove(getApplication<Application>().applicationContext)
            val authToken = AppObjectController.read(getApplication<Application>().applicationContext, AUTH)
            authToken?.let { AppObjectController.service.resetApi(it) }
        }
    }
}