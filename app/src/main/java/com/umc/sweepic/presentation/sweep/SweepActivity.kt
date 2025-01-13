package com.umc.sweepic.presentation.sweep

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.umc.sweepic.R
import com.umc.sweepic.databinding.ActivitySweepBinding
import com.umc.sweepic.domain.model.sweep.Gallery
import com.umc.sweepic.presentation.base.BaseActivity
import com.umc.sweepic.presentation.sweep.adapter.SweepTagRVA
import com.umc.sweepic.presentation.sweep.adapter.SweepVPA
import com.umc.sweepic.presentation.sweep.dialog.SweepTagDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class SweepActivity: BaseActivity<ActivitySweepBinding>(R.layout.activity_sweep) {
    private lateinit var adapter: SweepTagRVA
    private lateinit var pagerAdapter: SweepVPA
    private val moveViewModel: MoveViewModel by viewModels()
    private var locationTag: String? = null // 장소 태그를 저장할 변수
    private var peopleTag: String? = null // 사람 태그를 저장할 변수
    private var foodTag: String? = null // 음식 태그를 저장할 변수
    private var etcTag: String? = null // 기타 태그를 저장할 변수
    override fun initObserver() {

    }

    override fun initView() {
        switchToggle()
        setupMoveButton()
        setupTags()
        // 1) 인텐트로부터 선택된 이미지 URI 가져오기
        val selectedUriString = intent.getStringExtra(EXTRA_IMAGE_URI)

        // 2) 전체 갤러리 목록 로드
        val allImages: List<Gallery> = moveViewModel.loadAllImagesDesc() // ViewModel에서 전체 로드

        // 3) ViewPager 어댑터 생성
        pagerAdapter = SweepVPA(allImages)
        binding.vpSweepMainImg.adapter = pagerAdapter

        // 4) 선택된 URI를 가진 항목의 인덱스를 찾아서 ViewPager 초기 위치로 설정
        selectedUriString?.let { uriStr ->
            val selectedIndex = allImages.indexOfFirst { it.uri.toString() == uriStr }
            if (selectedIndex >= 0) {
                binding.vpSweepMainImg.setCurrentItem(selectedIndex, false)
            }
        }
        binding.tvSweepTotalCount.text = allImages.size.toString()
        // 4) 현재 페이지 인덱스 변화 콜백 등록
        binding.vpSweepMainImg.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 페이지 인덱스 (1-based)
                binding.tvSweepCount.text = (position + 1).toString()

                val currentItem = allImages[position]

                // "yy.MM.dd a HH:mm" → 예: "24.08.17 오전 10:11"
                val dateFormat = SimpleDateFormat("yy.MM.dd a HH:mm", Locale("ko","KR"))
                val dateString = dateFormat.format(currentItem.addedDate)

                // 텍스트뷰에 날짜/시간 표시
                binding.tvSweepDate.text = dateString
            }
        })
    }

    private fun setupMoveButton() {
        binding.ivSweepMove.setOnClickListener {
            startActivity(MoveActivity.newIntent(this))
        }
    }

    companion object {
        private const val EXTRA_IMAGE_URI = "extra_image_uri"

        fun newIntent(context: Context, imageUriString: String): Intent {
            return Intent(context, SweepActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_URI, imageUriString)
            }
        }
    }


    private fun switchToggle() {
        binding.switchSweepAiBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // ON 상태: 왼쪽에 AI 표시
                binding.tvSweepAiOn.visibility = View.VISIBLE
            } else {
                // OFF 상태: 오른쪽에 AI 표시
                binding.tvSweepAiOn.visibility = View.GONE
            }
        }
    }

    private fun setupTagClickListener(textView: TextView, dialogTag: String, title: String, hint: String, onSave: (String) -> Unit) {
        textView.setOnClickListener {
            // TextView의 현재 상태 저장
            val originalText = textView.text.toString()
            val originalTextColor = textView.currentTextColor
            val originalBackground = textView.background

            val dialog = SweepTagDialog(
                title = title,
                hint = hint,
                onTagEntered = { inputText ->
                    // 확인 시 TextView 업데이트
                    textView.text = inputText
                    textView.setTextColor(ContextCompat.getColor(this, R.color.sweepic))
                    textView.setBackgroundResource(R.drawable.shape_rect_16_blue_line)
                    onSave(inputText)
                },
                onCancel = {
                    // 취소 시 TextView 복원
                    textView.text = originalText
                    textView.setTextColor(originalTextColor)
                    textView.background = originalBackground
                }
            )
            dialog.show(supportFragmentManager, dialogTag)
        }
    }
    // 태그 관련 코드 정리
    private fun setupTags() {
        // 장소 태그
        setupTagClickListener(
            binding.tvSweepLocationTag,
            "LocationTagDialog",
            title = "#장소 입력하기",
            hint = "장소를 입력해주세요."
        ) { locationTag = it }

        // 사람 태그
        setupTagClickListener(
            binding.tvSweepPeopleTag,
            "PeopleTagDialog",
            title = "#사람 입력하기",
            hint = "사람 이름을 입력해주세요."
        ) { peopleTag = it }

        // 음식 태그
        setupTagClickListener(
            binding.tvSweepFoodTag,
            "FoodTagDialog",
            title = "#음식 입력하기",
            hint = "음식을 입력해주세요."
        ) { foodTag = it }

        // 기타 태그
        setupTagClickListener(
            binding.tvSweepEtcTag,
            "EtcTagDialog",
            title = "#기타 입력하기",
            hint = "기타 내용을 입력해주세요."
        ) { etcTag = it }
    }
}