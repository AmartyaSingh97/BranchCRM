package com.example.branchcrm.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageModel (

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("thread_id")
    @Expose
    var threadId: Int,

    @SerializedName("user_id")
    @Expose
    var userId: String,

    @SerializedName("body")
    @Expose
    var body: String,

    @SerializedName("timestamp")
    @Expose
    var timestamp: String,

    @SerializedName("agent_id")
    @Expose
    var agentId: Int?

): Parcelable