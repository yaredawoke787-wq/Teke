package com.example.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class Language(val code: String, val label: String) {
    object English : Language("en", "English")
    object Amharic : Language("am", "አማርኛ")
}

object LocalizationManager {
    var currentLanguage by mutableStateOf<Language>(Language.English)

    fun toggleLanguage() {
        currentLanguage = if (currentLanguage == Language.English) {
            Language.Amharic
        } else {
            Language.English
        }
    }

    fun setLanguage(lang: Language) {
        currentLanguage = lang
    }

    // Helper to get localized string dynamically based on Compose State
    fun string(key: String): String {
        val strings = translationMap[key] ?: return key
        return if (currentLanguage == Language.English) strings.en else strings.am
    }

    private data class LocalizedStrings(val en: String, val am: String)

    private val translationMap = mapOf(
        "app_tagline" to LocalizedStrings("Curated Premium Gifts", "የተመረጡ ውድ ስጦታዎች መገበያያ"),
        "search_hint" to LocalizedStrings("Search luxury gifts...", "ውድ ስጦታዎችን እዚህ ይፈልጉ..."),
        "search_voice" to LocalizedStrings("Listening for gift ideas...", "የስጦታ ሀሳብዎን በመስማት ላይ..."),
        "nav_home" to LocalizedStrings("Home", "ዋና ገጽ"),
        "nav_categories" to LocalizedStrings("Categories", "ምድቦች"),
        "nav_favorites" to LocalizedStrings("Favorites", "የወደዱት"),
        "nav_cart" to LocalizedStrings("Cart", "ቅርጫት"),
        "nav_profile" to LocalizedStrings("Profile", "ማንነት"),
        
        "welcome" to LocalizedStrings("Welcome back,", "እንኳን ደህና መጡ፣"),
        "guest" to LocalizedStrings("Valued Client", "ክቡር ደንበኛ"),
        "hero_title" to LocalizedStrings("The Art of Giving", "የስጦታ ውበት ጥበብ"),
        "hero_desc" to LocalizedStrings("Discover handcrafted, exclusive collections designed for memorable moments.", "ለማይረሱ ትዝታዎች በጥንቃቄ የተሰሩ ድንቅ ስብስቦችን ይመልከቱ።"),
        "categories_title" to LocalizedStrings("Shop by Occasion", "ለአጋጣሚዎች ይግዙ"),
        "trending_products" to LocalizedStrings("Exclusive Collections", "ልዩ ስብስቦች"),
        "all_products" to LocalizedStrings("All Luxury Items", "ሁሉም ውድ ዕቃዎች"),
        "empty_search" to LocalizedStrings("No premium matches found", "ምንም የተገኘ ዕቃ የለም"),
        "empty_favorites" to LocalizedStrings("Your wishlist is empty", "ያልተቀመጠ ስጦታ የለም"),
        "empty_favorites_desc" to LocalizedStrings("Explore our catalog and tap the heart on items that capture your emotion.", "ካታሎጋችንን ያስሱ እና ልብዎን የነካውን ዕቃ ይምረጡ።"),
        "empty_cart" to LocalizedStrings("Your shopping bag is empty", "ቅርጫትዎ ባዶ ነው"),
        "empty_cart_desc" to LocalizedStrings("Once you find a perfect item, add it to start creating a personalized luxury order.", "የሚፈልጉትን ስጦታ ሲያገኙ እዚህ ይጨምሩት።"),
        
        "add_to_cart" to LocalizedStrings("Add to Cart", "ቅርጫት ውስጥ ጨምር"),
        "buy_now" to LocalizedStrings("Buy Now", "አሁኑኑ ይግዙ"),
        "quantity" to LocalizedStrings("Quantity", "ብዛት"),
        "specifications" to LocalizedStrings("Specifications", "ዝርዝር መግለጫዎች"),
        "delivery_info" to LocalizedStrings("Delivery details", "የማድረሻ መረጃ"),
        "delivery_desc" to LocalizedStrings("Hand-delivered in luxury premium packaging within 1-2 hours in Addis Ababa.", "በጥንቃቄ ታሽጎ በአዲስ አበባ ውስጥ ከ1-2 ሰዓት በእጅዎ ይደርሳል።"),
        "reviews" to LocalizedStrings("Client Appreciations", "የደንበኞች ምስክርነት"),
        "related_products" to LocalizedStrings("Related Exquisite Items", "ሌሎች የሚመጥኑ ስጦታዎች"),
        
        "buy_confirm_title" to LocalizedStrings("Confirm Order", "ትዕዛዝ ያረጋግጡ"),
        "buy_confirm_desc" to LocalizedStrings("You are ordering '%s'. We will automatically direct you to our premium phone order desk to finalize.", "በአሁኑ ሰዓት '%s' ማዘዝ ይፈልጋሉ? ትዕዛዙን ለመጨረስ ወደ የጥሪ ማዕከላችን በቀጥታ እናገናኝዎታለን።"),
        "call_agent" to LocalizedStrings("Connect with VIP Concierge", "ከቪአይፒ ወኪል ጋር ተገናኝ"),
        "cancel" to LocalizedStrings("Cancel", "ይቅር"),
        "cart_total" to LocalizedStrings("Total Value", "አጠቃላይ ዋጋ"),
        "checkout" to LocalizedStrings("Proceed to Call Order", "ትዕዛዝ ለመስጠት ይደውሉ"),
        "favorites_count" to LocalizedStrings("%d Exquisite Items", "%d የተወደዱ ዕቃዎች"),
        
        "profile_title" to LocalizedStrings("VIP Profile", "የቪአይፒ መገለጫ"),
        "profile_lang" to LocalizedStrings("Language / ቋንቋ", "ቋንቋ / Language"),
        "profile_theme" to LocalizedStrings("Visual Theme", "የገጽታ ቀለም"),
        "profile_theme_dark" to LocalizedStrings("Dark Obsidian", "ጥቁር ገጽታ"),
        "profile_theme_light" to LocalizedStrings("Light Ivory", "ነጭ ገጽታ"),
        "profile_help" to LocalizedStrings("VIP Concierge & Help", "እርዳታ እና ድጋፍ"),
        "profile_about" to LocalizedStrings("About Teke Promotion", "ስለ ተከ ፕሮሞሽን"),
        "profile_history" to LocalizedStrings("Order History", "የማዘዣ ታሪክ"),
        "profile_logout" to LocalizedStrings("Logout Session", "ከቪአይፒ አካውንት ውጣ"),
        
        "contact_title" to LocalizedStrings("Direct Concierge Channels", "ቀጥታ የመገናኛ መንገዶች"),
        "contact_desc" to LocalizedStrings("Reach our design studio or support channels directly for bespoke orders.", "ለልዩ ትዕዛዞች ቀጥታ የመገናኛ መስመሮቻችንን ይጠቀሙ።"),
        "copy_success" to LocalizedStrings("Copied to clipboard", "ኮፒ ተደርጓል"),
        "gift_assistance" to LocalizedStrings("AI Gift Finder", "አርቴፊሻል የስጦታ ረዳት"),
        "gift_assistance_sub" to LocalizedStrings("Tell Gemini what you are celebrating today...", "ምን ዓይነት ስጦታ እንደሚፈልጉ ረዳታችንን ይጠይቁ..."),
        "assistant_greeting" to LocalizedStrings("Welcome to Teke Promotion. I am your personal Gemini Gift Curator. Tell me about the person and the occasion, and I will recommend the perfect gift.", "እንኳን ደህና መጡ። እኔ የግል የስጦታ መጋቢ ነኝ። የሚፈልጉትን ስጦታ ባህሪ ይንገሩኝ እና እኔ ምርጡን እመርጥልዎታለሁ።"),
        "assistant_btn" to LocalizedStrings("Ask Luxury Curator", "ረዳቱን ይጠይቁ"),
        "generating" to LocalizedStrings("Curating perfect suggestions...", "ምርጥ ሀሳብ በመምረጥ ላይ...")
    )
}
