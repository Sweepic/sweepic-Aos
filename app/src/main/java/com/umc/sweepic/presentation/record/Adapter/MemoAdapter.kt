import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umc.sweepic.R
import com.umc.sweepic.databinding.ItemMemoFolderBinding
import com.umc.sweepic.presentation.record.memo.MemoFolder

class MemoAdapter(
    private var memoFolders: List<MemoFolder>,
    private val onItemClick: (MemoFolder) -> Unit
) : RecyclerView.Adapter<MemoAdapter.MemoFolderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoFolderViewHolder {
        val binding = ItemMemoFolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MemoFolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoFolderViewHolder, position: Int) {
        holder.bind(memoFolders[position])
    }

    override fun getItemCount(): Int = memoFolders.size

//     데이터 갱신 메서드
    fun updateData(newData: List<MemoFolder>) {
        memoFolders = newData
        notifyDataSetChanged()
    }

    inner class MemoFolderViewHolder(private val binding: ItemMemoFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(memoFolder: MemoFolder) {
            binding.tvMemoFolderTitle.text = memoFolder.title
            binding.tvMemoDate.text = memoFolder.date
            binding.tvMemoContent.text = memoFolder.content ?: ""
            binding.tvMemoPhotoNum.text = if (memoFolder.imageResIds.isNotEmpty()) {
                "${memoFolder.imageResIds.size}장의 사진"
            } else {
                ""
            }
            binding.root.setOnClickListener { onItemClick(memoFolder) }

            binding.memoImage.setImageResource(
                memoFolder.imageResIds.firstOrNull() ?: R.drawable.img_record_ex
            )
            binding.memoImage.visibility =
                if (memoFolder.imageResIds.isNotEmpty()) View.VISIBLE else View.GONE

            binding.root.setOnClickListener { onItemClick(memoFolder) }
        }
    }
}
