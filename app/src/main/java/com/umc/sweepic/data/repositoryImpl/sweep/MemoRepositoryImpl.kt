package com.umc.sweepic.data.repositoryImpl.sweep

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
            val response = memoDataSource.searchMemos(keyword)
            response.success.toRecordMemoListModel()
        }

    override suspend fun fetchMemoFolderDetails(folderId: Long): Result<MemoFolderDetailModel> =
        runCatching {
            val response = memoDataSource.fetchMemoFolderDetails(folderId)
            response.success.toMemoFolderDetailModel()
        }
}
