package com.umc.sweepic.presentation.sweep.dialog

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.DialogAddExistingFolderBinding
import com.umc.sweepic.domain.model.sweep.AlbumList
import com.umc.sweepic.presentation.base.BaseDialogFragment
import com.umc.sweepic.presentation.sweep.AlbumViewModel
import com.umc.sweepic.presentation.sweep.adapter.AlbumDialogRVA
import com.umc.sweepic.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumSelectDialog(
    private val addedAlbums: List<AlbumList>, // 이미 추가된 앨범 목록
    private val onAlbumsSelected: (List<AlbumList>, List<AlbumList>) -> Unit // 선택된/해제된 앨범 목록 콜백
) : BaseDialogFragment<DialogAddExistingFolderBinding>(R.layout.dialog_add_existing_folder) {

    private val albumViewModel: AlbumViewModel by viewModels()
    private val selectedAlbums = mutableSetOf<AlbumList>() // 선택된 앨범 저장
    private val deselectedAlbums = mutableSetOf<AlbumList>() // 선택 해제된 앨범 저장
    private val adapter: AlbumDialogRVA by lazy {
        AlbumDialogRVA { album, isSelected ->
            if (isSelected) {
                deselectedAlbums.remove(album)
                selectedAlbums.add(album)
            } else {
                selectedAlbums.remove(album)
                deselectedAlbums.add(album)
            }
        }
    }

    override fun initView() {
        requireContext().dialogFragmentResize(this, 0.9f)

        // RecyclerView 설정
        binding.rvAlbumList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAlbumList.adapter = adapter

        // 취소 버튼
        binding.btnAddFolderCancel.setOnSingleClickListener {
            dismiss()
        }

        // 확인 버튼
        binding.btnAddFolderConfirm.setOnSingleClickListener {
            if (selectedAlbums.isNotEmpty() || deselectedAlbums.isNotEmpty()) {
                onAlbumsSelected(selectedAlbums.toList(), deselectedAlbums.toList())
                dismiss()
            } else {
                Toast.makeText(requireContext(), "앨범을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // ViewModel을 통해 앨범 목록 로드
        albumViewModel.albums.observe(viewLifecycleOwner) { albumList ->
            // 초기 선택 상태 설정
            selectedAlbums.addAll(albumList.filter { addedAlbums.contains(it) })
            adapter.setSelectedAlbums(selectedAlbums)
            adapter.submitList(albumList)
        }
        albumViewModel.loadAlbums() // 앨범 목록 불러오기
    }

    override fun initObserver() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyDialogStyle)
    }
}