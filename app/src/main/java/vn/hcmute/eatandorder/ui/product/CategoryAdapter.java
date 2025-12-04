package vn.hcmute.eatandorder.ui.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.hcmute.eatandorder.R;
import vn.hcmute.eatandorder.data.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> list;
    private int selectedPosition = -1;
    private OnCategoryClickListener listener;

    public CategoryAdapter(Context context, List<Category> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnCategoryClickListener(OnCategoryClickListener l) {
        this.listener = l;
    }

    public void updateData(List<Category> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category c = list.get(position);
        holder.txtName.setText(c.getName());

        // Load image with Glide, handle null/empty URLs
        String imageUrl = c.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .centerCrop()
                    .into(holder.img);
        } else {
            holder.img.setImageResource(R.drawable.ic_placeholder);
        }

        // set selected state (background selector reacts to android:state_selected)
        holder.layoutCategory.setSelected(selectedPosition == position);

        holder.itemView.setOnClickListener(v -> {
            int old = selectedPosition;
            selectedPosition = position;

            // update visuals
            if (old != -1) notifyItemChanged(old);
            notifyItemChanged(selectedPosition);

            if (listener != null) listener.onClick(c);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName;
        LinearLayout layoutCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgCategory);
            txtName = itemView.findViewById(R.id.txtCategoryName);
            layoutCategory = itemView.findViewById(R.id.layoutCategory);
        }
    }

    public interface OnCategoryClickListener {
        void onClick(Category category);
    }
}
