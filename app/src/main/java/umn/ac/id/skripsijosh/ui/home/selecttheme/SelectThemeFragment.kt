package umn.ac.id.skripsijosh.ui.home.selecttheme

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import umn.ac.id.skripsijosh.R
import umn.ac.id.skripsijosh.base.BaseDialogFragment
import umn.ac.id.skripsijosh.databinding.FragmentSelectThemeBinding
import umn.ac.id.skripsijosh.pojo.UserInventory

class SelectThemeFragment : BaseDialogFragment(), SelectThemeView {
    private lateinit var presenter: SelectThemePresenter
    private lateinit var binding: FragmentSelectThemeBinding
    private var itemList: MutableList<String> = arrayListOf()
    private var mCallback: SelectThemeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSelectThemeBinding.inflate(layoutInflater)
        binding.cvTheme.radius = resources.getDimension(com.intuit.sdp.R.dimen._8sdp)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        presenter = SelectThemePresenter(this)

        presenter.getItemList()

        binding.btnCancel.setOnClickListener {
            mCallback?.onDismissed()
            dismiss()
        }
        return binding.root
    }

    override fun onGetInventorySuccess(result: UserInventory) {
        itemList.clear()
        itemList = result.itemHave!!
        initAdapter()
    }

    private fun initAdapter() {
        val adapter = SelectThemeAdapter(itemList, requireActivity())
        binding.rvTheme.adapter = adapter
        binding.rvTheme.layoutManager = GridLayoutManager(requireActivity(), 2)

        val spacing = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp) / 2
        binding.rvTheme.apply {
            setPadding(spacing, spacing, spacing, spacing)
            clipToPadding = false
            clipChildren = false
        }
        adapter.onItemClick = {
            mCallback?.onChangeThemeSuccess(it)
        }
    }

    override fun startLoading() {
        if (checkIfFragmentNotAttachToActivity()) return
        showLoadingProgressOnly()
    }

    override fun stopLoading() {
        if (checkIfFragmentNotAttachToActivity()) return
        dismissLoadingProgress()
    }

    override fun showError(message: String) {}

    override fun showEmpty() {}

    interface SelectThemeListener {
        fun onDismissed()
        fun onChangeThemeSuccess(themeId: String)
    }

}