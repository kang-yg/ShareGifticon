package com.kyg.sharegifticon.view

import android.view.View
import com.kyg.sharegifticon.Utile.getSameItem
import com.kyg.sharegifticon.model.ItemCategory
import com.kyg.sharegifticon.model.ItemGifticon
import com.kyg.sharegifticon.view.adapter.ViewPagerItemListAdapter
import com.kyg.sharegifticon.view.diff.MyDiffUtil
import com.kyg.sharegifticon.viewmodel.BottomSheetItemViewModel
import com.kyg.sharegifticon.viewmodel.MainActivityViewModel

class BottomSheetItem(
    private val activity: MainActivity,
    private val mainActivityViewModel: MainActivityViewModel
) {
    private val bottomSheetItemBinding = activity.getDataBinding().mainBottomSheetItem
    private val bottomSheetItemViewModel: BottomSheetItemViewModel =
        BottomSheetItemViewModel(mainActivityViewModel)
    private var viewPagerItemAdapter: ViewPagerItemListAdapter? = null

    fun setStorageRef(category: ItemCategory) {
        this.bottomSheetItemViewModel.setStorageRef(category)
        this.bottomSheetItemViewModel.readDataFromFirebaseDatabase()
    }

    private fun setItemList() {
        viewPagerItemAdapter = ViewPagerItemListAdapter(
            itemClickCallBack = {
                activity.showGifticonDetailFragment(it)
            },
            diffUtil = MyDiffUtil<ItemGifticon>().defaultDiffUtil
        )
        bottomSheetItemBinding.vpBottomSheetItem.adapter = viewPagerItemAdapter
        bottomSheetItemBinding.indicatorBottomSheetItem.setViewPager2(bottomSheetItemBinding.vpBottomSheetItem)
    }

    fun addItem(itemGifticon: ItemGifticon) {
        viewPagerItemAdapter?.let {
            val newItemList: HashSet<ItemGifticon> = hashSetOf()
            bottomSheetItemViewModel.itemList.value?.forEach { item ->
                newItemList.add(item)
            }
            val newItem = newItemList.getSameItem(itemGifticon)
            if (newItem == null) {
                newItemList.add(itemGifticon)
            }
            bottomSheetItemViewModel.itemList.postValue(newItemList)
        }
    }

    fun removeAt(itemGifticon: ItemGifticon) {
        viewPagerItemAdapter?.let {
            val newItemList: HashSet<ItemGifticon> = hashSetOf()
            bottomSheetItemViewModel.itemList.value?.forEach { item ->
                newItemList.add(item)
            }
            val newItem = newItemList.getSameItem(itemGifticon)
            if (newItem != null) {
                newItemList.remove(newItem)
            }
            bottomSheetItemViewModel.itemList.postValue(newItemList)
        }
    }

    fun playLottie() {
        with(bottomSheetItemBinding.bottomLottie) {
            visibility = View.VISIBLE
            playAnimation()
        }
    }

    private fun pauseLottie() {
        with(bottomSheetItemBinding.bottomLottie) {
            visibility = View.GONE
            pauseAnimation()
        }
    }

    private fun observeItemList() {
        bottomSheetItemViewModel.itemList.observe(activity) {
            if (it.isNullOrEmpty()) {
                bottomSheetItemBinding.ivEmpty.visibility = View.VISIBLE
                bottomSheetItemBinding.vpBottomSheetItem.visibility = View.GONE
                bottomSheetItemBinding.indicatorBottomSheetItem.visibility = View.GONE
            } else {
                setItemList()
                viewPagerItemAdapter?.submitList(it.toMutableList())
                bottomSheetItemBinding.ivEmpty.visibility = View.GONE
                bottomSheetItemBinding.vpBottomSheetItem.visibility = View.VISIBLE
                bottomSheetItemBinding.indicatorBottomSheetItem.visibility = View.VISIBLE
            }
            pauseLottie()
        }
    }

    init {
        observeItemList()
        bottomSheetItemBinding.bottomSheetItem = this
    }
}