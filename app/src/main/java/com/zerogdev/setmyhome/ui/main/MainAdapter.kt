package com.zerogdev.setmyhome.ui.main

import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zerogdev.setmyhome.R
import com.zerogdev.setmyhome.data.LocationData
import com.zerogdev.setmyhome.data.PreferenceProvider
import com.zerogdev.setmyhome.receiver.AlarmReceiver
import com.zerogdev.setmyhome.rx.AutoClearedDisposable
import com.zerogdev.setmyhome.ui.register.RegisterModeOutActivity
import com.zerogdev.setmyhome.util.RingerUtil
import com.zerogdev.setmyhome.util.cancelAlarm
import com.zerogdev.setmyhome.util.startAlarm
import kotlinx.android.synthetic.main.item_add.view.*
import kotlinx.android.synthetic.main.item_main.view.*
import kotlinx.android.synthetic.main.item_mode_out.view.*

class MainAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_MODE_OUT : Int   = 0
        const val TYPE_ADD : Int        = 1
        const val TYPE_ITEM : Int       = 2
    }

    private var items : MutableList<LocationData> = mutableListOf()
    private var listener: ItemClickListener ?= null

    var modeOut: Int = -1
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    lateinit var preferenceProvider: PreferenceProvider


    override fun getItemCount(): Int {
            return items.size + 2
    }


    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_MODE_OUT
        } else if (itemCount - 1 == position) {
            return TYPE_ADD
        }
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_MODE_OUT) {
            return ModeOutHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_mode_out, parent, false))
        } else if (viewType == TYPE_ADD) {
            return AddItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_add, parent, false))
        }
        return MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ModeOutHolder ->
                preferenceProvider.modeOut.let { mode ->
                if (modeOut == -1) {
                    holder.modeOut.text = RingerUtil.getModeText(RingerUtil(holder.itemView.context).ringermode)
                } else {
                    holder.modeOut.text = RingerUtil.getModeText(modeOut)
                }
                holder.itemView.setOnClickListener {
                    it.context.startActivity(Intent(it.context, RegisterModeOutActivity::class.java))
                }
            }
            is MainHolder -> items[position - 1].let { data ->
                with(holder) {
                    registerName.text = data.name
                    registerAddress.text = data.address
                    registerModeIn.text = RingerUtil.getModeText(data.modeIn)
                    registerOnOff.isChecked = data.onOff
                    registerOnOff.setOnCheckedChangeListener { _, b ->
                        listener?.onItemOnOffClick(data, b)
                    }
                }
            }
            is AddItemHolder -> with(holder.addItemBtn) {
                setOnClickListener {
                    listener?.onAddClick()
                }
            }
        }
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }


    fun setItems(items: List<LocationData>) {
        this.items = items.toMutableList()
    }

    fun getItems(): List<LocationData> =
            items

    class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val registerName = itemView.registerName
        val registerAddress = itemView.registerAddress
        val registerModeIn = itemView.registerInMode
        val registerOnOff= itemView.registerOnOff
    }

    class AddItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addItemBtn = itemView.addItem
    }

    class ModeOutHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val modeOut = itemView.modeOut
    }

    interface ItemClickListener {
        fun onItemClick(locationData: LocationData)
        fun onItemOnOffClick(locationData: LocationData, checked: Boolean)
        fun onAddClick()
    }


}