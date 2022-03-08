package ir.omidrezabagherian.netflix.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.omidrezabagherian.netflix.data.network.NetworkManager
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private var _profileResponse = MutableLiveData<String>()
    var profileResponse: LiveData<String> = _profileResponse
    private var _profileError = MutableLiveData<String>()
    var profileError: LiveData<String> = _profileError

    fun uploadImage(src: ByteArray, nameFile: String) {
        val part = MultipartBody.Part.createFormData(
            "image",
            nameFile,
            RequestBody.create(MediaType.parse("image/*"), src)
        )
        NetworkManager.service.uploadImage(part).enqueue(object :
            Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _profileResponse.value = response.body()!!.toString()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _profileError.value = t.toString()
            }
        })
    }
}