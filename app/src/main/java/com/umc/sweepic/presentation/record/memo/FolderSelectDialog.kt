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
import com.umc.sweepic.R
import com.umc.sweepic.databinding.DialogMemoFolderSelectionBinding


class FolderSelectDialog(
    private val folders: List<MemoFolder>, // 폴더 목록
    private val onMoveButtonClick: (MemoFolder) -> Unit, // 이동 버튼 클릭 시 선택된 폴더 전달
    private val onDialogDismiss: () -> Unit // 다이얼로그 종료 콜백
) : DialogFragment() {

    private var _binding: DialogMemoFolderSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogMemoFolderSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 폴더 데이터를 기반으로 RadioButton 생성
        for (folder in folders) {
            val radioButton = RadioButton(requireContext()).apply {
                id = folder.id
                text = folder.title // 폴더 제목 설정
                setButtonDrawable(R.drawable.sel_memo_radio)
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_memo, 0, 0, 0) // 아이콘 설정
                compoundDrawablePadding = 8 // 아이콘과 텍스트 간격
                setPadding(13, 16, 13, 16)
                stateListAnimator = null // 애니메이션 효과 제거
                background = null // 배경 제거
            }
            binding.folderOptionsGroup.addView(radioButton)
        }

        // 취소 버튼 클릭 시 다이얼로그 닫기
        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        // 이동 버튼 클릭 시 선택된 폴더 전달
        binding.moveButton.setOnClickListener {
            val selectedFolderId = binding.folderOptionsGroup.checkedRadioButtonId
            if (selectedFolderId != -1) {
                val selectedFolder = folders.find { it.id == selectedFolderId }
                selectedFolder?.let { onMoveButtonClick(it) }
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogDismiss() // 다이얼로그 종료 시 콜백 호출
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                (resources.displayMetrics.widthPixels * 0.9).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(R.drawable.bg_dialog)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
