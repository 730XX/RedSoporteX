package com.example.redsoportex.core.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.redsoportex.R;
import com.example.redsoportex.databinding.ComponentItemCardBinding;

/**
 * Vincula la tarjeta compartida de catálogo, solicitudes y préstamos.
 */
public final class CardRenderer {
    private CardRenderer() {
    }

    public static void add(ViewGroup parent, String title, String subtitle, String status, boolean pending, Runnable click) {
        add(parent, title, subtitle, status, pending, R.drawable.ic_accessibility, click);
    }

    public static void add(ViewGroup parent, String title, String subtitle, String status, boolean pending, int icon, Runnable click) {
        ComponentItemCardBinding b = ComponentItemCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        b.imgItemIcon.setImageResource(icon);
        b.tvItemTitle.setText(title);
        b.tvItemSubtitle.setText(subtitle);
        b.tvItemStatus.setText(status);
        b.tvItemStatus.setBackgroundResource(pending ? R.drawable.bg_status_pending : R.drawable.bg_status);
        b.card.setClickable(click != null);
        b.card.setFocusable(click != null);
        if (click != null) b.card.setOnClickListener(v -> click.run());
        parent.addView(b.getRoot());
    }
}
