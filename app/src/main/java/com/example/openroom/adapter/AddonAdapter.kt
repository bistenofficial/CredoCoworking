package com.example.openroom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.openroom.R
import com.example.openroom.databinding.AddonItemBinding
import com.example.openroom.databinding.ServiceItemBinding
import com.example.openroom.model.AddonModel
import com.example.openroom.model.ServiceModel

class AddonAdapter : RecyclerView.Adapter<AddonAdapter.AddonHolder>() {
    private val addonList = ArrayList<AddonModel>()

    class AddonHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = AddonItemBinding.bind(item)
        fun bind(addonModel: AddonModel) = with(binding) {
            imageViewAddon.setImageResource(addonModel.image)
            textViewAddonName.text = addonModel.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddonHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AddonHolder(inflater.inflate(R.layout.addon_item,parent,false))
    }
    override fun onBindViewHolder(holder: AddonHolder, position: Int) {
        holder.bind(addonList[position])
    }
    override fun getItemCount(): Int = addonList.size

    fun addAddon(addonModel: AddonModel) {
        addonList.add(addonModel)
    }
}