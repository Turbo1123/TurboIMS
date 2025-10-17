package io.github.vvb2060.ims;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LocaleHelper {
    private static final String PREF_NAME = "locale_config";
    private static final String KEY_LANGUAGE = "language";

    public static void setLocale(Context context, String languageCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply();

        updateResources(context, languageCode);
    }

    public static String getLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // 如果用户已经手动设置过语言，使用用户设置
        if (prefs.contains(KEY_LANGUAGE)) {
            return prefs.getString(KEY_LANGUAGE, "zh");
        }

        // 否则根据系统语言自动判断
        String systemLang = Locale.getDefault().getLanguage();
        // 如果系统语言是中文（包括简体、繁体等），使用中文，否则使用英文
        return systemLang.startsWith("zh") ? "zh" : "en";
    }

    public static void updateResources(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);
        context.createConfigurationContext(config);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public static String toggleLanguage(Context context) {
        String currentLang = getLanguage(context);
        String newLang = currentLang.equals("zh") ? "en" : "zh";
        setLocale(context, newLang);
        return newLang;
    }
}
