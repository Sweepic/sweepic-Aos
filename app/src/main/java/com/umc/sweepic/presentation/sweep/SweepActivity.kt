package com.umc.sweepic.presentation.sweep

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.umc.sweepic.R
import com.umc.sweepic.databinding.ActivitySweepBinding
import com.umc.sweepic.domain.model.request.sweep.CreateTextFolderRequestModel
import com.umc.sweepic.domain.model.sweep.AlbumList
import com.umc.sweepic.domain.model.sweep.Gallery
import com.umc.sweepic.domain.repository.sweep.TrashRepository
import com.umc.sweepic.presentation.MainActivity
import com.umc.sweepic.presentation.base.BaseActivity
import com.umc.sweepic.presentation.sweep.adapter.AlbumListRVA
import com.umc.sweepic.presentation.sweep.adapter.SweepMemoFolderRVA
import com.umc.sweepic.presentation.sweep.adapter.SweepTagRVA
import com.umc.sweepic.presentation.sweep.adapter.SweepVPA
import com.umc.sweepic.presentation.sweep.dialog.AlbumSelectDialog
import com.umc.sweepic.presentation.sweep.dialog.CreateAlbumDialog
import com.umc.sweepic.presentation.sweep.dialog.CreateFolderDialog
import com.umc.sweepic.presentation.sweep.dialog.DeletedDialog
import com.umc.sweepic.presentation.sweep.dialog.QuitChallengeDialog
import com.umc.sweepic.presentation.sweep.dialog.SweepTagDialog
import com.umc.sweepic.util.extension.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class SweepActivity: BaseActivity<ActivitySweepBinding>(R.layout.activity_sweep) {
    private lateinit var adapter: SweepTagRVA
    private lateinit var pagerAdapter: SweepVPA
    private lateinit var albumAdapter: AlbumListRVA
    private val moveViewModel: MoveViewModel by viewModels()
    private val viewModel: SweepViewModel by viewModels()
    private val albumViewModel: AlbumViewModel by viewModels()
    private val currentImages = mutableListOf<Gallery>()
    private lateinit var deletePermissionLauncher: ActivityResultLauncher<IntentSenderRequest>
    private var pendingDeleteUri: Uri? = null
    private var pendingDeletePosition: Int? = null
    private val pendingTrashGalleries: MutableList<Gallery> = mutableListOf()
//    private val approvedUriSet = mutableSetOf<Uri>()
//    private var pendingTrashImage: Gallery? = null
//    private var pendingTrashPosition: Int? = null
//    private var pendingAllImages: List<Gallery>? = null
    private lateinit var updateTrashCount: () -> Unit // 리스너를 외부로 선언
    private val addedAlbums = mutableListOf<AlbumList>() // 현재 RecyclerView에 표시 중인 앨범 목록
    private var locationTag: String? = null // 장소 태그를 저장할 변수
    private var peopleTag: String? = null // 사람 태그를 저장할 변수
    private var foodTag: String? = null // 음식 태그를 저장할 변수
    private var etcTag: String? = null // 기타 태그를 저장할 변수

    private val gson = Gson()
    private val sharedPreferences by lazy {
        getSharedPreferences("SweepPrefs", Context.MODE_PRIVATE)
    }
    private val requiredPermissions: Array<String> by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun initObserver() {
        // 여기서 ViewModel의 LiveData를 관찰
        viewModel.imagesLiveData.observe(this) { newImages ->
            val currentPage = binding.vpSweepMainImg.currentItem // 🔥 현재 페이지 저장

            // 여기서 뷰페이저 어댑터를 업데이트
            currentImages.clear()
            currentImages.addAll(newImages)

            // 처음 초기화 시점에 어댑터가 없다면 initViewPager()에서 생성
            if (::pagerAdapter.isInitialized) {
                pagerAdapter.notifyDataSetChanged()
            }
            // 총 개수 등 UI 업데이트
            binding.tvSweepTotalCount.text = currentImages.size.toString()

            // 만약 첫 로딩 시점이라면 페이지 정보 갱신
            if (currentImages.isNotEmpty()) {
                binding.vpSweepMainImg.post {
                    val newPage = currentPage.coerceAtMost(currentImages.size - 1) // 🔥 기존 페이지 유지
                    binding.vpSweepMainImg.setCurrentItem(newPage, false)
                    updatePageInfo(newPage)
                }
            } else {
                displayNoImagesState()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun initView() {
        requestPermissionsAndLoadImages()
        setupSwitchToggle()
        setupButtons()
        initializeViewPager()
        setupBackPressHandler()
        setupTrashCountObserver()
        setupAlbumRecyclerView()
        setupClickListeners()
        setupQuitButton()
    }

    override fun onResume() {
        super.onResume()

        // 🔥 현재 페이지 번호 저장 (뷰페이저의 현재 위치)
        val currentPage = binding.vpSweepMainImg.currentItem

        viewModel.loadImages() // 🔥 최신 이미지 로드

        binding.vpSweepMainImg.post {
            if (currentPage in currentImages.indices) {
                binding.vpSweepMainImg.setCurrentItem(currentPage, false) // 🔥 기존 위치 복원
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // (예) onCreate 안에서 launcher 초기화
        deletePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // 만약 휴지통 삭제 대기 목록이 있다면 재시도
                if (pendingTrashGalleries.isNotEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        retryPendingTrashDeletions()
                    }
                } else if (pendingDeleteUri != null && pendingDeletePosition != null) {
                    // swipe 삭제 관련 재시도
                    doDeleteImage(pendingDeleteUri!!, pendingDeletePosition!!)
                }
            } else {
                Toast.makeText(this, "이미지 삭제 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
            pendingDeleteUri = null
            pendingDeletePosition = null
        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun retryPendingTrashDeletions() {
        var successfulDeletions = 0
        val iterator = pendingTrashGalleries.iterator()
        while (iterator.hasNext()) {
            val gallery = iterator.next()
            try {
                deleteImageToMediaStoreTrash(gallery)
                iterator.remove()  // 삭제 성공 시 목록에서 제거
                successfulDeletions++
            } catch (e: RecoverableSecurityException) {
                Log.e("SweepActivity", "재시도 중 권한 요청 실패: ${gallery.uri}", e)
                // 만약 여전히 예외가 발생하면 해당 이미지는 그대로 둡니다.
            } catch (e: Exception) {
                Log.e("SweepActivity", "재시도 중 삭제 실패: ${gallery.uri}", e)
            }
        }
        if (successfulDeletions > 0) {
            // 삭제 완료 후 삭제 완료 다이얼로그 표시 (원하는 경우)
            DeletedDialog(
                message = "사진이 지워졌습니다!",
                warning = "지워진 사진들은 30일 이내에 '휴지통'에서 다시 복구할 수 있습니다.",
                onOkClicked = { finish() }
            ).show(supportFragmentManager, "TrashDeletedDialog")
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun doDeleteImage(uri: Uri, position: Int) {
        try {
            val rows = contentResolver.delete(uri, null, null)
            if (rows > 0) {
                currentImages.removeAt(position)
                pagerAdapter.notifyDataSetChanged()
            }
        } catch (e: RecoverableSecurityException) {
            // 승인 요청
            pendingDeleteUri = uri
            pendingDeletePosition = position
            val intentSender = e.userAction.actionIntent.intentSender
            deletePermissionLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val EXTRA_IMAGE_URI = "extra_image_uri"

        fun newIntent(context: Context, imageUriString: String): Intent {
            return Intent(context, SweepActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_URI, imageUriString)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            }
        }
    }

    private fun requestPermissionsAndLoadImages() {
        // 기존과 동일 (권한 체크)
        if (hasAllPermissions()) {
            // 권한이 있다면 ViewModel에 이미지 로드를 요청
            viewModel.loadImages()
        } else {
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                if (permissions.all { it.value }) {
                    viewModel.loadImages()
                } else {
                    Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.launch(requiredPermissions)
        }
    }

    private fun hasAllPermissions() = requiredPermissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestDeletePermission(e: RecoverableSecurityException) {
        val intentSender = e.userAction.actionIntent.intentSender
        val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()
        deletePermissionLauncher.launch(intentSenderRequest)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun deleteImageToMediaStoreTrash(gallery: Gallery) {
        try {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.IS_TRASHED, 1)
            }
            val rowsUpdated = contentResolver.update(gallery.uri, values, null, null)
            if (rowsUpdated > 0) {
                TrashRepository.removeFromTrash(gallery)
                Log.d("SweepActivity", "Image moved to MediaStore trash: ${gallery.uri}")
            } else {
                Log.e("SweepActivity", "Failed to move image to MediaStore trash: ${gallery.uri}")
            }
        } catch (e: RecoverableSecurityException) {
            throw e  // 호출자에서 처리하도록 전달
        } catch (e: Exception) {
            Log.e("SweepActivity", "Error deleting image: ${gallery.uri}", e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun deleteImagesWithPermission(images: List<Gallery>) {
        pendingTrashGalleries.clear()  // 대기열 초기화
        images.forEach { gallery ->
            try {
                deleteImageToMediaStoreTrash(gallery)
            } catch (e: RecoverableSecurityException) {
                pendingTrashGalleries.add(gallery)  // 삭제 실패한 이미지를 대기열에 추가
                requestDeletePermission(e)
            }
        }
    }

    private fun initializeViewPager() {
        // 기존 이미지 리스트를 기반으로 어댑터 생성
        pagerAdapter = SweepVPA(currentImages)
        binding.vpSweepMainImg.adapter = pagerAdapter

        // 초기 위치, 스와이프 제스처, 터치 네비게이션 등 설정
        setupSwipeToTrashGesture()
        setupTouchNavigation()

        binding.vpSweepMainImg.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updatePageInfo(position)
                }
            }
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchNavigation() {
        binding.viewLeftTouch.setOnClickListener {
            val currentPosition = binding.vpSweepMainImg.currentItem
            if (currentPosition > 0) {
                binding.vpSweepMainImg.setCurrentItem(currentPosition - 1, true)
            }
        }

        binding.viewRightTouch.setOnClickListener {
            val currentPosition = binding.vpSweepMainImg.currentItem
            if (currentPosition < currentImages.size - 1) {
                binding.vpSweepMainImg.setCurrentItem(currentPosition + 1, true)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSwipeToTrashGesture() {
        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (e1 != null && e2 != null) {
                    val deltaY = e1.y - e2.y
                    // 스와이프 거리와 속도 조건은 적절히 조정
                    if (deltaY > 200 && velocityY < -500) {
                        val currentPosition = binding.vpSweepMainImg.currentItem
                        if (currentPosition in currentImages.indices) {
                            val currentImage = currentImages[currentPosition]

                            // 1) 휴지통으로 이동 (TrashRepository 추가)
                            TrashRepository.addToTrash(currentImage)

                            // 2) 현재 리스트/어댑터에서 제거 & 애니메이션
                            removeImageWithAnimation(currentPosition)
                        }
                        return true
                    }
                }
                return false
            }
        })

        binding.vpSweepMainImg.getChildAt(0).setOnTouchListener { view, event ->
            gestureDetector.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE ->
                    view.parent.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP ->
                    view.parent.requestDisallowInterceptTouchEvent(false)
            }
            true
        }
    }

    private fun removeImageWithAnimation(position: Int) {
        val viewPagerItemView = binding.vpSweepMainImg.findViewWithTag<View>("view_pager_$position")
        viewPagerItemView?.animate()
            ?.translationY(-binding.vpSweepMainImg.height.toFloat())
            ?.setDuration(300)
            ?.withEndAction {
                // 1) currentImages 에서 제거
                if (position in currentImages.indices) {
                    currentImages.removeAt(position)
                }

                // 2) 어댑터 재설정
                pagerAdapter = SweepVPA(currentImages)
                binding.vpSweepMainImg.adapter = pagerAdapter

                // 3) 전체 개수 갱신
                binding.tvSweepTotalCount.text = currentImages.size.toString()

                // 4) 다음 위치로 이동
                if (currentImages.isNotEmpty()) {
                    val nextPos = position.coerceAtMost(currentImages.size - 1)
                    binding.vpSweepMainImg.setCurrentItem(nextPos, false)
                    updatePageInfo(nextPos)
                } else {
                    displayNoImagesState()
                }
            }
            ?.start()
    }

    private fun displayNoImagesState() {
        binding.tvSweepTotalCount.text = "0"
        binding.tvSweepCount.text = "0"
        binding.tvSweepDate.text = "날짜 정보 없음"
        Log.d("SweepActivity", "갤러리에 이미지가 없습니다.")
    }


    private fun updatePageInfo(position: Int) {
        if (currentImages.isEmpty() || position !in currentImages.indices) {
            displayNoImagesState()
            return
        }
        binding.tvSweepCount.text = (position + 1).toString()

        val currentItem = currentImages[position]
        val dateFormat = SimpleDateFormat("yy.MM.dd a HH:mm", Locale("ko","KR"))
        val dateString = dateFormat.format(currentItem.addedDate)
        binding.tvSweepDate.text = dateString
    }

    private fun setupButtons() {
        setupMoveButton()
        setupTrashButton()
        setupTags()
    }

    private fun setupMoveButton() {
        binding.ivSweepMove.setOnClickListener {
            startActivity(MoveActivity.newIntent(this))
        }
    }

    private fun setupTrashButton() {
        binding.ivSweepTrash.setOnClickListener {
            startActivity(TrashActivity.newIntent(this))
        }
    }

    private fun setupSwitchToggle() {
        binding.switchSweepAiBtn.setOnCheckedChangeListener { _, isChecked ->
            binding.tvSweepAiOn.visibility = if (isChecked) View.VISIBLE else View.GONE
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

    private fun showCustomBottomSheetDialog(
        titleExisting: String,
        titleExistingEx: String,
        titleNew: String,
        titleNewEx: String,
        onExistingClick: () -> Unit,
        onNewClick: () -> Unit
    ) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_sheet_album, null)
        bottomSheetDialog.setContentView(view)

        val tvAddExisting: TextView = view.findViewById(R.id.tv_add_existing_album)
        val tvAddExistingEx: TextView = view.findViewById(R.id.tv_add_existing_album_ex)
        val tvCreateNew: TextView = view.findViewById(R.id.tv_create_new_album)
        val tvCreateNewEx: TextView = view.findViewById(R.id.tv_create_new_album_ex)
        val tvCancel: TextView = view.findViewById(R.id.tv_cancel)

        // 텍스트 설정
        tvAddExisting.text = titleExisting
        tvAddExistingEx.text = titleExistingEx
        tvCreateNew.text = titleNew
        tvCreateNewEx.text = titleNewEx

        // 클릭 동작 설정
        tvAddExisting.setOnClickListener {
            bottomSheetDialog.dismiss()
            onExistingClick()
        }
        tvCreateNew.setOnClickListener {
            bottomSheetDialog.dismiss()
            onNewClick()
        }
        tvCancel.setOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupClickListeners() {
        // layoutSweepAddFolderContainer 클릭
        binding.layoutSweepAddFolderContainer.setOnClickListener {
            showCustomBottomSheetDialog(
                titleExisting = "기존 앨범 추가하기",
                titleExistingEx = "안드로이드의 기존 앨범을 불러옵니다.",
                titleNew = "새 앨범 만들기",
                titleNewEx = "안드로이드에 새로운 앨범을 생성합니다.",
                onExistingClick = {
                    // 기존 앨범 선택 동작
                    AlbumSelectDialog(
                        addedAlbums = addedAlbums.toList(), // 이미 추가된 앨범 전달
                        onAlbumsSelected = { selectedAlbums, deselectedAlbums ->
                            // 새로 선택된 앨범 추가
                            val newAlbums = selectedAlbums.filter { album ->
                                addedAlbums.none { it.id == album.id }
                            }
                            addedAlbums.addAll(newAlbums)

                            // 선택 해제된 앨범 제거
                            addedAlbums.removeAll(deselectedAlbums)

                            // RecyclerView 업데이트
                            albumAdapter.submitList(addedAlbums.toList())

                            // 변경된 데이터를 SharedPreferences에 저장
                            saveAlbums()
                        }
                    ).show(supportFragmentManager, "AlbumSelectDialog")
                },
                onNewClick = {
                    // 새 앨범 만들기 동작
                    CreateAlbumDialog { albumName ->
                        addNewAlbum(albumName) // 새 앨범 추가
                    }.show(supportFragmentManager, "CreateAlbumDialog")
                }
            )
        }

        // ivSweepMemo 클릭
        binding.ivSweepMemo.setOnClickListener {
            showCustomBottomSheetDialog(
                titleExisting = "텍스트로 저장하기",
                titleExistingEx = "사진 속 텍스트만 보관 후, 기존 사진첩에서 삭제합니다.",
                titleNew = "사진으로 저장하기",
                titleNewEx = "사진을 앱 메모장에 저장 후, 기존 사진첩에서 삭제합니다.",
                onExistingClick = {
                    // 텍스트로 저장 선택 동작
                    showMemoTextBottomSheetDialog()
                },
                onNewClick = {
                    // 사진으로 저장 선택 동작
                    showMemoImageBottomSheetDialog()
                }
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showMemoTextBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_sheet_memo, null)
        bottomSheetDialog.setContentView(view)

        // 폴더 추가 클릭 리스너
        val layoutMemoAddFolder: LinearLayout = view.findViewById(R.id.layout_memo_add_folder)
        layoutMemoAddFolder.setOnClickListener {
            bottomSheetDialog.dismiss()
            // CreateFolderDialog 호출
            CreateFolderDialog(
                title = "새 폴더 추가하기",
                explanation = "폴더를 만들고 사진을 저장합니다.",
                confirmText = "추가 후 저장",
                hint = "추가할 폴더 이름을 입력해주세요.",
                onFolderCreated = { folderName ->
                    createTextFolderAndDeleteImage(folderName)
                }
            ).show(supportFragmentManager, "CreateFolderDialog")
        }

        // 취소 클릭 리스너
        val tvMemoCancel: TextView = view.findViewById(R.id.tv_memo_cancel)
        tvMemoCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // RecyclerView 설정
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_add_memo_folder_list)
        val adapter = SweepMemoFolderRVA { folder ->
            // 선택한 폴더 처리 동작
            showToast("${folder.folderName} 선택됨")
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 🔥 ViewModel의 데이터를 관찰하여 RecyclerView 업데이트
        viewModel.folderList.observe(this) { folderList ->
            adapter.submitList(folderList)
        }

        // 🔥 API 호출하여 폴더 목록 가져오기
        viewModel.fetchFolderList()

        bottomSheetDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showMemoImageBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_sheet_memo, null)
        bottomSheetDialog.setContentView(view)

        // 폴더 추가 클릭 리스너
        val layoutMemoAddFolder: LinearLayout = view.findViewById(R.id.layout_memo_add_folder)
        layoutMemoAddFolder.setOnClickListener {
            bottomSheetDialog.dismiss()
            // CreateFolderDialog 호출
            CreateFolderDialog(
                title = "새 폴더 추가하기",
                explanation = "폴더를 만들고 사진을 저장합니다.",
                confirmText = "추가 후 저장",
                hint = "추가할 폴더 이름을 입력해주세요.",
                onFolderCreated = { folderName ->
                    createImageFolderAndDeleteImage(folderName)
                }
            ).show(supportFragmentManager, "CreateFolderDialog")
        }

        // 취소 클릭 리스너
        val tvMemoCancel: TextView = view.findViewById(R.id.tv_memo_cancel)
        tvMemoCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // RecyclerView 설정 – 폴더 목록 표시 및 선택 시 처리
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_add_memo_folder_list)
        val adapter = SweepMemoFolderRVA { folder ->
            // 폴더 선택 시 처리
            bottomSheetDialog.dismiss()

            // 현재 뷰페이저의 선택된 이미지 가져오기
            val currentPosition = binding.vpSweepMainImg.currentItem
            if (currentPosition !in currentImages.indices) {
                Toast.makeText(this, "이미지를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                return@SweepMemoFolderRVA
            }
            val currentImage = currentImages[currentPosition]
            val imageUri = currentImage.uri

            // 이미지 URI를 ByteArray로 변환 (Activity에 있는 헬퍼 함수 사용)
            val imageByteArray = convertUriToByteArray(imageUri)
            if (imageByteArray == null) {
                Toast.makeText(this, "이미지 변환 실패", Toast.LENGTH_SHORT).show()
                return@SweepMemoFolderRVA
            }

            // MultipartBody.Part 생성
            val mediaType = "image/jpeg".toMediaTypeOrNull()
            val imageRequestBody = imageByteArray.toRequestBody(mediaType)
            val imagePart = MultipartBody.Part.createFormData("image", "image.jpg", imageRequestBody)

            // ViewModel의 API 호출 (코루틴 내에서 실행)
            lifecycleScope.launch {
                viewModel.fetchSweepSaveImageMemo(folder.folderId, imagePart).onSuccess { response ->
                    // API 호출 성공 시 현재 이미지 삭제
                    deleteCurrentImage(currentPosition)
                    Toast.makeText(
                        this@SweepActivity,
                        "사진이 폴더 ${folder.folderName}에 저장되었습니다. (ID: ${response.folderId})",
                        Toast.LENGTH_SHORT
                    ).show()
                }.onFailure { e ->
                    Toast.makeText(
                        this@SweepActivity,
                        "폴더 저장 실패: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 🔥 ViewModel의 데이터를 관찰하여 RecyclerView 업데이트
        viewModel.folderList.observe(this) { folderList ->
            adapter.submitList(folderList)
        }

        // 🔥 API 호출하여 폴더 목록 가져오기
        viewModel.fetchFolderList()

        bottomSheetDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createTextFolderAndDeleteImage(folderName: String) {
        // 1) 현재 뷰페이저의 이미지를 구한다 (ex. currentImages[currentPosition])
        val currentPosition = binding.vpSweepMainImg.currentItem
        if (currentPosition !in currentImages.indices) {
            Toast.makeText(this, "이미지를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val currentImage = currentImages[currentPosition]
        val imageUri = currentImage.uri

        // 2) 이미지 -> Base64 변환
        val base64Image = convertUriToBase64(imageUri)
        if (base64Image == null) {
            Toast.makeText(this, "이미지 변환 실패", Toast.LENGTH_SHORT).show()
            return
        }

        // 3) API 요청 모델 생성
        val requestModel = CreateTextFolderRequestModel(
            base64_image = base64Image,
            user_id = 12L, // 실제 유저 ID
            folder_name = folderName
        )

        // 4) ViewModel (or Repository directly) 통해 API 호출
        lifecycleScope.launch {
            val result = viewModel.fetchSweepCreateTextFolder(requestModel)
            result.onSuccess { response ->
                // 성공 시 → 실제 파일 OS에서 삭제
                deleteCurrentImage(currentPosition)
                Toast.makeText(
                    this@SweepActivity,
                    "폴더 생성+텍스트 저장 성공: folderId=${response.folderId}",
                    Toast.LENGTH_SHORT
                ).show()
            }.onFailure { e ->
                Toast.makeText(
                    this@SweepActivity,
                    "폴더 생성 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createImageFolderAndDeleteImage(folderName: String) {
        val currentPosition = binding.vpSweepMainImg.currentItem
        if (currentPosition !in currentImages.indices) {
            Toast.makeText(this, "이미지를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val currentImage = currentImages[currentPosition]
        val imageUri = currentImage.uri

        // 🛠 이미지 URI를 ByteArray로 변환
        val imageByteArray = convertUriToByteArray(imageUri)
        if (imageByteArray == null) {
            Toast.makeText(this, "이미지 변환 실패", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val result = viewModel.fetchSweepCreateImageFolder(folderName, imageByteArray)
            result.onSuccess { response ->
                // 🔥 성공 시 이미지 삭제 및 UI 업데이트
                deleteCurrentImage(currentPosition)
                Toast.makeText(
                    this@SweepActivity,
                    "사진이 폴더 ${response.folderName}에 저장되었습니다. (ID: ${response.folderId})",
                    Toast.LENGTH_SHORT
                ).show()
            }.onFailure { e ->
                Toast.makeText(
                    this@SweepActivity,
                    "폴더 생성 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun convertUriToByteArray(uri: Uri): ByteArray? {
        return try {
            contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun deleteCurrentImage(position: Int) {
        val img = currentImages[position]
        val uri = img.uri

        try {
            // ① 실제 삭제 시도
            val rowsDeleted = contentResolver.delete(uri, null, null)

            // ② 성공하면? → 뷰페이저 목록에서도 제거
            if (rowsDeleted > 0) {
                currentImages.removeAt(position)
                pagerAdapter.notifyDataSetChanged()
            }

        } catch (e: RecoverableSecurityException) {
            // ③ 삭제 권한 없음 → 승인 요청
            val intentSender = e.userAction.actionIntent.intentSender
            pendingDeleteUri = uri           // 나중에 재시도 하기 위해 저장
            pendingDeletePosition = position

            // 런처로 권한 다이얼로그 표시
            deletePermissionLauncher.launch(
                IntentSenderRequest.Builder(intentSender).build()
            )
        } catch (e: Exception) {
            // 그 밖의 오류 처리
            e.printStackTrace()
            Toast.makeText(this, "삭제 실패: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun convertUriToBase64(uri: Uri): String? {
        return try {
            contentResolver.openInputStream(uri)?.use { input ->
                val bytes = input.readBytes()
                android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(MainActivity.newIntent(this@SweepActivity))
                finish()
            }
        })
    }

    private fun setupTrashCountObserver() {
        // TrashRepository 변경 사항을 감지하고 UI를 업데이트
        val updateTrashCount = {
            val trashCount = TrashRepository.getAllTrashed().size

            if (trashCount == 0) {
                binding.tvSweepTrashCount.visibility = View.GONE
            } else {
                binding.tvSweepTrashCount.visibility = View.VISIBLE
                binding.tvSweepTrashCount.text = if (trashCount > 99) "99+" else trashCount.toString()
            }
        }

        TrashRepository.addListener(updateTrashCount)

        // 초기 상태 업데이트
        updateTrashCount()
    }

    private fun setupAlbumRecyclerView() {
        albumAdapter = AlbumListRVA { album ->
            showToast("${album.name} 클릭됨")
        }
        // 저장된 데이터를 복원
        restoreAlbums()

        binding.rvSweepSaveToAlbum.apply {
            layoutManager = LinearLayoutManager(this@SweepActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = albumAdapter
        }
    }

    private fun saveAlbums() {
        val albumJson = gson.toJson(addedAlbums)
        sharedPreferences.edit().putString("AddedAlbums", albumJson).apply()
    }

    private fun restoreAlbums() {
        val albumJson = sharedPreferences.getString("AddedAlbums", null)
        if (!albumJson.isNullOrEmpty()) {
            val albumType = object : TypeToken<List<AlbumList>>() {}.type
            val restoredAlbums: List<AlbumList> = gson.fromJson(albumJson, albumType)
            addedAlbums.clear()
            addedAlbums.addAll(restoredAlbums)
            albumAdapter.submitList(addedAlbums.toList())
        }
    }

    private fun addNewAlbum(albumName: String) {
        // 경로 확인
        val albumPath = "Pictures/$albumName"
        val projection = arrayOf(MediaStore.Images.Media.RELATIVE_PATH)
        val selection = "${MediaStore.Images.Media.RELATIVE_PATH} = ?"
        val selectionArgs = arrayOf(albumPath)

        // 해당 경로에 파일이 존재하는지 확인
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        val albumExists = cursor?.use { it.count > 0 } ?: false

        if (!albumExists) {
            // Placeholder 이미지 생성
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "PlaceholderImage_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.RELATIVE_PATH, albumPath)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }

            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null) {
                albumViewModel.loadAlbums() // 앨범 목록 새로 로드
                showToast("새 앨범 '$albumName'이(가) 추가되었습니다.")
            } else {
                showToast("앨범 추가에 실패했습니다.")
            }
        } else {
            showToast("이미 존재하는 앨범입니다.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupQuitButton() {
        binding.tvSweepQuit.setOnSingleClickListener {
            showQuitConfirmationDialog()
        }
    }

    private fun showQuitConfirmationDialog() {
        val trashCount = TrashRepository.getAllTrashed().size

        if (trashCount == 0) {
            // trashCount가 0일 경우 Activity 종료
            finish()
            return
        }

        QuitChallengeDialog(
            title = "휴지통 속 ${trashCount}장의 사진을\n비우고 챌린지를 그만할까요?",
            explanation = "휴지통을 비우고 다음에 다시 도전할 수 있어요!",
            confirmText = "그만하기",
            cancelText = "계속하기",
            onConfirm = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val allTrashed = TrashRepository.getAllTrashed()
                    if (allTrashed.isNotEmpty()) {
                        deleteImagesWithPermission(allTrashed)
                        // 만약 예외 없이 모든 이미지가 삭제되었다면
                        if (pendingTrashGalleries.isEmpty()) {
                            DeletedDialog(
                                message = "사진이 지워졌습니다!",
                                warning = "지워진 사진들은 30일 이내에 '휴지통'에서 다시 복구할 수 있습니다.",
                                onOkClicked = { finish() }
                            ).show(supportFragmentManager, "TrashDeletedDialog")
                        }
                        // 만약 일부 이미지가 대기 상태(pendingTrashGalleries)에 남았다면,
                        // 권한 요청 후 deletePermissionLauncher 콜백에서 retryPendingTrashDeletions()가 호출되어
                        // 삭제 완료 후 DeletedDialog를 보여줍니다.
                    }
                }
            },
            onCancel = {
                showToast("계속하기를 선택했습니다.")
            }
        ).show(supportFragmentManager, "QuitChallengeDialog")
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

    override fun onDestroy() {
        super.onDestroy()
        if (::updateTrashCount.isInitialized) {
            TrashRepository.removeListener(updateTrashCount)
        }
    }
}