package com.example.branchcrm.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun View.showToast(message:String){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun Fragment.showToast(message: String) = requireView().showToast(message)

@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.N)
fun String.formatDate(): String? {
    val simpleDateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH)
    val timeStamp: Date? = simpleDateFormat.parse(this)
    val formatter: DateFormat = SimpleDateFormat("dd/MM/yy" + "  HH:mm aaa", Locale.ENGLISH)
    return timeStamp?.let { formatter.format(it) }
}