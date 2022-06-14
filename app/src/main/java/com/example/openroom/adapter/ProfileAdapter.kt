package com.example.openroom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.openroom.R
import com.example.openroom.databinding.ProfileItemBinding
import com.example.openroom.databinding.ProfileItemLogoutBinding
import com.example.openroom.model.ProfileItemModel

class ProfileAdapter(private val listener: Listener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val profileList = ArrayList<ProfileItemModel>()

    class ProfileHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ProfileItemBinding.bind(item)
        fun bind(profileItem: ProfileItemModel, listener: Listener) = with(binding)
        {
            textViewProfileItem.text = profileItem.title
            imageViewProfileItem.setImageResource(profileItem.photo)

            itemView.setOnClickListener {
                listener.onClick(profileItem)
            }
        }
    }

    class LogoutHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding = ProfileItemLogoutBinding.bind(item)
        fun bind(profileItem: ProfileItemModel, listener: Listener) = with(binding)
        {
            textViewLogout.text = profileItem.title
            imageViewLogout.setImageResource(profileItem.photo)

            itemView.setOnClickListener {
                listener.onClick(profileItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.profile_item -> ProfileHolder(inflater.inflate(viewType, parent, false))

            R.layout.profile_item_logout -> LogoutHolder(inflater.inflate(viewType, parent, false))

            else -> throw IllegalArgumentException("Unsupported layout") // in case populated with a model we don't know how to display.
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProfileHolder -> holder.bind(profileList[position], listener)
            is LogoutHolder -> holder.bind(profileList[position], listener)
        }
    }

    override fun getItemCount(): Int = profileList.size

    override fun getItemViewType(position: Int): Int {

        return when (profileList[position].type) {
            0 -> R.layout.profile_item
            1 -> R.layout.profile_item_logout

            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    interface Listener {
        fun onClick(profileItem: ProfileItemModel)
    }

    fun addProfile(profileItem: ProfileItemModel) {
        profileList.add(profileItem)
    }

    fun clearProfileList() {
        profileList.clear()
    }
}