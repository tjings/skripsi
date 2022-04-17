package com.example.skripsijosh.ui.home

import com.example.skripsijosh.base.BaseView

interface HomeView : BaseView {
    fun onAddWaterSuccess()
    fun onLoadDataSuccess(result: Int)
}