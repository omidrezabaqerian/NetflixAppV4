package ir.omidrezabagherian.testapplicationfour

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ImageService {
    @Multipart
    @POST("http://51.195.19.222/users/omidrezabagherian")
    fun uploadImage(
        @Part photo: MultipartBody.Part,
        @Part("gender") gender: String,
        @Part("price") price: String
    ): Call<String>

    @GET("http://51.195.19.222/uploads/{image}")
    fun downloadImage(
        @Path("image") image:String
    ):Call<ResponseBody>

}