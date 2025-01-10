package com.umc.sweepic.presentation.sweep.dialog

import com.umc.sweepic.R
import com.umc.sweepic.databinding.DialogInputTagBinding
import com.umc.sweepic.presentation.base.BaseDialogFragment
import com.umc.sweepic.util.extension.setOnSingleClickListener

class SweepTagDialog(
    private val title: String, // 제목
    private val hint: String,  // 힌트
    private val onTagEntered: (String) -> Unit, // 콜백 함수
    private val onCancel: () -> Unit // 취소
): BaseDialogFragment<DialogInputTagBinding>(R.layout.dialog_input_tag) {
    override fun initView() {
        binding.tvSweepInputLocationTitle.text = title
        binding.etSweepInputLocation.hint = hint
        requireContext().dialogFragmentResize(this, 0.9f)

        with(binding){
            btnInputLocationCancel.setOnSingleClickListener {
                onCancel()
                dismiss()
            }
            btnInputLocationConfirm.setOnSingleClickListener {
                val tagText = binding.etSweepInputLocation.text.toString()
                if (tagText.isNotEmpty()) {
                    onTagEntered(tagText) // 입력값 전달
                    dismiss() // 다이얼로그 닫기
                } else {
                    binding.etSweepInputLocation.error = "값을 입력해주세요." // 에러 메시지 표시
                }
            }
        }
    }


    override fun initObserver() {

    }

}