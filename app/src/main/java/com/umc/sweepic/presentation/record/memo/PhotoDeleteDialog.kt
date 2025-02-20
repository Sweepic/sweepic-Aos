package com.umc.sweepic.presentation.record.memo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.umc.sweepic.databinding.DialogPhotoDeleteBinding

class PhotoDeleteDialog(
    private val onDeleteConfirmed: () -> Unit
) : DialogFragment() {

    private var _binding: DialogPhotoDeleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 기본 다이얼로그 타이틀 제거
        _binding = DialogPhotoDeleteBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        binding.tvDialogTitle.text = "폴더 속 사진을\n정말 삭제하시겠어요?"
        binding.tvDialogMessage.text = "지금 사진을 삭제하면 다시 복구할 수 없어요."


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
