package ir.omidrezabagherian.netflix.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import ir.omidrezabagherian.netflix.R
import ir.omidrezabagherian.netflix.databinding.FragmentFavoriteBinding
import ir.omidrezabagherian.netflix.ui.adapters.FavoriteAdapter
import ir.omidrezabagherian.netflix.model.VideoHandler

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    lateinit var favoriteBinding: FragmentFavoriteBinding
    lateinit var videoHandler: VideoHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteBinding = FragmentFavoriteBinding.bind(requireView())
        videoHandler = VideoHandler(this)

    }

    private fun setFavoriteItems() {
        val listItem = videoHandler.getFavorites()
        val adapter = FavoriteAdapter(listItem)
        favoriteBinding.recyclerviewFavorite.adapter = adapter
        favoriteBinding.recyclerviewFavorite.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    override fun onResume() {
        super.onResume()
        setFavoriteItems()
    }

}