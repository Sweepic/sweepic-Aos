package com.umc.sweepic.presentation.sweep.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.DialogInputTagBinding
import com.umc.sweepic.databinding.DialogTrashBinding
import com.umc.sweepic.presentation.base.BaseDialogFragment
import com.umc.sweepic.util.extension.setOnSingleClickListener

class TrashDeleteDialog (
    private val title: String, // 제목
    private val warning: String,  // 경고
    private val onConfirm: () -> Unit, // 확인
    private val onCancel: () -> Unit, // 취소
    private val confirmButtonText: String = "삭제" // 확인 버튼 텍스트 (기본값: "삭제")
): BaseDialogFragment<DialogTrashBinding>(R.layout.dialog_trash) {
    override fun initView() {
        binding.tvTrashTitle.text = title
        binding.tvTrashWarning.text = warning
        binding.btnTrashDeleteConfirm.text = confirmButtonText // 확인 버튼 텍스트 설정
        requireContext().dialogFragmentResize(this, 0.9f)

        with(binding){
            btnTrashDeleteCancel.setOnSingleClickListener {
                onCancel()
                dismiss()
            }
            btnTrashDeleteConfirm.setOnSingleClickListener {
                onConfirm()
                dismiss()
            }
        }
    }


    override fun initObserver() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 커스텀 다이얼로그 스타일 적용
        setStyle(STYLE_NORMAL, R.style.MyDialogStyle)
    }


}