package com.kyg.sharegifticon.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.kyg.sharegifticon.R
import com.kyg.sharegifticon.databinding.ActivityMainBinding
import com.kyg.sharegifticon.model.Category
import com.kyg.sharegifticon.model.ItemCategory
import com.kyg.sharegifticon.model.ItemGifticon
import com.kyg.sharegifticon.view.adapter.ItemCategoryAdapter
import com.kyg.sharegifticon.viewmodel.BaseViewModel
import com.kyg.sharegifticon.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ShareGifticonBaseActivity<ActivityMainBinding>() {
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private lateinit var bottomSheetItem: BottomSheetItem
    private lateinit var drawerMenu: DrawerMenu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActivity()
        initToolbar()
        initItemCategoryList()
        initItemCategoryAdapter()
    }

    private fun initActivity() {
        initDataBinding(R.layout.activity_main)
        bottomSheetItem =
            BottomSheetItem(
                activity = this@MainActivity,
                mainActivityViewModel = mainActivityViewModel
            )
        drawerMenu =
            DrawerMenu(
                activity = this@MainActivity,
                mainActivityViewModel = mainActivityViewModel
            )
    }

    private fun initToolbar() {
        getDataBinding().mainAppBar.ivToolbarMenu.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                getDataBinding().dlMain.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun initItemCategoryList() {
        with(BaseViewModel.itemCategoryList) {
            clear()
            add(ItemCategory(getString(R.string.CATEGORY_CAFE), Category.CAFE))
            add(
                ItemCategory(
                    getString(R.string.CATEGORY_CONVENIENCE_STORE),
                    Category.CONVENIENCE_STORE
                )
            )
            add(ItemCategory(getString(R.string.CATEGORY_ETC), Category.ETC))
        }
    }

    private fun initItemCategoryAdapter() {
        val itemCategoryAdapter = ItemCategoryAdapter {
            bottomSheetItem.setStorageRef(it)
            bottomSheetItem.playLottie()
        }.apply {
            submitList(BaseViewModel.itemCategoryList)
        }
        with(getDataBinding().rvItemCategory) {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = itemCategoryAdapter
        }
    }

    fun showGifticonDetailFragment(itemGifticon: ItemGifticon) {
        val gifticonDetailFragment = GifticonDetailFragment.instance(itemGifticon)
        supportFragmentManager.beginTransaction()
            .add(R.id.flMain, gifticonDetailFragment).addToBackStack(null).commit()
    }

    fun bottomGifticonAddItem(itemGifticon: ItemGifticon) {
        bottomSheetItem.addItem(itemGifticon)
    }

    fun bottomGifticonRemoveItem(itemGifticon: ItemGifticon) {
        bottomSheetItem.removeAt(itemGifticon)
    }

    fun deletedGifticonAddItem(itemGifticon: ItemGifticon) {
        drawerMenu.deletedGifticonFragment.addItem(itemGifticon)
    }

    fun deletedGifticonRemoveItem(itemGifticon: ItemGifticon) {
        drawerMenu.deletedGifticonFragment.removeItem(itemGifticon)
    }
}