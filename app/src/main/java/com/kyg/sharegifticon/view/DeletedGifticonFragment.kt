package com.kyg.sharegifticon.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyg.sharegifticon.R
import com.kyg.sharegifticon.Utile.getSameItem
import com.kyg.sharegifticon.databinding.FragmentGifticonDeleteBinding
import com.kyg.sharegifticon.model.ItemGifticon
import com.kyg.sharegifticon.view.adapter.DeletedGifticonListAdapter
import com.kyg.sharegifticon.view.diff.MyDiffUtil
import com.kyg.sharegifticon.viewmodel.DeletedGifticonFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeletedGifticonFragment :
    ShareGifticonBaseFragment<FragmentGifticonDeleteBinding>(R.layout.fragment_gifticon_delete) {
    private val deletedGifticonFragmentViewModel: DeletedGifticonFragmentViewModel by viewModels()
    private var deletedGifticonListAdapter: DeletedGifticonListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRvDeletedGifticon()
        observeItemList()
        readDataFromFirebaseDatabase()
    }

    private fun readDataFromFirebaseDatabase() {
        startLottie()
        deletedGifticonFragmentViewModel.readDataFromFirebaseDatabase()
    }

    private fun initRvDeletedGifticon() {
        deletedGifticonListAdapter = DeletedGifticonListAdapter(
            itemClickCallBack = {
                (activity as MainActivity).showGifticonDetailFragment(it)
            },
            diffUtil = MyDiffUtil<ItemGifticon>().defaultDiffUtil
        )
        with(getDataBinding().rvDeletedGifticon) {
            adapter = deletedGifticonListAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }
    }

    fun addItem(itemGifticon: ItemGifticon) {
        deletedGifticonListAdapter?.let {
            val newItemList: HashSet<ItemGifticon> = hashSetOf()
            deletedGifticonFragmentViewModel.itemList.value?.forEach { item ->
                newItemList.add(item)
            }
            val newItem = newItemList.getSameItem(itemGifticon)
            if (newItem == null) {
                newItemList.add(itemGifticon)
            }
            deletedGifticonFragmentViewModel.itemList.postValue(newItemList)
        }
    }

    fun removeItem(itemGifticon: ItemGifticon) {
        deletedGifticonListAdapter?.let {
            val newItemList: HashSet<ItemGifticon> = hashSetOf()
            deletedGifticonFragmentViewModel.itemList.value?.forEach { item ->
                newItemList.add(item)
            }
            val newItem = newItemList.getSameItem(itemGifticon)
            if (newItem != null) {
                newItemList.remove(newItem)
            }
            deletedGifticonFragmentViewModel.itemList.postValue(newItemList)
        }
    }

    private fun startLottie() {
        with(getDataBinding().deletedLottie) {
            visibility = View.VISIBLE
            playAnimation()
        }
    }

    private fun pauseLottie() {
        with(getDataBinding().deletedLottie) {
            pauseAnimation()
            visibility = View.GONE
        }
    }

    private fun observeItemList() {
        deletedGifticonFragmentViewModel.itemList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                getDataBinding().ivEmpty.visibility = View.VISIBLE
                getDataBinding().rvDeletedGifticon.visibility = View.GONE
            } else {
                getDataBinding().ivEmpty.visibility = View.GONE
                getDataBinding().rvDeletedGifticon.visibility = View.VISIBLE
                deletedGifticonListAdapter?.submitList(it.toMutableList())
            }
            pauseLottie()
        }
    }
}