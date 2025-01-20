package com.umc.sweepic.presentation.record.memo

import android.app.Dialog
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
    private val folders: List<MemoFolder>, // 다이얼로그 창 목록에 폴더 데이터를 전달받기
    private val onMoveButtonClick: (MemoFolder) -> Unit // 다이얼로그 창에서 선택한 폴더를 전달
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
                text = folder.title // 폴더 제목 설정하기
                setButtonDrawable(R.drawable.sel_memo_radio)
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_memo, 0, 0, 0) // 아이콘 설정
                compoundDrawablePadding = 8 // 아이콘이랑 텍스트 간격
                setPadding(13, 16, 13, 16)
                stateListAnimator = null //애니메이션 효과제거
                background = null //애니메이션 효과제거.. 위에거랑 뭔 차인지 모름

            }
            binding.folderOptionsGroup.addView(radioButton)
        }

        //취소 버튼 누르면 다이얼로그 창 닫힘
        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        // 이동 버튼 누르면 선택한 폴더 전달함
        binding.moveButton.setOnClickListener {
            val selectedFolderId = binding.folderOptionsGroup.checkedRadioButtonId
            if (selectedFolderId != -1) {
                val selectedFolder = folders.find { it.id == selectedFolderId }
                selectedFolder?.let { onMoveButtonClick(it) } // 선택된 폴더 전달
                dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.white) // 흰 배경 설정
        return Dialog(requireContext(), R.style.CustomDialogTheme).apply {
        }
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
