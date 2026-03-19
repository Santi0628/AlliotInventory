package com.alliot.inventory.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alliot.inventory.R;
import com.alliot.inventory.model.Item;
import com.alliot.inventory.util.ParcelCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import java.util.Locale;

public class ItemDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM = "extra_item";
    public static final String EXTRA_TRANSITION_NAME = "extra_transition_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Item item = ParcelCompat.getParcelableExtra(getIntent(), EXTRA_ITEM, Item.class);
        if (item == null) {
            finish();
            return;
        }

        ImageView ivDetailImage = findViewById(R.id.ivDetailImage);
        String transitionName = getIntent().getStringExtra(EXTRA_TRANSITION_NAME);
        if (transitionName != null) {
            ivDetailImage.setTransitionName(transitionName);
        }

        adaptLayoutForOrientation();
        populateData(item);

        MaterialButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void adaptLayoutForOrientation() {
        LinearLayout contentRow = findViewById(R.id.contentRow);
        MaterialCardView imageCard = findViewById(R.id.imageCard);
        LinearLayout infoSection = findViewById(R.id.infoSection);

        boolean isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (isLandscape) {
            contentRow.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
            imageParams.setMarginEnd(16);
            imageCard.setLayoutParams(imageParams);

            LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 3f);
            infoSection.setLayoutParams(infoParams);
        }
    }

    private void populateData(Item item) {
        TextView tvDetailName = findViewById(R.id.tvDetailName);
        ImageView ivDetailImage = findViewById(R.id.ivDetailImage);
        Chip chipStatus = findViewById(R.id.chipStatus);
        TextView tvDetailInternalSku = findViewById(R.id.tvDetailInternalSku);
        TextView tvDetailExternalSku = findViewById(R.id.tvDetailExternalSku);
        TextView tvDetailBarcode = findViewById(R.id.tvDetailBarcode);
        TextView tvDetailModel = findViewById(R.id.tvDetailModel);
        TextView tvDetailBrand = findViewById(R.id.tvDetailBrand);
        Chip chipConsumable = findViewById(R.id.chipConsumable);
        Chip chipRepairable = findViewById(R.id.chipRepairable);
        Chip chipRestricted = findViewById(R.id.chipRestricted);
        TextView tvDetailQuantity = findViewById(R.id.tvDetailQuantity);
        TextView tvDetailAvailable = findViewById(R.id.tvDetailAvailable);

        tvDetailName.setText(item.getName() != null ? item.getName() : getString(R.string.no_name));

        String imageUrl = item.getLargeImageUrl();
        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerInside())
                .into(ivDetailImage);

        if (item.isActive()) {
            chipStatus.setText(R.string.active);
            chipStatus.setChipBackgroundColorResource(R.color.chip_active_bg);
            chipStatus.setTextColor(getColor(R.color.status_active));
            chipStatus.setChipIconResource(R.drawable.status_active);
        } else {
            chipStatus.setText(R.string.inactive);
            chipStatus.setChipBackgroundColorResource(R.color.chip_inactive_bg);
            chipStatus.setTextColor(getColor(R.color.status_inactive));
            chipStatus.setChipIconResource(R.drawable.status_inactive);
        }

        tvDetailInternalSku.setText(item.getInternalSku() != null
                ? item.getInternalSku() : getString(R.string.not_available));
        tvDetailExternalSku.setText(item.getExternalSku() != null && !item.getExternalSku().isEmpty()
                ? item.getExternalSku() : getString(R.string.not_available));

        tvDetailBarcode.setText(item.getBarcode() != null
                ? item.getBarcode() : getString(R.string.not_available));

        tvDetailModel.setText(item.getModel() != null && !item.getModel().isEmpty()
                ? item.getModel() : getString(R.string.not_available));
        tvDetailBrand.setText(item.getBrand() != null && !item.getBrand().isEmpty()
                ? item.getBrand() : getString(R.string.not_available));

        stylePropertyChip(chipConsumable, item.isConsumable());
        stylePropertyChip(chipRepairable, item.isRepairable());
        stylePropertyChip(chipRestricted, item.isRestricted());

        tvDetailQuantity.setText(String.valueOf(item.getQuantity()));
        tvDetailAvailable.setText(String.format(Locale.getDefault(), "%.0f",
                item.getAvailableQuantity()));
    }

    private void stylePropertyChip(Chip chip, boolean isEnabled) {
        if (isEnabled) {
            chip.setChipBackgroundColorResource(R.color.chip_property_on);
            chip.setTextColor(getColor(R.color.primary));
            chip.setChipIconResource(R.drawable.status_active);
        } else {
            chip.setChipBackgroundColorResource(R.color.chip_property_off);
            chip.setTextColor(getColor(R.color.text_hint));
            chip.setChipIconResource(R.drawable.status_inactive);
        }
    }
}
