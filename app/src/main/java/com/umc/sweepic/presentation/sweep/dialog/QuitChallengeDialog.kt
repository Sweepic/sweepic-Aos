package com.umc.sweepic.presentation.sweep.dialog

import android.os.Bundle
import com.umc.sweepic.R
import com.umc.sweepic.databinding.DialogTrashBinding
import com.umc.sweepic.presentation.base.BaseDialogFragment
import com.umc.sweepic.util.extension.setOnSingleClickListener

class QuitChallengeDialog(
    private val title: String,
    private val explanation: String,
    private val confirmText: String,
    private val cancelText: String,
    private val onConfirm: () -> Unit, // 확인 버튼 콜백
    private val onCancel: (() -> Unit)? = null // 취소 버튼 콜백 (선택)
) : BaseDialogFragment<DialogTrashBinding>(R.layout.dialog_trash) {

    override fun initView() {
        requireContext().dialogFragmentResize(this, 0.9f)

        // 동적 텍스트 설정
        binding.tvTrashTitle.text = title
        binding.tvTrashWarning.text = explanation
        binding.btnTrashDeleteConfirm.text = confirmText
        binding.btnTrashDeleteCancel.text = cancelText

        // 취소 버튼
        binding.btnTrashDeleteCancel.setOnSingleClickListener {
            dismiss()
            onCancel?.invoke()
        }

        // 추가 버튼
        binding.btnTrashDeleteConfirm.setOnSingleClickListener {
            dismiss()
            onConfirm.invoke()
        }
    }

    override fun initObserver() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyDialogStyle)
    }
}