package com.umc.sweepic.presentation.record.memo

import MemoDetailImageAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentMemoDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemoDetailFragment : Fragment() {
    private var _binding: FragmentMemoDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageAdapter: MemoDetailImageAdapter
    private var isSelectionMode = false //사진 선택 버튼 눌렀는지 안눌렀는지를 저장할 변수

    private val memoFolderViewModel: MemoFolderViewModel by activityViewModels() // 뷰모델 연결

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ 전달받은 folderId 가져오기
        val folderId = arguments?.getLong("folderId") ?: return

        // ✅ ViewModel에서 폴더 상세 데이터 가져오기
        memoFolderViewModel.fetchMemoFolderDetails(folderId)

        // ✅ RecyclerView 설정
        imageAdapter = MemoDetailImageAdapter(emptyList())
        binding.rvMemoDetailImages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvMemoDetailImages.adapter = imageAdapter

        // ✅ 데이터 변경 감지 및 UI 업데이트
        memoFolderViewModel.memoFolderDetail.observe(viewLifecycleOwner) { memoDetail ->
            memoDetail?.let {
                binding.tvMemoDetailFolderTitle.text = it.folderName
                binding.tvMemoDetailFolderContent.text = it.imageText ?: ""

//                // 이미지 리스트 업데이트
//                imageAdapter.updateData(it.images.map { image -> image.imageUrl })
            }
        }

        // ✅ 뒤로 가기 버튼 설정
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // ✅ 메뉴 버튼 클릭 시 팝업 메뉴 표시
        binding.btnMenu.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }


    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(requireContext(), view)

        if (isSelectionMode) {
            // 사진 선택 누르면 -> 사진 삭제, 사진 이동, 취소 메뉴가 뜸
            popup.inflate(R.menu.menu_selection_mode)
        } else {
            // 사진 선택 탭 안누른 경우 -> 폴더 삭제, 사진 선택 메뉴가 뜸
            popup.inflate(R.menu.menu_memo_popup)
        }

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.delete_folder -> {
                    // 폴더 삭제
                    handleFolderDelete()
                    true
                }
                R.id.select_photo -> {
                    // 사진 선택
                    handlePhotoSelect()
                    true
                }
                R.id.delete_photo -> {
                    // 선택된 사진 삭제
                    handlePhotoDelete()
                    true
                }
                R.id.move_photo -> {
                    // 선택된 사진 이동
                    handlePhotoMove()
                    true
                }
                R.id.cancel_selection -> {
                    // 다 취소하고 처음 메뉴로 돌아감
                    handleCancelSelection()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    //폴더 삭제 (미구현)
    private fun handleFolderDelete() {

    }


    //사진 선택 (누르면 다른 메뉴 뜨게)
    private fun handlePhotoSelect() {
        isSelectionMode = true
        imageAdapter.setSelectionMode(true)
    }

    //선택한 사진 삭제
    private fun handlePhotoDelete() {
        val selectedItems = imageAdapter.getSelectedItems()
    }

    //사진 이동(다이얼로그 창이 떠야함!!)
    private fun handlePhotoMove() {
        val selectedItems = imageAdapter.getSelectedItems() // 선택된 사진 가져오기

        if (selectedItems.isEmpty()) {
            Toast.makeText(requireContext(), "이동할 사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        memoFolderViewModel.memoFolders.value?.let { folders ->
            val currentFolder = arguments?.getParcelable<MemoFolder>("memoFolder")
            if (currentFolder != null) {
                // 현재 폴더 제외하고 다이얼로그에 뜨게끔하기
                val filteredFolders = folders.filter { it.id != currentFolder.id }
                val folderSelectDialog = FolderSelectDialog(
                    folders = filteredFolders,
                    onMoveButtonClick = { selectedFolder ->
                        movePhotosToFolder(selectedFolder)
                    },
                    onDialogDismiss = {
                        resetToDefaultState()
                    }
                )
                folderSelectDialog.show(parentFragmentManager, "FolderSelectDialog")
            }
        }
    }


    //선택된 폴더로 이동
    private fun movePhotosToFolder(selectedFolder: MemoFolder) {

    }

    //다이얼로그 창 닫을때 실행되는 메서드
    private fun resetToDefaultState() {
        isSelectionMode = false
        imageAdapter.setSelectionMode(false)
        binding.btnMenu.setImageResource(R.drawable.ic_menu)
    }

    //사진 선택 버튼 누르고 -> 취소버튼 누르면
    private fun handleCancelSelection() {
        resetToDefaultState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
