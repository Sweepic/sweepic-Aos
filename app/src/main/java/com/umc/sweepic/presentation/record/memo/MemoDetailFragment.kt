package com.umc.sweepic.presentation.record.memo

import MemoDetailImageAdapter
import android.app.AlertDialog
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.Log.*
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.util.TypedValueCompat.dpToPx
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
            showCustomPopupMenu(view)
        }
    }

    private fun showCustomPopupMenu(view: View) {
        val inflater = LayoutInflater.from(requireContext())

        val popupView = if (isSelectionMode) {
            inflater.inflate(R.layout.popup_selection_memo, null)
        } else {
            inflater.inflate(R.layout.popup_memo_menu, null)
        }

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.elevation = 10f


        //폴더 삭제
        popupView.findViewById<TextView>(R.id.delete_folder)?.setOnClickListener {
            handleFolderDelete()
            popupWindow.dismiss()
        }

        //사진 선택
        popupView.findViewById<TextView>(R.id.select_photo)?.setOnClickListener {
            handlePhotoSelect()
            popupWindow.dismiss()
        }

        //사진 삭제
        popupView.findViewById<TextView>(R.id.delete_photo)?.setOnClickListener {
            handlePhotoDelete()
            popupWindow.dismiss()
        }

        //사진 이동
        popupView.findViewById<TextView>(R.id.move_photo)?.setOnClickListener {
            handlePhotoMove()
            popupWindow.dismiss()
        }

        //취소
        popupView.findViewById<TextView>(R.id.cancel_selection)?.setOnClickListener {
            handleCancelSelection()
            popupWindow.dismiss()
        }

        //팝업 창 위치 조정...
        val location = IntArray(2)
        view.getLocationOnScreen(location)

        val xOffset = location[0] - dpToPx(50)
        val yOffset = location[1] + view.height + dpToPx(8)

        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, xOffset, yOffset)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }



    private fun handleFolderDelete() {
        val folderId = arguments?.getLong("folderId") ?: return
        val folderName = binding.tvMemoDetailFolderTitle.text.toString()
        showDeleteConfirmationDialog(folderId, folderName)
    }

    // 폴더 삭제 다이얼로그 창
    private fun showDeleteConfirmationDialog(folderId: Long, folderName: String) {
        val dialog = FolderDeleteDialog(folderName) { // ✅ 폴더 이름을 전달
            memoFolderViewModel.deleteMemoFolder(folderId)
            parentFragmentManager.popBackStack()
        }
        dialog.show(parentFragmentManager, "FolderDeleteDialog")
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

        val dialog = PhotoDeleteDialog {
            memoFolderViewModel.deleteImages(folderId, selectedImageIds)

            isSelectionMode = false
            imageAdapter.setSelectionMode(false) // 선택 모드 비활성화
            binding.btnMenu.setImageResource(R.drawable.ic_menu) // 기존 메뉴 버튼으로 변경
            memoFolderViewModel.fetchMemoFolderDetails(folderId.toLong())
        }
        dialog.show(parentFragmentManager, "PhotoDeleteDialog")
    }

    private fun handlePhotoMove() {
        val selectedItems = imageAdapter.getSelectedItems()

        if (selectedItems.isEmpty()) {
            Toast.makeText(requireContext(), "이동할 사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val currentFolderId = arguments?.getLong("folderId") ?: return

        val folderSelectDialog = FolderSelectDialog(
            currentFolderId = currentFolderId, //현재 폴더 ID 전달
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

    //사진 선택 버튼 누르고 -> 취소버튼 누르면
    private fun handleCancelSelection() {
        resetToDefaultState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
