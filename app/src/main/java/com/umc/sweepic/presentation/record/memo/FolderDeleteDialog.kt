package com.umc.sweepic.presentation.record.memo

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.umc.sweepic.databinding.DialogFolderDeleteBinding

class FolderDeleteDialog(
    private val folderName: String,
    private val onDeleteConfirmed: () -> Unit
) : DialogFragment() {

    private var _binding: DialogFolderDeleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 기본 다이얼로그 타이틀 제거
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogFolderDeleteBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        binding.tvDialogTitle.text = "$folderName"+"폴더를\n정말 삭제하시겠어요?"
        binding.tvDialogMessage.text = "지금 폴더를 삭제하면 다시 복구할 수 없어요."

        // 버튼 클릭 리스너
        binding.cancelButton.setOnClickListener { dismiss() }
        binding.deleteButton.setOnClickListener {
            onDeleteConfirmed()
            dismiss()
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
