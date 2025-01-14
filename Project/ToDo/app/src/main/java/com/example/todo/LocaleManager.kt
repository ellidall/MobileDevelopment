import android.content.Context
import java.util.Locale

object LocaleManager {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_LOCALE = "locale"

    fun setLocale(context: Context, localeCode: String) {
        val locale = Locale(localeCode)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(KEY_LOCALE, localeCode)
            apply()
        }
    }

    fun getLocale(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_LOCALE, "en") ?: "en"
    }
}
