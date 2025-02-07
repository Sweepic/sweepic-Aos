package com.umc.sweepic.presentation.record.memo

import MemoDetailImageAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.Log.*
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

        // 전달받은 폴더아이디 가져오기
        val folderId = arguments?.getLong("folderId") ?: return

        // 뷰모델에서 폴더 상세 데이터 가져오기
        memoFolderViewModel.fetchMemoFolderDetails(folderId)

        // RecyclerView 설정
        imageAdapter = MemoDetailImageAdapter(emptyList())
        binding.rvMemoDetailImages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvMemoDetailImages.adapter = imageAdapter

        // UI 변환
        memoFolderViewModel.memoFolderDetail.observe(viewLifecycleOwner) { memoDetail ->
            if (memoDetail != null) {
                binding.tvMemoDetailFolderTitle.text = memoDetail.folderName
                binding.tvMemoDetailFolderContent.text = memoDetail.imageText?.takeIf { it.isNotEmpty() } ?: ""

                // 이미지 리스트가 있으면 리사이클러뷰 업데이트
                val imageUrls = memoDetail.images.mapNotNull { it.imageUrl }
                if (imageUrls.isNotEmpty()) {
                    imageAdapter.updateData(imageUrls)
                    binding.rvMemoDetailImages.visibility = View.VISIBLE
                } else {
                    binding.rvMemoDetailImages.visibility = View.GONE
                }
            }
        }

        // 뒤로 가기 버튼 설정
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 메뉴 버튼 클릭 시 팝업 메뉴 표시
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

    private fun handleFolderDelete() {
        val folderId = arguments?.getLong("folderId") ?: return
        val folderName = binding.tvMemoDetailFolderTitle.text.toString() // ✅ 현재 폴더 이름 가져오기
        showDeleteConfirmationDialog(folderId, folderName) // ✅ 폴더 이름 전달
    }

    // 폴더 삭제 다이얼로그 창
    private fun showDeleteConfirmationDialog(folderId: Long, folderName: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("폴더 삭제")
            .setMessage("[$folderName] 폴더를 정말 삭제하시겠어요?\n지금 폴더를 삭제하면 다시 복구할 수 없어요.")
            .setPositiveButton("삭제") { _, _ ->
                // ✅ 사용자가 '삭제' 버튼을 눌렀을 때 폴더 삭제 실행
                memoFolderViewModel.deleteMemoFolder(folderId)
                Toast.makeText(requireContext(), "폴더가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack() // 이전 화면으로 이동
            }
            .setNegativeButton("취소", null) // 취소 버튼 누르면 아무 동작 없음
            .show()
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

        val currentFolderId = arguments?.getLong("folderId") ?: return // ✅ 현재 폴더 ID 가져오기

        val folderSelectDialog = FolderSelectDialog(
            currentFolderId = currentFolderId, // ✅ 현재 폴더 ID 전달
            onMoveButtonClick = { selectedFolder ->
                movePhotosToFolder(selectedFolder)
            },
            onDialogDismiss = {
                resetToDefaultState()
            }
        )
        folderSelectDialog.show(parentFragmentManager, "FolderSelectDialog") // ✅ 실행
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
