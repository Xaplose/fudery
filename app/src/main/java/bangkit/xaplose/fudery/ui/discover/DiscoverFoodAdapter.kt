package bangkit.xaplose.fudery.ui.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bangkit.xaplose.fudery.R
import bangkit.xaplose.fudery.data.model.Food
import bangkit.xaplose.fudery.databinding.ItemRowFoodBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*

class DiscoverFoodAdapter : RecyclerView.Adapter<DiscoverFoodAdapter.ListViewHolder>() {

    private var listData = ArrayList<Food>()
    var onItemClick: ((Food) -> Unit)? = null

    fun setData(newListData: List<Food>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_food, parent, false)
        )

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowFoodBinding.bind(itemView)
        fun bind(data: Food) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(data.imageUrl)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgFood)
                tvName.text = data.name
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }
}