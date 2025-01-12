package com.umc.sweepic.data.repositoryImpl.sweep

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
}