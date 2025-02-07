package com.umc.sweepic.data.repositoryImpl.sweep

import android.util.Log
import com.umc.sweepic.data.datasource.MemoDataSource
import com.umc.sweepic.data.dto.request.MoveImagesRequestDto
import com.umc.sweepic.domain.model.MemoFolderDetailModel
import com.umc.sweepic.domain.model.RecordMemoListModel
import com.umc.sweepic.domain.repository.sweep.MemoRepository
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoDataSource: MemoDataSource
) : MemoRepository {

    override suspend fun recordMemoList(): Result<RecordMemoListModel> = runCatching {
        val response = memoDataSource.recordMemoList()
        response.success.toRecordMemoListModel() // 변환된 데이터 반환
    }

    override suspend fun searchMemos(keyword: String): Result<RecordMemoListModel> =
        runCatching {
            Log.d("MemoRepositoryImpl", "API 요청: searchMemo($keyword)")
            val response = memoDataSource.searchMemos(keyword)
            Log.d("MemoRepositoryImpl", "API 응답: $response")
            response.success.toRecordMemoListModel()
        }.onFailure { Log.e("MemoRepositoryImpl", "API 실패: ${it.message}") }

    override suspend fun fetchMemoFolderDetails(folderId: Long): Result<MemoFolderDetailModel> =
        runCatching {
            val response = memoDataSource.fetchMemoFolderDetails(folderId)
            response.success.toMemoFolderDetailModel()
        }

    override suspend fun deleteMemoFolder(folderId: Long): Result<Unit> =
        runCatching {
            Log.d("MemoRepositoryImpl", "API 요청: deleteMemoFolder($folderId)")
            val response = memoDataSource.deleteMemoFolder(folderId)

            if (response.resultType == "SUCCESS") {
                Log.d("MemoRepositoryImpl", "폴더 삭제 성공: $folderId")
                Unit // 성공 시 `Unit` 반환
            } else {
                val errorMessage = response.error?.let {
                    it.toString() // `error` 객체를 문자열로 변환
                } ?: "알 수 없는 오류"

                throw Exception("폴더 삭제 실패: $errorMessage")
            }
        }.onFailure { Log.e("MemoRepositoryImpl", "폴더 삭제 오류: ${it.message}") }
//
//    override suspend fun deleteImages(folderId: Long, imageIds: List<Long>): Result<Unit> =
//        runCatching {
//            val requestDto = DeleteImagesRequestDto(imageIds)
//            val response = memoDataSource.deleteImages(folderId, imageIds)
//            Log.d("MemoRepositoryImpl", "사진 삭제 응답: $response")
//
//            if (response.resultType == "SUCCESS") {
//                Log.d("MemoRepositoryImpl", "사진 삭제 성공")
//                Unit // ✅ 반환 타입을 `Unit`으로 변경
//            } else {
//                val errorMessage = response.error?.toString() ?: "알 수 없는 오류"
//                throw Exception("사진 삭제 실패: $errorMessage")
//            }
//        }.onFailure { Log.e("MemoRepositoryImpl", "사진 삭제 오류: ${it.message}") }

    override suspend fun moveImages(folderId: Long, targetFolderId: Long, imageIds: List<Long>): Result<Unit> =
        runCatching {
            val requestDto = MoveImagesRequestDto(targetFolderId, imageIds)
            val response = memoDataSource.moveImages(folderId, requestDto)

            Log.d("MemoRepositoryImpl", "사진 이동 요청: folderId=$folderId, targetFolderId=$targetFolderId, imageIds=$imageIds")
            Log.d("MemoRepositoryImpl", "사진 이동 응답: $response")

            if (response.resultType == "SUCCESS") {
                Unit
            } else {
                val errorMessage = response.error?.toString() ?: "알 수 없는 오류"
                throw Exception("사진 이동 실패: $errorMessage")
            }
        }.onFailure { Log.e("MemoRepositoryImpl", "사진 이동 오류: ${it.message}") }
}


