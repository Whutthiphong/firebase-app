package com.example.foodorderapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapplication.R
import com.example.foodorderapplication.interfaces.IOnItemClickListener
import com.example.foodorderapplication.models.Events

class ListEventAdapter(private var context: Context,private var iOnItemClickListener: IOnItemClickListener) : RecyclerView.Adapter<ListEventAdapter.EventViewHolder>() {
    private  var listEvents: List<Events> = ArrayList()
    private lateinit var views:View
    fun setListItem(listEvents: List<Events>){
        this.listEvents = listEvents
        notifyDataSetChanged()
    }
    class EventViewHolder(itemView: View,private var iOnItemClickListener: IOnItemClickListener ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val rootItem : ConstraintLayout = itemView.findViewById(R.id.rootItem)

        fun bind( events:Events) {
            rootItem.setOnClickListener(this)
        }
        override fun onClick(v: View) {
            when(v.id){
                R.id.rootItem->{
                    iOnItemClickListener.onItemCLick(adapterPosition)
                }
            }
        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        views = LayoutInflater.from(context).inflate(R.layout.events_item,parent,false)
        return EventViewHolder(views,iOnItemClickListener)
    }

    override fun getItemCount()= listEvents.size

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(listEvents[position])
    }
}