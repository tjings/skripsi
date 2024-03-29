package umn.ac.id.skripsijosh.ui.shop

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.recyclerview.widget.GridLayoutManager
import com.skydoves.balloon.*
import com.skydoves.balloon.overlay.BalloonOverlayRect
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.base.BaseActivity
import umn.ac.id.skripsijosh.databinding.ActivityShopBinding
import umn.ac.id.skripsijosh.pojo.ShopItem
import umn.ac.id.skripsijosh.pojo.UserBalance
import umn.ac.id.skripsijosh.pojo.UserInventory
import umn.ac.id.skripsijosh.utils.DialogUtil
import umn.ac.id.skripsijosh.utils.Util

class ShopActivity : BaseActivity(), ShopView {
    private lateinit var binding: ActivityShopBinding
    private lateinit var presenter: ShopPresenter
    private var userLevel = 0
    private var inventory: MutableList<String> = arrayListOf()
    private var itemList: MutableList<ShopItem> = arrayListOf()
    private var userBalance = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = ShopPresenter(this)
        presenter.getItemList()
        presenter.getUserBalance()
        presenter.getInventoryList()

        binding.include2.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.include2.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        userLevel = sharedPreferences.getInt("level", 0)
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

        Balloon.Builder(this)
            .setArrowSize(10)
            .setText(getString(R.string.tooltip_shop))
            .setTextSize(15f)
            .setTextGravity(Gravity.START)
            .setArrowOrientation(ArrowOrientation.TOP)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setWidth(BalloonSizeSpec.WRAP)
            .setHeight(BalloonSizeSpec.WRAP)
            .setPadding(6)
            .setMarginRight(8)
            .setCornerRadius(0f)
            .setOverlayShape(BalloonOverlayRect)
            .setBackgroundColor(getColor(R.color.col_def))
            .setBalloonAnimation(BalloonAnimation.CIRCULAR)
            .setAutoDismissDuration(5000L)
            .build()
            .showAlignBottom(binding.tvBalance)
    }

    override fun showSuccess() {
        DialogUtil(this).showSuccess(getString(R.string.purchase_success))
    }

    override fun onGetInventorySuccess(result: UserInventory) {
        if (Util.isNotNull(result.itemHave)) {
            inventory.addAll(result.itemHave!!)
        } else {
            inventory.add("circle")
        }
    }

    private fun initAdapter() {
        val adapter = ShopAdapter(itemList, inventory, userLevel, this)
        binding.rvShop.adapter = adapter
        binding.rvShop.layoutManager = GridLayoutManager(this, 2)

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
                            val pointDeducted = -it.itemPrice
                            presenter.purchaseItem(it.itemId, pointDeducted)
                            mDialog?.dismiss()
                        } else {
                            DialogUtil(this@ShopActivity).showFailed()
                        }
                    }

                    override fun onNegative() {}
                })
        }
    }

    override fun startLoading() {
        if (checkIfActivityFinished()) return
        showLoadingProgress()
    }

    override fun stopLoading() {
        if (checkIfActivityFinished()) return
        dismissLoading()
    }

    override fun showError(message: String) {
        if (checkIfActivityFinished()) {
            return
        }
        Log.d("errorShop", message)
    }

    override fun showEmpty() {}

}