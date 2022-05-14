package com.sample.githubuser.presentation.ui.home.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sample.core.extensions.safeGet
import com.sample.github.R
import com.sample.github.databinding.ItemEachUserGridBinding
import com.sample.githubuser.base.BaseViewHolder
import com.sample.githubuser.business.domain.model.ResponseItem
import com.sample.githubuser.presentation.event.ClickEvent

/**
 * View holder class bind the data to the view
 */
class GithubListViewHolderGrid(
    private val recyclerBinding: ItemEachUserGridBinding,
    event: (ClickEvent) -> Unit
) : BaseViewHolder<ResponseItem>(event, recyclerBinding.root) {


    // region OVERRIDDEN

    override fun bindView(item: ResponseItem) {
        loadUserImage(item)
        showHeadingSnippetAndDate(item)
    }

    // endregion


    // region UTIL

    private fun loadUserImage(item: ResponseItem) {
        if (item.avatarUrl?.isNotEmpty() == true) {
            Glide.with(recyclerBinding.userImages.context)
                .load(item.avatarUrl)
                .centerCrop()
                .error(R.drawable.background_gradient)
                .placeholder(R.drawable.background_gradient)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(recyclerBinding.userImages)
        } else {
            setDefaultImage()
        }

    }

    private fun setDefaultImage() {
        recyclerBinding.userImages.setImageResource(R.drawable.background_gradient)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun showHeadingSnippetAndDate(item: ResponseItem) {
        recyclerBinding.apply {
            nameTextView.text = item.login.safeGet().replaceFirstChar { it.uppercase() }
        }
    }

    // endregion

}
