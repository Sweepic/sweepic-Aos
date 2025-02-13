package com.umc.sweepic.presentation.record.memo

import MemoDetailImageAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.text.Selection.setSelection
import android.util.Log
import android.util.Log.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.FragmentMemoDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        binding.tvMemoDetailFolderTitle.setOnClickListener {
            binding.etMemoDetailFolderTitle.setText(binding.tvMemoDetailFolderTitle.text.toString())
            binding.tvMemoDetailFolderTitle.visibility = View.GONE
            binding.etMemoDetailFolderTitle.visibility = View.VISIBLE
            binding.etMemoDetailFolderTitle.requestFocus()
        }

        binding.etMemoDetailFolderTitle.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val newName = binding.etMemoDetailFolderTitle.text.toString().trim()
                val folderId = arguments?.getLong("folderId")?.toString() ?: return@setOnFocusChangeListener

                if (newName.isEmpty()) {
                    Toast.makeText(requireContext(), "폴더 제목을 입력해주세요.", Toast.LENGTH_SHORT).show()

                    binding.etMemoDetailFolderTitle.setText(binding.tvMemoDetailFolderTitle.text.toString())
                    binding.tvMemoDetailFolderTitle.visibility = View.VISIBLE
                    binding.etMemoDetailFolderTitle.visibility = View.GONE

                    return@setOnFocusChangeListener
                }

                memoFolderViewModel.updateFolderName(folderId, newName,
                    onSuccess = {
                        binding.tvMemoDetailFolderTitle.text = newName
                        binding.tvMemoDetailFolderTitle.visibility = View.VISIBLE
                        binding.etMemoDetailFolderTitle.visibility = View.GONE
                    },

                    onFailure = { errorMessage ->
                        if (errorMessage.contains("409")) {
                            Toast.makeText(requireContext(), "이미 존재하는 폴더명입니다.", Toast.LENGTH_SHORT).show()
                        }

                        binding.etMemoDetailFolderTitle.setText(binding.tvMemoDetailFolderTitle.text.toString())
                        binding.tvMemoDetailFolderTitle.visibility = View.VISIBLE
                        binding.etMemoDetailFolderTitle.visibility = View.GONE
                    }
                )
            }
        }

        binding.tvMemoDetailFolderContent.setOnClickListener {
            binding.etMemoDetailFolderContent.setText(binding.tvMemoDetailFolderContent.text.toString())
            binding.tvMemoDetailFolderContent.visibility = View.GONE
            binding.etMemoDetailFolderContent.visibility = View.VISIBLE
            binding.etMemoDetailFolderContent.requestFocus()
        }

        binding.etMemoDetailFolderContent.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val newText = binding.etMemoDetailFolderContent.text.toString()
                val folderId = arguments?.getLong("folderId")?.toString() ?: return@setOnFocusChangeListener

                memoFolderViewModel.updateMemoText(folderId, newText)

                binding.tvMemoDetailFolderContent.text = newText
                binding.tvMemoDetailFolderContent.visibility = View.VISIBLE
                binding.etMemoDetailFolderContent.visibility = View.GONE
            }
        }
        

        // RecyclerView 설정
        imageAdapter = MemoDetailImageAdapter(emptyList())
        binding.rvMemoDetailImages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvMemoDetailImages.adapter = imageAdapter

        memoFolderViewModel.memoFolderDetail.observe(viewLifecycleOwner) { memoDetail ->
            if (memoDetail != null) {
                binding.tvMemoDetailFolderTitle.text = memoDetail.folderName
                binding.tvMemoDetailFolderContent.text = memoDetail.imageText?.takeIf { it.isNotEmpty() } ?: ""

                // 폴더 내에 이미지 없을 경우 리사이클러뷰 숨기기
                val imageData = memoDetail.images?.map { it.imageId.toString() to it.imageUrl } ?: emptyList()

                if (imageData.isNotEmpty()) {
                    imageAdapter.updateData(imageData)
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
                    handleFolderDelete()
                    true
                }
                R.id.select_photo -> {
                    handlePhotoSelect()
                    true
                }
                R.id.delete_photo -> {
                    handlePhotoDelete()
                    true
                }
                R.id.move_photo -> {
                    handlePhotoMove()
                    true
                }
                R.id.cancel_selection -> {
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
        Log.d("MemoDetailFragment", "삭제 요청 전, 선택된 이미지 리스트: $selectedItems")

        if (selectedItems.isEmpty()) {
            Toast.makeText(requireContext(), "삭제할 사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val folderId = arguments?.getLong("folderId")?.toString() ?: return
        val selectedImageIds = selectedItems.map { it.toString() }

        Log.d("MemoDetailFragment", "삭제 요청: folderId=$folderId, imageIds=$selectedImageIds")

        AlertDialog.Builder(requireContext())
            .setTitle("사진 삭제")
            .setMessage("선택한 사진을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                memoFolderViewModel.deleteImages(folderId, selectedImageIds)

                isSelectionMode = false
                imageAdapter.setSelectionMode(false) // 선택 모드 비활성화
                binding.btnMenu.setImageResource(R.drawable.ic_menu) // 기존 메뉴 버튼으로 변경
                memoFolderViewModel.fetchMemoFolderDetails(folderId.toLong())
                Toast.makeText(requireContext(), "사진이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun handlePhotoMove() {
        val selectedItems = imageAdapter.getSelectedItems() // 선택된 사진 가져오기

        if (selectedItems.isEmpty()) {
            Toast.makeText(requireContext(), "이동할 사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val currentFolderId = arguments?.getLong("folderId") ?: return

        val folderSelectDialog = FolderSelectDialog(
            currentFolderId = currentFolderId, // ✅ 현재 폴더 ID 전달
            onMoveButtonClick = { selectedFolder ->
                movePhotosToFolder(selectedFolder)
            },
            onDialogDismiss = {
                resetToDefaultState()
            }
        )
        folderSelectDialog.show(parentFragmentManager, "FolderSelectDialog")
    }

    private fun movePhotosToFolder(selectedFolder: MemoFolder) {
        val selectedItems = imageAdapter.getSelectedItems()

        if (selectedItems.isEmpty()) {
            Toast.makeText(requireContext(), "이동할 사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val folderId = arguments?.getLong("folderId") ?: return
        val targetFolderId = selectedFolder.id.toString()
        val selectedImageIds = selectedItems.map { it.toString() }

        Log.d("MemoDetailFragment", "사진 이동 요청: folderId=$folderId, targetFolderId=$targetFolderId, imageIds=$selectedImageIds") // ✅ 로그 추가

        AlertDialog.Builder(requireContext())
            .setTitle("사진 이동")
            .setMessage("선택한 사진을 '${selectedFolder.title}' 폴더로 이동하시겠습니까?")
            .setPositiveButton("이동") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        memoFolderViewModel.moveImages(folderId, targetFolderId, selectedImageIds)

                        memoFolderViewModel.fetchMemoFolderDetails(folderId)

                        requireActivity().runOnUiThread {
                            imageAdapter.setSelectionMode(false) // 선택 모드 종료
                            imageAdapter.notifyDataSetChanged() // RecyclerView 새로고침
                        }

                        Toast.makeText(requireContext(), "사진이 이동되었습니다.", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Log.e("MemoDetailFragment", "사진 이동 실패: ${e.message}")
                        Toast.makeText(requireContext(), "사진 이동 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    //다이얼로그 창 닫을때 실행되는 메서드
    private fun resetToDefaultState() {
        isSelectionMode = false
        imageAdapter.setSelectionMode(false)
        binding.btnMenu.setImageResource(R.drawable.ic_menu)
    }

    private fun handleCancelSelection() {
        resetToDefaultState()
    }

    private fun showFolderRenameDialog(folderId: String, currentName: String) {
        val editText = EditText(requireContext()).apply {
            setText(currentName)
            setSelection(currentName.length)
        }
    }

    private fun showMemoTextEditDialog(folderId: Long, currentText: String) {
        val editText = EditText(requireContext()).apply {
            setText(currentText)
            setPadding(32, 32, 32, 32)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
