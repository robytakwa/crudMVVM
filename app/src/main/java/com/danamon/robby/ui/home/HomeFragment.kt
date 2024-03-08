package com.danamon.robby.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.danamon.robby.R
import com.danamon.robby.databinding.HomeFragmentBinding
import com.danamon.robby.model.album.AlbumItem
import com.danamon.robby.model.photos.PhotosItem
import com.danamon.robby.ui.adapter.AlbumAdapter
import com.danamon.robby.ui.dashboard.DashboardViewModel


class HomeFragment : Fragment() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var albumAdapter: AlbumAdapter
    private var albumList: ArrayList<AlbumItem?>? = null
    private var photosList: ArrayList<PhotosItem?>? = null
    lateinit var binding: HomeFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
        init()
    }


    private fun init() {
        albumList = ArrayList()
        photosList = ArrayList()
        viewModel.init()
        observeData()
        getAlbumListFromServer()
    }

    private fun setAlbumRecyclerView() {
        albumAdapter = AlbumAdapter(requireContext(), albumList)
//        binding.apply {
//            allAlbumsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//            allAlbumsRecyclerView.setHasFixedSize(true)
//            allAlbumsRecyclerView.setItemViewCacheSize(20)
//            allAlbumsRecyclerView.adapter = albumAdapter
//        }
        binding.allAlbumsRecyclerView.setHasFixedSize(true)
        binding.allAlbumsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.allAlbumsRecyclerView.adapter = albumAdapter
//        binding.progress.visibility = View.GONE
    }

    private fun observeData() {
        viewModel.photosLiveList!!.observe(viewLifecycleOwner) { updatedPhotoList: List<PhotosItem?>? ->
            if (!updatedPhotoList.isNullOrEmpty()) {
                photosList!!.clear()
                photosList!!.addAll(updatedPhotoList)
                binding.allAlbumsRecyclerView.apply {
                    (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                }
                albumAdapter.notifyDataSetChanged()
            }
        }

        viewModel.albumLiveList!!.observe(viewLifecycleOwner) { updatedAlbumList: List<AlbumItem?>? ->
            if (!updatedAlbumList.isNullOrEmpty()) {
                albumList!!.clear()
                albumList!!.addAll(updatedAlbumList)
                setAlbumRecyclerView()
                albumAdapter.updateList(albumList!!)
            }
        }
    }


    private fun getAlbumListFromServer() {
        binding.progress.visibility = View.VISIBLE
        if (!viewModel.photosList.hasObservers()) {
            viewModel.photosList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    viewModel.getPhotosListByIDAPI(it[0].albumId + 1)
                }
            }
        }
        if (!viewModel.albumList.hasObservers()) {
            viewModel.albumList.observe(viewLifecycleOwner) {
                binding.progress.visibility = View.GONE
                if (it.isNotEmpty()) {
                    viewModel.getPhotosListByIDAPI(it[0].id)  // Get all photos
                }
            }
        }
        viewModel.getAlbumListAPI()  // Get all albums
    }

    fun getPhotosByID(id: Int): List<PhotosItem?>? {
        return viewModel.repository.getPhotosListByID(id)
    }

}
