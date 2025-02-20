package com.umc.sweepic.presentation.sweep.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.umc.sweepic.R
import com.umc.sweepic.databinding.DialogInputTagBinding
import com.umc.sweepic.domain.model.response.sweep.AiTagResponseModel
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

        // EditText에 TextWatcher 추가
        binding.etSweepInputLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.length ?: 0
                // tv_tag_name_length에 현재 글자 수 업데이트 (예를 들어 "5")
                binding.tvTagNameLength.text = length.toString()
                if (length > 10) {
                    // 10글자 초과시 스타일 업데이트
                    binding.tvTagNameLength.setTextColor(ContextCompat.getColor(requireContext(), R.color.error))
                    binding.tvTagFullLength.setTextColor(ContextCompat.getColor(requireContext(), R.color.error))
                    binding.etSweepInputLocation.setTextColor(ContextCompat.getColor(requireContext(), R.color.error))
                    binding.etSweepInputLocation.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.error)
                    binding.tvAiRecommend.text = "10자를 초과할 수 없습니다."
                    binding.tvAiRecommend.setTextColor(ContextCompat.getColor(requireContext(), R.color.error))
                    binding.ivTagLengthError.visibility = View.VISIBLE
                } else {
                    // 기본 스타일 복원
                    binding.tvTagNameLength.setTextColor(ContextCompat.getColor(requireContext(), R.color.sw_gray2))
                    binding.tvTagFullLength.setTextColor(ContextCompat.getColor(requireContext(), R.color.sw_gray2))
                    binding.etSweepInputLocation.setTextColor(ContextCompat.getColor(requireContext(), R.color.sw_bk))
                    binding.etSweepInputLocation.backgroundTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.sw_gray2)
                    binding.tvAiRecommend.text = "AI가 태그를 추천해드려요."
                    binding.tvAiRecommend.setTextColor(ContextCompat.getColor(requireContext(), R.color.sweepic))
                    binding.ivTagLengthError.visibility = View.GONE
                }
            }
            override fun afterTextChanged(s: Editable?) { }
        })

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
//                    binding.etSweepInputLocation.error = "값을 입력해주세요." // 에러 메시지 표시
                }
            }
        }
    }

    // AI 태그 결과를 업데이트하는 함수
    fun updateAiTags(labels: List<AiTagResponseModel.AiTagContentModel>) {
        // 태그 1 업데이트
        val tag1 = labels.getOrNull(0)?.description ?: ""
        if (tag1.isNotEmpty()) {
            binding.tvAiTag1.text = tag1
            binding.tvAiTag1.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate().alpha(1f).setDuration(300).start()
            }
        } else {
            binding.tvAiTag1.visibility = View.INVISIBLE
        }

        // 태그 2 업데이트
        val tag2 = labels.getOrNull(1)?.description ?: ""
        if (tag2.isNotEmpty()) {
            binding.tvAiTag2.text = tag2
            binding.tvAiTag2.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate().alpha(1f).setDuration(300).start()
            }
        } else {
            binding.tvAiTag2.visibility = View.INVISIBLE
        }

        // 태그 3 업데이트
        val tag3 = labels.getOrNull(2)?.description ?: ""
        if (tag3.isNotEmpty()) {
            binding.tvAiTag3.text = tag3
            binding.tvAiTag3.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate().alpha(1f).setDuration(300).start()
            }
        } else {
            binding.tvAiTag3.visibility = View.INVISIBLE
        }
    }

    override fun initObserver() {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 커스텀 다이얼로그 스타일 적용
        setStyle(STYLE_NORMAL, R.style.MyDialogStyle)
    }

    override fun getTheme(): Int = R.style.MyDialogStyle

}