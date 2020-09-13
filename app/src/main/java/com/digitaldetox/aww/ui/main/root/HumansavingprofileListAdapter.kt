package com.digitaldetox.aww.ui.main.root

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.digitaldetox.aww.R
import com.digitaldetox.aww.models.HumansavingprofileRoom
import com.digitaldetox.aww.util.GenericViewHolder
import kotlinx.android.synthetic.main.layout_humansavingprofile_list_item.view.*

private const val TAG = "HumansavingprofileListAdapter"
class HumansavingprofileListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val HUMANSAVINGPROFILE_ITEM = 0
    private val NO_MORE_RESULTS_HUMANSAVINGPROFILE_MARKER = HumansavingprofileRoom(
        pk = NO_MORE_RESULTS,
        title = "",
        albumId = ""
    )

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HumansavingprofileRoom>() {

        override fun areItemsTheSame(oldItem: HumansavingprofileRoom, newItem: HumansavingprofileRoom): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: HumansavingprofileRoom, newItem: HumansavingprofileRoom): Boolean {
            return oldItem == newItem
        }

    }
    private val differ =
        AsyncListDiffer(
            HumansavingprofileRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        Log.d(TAG, "onCreateViewHolder: HUMANSAVINGPROFILE")
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

            HUMANSAVINGPROFILE_ITEM -> {
                return HumansavingprofileViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_humansavingprofile_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
            else -> {
                return HumansavingprofileViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_humansavingprofile_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
        }
    }

    internal inner class HumansavingprofileRecyclerChangeCallback(
        private val adapter: HumansavingprofileListAdapter
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
            is HumansavingprofileViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (differ.currentList.get(position).pk > -1) {
            return HUMANSAVINGPROFILE_ITEM
        }
        return differ.currentList.get(position).pk
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<HumansavingprofileRoom>
    ) {
        for (humansavingprofile in list) {

        }
    }

    fun submitList(
        humansavingprofileList: List<HumansavingprofileRoom>?,
        isQueryExhausted: Boolean
    ) {
        val newList = humansavingprofileList?.toMutableList()
        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_HUMANSAVINGPROFILE_MARKER)
        val commitCallback = Runnable {


            interaction?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)
    }

    class HumansavingprofileViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: HumansavingprofileRoom) = with(itemView) {

            itemView.setOnLongClickListener {
                interaction?.onItemLongSelected(adapterPosition, item)
                return@setOnLongClickListener true
            }


            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            Log.d(TAG, "bind: ${item.title} ")
            Log.d(TAG, "bind: ${item} ")

            itemView.humansavingprofile_title.text = item.title

//            itemView.humansavingprofile_update_date.text = item.pk.toString()
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: HumansavingprofileRoom)

        fun onItemLongSelected(position: Int, item: HumansavingprofileRoom)

        fun restoreListPosition()
    }
}