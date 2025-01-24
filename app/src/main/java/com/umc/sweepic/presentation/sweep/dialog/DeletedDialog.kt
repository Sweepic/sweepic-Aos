package com.umc.sweepic.presentation.sweep.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContentProviderCompat.requireContext
import com.umc.sweepic.R
import com.umc.sweepic.databinding.DialogTrashDeletedBinding
import com.umc.sweepic.presentation.base.BaseDialogFragment

class DeletedDialog (
    private val message: String,
    private val warning: String,
    private val onOkClicked: () -> Unit
    ) : BaseDialogFragment<DialogTrashDeletedBinding>(R.layout.dialog_trash_deleted) {

    override fun initView() {
        // 메시지 표시
        binding.tvTrashTitle.text = message
        binding.tvTrashWarning.text = warning
        requireContext().dialogFragmentResize(this, 0.9f)

        binding.btnTrashDeleteConfirm.setOnClickListener {
            onOkClicked()
            dismiss()
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
