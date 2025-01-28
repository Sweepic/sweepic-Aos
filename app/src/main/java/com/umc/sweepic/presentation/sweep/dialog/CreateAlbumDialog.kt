package com.umc.sweepic.presentation.sweep.dialog

import android.os.Bundle
import com.umc.sweepic.R
import com.umc.sweepic.databinding.DialogAddNewAlbumBinding
import com.umc.sweepic.presentation.base.BaseDialogFragment
import com.umc.sweepic.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAlbumDialog(
    private val onAlbumCreated: (String) -> Unit // 새 앨범 이름 전달 콜백
) : BaseDialogFragment<DialogAddNewAlbumBinding>(R.layout.dialog_add_new_album) {

    override fun initView() {
        requireContext().dialogFragmentResize(this, 0.9f)

        // 취소 버튼
        binding.btnAddFolderCancel.setOnSingleClickListener {
            dismiss()
        }

        // 추가 버튼
        binding.btnAddFolderConfirm.setOnSingleClickListener {
            val albumName = binding.etAddAlbumName.text.toString().trim()
            if (albumName.isNotEmpty()) {
                onAlbumCreated(albumName)
                dismiss()
            } else {
                binding.etAddAlbumName.error = "앨범 명을 입력해주세요." // 에러 메시지 표시
            }
        }
    }

    override fun initObserver() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.MyDialogStyle)
    }
}