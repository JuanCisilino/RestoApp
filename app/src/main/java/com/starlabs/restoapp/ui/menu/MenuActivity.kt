package com.starlabs.restoapp.ui.menu

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.starlabs.restoapp.R
import com.starlabs.restoapp.ui.adapters.ItemAdapter
import com.starlabs.restoapp.databinding.ActivityMenuBinding
import com.starlabs.restoapp.helpers.LoadingDialog
import com.starlabs.restoapp.model.MenuItem
import com.starlabs.restoapp.ui.abm.ABMActivity
import com.starlabs.restoapp.ui.extentions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuActivity : AppCompatActivity() {

    private val viewModel by viewModels<MenuViewModel>()
    private lateinit var binding: ActivityMenuBinding
    private var loadingDialog = LoadingDialog()
    companion object {
        fun start(activity: Activity){
            activity.startActivity(Intent(activity, MenuActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog.show(supportFragmentManager)
        viewModel.init(this)
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.menuLiveData.observe(this) { handleMenuItems(it) }
    }

    private fun handleMenuItems(itemList: List<MenuItem>?) {
        loadingDialog.dismiss()
        itemList
            ?.let { setAdapter(it) }
            ?:run { showToast(this, getString(R.string.error_data)) }
    }

    private fun setAdapter(itemList: List<MenuItem>) {
        val adapter = ItemAdapter(itemList)
        with(binding){
            menuItemListrecyclerView.layoutManager = GridLayoutManager(this@MenuActivity, 2)
            menuItemListrecyclerView.adapter = adapter
        }
        if (viewModel.isAdmin()){
            adapter.onItemMenuClickCallback = { ABMActivity.start(this, it.itemId!!) }
        }
    }
}