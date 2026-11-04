package com.example.redsoportex.core.ui;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import com.example.redsoportex.R;
import com.example.redsoportex.core.session.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public final class BottomNavHelper {
    private BottomNavHelper() {
    }

    public static void setup(BottomNavigationView bar, NavController nav, int selected) {
        boolean donor = SessionManager.getInstance(bar.getContext()).isDonor();
        if (donor) {
            bar.getMenu().findItem(R.id.nav_loans).setTitle(R.string.donor_items).setIcon(R.drawable.ic_donor_package);
            bar.getMenu().findItem(R.id.nav_alerts).setTitle(R.string.donor_requests).setIcon(R.drawable.ic_nav_loans);
        }
        bar.setSelectedItemId(selected);
        bar.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selected) return true;


            int dest = id == R.id.nav_catalog ? R.id.catalogFragment : id == R.id.nav_loans ? R.id.loansFragment : id == R.id.nav_alerts ? R.id.alertsFragment : R.id.profileFragment;
            if (donor && id == R.id.nav_loans) dest = R.id.donorItemsFragment;
            if (donor && id == R.id.nav_alerts) dest = R.id.incomingFragment;
            NavOptions options = new NavOptions.Builder().setLaunchSingleTop(true).setPopUpTo(R.id.catalogFragment, false).build();
            nav.navigate(dest, null, options);
            return true;
        });
    }
}
