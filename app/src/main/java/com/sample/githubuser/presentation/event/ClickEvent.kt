package com.sample.githubuser.presentation.event

sealed class ClickEvent {

    data class ItemClicked(val pos: Int): ClickEvent()

}
