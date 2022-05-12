package com.kyg.sharegifticon.view

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.kyg.sharegifticon.Constants
import com.kyg.sharegifticon.R
import com.kyg.sharegifticon.databinding.FragmentGifticonDetailBinding
import com.kyg.sharegifticon.model.GifticonStatus
import com.kyg.sharegifticon.model.ItemGifticon
import com.kyg.sharegifticon.viewmodel.GifticonDetailFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class GifticonDetailFragment :
    ShareGifticonBaseFragment<FragmentGifticonDetailBinding>(R.layout.fragment_gifticon_detail) {
    private val gifticonDetailFragmentViewModel: GifticonDetailFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initAppbar()
        initIvGifticon()
        initTvExpirationDate()
        initTvRegistered()
        initBtSaveImage()
    }

    private fun initData() {
        arguments.let {
            gifticonDetailFragmentViewModel.itemGifticon.value =
                it?.getParcelable(Constants.parcelableItemGifticon)
        }
    }

    private fun initAppbar() {
        with(getDataBinding().gifticonDetailAppbar.ivToolbarDeleteOrRestore) {
            visibility = View.VISIBLE
        }
        observeItemGifticon()
    }

    private fun initIvGifticon() {
        gifticonDetailFragmentViewModel.itemGifticon.value?.let { item ->
            Glide.with(this).load(item.imageSrc).fitCenter().into(getDataBinding().ivGifticon)
        }
    }

    private fun initTvExpirationDate() {
        gifticonDetailFragmentViewModel.itemGifticon.value?.let { item ->
            val expirationDate =
                String.format("%s: ~%s", getString(R.string.EXPIRATION_DATE), item.expirationDate)
            getDataBinding().tvExpirationDate.text = expirationDate
        }
    }

    private fun initTvRegistered() {
        gifticonDetailFragmentViewModel.itemGifticon.value?.let { item ->
            val registrant =
                String.format("%s: %s", getString(R.string.REGISTRANT), item.registrant)
            getDataBinding().tvRegistered.text = registrant
        }
    }

    private fun initBtSaveImage() {
        getDataBinding().btSaveImage.setOnClickListener {
            gifticonDetailFragmentViewModel.itemGifticon.value?.let { item ->
                CoroutineScope(Dispatchers.IO).launch {
                    gifticonDetailFragmentViewModel.setBitMap()
                    saveImageToOverQ()
                    withContext(Dispatchers.Main) {
                        (activity as MainActivity).showToast(getString(R.string.DOWN_LOAD_COMPLETE))
                    }
                }
            }
        }
    }

    private fun saveImageToOverQ() {
        var fos: OutputStream?
        gifticonDetailFragmentViewModel.itemGifticon.value?.let { item ->
            val filename = "${item.key}.jpg"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                activity?.contentResolver?.also { resolver ->
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, item.key)
                        put(MediaStore.MediaColumns.MIME_TYPE, Constants.mime_image)
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }
                    val imageUri: Uri? =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    fos = imageUri?.let { resolver.openOutputStream(it) }
                    fos?.use {
                        gifticonDetailFragmentViewModel.mBitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            100,
                            it
                        )
                    }
                }
            } else {
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, filename)
                fos = FileOutputStream(image)
            }
        }
    }

    private fun observeItemGifticon() {
        val ivToolbarDeleteOrRestore =
            getDataBinding().gifticonDetailAppbar.ivToolbarDeleteOrRestore
        gifticonDetailFragmentViewModel.itemGifticon.observe(viewLifecycleOwner) { item ->
            with(ivToolbarDeleteOrRestore) {
                if (item.status == GifticonStatus.AVAILABLE) {
                    setImageResource(R.drawable.ic_baseline_delete_24)
                    setOnClickListener { view ->
                        gifticonDetailFragmentViewModel.deleteGifticon(item)
                        item.status = GifticonStatus.DELETE
                        (activity as MainActivity).deletedGifticonAddItem(item)
                        (activity as MainActivity).bottomGifticonRemoveItem(item)
                    }
                } else {
                    setImageResource(R.drawable.ic_baseline_restore_24)
                    setOnClickListener { view ->
                        gifticonDetailFragmentViewModel.restoreGifticon(item)
                        item.status = GifticonStatus.AVAILABLE
                        (activity as MainActivity).deletedGifticonRemoveItem(item)
                        (activity as MainActivity).bottomGifticonAddItem(item)
                    }
                }
            }
        }
    }

    companion object {
        fun instance(itemGifticon: ItemGifticon): GifticonDetailFragment {
            return GifticonDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.parcelableItemGifticon, itemGifticon)
                }
            }
        }
    }
}