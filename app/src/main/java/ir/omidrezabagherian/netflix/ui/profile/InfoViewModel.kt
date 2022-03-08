package ir.omidrezabagherian.netflix.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.omidrezabagherian.netflix.R
import ir.omidrezabagherian.netflix.data.network.NetworkManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoViewModel : ViewModel() {

    private var _infoResponse = MutableLiveData<Bitmap>()
    var infoResponse: LiveData<Bitmap> = _infoResponse
    private var _infoError = MutableLiveData<String>()
    var infoError: LiveData<String> = _infoError

    fun showInfo(context: Context, picture: String) {
        NetworkManager.service.downloadImage(picture).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.body() != null) {
                        val stream = response.body()!!.byteStream()
                        stream.let {
                            val bitmap = BitmapFactory.decodeStream(stream)
                            _infoResponse.value = bitmap
                        }
                    } else {
                        _infoResponse.value = BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.ic_baseline_person_24
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("Throwable", t.toString())
                }

            }
        )
    }
}