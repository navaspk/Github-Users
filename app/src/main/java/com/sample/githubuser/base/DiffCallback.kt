package com.sample.githubuser.base

import androidx.recyclerview.widget.DiffUtil
import com.sample.githubuser.business.domain.model.ResponseItem

class DiffCallback : DiffUtil.ItemCallback<ResponseItem>() {

    override fun areItemsTheSame(oldItem: ResponseItem, newItem: ResponseItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ResponseItem, newItem: ResponseItem): Boolean {
        return oldItem == newItem
    }
}
