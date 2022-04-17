package com.example.skripsijosh.base

interface BaseView {
    fun startLoading()
    fun stopLoading()
    fun showError(message:String)
    fun showEmpty()
}