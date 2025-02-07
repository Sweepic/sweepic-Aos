package com.umc.sweepic.data.repositoryImpl.sweep

import android.util.Log
import com.umc.sweepic.data.datasource.MemoDataSource
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
        }.onFailure {Log.e("MemoRepositoryImpl", "API 실패: ${it.message}")  }

    override suspend fun fetchMemoFolderDetails(folderId: Long): Result<MemoFolderDetailModel> =
        runCatching {
            val response = memoDataSource.fetchMemoFolderDetails(folderId)
            response.success.toMemoFolderDetailModel()
        }
}
