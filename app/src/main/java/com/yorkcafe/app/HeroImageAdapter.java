package com.yorkcafe.app;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HeroImageAdapter extends RecyclerView.Adapter<HeroImageAdapter.HeroViewHolder> {
    private List<Integer> imageList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public HeroImageAdapter(List<Integer> imageList, OnItemClickListener listener) {
        this.imageList = imageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setClickable(true);
        imageView.setFocusable(true);

        // Add ripple effect
        TypedValue outValue = new TypedValue();
        if (parent.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)) {
            imageView.setForeground(parent.getContext().getDrawable(outValue.resourceId));
        }

        return new HeroViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
        // Downsample the image to reduce memory usage and eliminate lag
        int resId = imageList.get(position);
        Bitmap bitmap = decodeSampledBitmap(holder.imageView.getContext().getResources(), resId, 720, 480);
        holder.imageView.setImageBitmap(bitmap);
        holder.imageView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    /**
     * Decodes a bitmap from resources with inSampleSize to avoid loading
     * the full-resolution image into memory (which causes lag on scroll).
     */
    private static Bitmap decodeSampledBitmap(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    static class HeroViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        HeroViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
        }
    }
}
