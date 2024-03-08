package com.danamon.robby.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bekawestberg.loopinglayout.library.LoopingLayoutManager
import com.danamon.robby.R
import com.danamon.robby.databinding.ActivityMainBinding
import com.danamon.robby.model.album.AlbumItem
import com.danamon.robby.model.photos.PhotosItem
import com.danamon.robby.ui.adapter.AlbumAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity(), AlbumAdapter.OnItemClickListener {

    private lateinit var albumAdapter: AlbumAdapter
    private var albumList: ArrayList<AlbumItem?>? = null
    private var photosList: ArrayList<PhotosItem?>? = null

    lateinit var binding: ActivityMainBinding
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.dashboard_activity)
        init()
    }

    /**
     * UI initialization
     */
    private fun init() {
        albumList = ArrayList()
        photosList = ArrayList()
        viewModel.init()
        observeData()
        getAlbumListFromServer()
    }

    /**
     * Set data to recyclerview
     */
    private fun setAlbumRecyclerView() {
        albumAdapter = AlbumAdapter(this@DashboardActivity, albumList)
        binding.apply {
            allAlbumsRecyclerView.setHasFixedSize(true)
            allAlbumsRecyclerView.layoutManager =
                LoopingLayoutManager(this@DashboardActivity, LoopingLayoutManager.VERTICAL, false)
            allAlbumsRecyclerView.adapter = albumAdapter
        }
        binding.progress.visibility = View.GONE
    }

    /**
     * Observe data to update in UI
     */
    private fun observeData() {
        viewModel.photosLiveList!!.observe(this) { updatedPhotoList: List<PhotosItem?>? ->
            if (!updatedPhotoList.isNullOrEmpty()) {
                photosList!!.clear()
                photosList!!.addAll(updatedPhotoList)
                binding.allAlbumsRecyclerView.apply {
                    (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                }
                albumAdapter.notifyDataSetChanged()
            }
        }

        viewModel.albumLiveList!!.observe(this) { updatedAlbumList: List<AlbumItem?>? ->
            if (!updatedAlbumList.isNullOrEmpty()) {
                albumList!!.clear()
                albumList!!.addAll(updatedAlbumList)
                setAlbumRecyclerView()
                albumAdapter.updateList(albumList!!)
            }
        }
    }

    /**
     * Get data from server
     */
    private fun getAlbumListFromServer() {
        binding.progress.visibility = View.VISIBLE
        if (!viewModel.photosList.hasObservers()) {
            viewModel.photosList.observe(this) {
//
                if (it.isNotEmpty()) {
                    viewModel.getPhotosListByIDAPI(it[0].albumId + 1)
                }
            }
        }
        if (!viewModel.albumList.hasObservers()) {
            viewModel.albumList.observe(this) {
                binding.progress.visibility = View.GONE
                if (it.isNotEmpty()) {
                    viewModel.getPhotosListByIDAPI(it[0].id)  // Get all photos
                }
            }
        }
        viewModel.getAlbumListAPI()  // Get all albums
    }

    override fun onItemClick(view: View?, position: Int) {

    }

    /**
     * Get photos of album by album id
     */
    fun getPhotosByID(id: Int): List<PhotosItem?>? {
        return viewModel.repository.getPhotosListByID(id)
    }
}