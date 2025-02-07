package com.umc.sweepic.presentation.record.memo

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.umc.sweepic.R
import com.umc.sweepic.databinding.DialogMemoFolderSelectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FolderSelectDialog(
    private val currentFolderId: Long, // 현재 폴더 ID 추가
    private val onMoveButtonClick: (MemoFolder) -> Unit,
    private val onDialogDismiss: () -> Unit
) : DialogFragment() {

    private var _binding: DialogMemoFolderSelectionBinding? = null
    private val binding get() = _binding!!
    private val memoFolderViewModel: MemoFolderViewModel by activityViewModels() // ✅ ViewModel 연결

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMemoFolderSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 폴더 데이터 API 호출
        memoFolderViewModel.fetchMemoFolders()

        // 폴더 데이터 변경 감지
        memoFolderViewModel.memoFolders.observe(viewLifecycleOwner) { folders ->
            updateFolderList(folders.filter { it.id.toLong() != currentFolderId }) // 현재 폴더 제외
        }

        //취소 버튼 클릭 시 다이얼로그 닫기
        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        // 이동 버튼 클릭 시 선택된 폴더 전달
        binding.moveButton.setOnClickListener {
            val selectedFolderId = binding.folderOptionsGroup.checkedRadioButtonId
            if (selectedFolderId != -1) {
                val selectedFolder = memoFolderViewModel.memoFolders.value?.find { it.id == selectedFolderId }
                selectedFolder?.let { onMoveButtonClick(it) }
                dismiss()
            }
        }
    }

    // 폴더 리스트 UI 업데이트 함수
    private fun updateFolderList(folders: List<MemoFolder>) {
        binding.folderOptionsGroup.removeAllViews() // 기존 목록 초기화

        for (folder in folders) {
            val radioButton = RadioButton(requireContext()).apply {
                id = folder.id
                text = folder.title // 폴더 제목 설정
                setButtonDrawable(R.drawable.sel_memo_radio)
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_memo, 0, 0, 0)
                compoundDrawablePadding = 8
                setPadding(13, 32, 13, 32)
                stateListAnimator = null
                background = null
            }
            binding.folderOptionsGroup.addView(radioButton)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogDismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
