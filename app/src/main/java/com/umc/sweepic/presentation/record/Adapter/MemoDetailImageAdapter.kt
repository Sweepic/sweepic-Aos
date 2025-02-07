import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.sweepic.R

class MemoDetailImageAdapter(private var images: List<String>) :
    RecyclerView.Adapter<MemoDetailImageAdapter.ImageViewHolder>() {

    private val selectedItems = mutableSetOf<Int>() // 선택된 사진의 위치 저장
    private var isSelectionMode = false // 선택 모드 상태

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_memodetail_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = images[position]

        Log.d("MemoDetailImageAdapter", "이미지 로드: $imageUrl") // ✅ 로드되는 이미지 확인

        // 이미지 로드
        Glide.with(holder.imageView.context)
            .load(imageUrl)
            .placeholder(R.drawable.img_record_ex)
            .into(holder.imageView)

        // 선택 상태 초기화
        val isSelected = selectedItems.contains(position)
        holder.overlay.visibility = if (isSelected) View.VISIBLE else View.GONE
        holder.checkIcon.visibility = if (isSelected) View.VISIBLE else View.GONE

        // 클릭 이벤트 (선택 모드에서만 작동)
        holder.itemView.setOnClickListener {
            if (isSelectionMode) { // 선택 모드 활성화 시 클릭 가능
                if (selectedItems.contains(position)) {
                    selectedItems.remove(position)
                    holder.overlay.visibility = View.GONE
                    holder.checkIcon.visibility = View.GONE
                } else {
                    selectedItems.add(position)
                    holder.overlay.visibility = View.VISIBLE
                    holder.checkIcon.visibility = View.VISIBLE
                }
            }
        }
    }

    // ✅ 데이터 업데이트 (리스트 변경 가능하도록 `var images` 사용)
    fun updateData(newImages: List<String>) {
        Log.d("MemoDetailImageAdapter", "업데이트할 데이터: $newImages") // ✅ 로그 확인
        images = newImages
        notifyDataSetChanged() // ✅ UI 갱신
    }

    override fun getItemCount(): Int = images.size

    // 선택된 항목 반환
    fun getSelectedItems(): List<Int> = selectedItems.toList()

    // 선택 모드 활성화/비활성화
    fun setSelectionMode(enabled: Boolean) {
        isSelectionMode = enabled
        if (!enabled) {
            // 선택 모드 비활성화 시 선택 상태 초기화
            selectedItems.clear()
            notifyDataSetChanged()
        }
    }

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.iv_memoDetail_image)
        val overlay: View = view.findViewById(R.id.overlay)
        val checkIcon: ImageView = view.findViewById(R.id.iv_check_icon)
    }
}
