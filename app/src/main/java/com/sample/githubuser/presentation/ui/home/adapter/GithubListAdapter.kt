package com.sample.githubuser.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.sample.github.databinding.ItemEachUserBinding
import com.sample.github.databinding.ItemEachUserGridBinding
import com.sample.githubuser.GithubApp
import com.sample.githubuser.base.BaseViewHolder
import com.sample.githubuser.base.DiffCallback
import com.sample.githubuser.business.domain.model.ResponseItem
import com.sample.githubuser.presentation.event.ClickEvent

/**
 * Adapter class which create the view for the item
 */
class GithubListAdapter(
    private val event: (ClickEvent) -> Unit
) : PagedListAdapter<ResponseItem, BaseViewHolder<ResponseItem>>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ResponseItem> {

        return when (GithubApp.instance.isListView) {
            true -> GithubListViewHolder(
                ItemEachUserBinding.inflate(LayoutInflater.from(parent.context)), event
            )

            else -> GithubListViewHolderGrid(
                ItemEachUserGridBinding.inflate(LayoutInflater.from(parent.context)), event
            )
        }

    }

    override fun onBindViewHolder(holder: BaseViewHolder<ResponseItem>, position: Int) {
        val userItem = getItem(position)
        holder.apply {
            holder.bindView(userItem!!)
            itemView.tag = userItem
        }
    }

}
