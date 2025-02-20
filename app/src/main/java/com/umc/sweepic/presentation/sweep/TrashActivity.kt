package com.umc.sweepic.presentation.sweep

import android.app.RecoverableSecurityException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.sweepic.R
import com.umc.sweepic.databinding.ActivityTrashBinding
import com.umc.sweepic.domain.model.request.sweep.TrashImageRequestModel
import com.umc.sweepic.domain.model.sweep.Gallery
import com.umc.sweepic.domain.repository.sweep.TrashRepository
import com.umc.sweepic.presentation.base.BaseActivity
import com.umc.sweepic.presentation.sweep.adapter.TrashRVA
import com.umc.sweepic.presentation.sweep.dialog.DeletedDialog
import com.umc.sweepic.presentation.sweep.dialog.TrashDeleteDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrashActivity: BaseActivity<ActivityTrashBinding>(R.layout.activity_trash) {
    private lateinit var adapter: TrashRVA
    private lateinit var deletePermissionLauncher: ActivityResultLauncher<IntentSenderRequest>
    private var selectedCount: Int = 0
    private val pendingTrashGalleries: MutableList<Gallery> = mutableListOf()
    private val sweepViewModel: SweepViewModel by viewModels()

    override fun initObserver() {

    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initView() {
        setupRecyclerView()
        setBackBtn()
        loadTrashedImages()
        setupButtons()

    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, TrashActivity::class.java)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 사용자 승인 요청 런처 등록
        deletePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                // 승인 후 삭제 재시도
                retryPendingDeletions()
            } else {
                Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestDeletePermission(e: RecoverableSecurityException) {
        val intentSender = e.userAction.actionIntent.intentSender
        val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()
        deletePermissionLauncher.launch(intentSenderRequest)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun retryPendingDeletions() {
        val iterator = pendingTrashGalleries.iterator()
        var successfulDeletions = 0 // 성공적으로 삭제된 사진 수

        while (iterator.hasNext()) {
            val gallery = iterator.next()
            try {
                deleteImageToMediaStoreTrash(gallery)
                iterator.remove() // 삭제 성공 시 대기열에서 제거
                successfulDeletions++
            } catch (e: RecoverableSecurityException) {
                Log.e("TrashActivity", "권한 요청 중 예외 발생: ${gallery.uri}", e)
            }
        }

        // RecyclerView 갱신
        loadTrashedImages()

        // 성공적으로 삭제된 사진이 있을 경우 삭제 완료 다이얼로그 표시
        if (successfulDeletions > 0) {
            DeletedDialog(
                message = "사진이 지워졌습니다!",
                warning = "지워진 사진들은 30일 이내에 '휴지통'에서 다시 복구할 수 있습니다.",
                onOkClicked = {
                    // 추가 작업 필요 시 여기에 작성
                }
            ).show(supportFragmentManager, "TrashDeletedDialog")
        }
    }

    private fun setupRecyclerView() {
        // 어댑터 생성. 선택된 개수가 바뀔 때마다 updateButtonLabels 호출
        adapter = TrashRVA { newSelectedCount ->
            selectedCount = newSelectedCount
            updateButtonLabels()
        }

        binding.rvTrashGallery.layoutManager = GridLayoutManager(this, 3)
        binding.rvTrashGallery.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun setupButtons() {
        // 삭제 버튼
        binding.tvTrashDelete.setOnClickListener {
            if (selectedCount > 0) {
                // 1) 선택된 사진만 삭제
                confirmDeleteSelectedImages()
            } else {
                // 2) 아무것도 선택되지 않은 경우 → 휴지통의 모든 사진 삭제
                deleteAllImages()
            }
        }

        // 복구 버튼
        binding.tvTrashRestore.setOnClickListener {
            if (selectedCount > 0) {
                // 1) 선택된 사진만 복구
                confirmRestoreSelectedImages()
            } else {
                // 2) 아무것도 선택되지 않은 경우 → 휴지통의 모든 사진 복구
                restoreAllImages()
            }
        }

        // 뒤로 가기
        binding.ivTrashBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun loadTrashedImages() {
        val trashedList = TrashRepository.getAllTrashed()
        adapter.submitList(trashedList)

        // 선택 초기화
        adapter.clearSelection()
        selectedCount = 0
        updateButtonLabels()
    }

    private fun updateButtonLabels() {
        if (selectedCount > 0) {
            binding.tvTrashDelete.text = "선택된 사진 삭제하기 ($selectedCount)"
            binding.tvTrashRestore.text = "선택된 사진 복구하기 ($selectedCount)"
        } else {
            binding.tvTrashDelete.text = "모든 사진 삭제하기"
            binding.tvTrashRestore.text = "모든 사진 복구하기"
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun confirmDeleteSelectedImages() {
        val selectedItems = adapter.getSelectedItems()
        if (selectedItems.isEmpty()) return

        val deleteDialog = TrashDeleteDialog(
            title = "선택된 사진 삭제하기",
            warning = "선택된 사진을 삭제하시겠습니까?",
            onCancel = {},
            onConfirm = {
                val mediaIdList = selectedItems.map { it.id.toString() } // mediaId 리스트 생성
                sweepViewModel.fetchDeleteTrashImage(TrashImageRequestModel(mediaIdList)) // API 호출

                deleteImagesWithPermission(selectedItems)
            },
            confirmButtonText = "삭제"
        )
        deleteDialog.show(supportFragmentManager, "TrashDeleteSelectedDialog")
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun deleteAllImages() {
        val allTrashed = TrashRepository.getAllTrashed()
        if (allTrashed.isEmpty()) {
            Toast.makeText(this, "휴지통이 비어있습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = TrashDeleteDialog(
            title = "모든 사진 삭제하기",
            warning = "정말 모든 사진을 삭제하시겠습니까?",
            onCancel = {},
            onConfirm = {
                val mediaIdList = allTrashed.map { it.id.toString() } // mediaId 리스트 생성
                sweepViewModel.fetchDeleteTrashImage(TrashImageRequestModel(mediaIdList)) // API 호출

                deleteImagesWithPermission(allTrashed)
            }
        )
        dialog.show(supportFragmentManager, "TrashDeleteDialog")
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun deleteImagesWithPermission(images: List<Gallery>) {
        pendingTrashGalleries.clear() // 대기열 초기화
        images.forEach { gallery ->
            try {
                deleteImageToMediaStoreTrash(gallery)
            } catch (e: RecoverableSecurityException) {
                pendingTrashGalleries.add(gallery) // 예외가 발생한 이미지를 대기열에 추가
                requestDeletePermission(e) // 예외 객체를 통해 권한 요청
            }
        }
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
                Log.d("TrashActivity", "Image moved to MediaStore trash: ${gallery.uri}")
            } else {
                Log.e("TrashActivity", "Failed to move image to MediaStore trash: ${gallery.uri}")
            }
        } catch (e: RecoverableSecurityException) {
            throw e // 예외를 호출자로 전달
        } catch (e: Exception) {
            Log.e("TrashActivity", "Error deleting image: ${gallery.uri}", e)
        }
    }

    private fun confirmRestoreSelectedImages() {
        val selectedItems = adapter.getSelectedItems()
        if (selectedItems.isEmpty()) return

        val restoreDialog = TrashDeleteDialog(
            title = "선택된 사진 복구하기",
            warning = "선택된 사진을 복구하시겠습니까?",
            onCancel = {
                // 취소 시 아무것도 안 함
            },
            onConfirm = {
                val mediaIdList = selectedItems.map { it.id.toString() } // Gallery의 id 사용
                sweepViewModel.fetchRestoreTrashImage(TrashImageRequestModel(mediaIdList))

                selectedItems.forEach { gallery ->
                    TrashRepository.removeFromTrash(gallery)
                }

                // 복구 후 리스트 갱신
                loadTrashedImages()

                // 복구 완료 다이얼로그 표시
                DeletedDialog(
                    message = "복구가 완료됐습니다!",
                    warning = "복구 된 사진을 다시 확인하실 수 있습니다.",
                    onOkClicked = {
                        // 확인 누르면 할 일 (없으면 빈 칸)
                    }
                ).show(supportFragmentManager, "TrashDeletedDialog")
            },
            confirmButtonText = "복구" // 확인 버튼 텍스트를 "복구"로 설정
        )
        restoreDialog.show(supportFragmentManager, "TrashRestoreSelectedDialog")
    }

    private fun restoreAllImages() {
        val allTrashed = TrashRepository.getAllTrashed()
        if (allTrashed.isEmpty()) {
            Toast.makeText(this, "휴지통이 비어있습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // 1) 복구 확인 다이얼로그 표시
        val restoreDialog = TrashDeleteDialog(
            title = "모든 사진 복구하기",
            warning = "휴지통의 모든 사진을 복구하시겠습니까?",
            onCancel = {
                // 취소 시 아무것도 안 함
            },
            onConfirm = {
                // 1) 모든 이미지의 mediaId 리스트 생성
                val mediaIdList = allTrashed.map { it.id.toString() }
                // 2) API 호출
                sweepViewModel.fetchRestoreTrashImage(TrashImageRequestModel(mediaIdList))

                // "복구" 누른 경우, 실제 복구 로직
                allTrashed.forEach { gallery ->
                    TrashRepository.removeFromTrash(gallery)
                    // SweepActivity에 다시 보여주기 로직
                    // 예: GalleryRepository.add(gallery) 등
                }

                // 복구 후 리스트 갱신
                loadTrashedImages()

                // 2) 복구 완료 다이얼로그 표시
                DeletedDialog(
                    message = "복구가 완료됐습니다!",
                    warning = "복구 된 사진을 다시 확인하실 수 있습니다.",
                    onOkClicked = {
                        // 확인 누르면 할 일 (없으면 빈 칸)
                    }
                ).show(supportFragmentManager, "TrashDeletedDialog")
            },
            confirmButtonText = "복구" // 확인 버튼 텍스트를 "복구"로 설정
        )
        restoreDialog.show(supportFragmentManager, "TrashRestoreDialog")
    }


    private fun setBackBtn() {
        binding.ivTrashBackBtn.setOnClickListener {
            finish()
        }
    }
}