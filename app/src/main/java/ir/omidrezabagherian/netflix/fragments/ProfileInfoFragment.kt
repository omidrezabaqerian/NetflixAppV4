package ir.omidrezabagherian.netflix.fragments

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ir.omidrezabagherian.netflix.R
import ir.omidrezabagherian.netflix.databinding.FragmentProfileInfoBinding
import ir.omidrezabagherian.testapplicationfour.NetworkManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileInfoFragment : Fragment(R.layout.fragment_profile_info) {

    private lateinit var profileInfoBinding: FragmentProfileInfoBinding
    private lateinit var profileInfoNavController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref =
            activity?.getSharedPreferences(SharedPreferences.FILE_NAME, Context.MODE_PRIVATE)

        profileInfoBinding = FragmentProfileInfoBinding.bind(view)

        val profileInfoNavHostFragment =
            activity!!.supportFragmentManager.findFragmentById(R.id.fragment_container_main) as NavHostFragment

        profileInfoNavController = profileInfoNavHostFragment.navController

        val picture =
            sharedPref?.getString(SharedPreferences.PICTURE_KEY, R.drawable.ic_baseline_person_24.toString()).toString()
        val fullName =
            sharedPref?.getString(SharedPreferences.FULLNAME_KEY, "No FullName").toString()
        val email = sharedPref?.getString(SharedPreferences.EMAIL_KEY, "No Email").toString()
        val userName =
            sharedPref?.getString(SharedPreferences.USERNAME_KEY, "No UserName").toString()

        NetworkManager.service.downloadImage(picture).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.body()!=null){
                        val stream = response.body()!!.byteStream()
                        stream.let {
                            val bitmap = BitmapFactory.decodeStream(stream)
                            profileInfoBinding.imageviewUser.setImageBitmap(bitmap)
                        }
                    }else{
                        profileInfoBinding.imageviewUser.setImageResource(R.drawable.ic_baseline_person_24)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("Throwable",t.toString())
                }

            }
        )

        /*val url = "http://51.195.19.222/uploads/${args.imageFileName}"
        Glide.with(this)
            .load(url)
            .centerCrop()
            .into(profileInfoBinding.imageviewUser)*/

        profileInfoBinding.fullName = fullName
        profileInfoBinding.email = email
        profileInfoBinding.userName = userName

        profileInfoBinding.buttonLogout.setOnClickListener {
            val sharedPrefEditor = sharedPref?.edit()
            sharedPrefEditor?.clear()
            sharedPrefEditor?.apply()

            profileInfoNavController.navigate(R.id.profileLoginFragment)

        }

    }

}
