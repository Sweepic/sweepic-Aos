package com.umc.sweepic.presentation.sweep

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.sweepic.R
import com.umc.sweepic.databinding.ActivityMoveBinding
import com.umc.sweepic.domain.model.sweep.Gallery
import com.umc.sweepic.presentation.base.BaseActivity
import com.umc.sweepic.presentation.sweep.adapter.GalleryRVA
import com.umc.sweepic.util.extension.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoveActivity: BaseActivity<ActivityMoveBinding>(R.layout.activity_move) {
    private val viewModel: MoveViewModel by viewModels()
    private lateinit var adapter: GalleryRVA
    private val requiredPermissions: Array<String> by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun initObserver() {

    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MoveActivity::class.java)
        }
    }

    override fun initView() {
        setRecyclerView()
        fetchGalleryAdapter()
        setupFloatingButtons()
        binding.ivMoveBackBtn.setOnClickListener {
            finish()
        }
    }
    private fun fetchGalleryAdapter(){
        if (hasAllPermissions()) {
            loadGalleryImages() // 권한이 이미 있다면, 곧바로 갤러리 로딩
        } else {
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { isGranted ->
                if (isGranted.all { it.value }) {
                    loadGalleryImages()
                } else {
                    Toast.makeText(this, "permission error", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.launch(requiredPermissions)
        }
    }

    private fun loadGalleryImages() {
        lifecycleScope.launch {
            viewModel.galleryPager.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    private fun hasAllPermissions() = requiredPermissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setRecyclerView() {
        adapter = GalleryRVA { clickedGallery ->
            // GalleryRVA에서 Gallery 객체를 클릭한 경우
            // SweepActivity로 이동하며, clickedGallery.uri를 인텐트에 담아 전송
            val intent = SweepActivity.newIntent(this, clickedGallery.uri.toString())
            startActivity(intent)
            finish()
        }
        binding.rvMoveGallery.layoutManager = GridLayoutManager(this, 3)
        binding.rvMoveGallery.adapter = adapter
    }

    private fun setupFloatingButtons() {
        // 맨 위로 스크롤 버튼
        binding.fabMoveScrollUp.setOnClickListener {
            binding.rvMoveGallery.smoothScrollToPosition(0) // 첫 번째 아이템으로 스크롤
        }

        // 맨 아래로 스크롤 버튼
        binding.fabMoveScrollDown.setOnClickListener {
            // Adapter 데이터의 마지막 아이템으로 스크롤
            adapter.itemCount.let { itemCount ->
                if (itemCount > 0) {
                    binding.rvMoveGallery.smoothScrollToPosition(itemCount - 1)
                }
            }
        }
    }
}