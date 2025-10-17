package io.github.vvb2060.ims;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import rikka.shizuku.Shizuku;

public class MainActivity extends Activity {

    private static final String PREFS_NAME = "ims_config";
    private static final String TAG = "IMS_MainActivity";

    private TextView tvAndroidVersion;
    private TextView tvShizukuStatus;
    private TextView tvPersistentWarning;
    private TextView tvSimInfo;
    private Button btnSelectSim;
    private Button btnSwitchLanguage;
    private Switch switchVoLTE;
    private Switch switchVoWiFi;
    private Switch switchVT;
    private Switch switchVoNR;
    private Switch switchCrossSIM;
    private Switch switchUT;
    private Switch switch5GNR;
    private Button btnApply;

    private SharedPreferences prefs;
    private int selectedSubId = 1; // 默认SIM 1, -1表示全部应用

    private final Shizuku.OnBinderReceivedListener binderListener = this::updateShizukuStatus;
    private final Shizuku.OnBinderDeadListener binderDeadListener = this::updateShizukuStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 应用保存的语言设置
        String language = LocaleHelper.getLanguage(this);
        LocaleHelper.updateResources(this, language);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        initViews();
        loadPreferences();
        updateSimInfo();
        updateAndroidVersionInfo();
        updateShizukuStatus();

        Shizuku.addBinderReceivedListener(binderListener);
        Shizuku.addBinderDeadListener(binderDeadListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Shizuku.removeBinderReceivedListener(binderListener);
        Shizuku.removeBinderDeadListener(binderDeadListener);
    }

    private void initViews() {
        tvAndroidVersion = findViewById(R.id.tv_android_version);
        tvShizukuStatus = findViewById(R.id.tv_shizuku_status);
        tvPersistentWarning = findViewById(R.id.tv_persistent_warning);
        tvSimInfo = findViewById(R.id.tv_sim_info);
        btnSelectSim = findViewById(R.id.btn_select_sim);
        btnSwitchLanguage = findViewById(R.id.btn_switch_language);

        // Find switches from included layouts
        switchVoLTE = findViewById(R.id.item_volte).findViewById(R.id.feature_switch);
        switchVoWiFi = findViewById(R.id.item_vowifi).findViewById(R.id.feature_switch);
        switchVT = findViewById(R.id.item_vt).findViewById(R.id.feature_switch);
        switchVoNR = findViewById(R.id.item_vonr).findViewById(R.id.feature_switch);
        switchCrossSIM = findViewById(R.id.item_cross_sim).findViewById(R.id.feature_switch);
        switchUT = findViewById(R.id.item_ut).findViewById(R.id.feature_switch);
        switch5GNR = findViewById(R.id.item_5g_nr).findViewById(R.id.feature_switch);

        // Set feature titles and descriptions
        ((TextView) findViewById(R.id.item_volte).findViewById(R.id.feature_title))
            .setText(R.string.volte);
        ((TextView) findViewById(R.id.item_volte).findViewById(R.id.feature_desc))
            .setText(R.string.volte_desc);

        ((TextView) findViewById(R.id.item_vowifi).findViewById(R.id.feature_title))
            .setText(R.string.vowifi);
        ((TextView) findViewById(R.id.item_vowifi).findViewById(R.id.feature_desc))
            .setText(R.string.vowifi_desc);

        ((TextView) findViewById(R.id.item_vt).findViewById(R.id.feature_title))
            .setText(R.string.vt);
        ((TextView) findViewById(R.id.item_vt).findViewById(R.id.feature_desc))
            .setText(R.string.vt_desc);

        ((TextView) findViewById(R.id.item_vonr).findViewById(R.id.feature_title))
            .setText(R.string.vonr);
        ((TextView) findViewById(R.id.item_vonr).findViewById(R.id.feature_desc))
            .setText(R.string.vonr_desc);

        ((TextView) findViewById(R.id.item_cross_sim).findViewById(R.id.feature_title))
            .setText(R.string.cross_sim);
        ((TextView) findViewById(R.id.item_cross_sim).findViewById(R.id.feature_desc))
            .setText(R.string.cross_sim_desc);

        ((TextView) findViewById(R.id.item_ut).findViewById(R.id.feature_title))
            .setText(R.string.ut);
        ((TextView) findViewById(R.id.item_ut).findViewById(R.id.feature_desc))
            .setText(R.string.ut_desc);

        ((TextView) findViewById(R.id.item_5g_nr).findViewById(R.id.feature_title))
            .setText(R.string._5g_nr);
        ((TextView) findViewById(R.id.item_5g_nr).findViewById(R.id.feature_desc))
            .setText(R.string._5g_nr_desc);

        btnApply = findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(v -> applyConfiguration());

        btnSelectSim.setOnClickListener(v -> showSimSelectionDialog());

        btnSwitchLanguage.setOnClickListener(v -> {
            LocaleHelper.toggleLanguage(this);
            recreate(); // 重新创建 Activity 以应用新语言
        });
    }

