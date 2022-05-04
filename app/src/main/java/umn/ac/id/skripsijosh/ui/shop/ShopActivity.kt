package umn.ac.id.skripsijosh.ui.shop

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.base.BaseActivity
import umn.ac.id.skripsijosh.databinding.ActivityShopBinding
import umn.ac.id.skripsijosh.pojo.ShopItem
import umn.ac.id.skripsijosh.pojo.UserBalance
import umn.ac.id.skripsijosh.utils.DialogUtil
import umn.ac.id.skripsijosh.utils.Util


class ShopActivity : BaseActivity(), ShopView {
    private lateinit var binding: ActivityShopBinding
    private lateinit var presenter: ShopPresenter
    private var itemList: MutableList<ShopItem> = arrayListOf()
    private var userBalance = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = ShopPresenter(this)
        presenter.getItemList()
        presenter.getUserBalance()

        binding.include2.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.include2.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.include2.tvToolbarTitle.text = getString(R.string.navigation_shop)

    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount == 0) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onGetItemSuccess(result: MutableList<ShopItem>) {
        itemList.clear()
        itemList.addAll(result)
        initAdapter()
    }

    override fun onGetBalanceSuccess(result: UserBalance) {
        userBalance = result.balance
        binding.tvBalance.text = String.format("$userBalance P")
    }

    private fun initAdapter() {
        val adapter = ShopAdapter(itemList)
        binding.rvShop.adapter = adapter
        binding.rvShop.layoutManager = GridLayoutManager(this, 3)

        val spacing = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp) / 2
        binding.rvShop.apply {
            setPadding(spacing, spacing, spacing, spacing)
            clipToPadding = false
            clipChildren = false
        }
        adapter.onItemClick = {
            DialogUtil(this).showConfirmationDialog(
                it.itemName,
                callback = object : DialogUtil.DialogConfirmationCallback {
                    override fun onPositive() {
                        if (it.itemPrice <= userBalance) {
                            Log.d("SHOP", "purchased item")
                            val pointDeducted = -it.itemPrice
                            presenter.purchaseItem(it.itemId, pointDeducted)
                            mDialog?.dismiss()
                        } else {
                            Log.d("SHOP", "purchase failed")
                            DialogUtil(this@ShopActivity).showFailed()
                        }
                    }

                    override fun onNegative() {}
                })
        }
    }

    override fun startLoading() {
        showLoadingProgress()
    }

    override fun stopLoading() {
        dismissLoading()
    }

    override fun showError(message: String) {}

    override fun showEmpty() {}

}