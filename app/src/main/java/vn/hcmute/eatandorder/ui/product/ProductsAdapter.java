package vn.hcmute.eatandorder.ui.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import vn.hcmute.eatandorder.R;
import vn.hcmute.eatandorder.data.model.Product;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductsAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void updateData(List<Product> newList) {
        if (newList != null) {
            this.productList = newList;
            notifyDataSetChanged();
        }
    }

    public void addData(List<Product> newProducts) {
        if (newProducts != null && !newProducts.isEmpty()) {
            int startPosition = productList.size();
            productList.addAll(newProducts);
            notifyItemRangeInserted(startPosition, newProducts.size());
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = productList.get(position);

        holder.txtName.setText(p.getName() != null ? p.getName() : "");

        // Format price
        Double price = p.getPrice();
        if (price != null) {
            holder.txtPrice.setText(formatPrice(price));
        } else {
            holder.txtPrice.setText("0₫");
        }

        // Format sold
        Integer sold = p.getSold();
        if (sold != null) {
            holder.txtSold.setText("Sold: " + sold);
        } else {
            holder.txtSold.setText("Sold: 0");
        }

        // Load image with Glide
        String imageUrl = p.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .centerCrop()
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.ic_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return (productList != null) ? productList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtPrice, txtSold;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtSold = itemView.findViewById(R.id.txtSold);
        }
    }

    private String formatPrice(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(price) + "₫";
    }
}
