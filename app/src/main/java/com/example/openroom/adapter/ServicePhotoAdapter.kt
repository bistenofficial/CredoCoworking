package com.example.openroom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.openroom.R
import com.example.openroom.databinding.ServiceItemBinding
import com.example.openroom.databinding.ServicePhotoItemBinding

class ServicePhotoAdapter : RecyclerView.Adapter<ServicePhotoAdapter.Holder>() {
    private val imageDrawable = ArrayList<Int>()

    class Holder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ServicePhotoItemBinding.bind(item)
        fun bind(intImg: Int) = with(binding)
        {
            imageViewPhoto.setImageResource(intImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.service_photo_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(imageDrawable[position])
    }

    override fun getItemCount() = imageDrawable.size

    fun addPhoto(image: Int) {
        imageDrawable.add(image)
    }

    fun clearPhotoList() {
        imageDrawable.clear()
    }
}