package com.example.branchcrm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.branchcrm.API_FAILED
import com.example.branchcrm.AUTH
import com.example.branchcrm.models.MessageModel
import com.example.branchcrm.network.AppObjectController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class ThreadDetailsViewModel(application: Application): AndroidViewModel(application)   {

    var apiStatus = MutableSharedFlow<String?>(replay = 0)

    val message = MutableSharedFlow<MessageModel?>(replay = 0)

    fun sendMessages(threadId: Int, messageToSend: String){
        viewModelScope.launch(Dispatchers.IO) {
            val authToken = AppObjectController.read(getApplication<Application>().applicationContext, AUTH)
            val resp = authToken?.let { AppObjectController.service.sendMessage(it,threadId,messageToSend) }
            if (resp != null) {
                if (resp.isSuccessful){
                    message.emit(resp.body())
                }else{
                    apiStatus.emit(API_FAILED)
                }
            }
        }
    }

}