    private void showSimSelectionDialog() {
        String[] items = {
            getString(R.string.sim_1),
            getString(R.string.sim_2),
            getString(R.string.apply_to_all_sims)
        };

        int selectedIndex = 0;
        if (selectedSubId == 1) {
            selectedIndex = 0;
        } else if (selectedSubId == 2) {
            selectedIndex = 1;
        } else if (selectedSubId == -1) {
            selectedIndex = 2;
        }

        new AlertDialog.Builder(this)
            .setTitle(R.string.select_sim)
            .setSingleChoiceItems(items, selectedIndex, (dialog, which) -> {
                if (which == 0) {
                    selectedSubId = 1;
                } else if (which == 1) {
                    selectedSubId = 2;
                } else {
                    selectedSubId = -1;
                }
                updateSimInfo();
                dialog.dismiss();
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();
    }

    private void updateSimInfo() {
        if (selectedSubId == 1) {
            tvSimInfo.setText(R.string.sim_1);
            btnApply.setText(R.string.apply_to_sim_1);
        } else if (selectedSubId == 2) {
            tvSimInfo.setText(R.string.sim_2);
            btnApply.setText(R.string.apply_to_sim_2);
        } else {
            tvSimInfo.setText(R.string.apply_to_all_sims);
            btnApply.setText(R.string.apply_to_all);
        }
    }

    private void loadPreferences() {
        switchVoLTE.setChecked(prefs.getBoolean("volte", true));
        switchVoWiFi.setChecked(prefs.getBoolean("vowifi", true));
        switchVT.setChecked(prefs.getBoolean("vt", true));
        switchVoNR.setChecked(prefs.getBoolean("vonr", true));
        switchCrossSIM.setChecked(prefs.getBoolean("cross_sim", true));
        switchUT.setChecked(prefs.getBoolean("ut", true));
        switch5GNR.setChecked(prefs.getBoolean("5g_nr", true));
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("volte", switchVoLTE.isChecked());
        editor.putBoolean("vowifi", switchVoWiFi.isChecked());
        editor.putBoolean("vt", switchVT.isChecked());
        editor.putBoolean("vonr", switchVoNR.isChecked());
        editor.putBoolean("cross_sim", switchCrossSIM.isChecked());
        editor.putBoolean("ut", switchUT.isChecked());
        editor.putBoolean("5g_nr", switch5GNR.isChecked());
        editor.apply();
    }

    private void updateAndroidVersionInfo() {
        String version = String.format(getString(R.string.android_version),
                "Android " + Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT + ")");
        tvAndroidVersion.setText(version);

        // Check if it's QPR2 Beta 3 or higher (API 36+)
        if (Build.VERSION.SDK_INT >= 36) {
            tvPersistentWarning.setVisibility(View.VISIBLE);
        } else {
            tvPersistentWarning.setVisibility(View.GONE);
        }
    }

    private void updateShizukuStatus() {
        runOnUiThread(() -> {
            String statusText;
            int statusColor;

            if (!Shizuku.pingBinder()) {
                statusText = String.format(getString(R.string.shizuku_status),
                        getString(R.string.shizuku_not_running));
                statusColor = 0xFFFF0000;
                btnApply.setEnabled(false);
            } else if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                statusText = String.format(getString(R.string.shizuku_status),
                        getString(R.string.shizuku_no_permission));
                statusColor = 0xFFFF9800;
                btnApply.setEnabled(false);
                requestShizukuPermission();
            } else {
                statusText = String.format(getString(R.string.shizuku_status),
                        getString(R.string.shizuku_ready));
                statusColor = 0xFF4CAF50;
                btnApply.setEnabled(true);
            }

            tvShizukuStatus.setText(statusText);
            tvShizukuStatus.setTextColor(statusColor);
        });
    }

    private void requestShizukuPermission() {
        if (Shizuku.isPreV11()) {
            Toast.makeText(this, R.string.update_shizuku, Toast.LENGTH_LONG).show();
            return;
        }
        Shizuku.requestPermission(0);
    }

    private void applyConfiguration() {
        savePreferences();

        if (!Shizuku.pingBinder()) {
            Toast.makeText(this, R.string.shizuku_not_running_msg, Toast.LENGTH_LONG).show();
            return;
        }

        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.shizuku_no_permission_msg, Toast.LENGTH_LONG).show();
            requestShizukuPermission();
            return;
        }

        // 保存选中的 SubId 供 PrivilegedProcess 使用
        prefs.edit().putInt("selected_subid", selectedSubId).apply();

        // 使用原版的 Instrumentation 方式
        ShizukuProvider.startInstrument(this);

        // Instrumentation 会导致应用退到后台，延迟后重新启动以显示结果
        new Thread(() -> {
            try {
                Thread.sleep(3000); // 等待配置完成
                runOnUiThread(() -> {
                    // 重新启动 MainActivity
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);

                    // 显示成功对话框，提供跳转到网络设置的选项
                    showNetworkSettingsDialog();
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        }).start();
    }

    private void showNetworkSettingsDialog() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.config_applied)
            .setMessage(R.string.config_success_message)
            .setPositiveButton(R.string.go_to_network_settings, (dialog, which) -> {
                // 跳转到网络设置页面
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "Unable to open network settings", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton(R.string.later, null)
            .show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        String language = LocaleHelper.getLanguage(newBase);
        LocaleHelper.updateResources(newBase, language);
        super.attachBaseContext(newBase);
    }
}
