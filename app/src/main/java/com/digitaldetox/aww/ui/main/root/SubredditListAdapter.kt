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

class SubredditListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val SUBREDDIT_ITEM = 0
    private val NO_MORE_RESULTS_SUBREDDIT_MARKER = SubredditRoom(
        pk = NO_MORE_RESULTS,
        title = "",
        memberscount = NO_MORE_RESULTS,

        description = "",

        members = arrayListOf(),

        albumId = ""
    )

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SubredditRoom>() {

        override fun areItemsTheSame(oldItem: SubredditRoom, newItem: SubredditRoom): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: SubredditRoom, newItem: SubredditRoom): Boolean {
            return oldItem == newItem
        }

    }
    private val differ =
        AsyncListDiffer(
            SubredditRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

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

            SUBREDDIT_ITEM -> {
                return SubredditViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_subreddit_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
            else -> {
                return SubredditViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_subreddit_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
        }
    }

    internal inner class SubredditRecyclerChangeCallback(
        private val adapter: SubredditListAdapter
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
            is SubredditViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (differ.currentList.get(position).pk > -1) {
            return SUBREDDIT_ITEM
        }
        return differ.currentList.get(position).pk
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<SubredditRoom>
    ) {
        for (subreddit in list) {

        }
    }

    fun submitList(
        subredditList: List<SubredditRoom>?,
        isQueryExhausted: Boolean
    ) {
        val newList = subredditList?.toMutableList()
        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_SUBREDDIT_MARKER)
        val commitCallback = Runnable {


            interaction?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)
    }

    class SubredditViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: SubredditRoom) = with(itemView) {

            itemView.setOnLongClickListener {
                interaction?.onItemLongSelected(adapterPosition, item)
                return@setOnLongClickListener true
            }

            itemView.activity_add_title_proteins.setOnLongClickListener {

                val text = itemView.activity_add_title_proteins.text.toString()
                interaction?.onAllSpeech(text)
                return@setOnLongClickListener true
            }
            itemView.shg_members_count.setOnLongClickListener {

                val text = itemView.shg_members_count.text.toString()
                interaction?.onAllSpeech(text)
                return@setOnLongClickListener true
            }
            itemView.shg_location.setOnLongClickListener {

                val text = itemView.shg_location.text.toString()
                interaction?.onAllSpeech(text)
                return@setOnLongClickListener true
            }

            itemView.subreddit_title.setOnLongClickListener {

                val text = itemView.subreddit_title.text.toString()
                interaction?.onAllSpeech(text)
                return@setOnLongClickListener true
            }

            itemView.subreddit_update_date.setOnLongClickListener {

                val text = itemView.subreddit_update_date.text.toString()
                interaction?.onAllSpeech(text)
                return@setOnLongClickListener true
            }

            itemView.activity_add_title_require.setOnLongClickListener {

                val text = itemView.activity_add_title_require.text.toString()
                interaction?.onAllSpeech(text)
                return@setOnLongClickListener true
            }
            itemView.subreddit_body.setOnLongClickListener {

                val text = itemView.subreddit_body.text.toString()
                interaction?.onAllSpeech(text)
                return@setOnLongClickListener true
            }



            itemView.shg_members_count.setOnLongClickListener {

                val text = itemView.shg_members_count.text.toString()
                interaction?.onAllSpeech(text)
                return@setOnLongClickListener true
            }



            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

//            itemView.loan_button.setOnClickListener {
//                interaction?.onLoanButtonSelected(adapterPosition, item)
//            }
//
//
//            itemView.saving_button.setOnClickListener {
//                interaction?.onSavingButtonSelected(adapterPosition, item)
//            }

            itemView.update_subreddit_button.setOnClickListener {
                interaction?.onUpdateSubredditButtonSelected(adapterPosition, item)
            }

            itemView.subreddit_title.text = item.title

            itemView.subreddit_body.text = item.description
//            itemView.subreddit_update_date.text = item.pk.toString()
            itemView.subreddit_update_date.text = item.memberscount.toString()
        }
    }


    interface Interaction {

        fun onItemSelected(position: Int, item: SubredditRoom)

        fun onLoanButtonSelected(position: Int, item: SubredditRoom)

        fun onSavingButtonSelected(position: Int, item: SubredditRoom)

        fun onAllSpeech(speechString: String)


        fun onUpdateSubredditButtonSelected(position: Int, item: SubredditRoom)

        fun onItemLongSelected(position: Int, item: SubredditRoom)

        fun restoreListPosition()
    }
}