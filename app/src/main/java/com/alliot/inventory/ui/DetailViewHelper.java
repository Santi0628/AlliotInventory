package com.alliot.inventory.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alliot.inventory.R;
import com.alliot.inventory.model.Item;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.chip.Chip;

import java.util.Locale;

public final class DetailViewHelper {

    private DetailViewHelper() {}

    public static void populate(View detailRoot, Context context, Item item) {
        TextView tvDetailName = detailRoot.findViewById(R.id.tvDetailName);
        ImageView ivDetailImage = detailRoot.findViewById(R.id.ivDetailImage);
        Chip chipStatus = detailRoot.findViewById(R.id.chipStatus);
        TextView tvInternalSku = detailRoot.findViewById(R.id.tvDetailInternalSku);
        TextView tvExternalSku = detailRoot.findViewById(R.id.tvDetailExternalSku);
        TextView tvBarcode = detailRoot.findViewById(R.id.tvDetailBarcode);
        TextView tvModel = detailRoot.findViewById(R.id.tvDetailModel);
        TextView tvBrand = detailRoot.findViewById(R.id.tvDetailBrand);
        Chip chipConsumable = detailRoot.findViewById(R.id.chipConsumable);
        Chip chipRepairable = detailRoot.findViewById(R.id.chipRepairable);
        Chip chipRestricted = detailRoot.findViewById(R.id.chipRestricted);
        TextView tvQuantity = detailRoot.findViewById(R.id.tvDetailQuantity);
        TextView tvAvailable = detailRoot.findViewById(R.id.tvDetailAvailable);

        tvDetailName.setText(item.getName() != null
                ? item.getName() : context.getString(R.string.no_name));

        Glide.with(context)
                .load(item.getLargeImageUrl())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerInside())
                .into(ivDetailImage);

        if (item.isActive()) {
            chipStatus.setText(R.string.active);
            chipStatus.setChipBackgroundColorResource(R.color.chip_active_bg);
            chipStatus.setTextColor(context.getColor(R.color.status_active));
            chipStatus.setChipIconResource(R.drawable.status_active);
        } else {
            chipStatus.setText(R.string.inactive);
            chipStatus.setChipBackgroundColorResource(R.color.chip_inactive_bg);
            chipStatus.setTextColor(context.getColor(R.color.status_inactive));
            chipStatus.setChipIconResource(R.drawable.status_inactive);
        }

        tvInternalSku.setText(item.getInternalSku() != null
                ? item.getInternalSku() : context.getString(R.string.not_available));
        tvExternalSku.setText(
                item.getExternalSku() != null && !item.getExternalSku().isEmpty()
                        ? item.getExternalSku() : context.getString(R.string.not_available));

        tvBarcode.setText(item.getBarcode() != null
                ? item.getBarcode() : context.getString(R.string.not_available));

        tvModel.setText(
                item.getModel() != null && !item.getModel().isEmpty()
                        ? item.getModel() : context.getString(R.string.not_available));
        tvBrand.setText(
                item.getBrand() != null && !item.getBrand().isEmpty()
                        ? item.getBrand() : context.getString(R.string.not_available));

        stylePropertyChip(chipConsumable, context, item.isConsumable(), R.drawable.ic_consumable);
        stylePropertyChip(chipRepairable, context, item.isRepairable(), R.drawable.ic_repairable);
        stylePropertyChip(chipRestricted, context, item.isRestricted(), R.drawable.ic_restricted);

        tvQuantity.setText(String.valueOf(item.getQuantity()));
        tvAvailable.setText(String.format(Locale.getDefault(), "%.0f",
                item.getAvailableQuantity()));
    }

    public static void stylePropertyChip(Chip chip, Context context,
                                          boolean isEnabled, int iconRes) {
        chip.setChipIconResource(iconRes);
        if (isEnabled) {
            chip.setChipBackgroundColorResource(R.color.chip_property_on);
            chip.setTextColor(context.getColor(R.color.primary));
            chip.setChipIconTint(ColorStateList.valueOf(context.getColor(R.color.primary)));
        } else {
            chip.setChipBackgroundColorResource(R.color.chip_property_off);
            chip.setTextColor(context.getColor(R.color.text_hint));
            chip.setChipIconTint(ColorStateList.valueOf(context.getColor(R.color.text_hint)));
        }
    }
}
