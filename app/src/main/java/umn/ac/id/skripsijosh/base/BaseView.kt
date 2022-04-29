package umn.ac.id.skripsijosh.base

interface BaseView {
    fun startLoading()
    fun stopLoading()
    fun showError(message:String)
    fun showEmpty()
}