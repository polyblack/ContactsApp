package com.polyblack.domain.entities

data class ContactState(val isLoading: Boolean, val error: Throwable?, val data: Contact?)
