package ir.omidrezabagherian.netflix.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ir.omidrezabagherian.netflix.R
import ir.omidrezabagherian.netflix.databinding.FragmentProfileLoginBinding
import ir.omidrezabagherian.netflix.ui.SharedPreferences
import java.io.ByteArrayOutputStream
import kotlin.random.Random

class ProfileLoginFragment : Fragment(R.layout.fragment_profile_login) {

    private lateinit var profileLoginBinding: FragmentProfileLoginBinding
    private lateinit var profileLoginNavController: NavController
    private val loginViewModel: LoginViewModel by viewModels()

    lateinit var image: Bitmap
    private var nameFile = "test"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        checkLogin()

        profileLoginBinding = FragmentProfileLoginBinding.bind(view)

        val profileLoginNavHostFragment =
            activity!!.supportFragmentManager.findFragmentById(R.id.fragment_container_main) as NavHostFragment

        profileLoginNavController = profileLoginNavHostFragment.navController

        val takePicture =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { result ->
                profileLoginBinding.imageviewUser.setImageBitmap(result)

                val stream = ByteArrayOutputStream()
                result!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val imageByteArray = stream.toByteArray()
                val picture = Base64.encodeToString(imageByteArray, Base64.DEFAULT)

                val decodedByte = Base64.decode(picture, 0)
                image = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
            }

        val findPicture =
            registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
                profileLoginBinding.imageviewUser.setImageURI(result)

                val path = profileLoginBinding.imageviewUser.drawable.toBitmap()

                val stream = ByteArrayOutputStream()
                path.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val imageByteArray = stream.toByteArray()
                val picture = Base64.encodeToString(imageByteArray, Base64.DEFAULT)

                val decodedByte = Base64.decode(picture, 0)
                image = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
            }

        profileLoginBinding.buttonOpenCamera.setOnClickListener {
            takePicture.launch()
        }

        profileLoginBinding.buttonOpenFile.setOnClickListener {
            findPicture.launch("image/*")
        }

        profileLoginBinding.buttonRegister.setOnClickListener {
            var fullname = ""
            var email = ""
            var username = ""

            if (profileLoginBinding.edittextFullname.text!!.isEmpty())
                profileLoginBinding.edittextFullname.error =
                    "لطفا نام و نام خانوادگی خود را وارد کنید"
            else
                fullname = profileLoginBinding.edittextFullname.text.toString()

            if (profileLoginBinding.edittextEmail.text!!.isEmpty())
                profileLoginBinding.edittextEmail.error =
                    "لطفا ایمیل خود را وارد کنید"
            else
                email = profileLoginBinding.edittextEmail.text.toString()

            if (profileLoginBinding.edittextUsername.text!!.isEmpty())
                profileLoginBinding.edittextUsername.error =
                    "لطفا نام کاربری خود را وارد کنید"
            else
                username = profileLoginBinding.edittextUsername.text.toString()

            if (
                fullname.isNotEmpty() &&
                email.isNotEmpty() &&
                username.isNotEmpty()
            ) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val imageInBytes = byteArrayOutputStream.toByteArray()

                val rndNumber = Random.nextInt()
                nameFile = "Profile_OmidrezaBagherian_$rndNumber.JPEG"
                loginViewModel.uploadImage(imageInBytes, nameFile)

                loginViewModel.profileResponse.observe(viewLifecycleOwner, Observer {
                    Log.i("Response", it)
                })

                loginViewModel.profileError.observe(viewLifecycleOwner, Observer {
                    Log.i("Failure", it)
                })

                val sharedPref =
                    activity!!.getSharedPreferences(
                        SharedPreferences.FILE_NAME,
                        Context.MODE_PRIVATE
                    )

                val sharedPrefEditor = sharedPref.edit()

                sharedPrefEditor.putString(SharedPreferences.PICTURE_KEY, nameFile)
                sharedPrefEditor.putString(SharedPreferences.FULLNAME_KEY, fullname)
                sharedPrefEditor.putString(SharedPreferences.EMAIL_KEY, email)
                sharedPrefEditor.putString(SharedPreferences.USERNAME_KEY, username)

                sharedPrefEditor.commit()

                profileLoginNavController.navigate(R.id.profileInfoFragment)

            }

        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun checkLogin() {
        val sharedPref =
            activity?.getSharedPreferences(SharedPreferences.FILE_NAME, Context.MODE_PRIVATE)

        val profileLoginNavHostFragment =
            activity!!.supportFragmentManager.findFragmentById(R.id.fragment_container_main) as NavHostFragment

        profileLoginNavController = profileLoginNavHostFragment.navController

        val picture = sharedPref?.getString(SharedPreferences.PICTURE_KEY, "")
        val fullname = sharedPref?.getString(SharedPreferences.FULLNAME_KEY, "")
        val email = sharedPref?.getString(SharedPreferences.EMAIL_KEY, "")
        val username = sharedPref?.getString(SharedPreferences.USERNAME_KEY, "")

        try {
            if (
                fullname != "" ||
                email != "" ||
                username != ""
            ) {
                profileLoginNavController.navigate(R.id.profileInfoFragment)
            }

        } catch (e: Exception) {
            Log.i("AppException", e.toString())
        }

    }

}