package com.mmnuradityo.openrepo.data.model.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mmnuradityo.openrepo.data.model.repository.GithubRepository
import kotlinx.android.parcel.Parcelize

/**
 * Created on : 01/09/20
 * Author     : mmnuradityo
 * GitHub     : https://github.com/mmnuradityo
 */
@Parcelize
data class Search(

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean? = null,

	@field:SerializedName("items")
	val items: List<GithubRepository>? = null
) : Parcelable
