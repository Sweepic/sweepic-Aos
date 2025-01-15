package com.umc.sweepic.presentation.sweep

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.umc.sweepic.R
import com.umc.sweepic.databinding.ActivitySweepBinding
import com.umc.sweepic.domain.model.sweep.Gallery
import com.umc.sweepic.presentation.MainActivity
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

        val selectedUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val allImages: List<Gallery> = moveViewModel.loadAllImagesDesc()
        // 이미지가 하나도 없다면 처리
        if (allImages.isEmpty()) {
            binding.tvSweepTotalCount.text = "0"
            binding.tvSweepCount.text = "0"
            binding.tvSweepDate.text = "날짜 정보 없음"
            Log.d("SweepActivity", "갤러리에 이미지가 없습니다.")
            return
        }
        pagerAdapter = SweepVPA(allImages)
        binding.vpSweepMainImg.adapter = pagerAdapter

        // 선택된 이미지 인덱스 찾기
        var selectedIndex = 0
        if (!selectedUriString.isNullOrEmpty()) {
            val foundIndex = allImages.indexOfFirst { it.uri.toString() == selectedUriString }
            if (foundIndex >= 0) selectedIndex = foundIndex
        }

        // ViewPager2 초기 위치 설정
        binding.vpSweepMainImg.setCurrentItem(selectedIndex, false)

        // ■■■ 추가 부분: 초기 페이지 데이터 직접 설정
        updatePageInfo(allImages, selectedIndex)

        // 전체 개수
        binding.tvSweepTotalCount.text = allImages.size.toString()

        // 페이지 변경 콜백 등록
        binding.vpSweepMainImg.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updatePageInfo(allImages, position)
            }
        })

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // SweepActivity에서 뒤로가기 누르면 MainActivity로 이동
                startActivity(MainActivity.newIntent(this@SweepActivity))
                finish()
            }
        })

        binding.layoutSweepAddFolderContainer.setOnClickListener {
            showAlbumBottomSheet()
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

    private fun updatePageInfo(allImages: List<Gallery>, position: Int) {
        // 페이지 인덱스 (1-based)
        binding.tvSweepCount.text = (position + 1).toString()

        val currentItem = allImages[position]
        val dateFormat = SimpleDateFormat("yy.MM.dd a HH:mm", Locale("ko","KR"))

        // currentItem.addedDate가 Long이면 new Date(...),
        val dateString = dateFormat.format(currentItem.addedDate)

        binding.tvSweepDate.text = dateString
    }

    private fun setupMoveButton() {
        binding.ivSweepMove.setOnClickListener {
            startActivity(MoveActivity.newIntent(this))
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

    private fun showAlbumBottomSheet() {
        // BottomSheetDialog 생성
        val bottomSheetDialog = BottomSheetDialog(this)
        // inflate하기 위해 LayoutInflater 사용
        val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_sheet_album, null)
        bottomSheetDialog.setContentView(view)

        // 터치 영역들
        val tvAddExisting: TextView = view.findViewById(R.id.tv_add_existing_album)
        val tvCreateNew: TextView = view.findViewById(R.id.tv_create_new_album)
        val tvCancel: TextView = view.findViewById(R.id.tv_cancel)

        // 터치 리스너 설정 (필요에 따라 실제 동작 구현)
        tvAddExisting.setOnClickListener {
            // 기존 앨범 추가하기 처리
            // 예: 기존 앨범 리스트를 보여준다거나, 해당 액티비티로 이동
            bottomSheetDialog.dismiss()
        }

        tvCreateNew.setOnClickListener {
            // 새 앨범 만들기 처리
            bottomSheetDialog.dismiss()
        }

        tvCancel.setOnClickListener {
            // 취소: 그냥 BottomSheetDialog 닫기
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
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