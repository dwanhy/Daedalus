package org.itxtech.daedalus.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import org.itxtech.daedalus.Daedalus;
import org.itxtech.daedalus.R;
import org.itxtech.daedalus.fragment.ConfigFragment;
import org.itxtech.daedalus.fragment.DnsServerConfigFragment;
import org.itxtech.daedalus.fragment.RuleConfigFragment;

/**
 * Daedalus Project
 *
 * @author iTX Technologies
 * @link https://itxtech.org
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
public class ConfigActivity extends AppCompatActivity {
    public static final String LAUNCH_ACTION_FRAGMENT = "org.itxtech.daedalus.activity.ConfigActivity.LAUNCH_ACTION_FRAGMENT";
    public static final int LAUNCH_FRAGMENT_DNS_SERVER = 0;
    public static final int LAUNCH_FRAGMENT_RULE = 1;

    public static final String LAUNCH_ACTION_ID = "org.itxtech.daedalus.activity.ConfigActivity.LAUNCH_ACTION_ID";
    public static final int ID_NONE = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        ConfigFragment fragment;
        switch (getIntent().getIntExtra(LAUNCH_ACTION_FRAGMENT, LAUNCH_FRAGMENT_DNS_SERVER)) {
            case LAUNCH_FRAGMENT_DNS_SERVER:
                fragment = new DnsServerConfigFragment();
                break;
            case LAUNCH_FRAGMENT_RULE:
                fragment = new RuleConfigFragment();
                break;
            default://should never reach this
                fragment = new DnsServerConfigFragment();
                break;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_config);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_clear);
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, Color.WHITE);
        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(fragment);
        toolbar.inflateMenu(R.menu.custom_dns_server_menu);

        FragmentManager manager = getFragmentManager();
        fragment.setIntent(getIntent());
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.id_config, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_config);
        switch (getIntent().getIntExtra(LAUNCH_ACTION_FRAGMENT, LAUNCH_FRAGMENT_DNS_SERVER)) {
            case LAUNCH_FRAGMENT_DNS_SERVER:
                toolbar.setTitle(R.string.config_dns_server);
                break;
            case LAUNCH_FRAGMENT_RULE:
                toolbar.setTitle(R.string.config_rule);
                break;
            default://should never reach this
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Daedalus.configurations.save();
    }
}
