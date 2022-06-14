package com.example.openroom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.openroom.R
import com.example.openroom.databinding.ServiceItemBinding
import com.example.openroom.model.ServiceModel

class ServiceAdapter(private val listener: Listener) :
    RecyclerView.Adapter<ServiceAdapter.ServiceHolder>() {
    private val serviceList = ArrayList<ServiceModel>()

    class ServiceHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ServiceItemBinding.bind(item)
        fun bind(serviceModel: ServiceModel, listener: Listener) = with(binding)
        {
            imageViewService.setImageResource(serviceModel.imageId)
            textViewServiceItem.text = serviceModel.title

            itemView.setOnClickListener {
                listener.onClick(serviceModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.service_item, parent, false)
        return ServiceHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceHolder, position: Int) {
        holder.bind(serviceList[position], listener)
    }

    override fun getItemCount() = serviceList.size

    fun addService(service: ServiceModel) {
        serviceList.add(service)
    }

    fun clearServiceList() {
        serviceList.clear()
    }

    interface Listener {
        fun onClick(service: ServiceModel)
    }
}
