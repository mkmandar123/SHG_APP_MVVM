package com.digitaldetox.aww.ui.main.root


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.digitaldetox.aww.R
import com.digitaldetox.aww.models.SubredditRoom
import com.digitaldetox.aww.util.GenericViewHolder
import kotlinx.android.synthetic.main.layout_subreddit_list_item.view.*
import kotlinx.android.synthetic.main.layout_subuser_list_item.view.*

class SubuserListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "lgx_SubuserListAd"
    private val NO_MORE_RESULTS = -1
    private val SUBUSER_ITEM = 0
    private val NO_MORE_RESULTS_SUBUSER_MARKER = NO_MORE_RESULTS

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
    private val differ =
        AsyncListDiffer(
            SubuserRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {

            NO_MORE_RESULTS -> {
                Log.d(TAG, "111SUBUSER --> onCreateViewHolder: No more results...")
                return GenericViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_no_more_results,
                        parent,
                        false
                    )
                )
            }

            SUBUSER_ITEM -> {
                Log.d(TAG, "onCreateViewHolder: 71: 111SUBUSER --> ")
                return SubuserViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_subuser_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
            else -> {
                Log.d(TAG, "onCreateViewHolder: 83: 111SUBUSER --> ")
                return SubuserViewHolder(

                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_subuser_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
        }
    }

    internal inner class SubuserRecyclerChangeCallback(
        private val adapter: SubuserListAdapter
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
            is SubuserViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (!differ.currentList.get(position).isNullOrEmpty() && differ.currentList.get(position) != NO_MORE_RESULTS.toString()) {
            Log.d(TAG, "getItemViewType: 129: 111SUBUSER ${differ.currentList.get(position)}")
            Log.d(
                TAG,
                "getItemViewType: 129: 111SUBUSER ${differ.currentList.get(position).isEmpty()}"
            )

            return SUBUSER_ITEM
        }
        return NO_MORE_RESULTS
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // Prepare the images that will be displayed in the RecyclerView.
    // This also ensures if the network connection is lost, they will be in the cache
    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<String>
    ) {
        for (subuser in list) {

        }
    }

    fun submitList(
        subuserList: List<String>?,
        isQueryExhausted: Boolean
    ) {

        val newList = subuserList?.toMutableList()
        if (isQueryExhausted) {

            newList?.add(NO_MORE_RESULTS_SUBUSER_MARKER.toString())
        }
        val commitCallback = Runnable {
            // if process died must restore list position
            // very annoying
            interaction?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)
    }

    class SubuserViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {
        val TAG = "lgx_SubuserListAd"

        fun bind(item: String) = with(itemView) {
            itemView.setOnClickListener {
                Log.d(TAG, "bind: 200: returns guest ${item}")
                interaction?.onItemSelected(adapterPosition, item)
            }


            itemView.loan_button.setOnClickListener {
                interaction?.onLoanButtonSelected(adapterPosition, item)
            }
            itemView.saving_button.setOnClickListener {
                interaction?.onSavingButtonSelected(adapterPosition, item)
            }
            itemView.setOnLongClickListener {
                interaction?.onItemLongSelected(adapterPosition, item)
                return@setOnLongClickListener true
            }

            itemView.subuser_title.text = item
//            itemView.subuser_body.text = item
//            itemView.subuser_update_date.text = ""
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: String)

        fun onItemLongSelected(position: Int, item: String)
        fun onLoanButtonSelected(position: Int, item: String)
        fun onSavingButtonSelected(position: Int, item: String)


        fun restoreListPosition()
    }
}
