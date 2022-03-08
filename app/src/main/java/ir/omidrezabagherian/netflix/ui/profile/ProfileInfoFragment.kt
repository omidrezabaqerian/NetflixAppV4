package ir.omidrezabagherian.netflix.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ir.omidrezabagherian.netflix.R
import ir.omidrezabagherian.netflix.databinding.FragmentProfileInfoBinding
import ir.omidrezabagherian.netflix.ui.SharedPreferences


class ProfileInfoFragment : Fragment(R.layout.fragment_profile_info) {

    private lateinit var profileInfoBinding: FragmentProfileInfoBinding
    private lateinit var profileInfoNavController: NavController
    private val infoViewModel: InfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref =
            activity?.getSharedPreferences(SharedPreferences.FILE_NAME, Context.MODE_PRIVATE)

        profileInfoBinding = FragmentProfileInfoBinding.bind(view)

        val profileInfoNavHostFragment =
            activity!!.supportFragmentManager.findFragmentById(R.id.fragment_container_main) as NavHostFragment

        profileInfoNavController = profileInfoNavHostFragment.navController

        val picture =
            sharedPref?.getString(
                SharedPreferences.PICTURE_KEY,
                R.drawable.ic_baseline_person_24.toString()
            ).toString()
        val fullName =
            sharedPref?.getString(SharedPreferences.FULLNAME_KEY, "No FullName").toString()
        val email = sharedPref?.getString(SharedPreferences.EMAIL_KEY, "No Email").toString()
        val userName =
            sharedPref?.getString(SharedPreferences.USERNAME_KEY, "No UserName").toString()

        infoViewModel.showInfo(requireContext(), picture)

        infoViewModel.infoResponse.observe(viewLifecycleOwner, Observer { image ->
            profileInfoBinding.imageviewUser.setImageBitmap(image)
        })

        infoViewModel.infoError.observe(viewLifecycleOwner, Observer {
            Log.i("Error", it)
        })

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
