package com.enes.appleapi.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.enes.appleapi.model.BaseModel
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class Util {
    companion object{
        fun hideKeyboard(context: Context, view: View){
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        fun showKeyboard(context: Context,view: View){
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view,InputMethodManager.SHOW_FORCED)
        }
    }
}

fun String.getFormattedDate(control: Boolean = true): String {
    if (this.isNotEmpty()){
        var  inputDate : Date? = null
        var outputDate: String? = null
        val  inputFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = if (!control) SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()) else SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
        inputDate = inputFormat.parse(this)
        outputDate = outputFormat.format(inputDate)
        return outputDate
    }else return ""
}

fun String.getSpaceReplace(): String{
    var index: Int = 0
    var search : String = ""
    if (this.isNotEmpty())
    for (i in 0 until this.length){
        if (this[i] == ' '){
            if (index + 1 == i){

            }else{
                search += "+"
                index = i
            }

        } else search += this[i]
    }
    return search
}

class JsonCast {

    companion object{
        @JvmStatic
        fun < T : BaseModel?> castClassList(jsonElements: JsonElement, clazz: Class<T>) : ArrayList<T>{
            val  modelClassList : ArrayList<T> = ArrayList()
            try{
                for (i in 0 until (jsonElements as JsonArray).size()) {
                    val model : T = Gson().fromJson(jsonElements[i], clazz)
                    modelClassList.add(model)
                }
            }catch (excaption : ClassCastException){
                Log.d("Exception",excaption.message.toString())
            }
            return modelClassList

        }

        fun < T : BaseModel?> castClass(jsonElements: JsonElement, clazz: Class<T>) : T? {
            val model = if (jsonElements != null) Gson().fromJson(jsonElements, clazz) else null
            return model
        }
    }


}
