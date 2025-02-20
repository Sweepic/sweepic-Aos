import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.sweepic.databinding.ItemHistoryLastBestBinding
import com.umc.sweepic.domain.model.Award

class HistoryLastBestAdapter :
    ListAdapter<Award, HistoryLastBestAdapter.AwardViewHolder>(AwardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AwardViewHolder {
        val binding = ItemHistoryLastBestBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AwardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AwardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AwardViewHolder(private val binding: ItemHistoryLastBestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(award: Award) {
            binding.tvBestMonth.text = award.date

            // 🔹 `photoUris` 리스트 크기 체크 후 Glide로 이미지 로드
            val safePhotos = award.photoUris.takeIf { it.size >= 5 }
                ?: List(5) { Uri.EMPTY }

            Glide.with(binding.ivPhoto1.context).load(safePhotos[0]).into(binding.ivPhoto1)
            Glide.with(binding.ivPhoto2.context).load(safePhotos[1]).into(binding.ivPhoto2)
            Glide.with(binding.ivPhoto3.context).load(safePhotos[2]).into(binding.ivPhoto3)
            Glide.with(binding.ivPhoto4.context).load(safePhotos[3]).into(binding.ivPhoto4)
            Glide.with(binding.ivPhoto5.context).load(safePhotos[4]).into(binding.ivPhoto5)
        }
    }

    class AwardDiffCallback : DiffUtil.ItemCallback<Award>() {
        override fun areItemsTheSame(oldItem: Award, newItem: Award): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Award, newItem: Award): Boolean {
            return oldItem == newItem
        }
    }
}
