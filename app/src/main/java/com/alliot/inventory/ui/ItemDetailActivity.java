package com.alliot.inventory.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.alliot.inventory.R;
import com.alliot.inventory.databinding.ActivityDetailBinding;
import com.alliot.inventory.model.Item;
import com.alliot.inventory.util.ParcelCompat;

public class ItemDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM = "extra_item";
    public static final String EXTRA_TRANSITION_NAME = "extra_transition_name";

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Item item = ParcelCompat.getParcelableExtra(getIntent(), EXTRA_ITEM, Item.class);
        if (item == null) {
            finish();
            return;
        }

        String transitionName = getIntent().getStringExtra(EXTRA_TRANSITION_NAME);
        boolean isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (transitionName != null && !isLandscape) {
            binding.detailContent.ivDetailImage.setTransitionName(transitionName);
        }

        adaptLayoutForOrientation();

        DetailViewHelper.populate(binding.detailContent.getRoot(), this, item);

        binding.detailContent.btnBack.setOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed());
    }

    @Override
    public void finish() {
        super.finish();
        boolean isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape) {
            overridePendingTransition(R.anim.fade_slide_pop_in, R.anim.fade_slide_pop_out);
        }
    }

    private void adaptLayoutForOrientation() {
        boolean isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (isLandscape) {
            binding.detailContent.contentRow.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
            imageParams.setMarginEnd(16);
            binding.detailContent.imageCard.setLayoutParams(imageParams);

            LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 3f);
            binding.detailContent.infoSection.setLayoutParams(infoParams);
        }
    }
}
