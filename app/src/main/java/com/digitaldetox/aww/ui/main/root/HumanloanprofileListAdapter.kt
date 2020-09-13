package com.digitaldetox.aww.ui.main.root

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.digitaldetox.aww.R
import com.digitaldetox.aww.models.HumanloanprofileRoom
import com.digitaldetox.aww.util.GenericViewHolder
import kotlinx.android.synthetic.main.layout_humanloanprofile_list_item.view.*

private const val TAG = "HumanloanprofileListAdapter"
class HumanloanprofileListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val HUMANLOANPROFILE_ITEM = 0
    private val NO_MORE_RESULTS_HUMANLOANPROFILE_MARKER = HumanloanprofileRoom(
        pk = NO_MORE_RESULTS,
        title = "",
        albumId = ""
    )

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HumanloanprofileRoom>() {

        override fun areItemsTheSame(oldItem: HumanloanprofileRoom, newItem: HumanloanprofileRoom): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: HumanloanprofileRoom, newItem: HumanloanprofileRoom): Boolean {
            return oldItem == newItem
        }

    }
    private val differ =
        AsyncListDiffer(
            HumanloanprofileRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        Log.d(TAG, "onCreateViewHolder: HUMANLOANPROFILE")
        when (viewType) {

            NO_MORE_RESULTS -> {
                Log.e(TAG, "onCreateViewHolder: No more results...")
                return GenericViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_no_more_results,
                        parent,
                        false
                    )
                )
            }

            HUMANLOANPROFILE_ITEM -> {
                return HumanloanprofileViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_humanloanprofile_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
            else -> {
                return HumanloanprofileViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_humanloanprofile_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
        }
    }

    internal inner class HumanloanprofileRecyclerChangeCallback(
        private val adapter: HumanloanprofileListAdapter
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HumanloanprofileViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (differ.currentList.get(position).pk > -1) {
            return HUMANLOANPROFILE_ITEM
        }
        return differ.currentList.get(position).pk
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<HumanloanprofileRoom>
    ) {
        for (humanloanprofile in list) {

        }
    }

    fun submitList(
        humanloanprofileList: List<HumanloanprofileRoom>?,
        isQueryExhausted: Boolean
    ) {
        val newList = humanloanprofileList?.toMutableList()
        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_HUMANLOANPROFILE_MARKER)
        val commitCallback = Runnable {


            interaction?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)
    }

    class HumanloanprofileViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: HumanloanprofileRoom) = with(itemView) {

            itemView.setOnLongClickListener {
                interaction?.onItemLongSelected(adapterPosition, item)
                return@setOnLongClickListener true
            }


            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            Log.d(TAG, "bind: ${item.title} ")
            Log.d(TAG, "bind: ${item} ")

            itemView.humanloanprofile_title.text = item.title

//            itemView.humanloanprofile_update_date.text = item.pk.toString()
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: HumanloanprofileRoom)

        fun onItemLongSelected(position: Int, item: HumanloanprofileRoom)

        fun restoreListPosition()
    }
}