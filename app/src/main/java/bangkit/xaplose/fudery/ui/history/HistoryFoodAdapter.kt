package bangkit.xaplose.fudery.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bangkit.xaplose.fudery.R
import bangkit.xaplose.fudery.data.model.FoodDetails
import bangkit.xaplose.fudery.databinding.ItemRowHistoryBinding
import com.bumptech.glide.Glide
import java.util.*

class HistoryFoodAdapter: RecyclerView.Adapter<HistoryFoodAdapter.ListViewHolder>() {

    private var listData = ArrayList<FoodDetails>()
    var onItemClick: ((FoodDetails) -> Unit)? = null
    var onDeleteIconClick: ((FoodDetails) -> Unit)? = null

    fun setData(data: List<FoodDetails>) {
        listData.clear()
        listData.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryFoodAdapter.ListViewHolder =
        ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_history, parent, false)
        )

    override fun onBindViewHolder(holder: HistoryFoodAdapter.ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount() = listData.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowHistoryBinding.bind(itemView)
        fun bind(data: FoodDetails) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(data.imageUrl)
                    .into(imgFood)

                tvName.text = data.name.capitalize(Locale.ROOT)

                icDelete.setOnClickListener {
                    onDeleteIconClick?.invoke(data)
                }
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}