package com.umc.sweepic.data.repositoryImpl.sweep

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.umc.sweepic.data.datasource.GalleryDataSource
import com.umc.sweepic.domain.model.sweep.Gallery
import com.umc.sweepic.domain.repository.sweep.GalleryRepository
import javax.inject.Inject

class GalleryRepositoryImpl @Inject constructor(
    private val galleryDataSource: GalleryDataSource,
): GalleryRepository {
    override fun getGalleryImagePagingSource(): PagingSource<Int, Gallery> {
        return object: PagingSource<Int, Gallery>() {
            override fun getRefreshKey(state: PagingState<Int, Gallery>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    val anchorPage = state.closestPageToPosition(anchorPosition)
                    anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                }
            }
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gallery> {
                try {
                    val pageNumber = params.key ?: 0
                    val response = galleryDataSource.fetchGalleryImages(params.loadSize, pageNumber*params.loadSize)
                    return LoadResult.Page(
                        data = response.map { it.toGallery() },
                        prevKey = if(pageNumber==0) null else pageNumber-1,
                        nextKey = if(response.isEmpty()) null else pageNumber+1
                    )
                } catch (e: Exception) {
                    // TODO("error process")
                    throw e
                }
            }
        }
    }
    override fun getAllGalleryImagesDesc(): List<Gallery> {
        val galleryModels = galleryDataSource.fetchGalleryImages(
            limit = Int.MAX_VALUE,
            offset = 0
        )
        return galleryModels.map { it.toGallery() }
    }

    override fun getTrashedImagesPagingSource(): PagingSource<Int, Gallery> {
        return object : PagingSource<Int, Gallery>() {
            // Refresh 시 사용할 키를 결정
            override fun getRefreshKey(state: PagingState<Int, Gallery>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    val anchorPage = state.closestPageToPosition(anchorPosition)
                    anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
                }
            }
            // 페이지 로딩
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gallery> {
                try {
                    Log.d("PagingSource", "Load params: $params")
                    val pageNumber = params.key ?: 0
                    val response = galleryDataSource.fetchTrashedImages(
                        limit = params.loadSize,
                        offset = pageNumber * params.loadSize
                    )
                    Log.d("PagingSource", "Fetched trashed images: ${response.size}")
                    return LoadResult.Page(
                        data = response.map { it.toGallery() },
                        prevKey = if(pageNumber==0) null else pageNumber-1,
                        nextKey = if(response.isEmpty()) null else pageNumber+1
                    )
                } catch (e: Exception) {
                    // TODO("error process")
                    throw e
                }
            }
        }
    }

    override fun moveToTrash(uri: Uri): Boolean {
        return galleryDataSource.moveToTrash(uri) // DataSource의 moveToTrash 호출
    }
}