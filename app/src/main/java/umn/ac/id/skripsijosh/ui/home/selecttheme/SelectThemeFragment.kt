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
    private var selectedTheme = ""
    private var mCallback: SelectThemeListener? = null

    companion object {
        const val TAG = "SelectThemeFragment"
        fun getInstance(callback: SelectThemeListener) : SelectThemeFragment {
            val fragment = SelectThemeFragment()
            fragment.mCallback = callback
            return fragment
        }
    }

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
        selectedTheme = sharedPreferences.getString("theme_applied", "").toString()

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putString("theme_applied", selectedTheme)
            editor.apply()
            mCallback?.onChangeThemeSuccess(selectedTheme)
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
        binding.rvTheme.layoutManager = GridLayoutManager(requireActivity(), 3)

        adapter.onItemClick = {
            selectedTheme = it
        }

        val spacing = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp) / 2
        binding.rvTheme.apply {
            setPadding(spacing, spacing, spacing, spacing)
            clipToPadding = false
            clipChildren = false
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
        fun onChangeThemeSuccess(themeId: String)
    }
}