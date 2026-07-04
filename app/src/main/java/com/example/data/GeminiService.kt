package com.example.data

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@JsonClass(generateAdapter = true)
data class Part(
    @Json(name = "text") val text: String? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    @Json(name = "parts") val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    @Json(name = "contents") val contents: List<Content>,
    @Json(name = "systemInstruction") val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(
    @Json(name = "content") val content: Content
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    @Json(name = "candidates") val candidates: List<Candidate>
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }
}

class GeminiAssistantManager {
    suspend fun queryAssistant(userPrompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        
        // Graceful safety check for empty or placeholder key
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY" || apiKey.contains("PLACEHOLDER")) {
            return@withContext getLocalCreativeSuggestion(userPrompt) + 
                    "\n\n*(Note: Running in offline prototype mode. To enable real-time Gemini AI, enter your GEMINI_API_KEY in the AI Studio Secrets panel.)*"
        }

        val systemPrompt = """
            You are the Elite AI Gift Curator for "Teke Promotion", a premium, luxury gift selling platform in Ethiopia. 
            Your role is to offer highly personalized, sophisticated, and warm recommendations.
            Here is our official curated catalog of exquisite gifts:
            1. Royal Crimson Rose & Truffle Box (6,800 ETB) - Circular black velvet box, 24 preserved roses, 16 gold-dusted Belgian chocolate truffles. Perfect for Valentine's, anniversaries, romance.
            2. Presidential Gold Chronograph Set (18,500 ETB) - Automatic mechanical chronograph watch (gold/sapphire), 14K gold nib lacquer fountain pen, Italian calf-leather slim wallet in a wooden oak chest. Perfect for weddings, corporate milestones, VIP retirements.
            3. Majestic Orchid & Champagne Basket (9,500 ETB) - White Phalaenopsis orchids, non-alcoholic sparkling grape elixir in a gilded bottle, crystal flutes, gold-wrapped pralines in suede suede-lined basket. Perfect for weddings, elegance, congratulations.
            4. Signature Handcrafted Gift Chest (12,500 ETB) - Obsidian hardwood box, personalized engraved crystal plaque, organic lavender soy candle in marble, metallic cardholder, dark truffles. Perfect for birthdays, bespoke appreciation.
            5. Exotic Floral Sensation Dome (5,400 ETB) - Ecuadorian roses and hydrangeas sealed in tempered glass cloche dome with rotating oak base, warm micro-LED lights. Perfect for romance, home decor, flowers lovers.
            6. Bespoke Royal Truffle Tray (4,500 ETB) - 36 dark/milk/white luxury spiced truffles on brushed brass tray with mirror glass detail. Perfect for chocolate aficionados, parties, hosting.

            Suggest 1 or 2 items from our actual catalog above that match the user's celebration occasion. 
            Describe them in highly elegant, sensory-rich prose. Explain why this specific choice embodies premium refinement.
            Use a professional, warm, and elite tone. Mention the price in ETB. Speak directly to the client. Keep the response under 160 words.
        """.trimIndent()

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = userPrompt)))),
            systemInstruction = Content(parts = listOf(Part(text = systemPrompt)))
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: "I apologize, my luxury recommendation engine returned an empty result. Please let me try curating again."
        } catch (e: Exception) {
            // Fallback gracefully on network issues
            getLocalCreativeSuggestion(userPrompt) + "\n\n*(Curated in local backup mode due to network: ${e.localizedMessage})*"
        }
    }

    private fun getLocalCreativeSuggestion(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("wedding") || lower.contains("marriage") || lower.contains("💍") || lower.contains("ሰርግ") -> {
                "For a momentous wedding celebration, I highly recommend our **Presidential Gold Chronograph Set** (18,500 ETB) or **Majestic Orchid & Champagne Basket** (9,500 ETB).\n\n" +
                "These offerings represent the pinnacle of timeless union, combining elegant mechanical artistry with cascading Phalaenopsis orchids to leave a breathtaking, lifelong impression."
            }
            lower.contains("birthday") || lower.contains("ልደት") || lower.contains("🎂") -> {
                "For a distinguished birthday, our **Signature Handcrafted Gift Chest** (12,500 ETB) provides an unparalleled bespoke experience.\n\n" +
                "Encased in an obsidian hardwood case, the personalized engraved crystal plaque and marble soy candle speak directly to the recipient's premium status."
            }
            lower.contains("love") || lower.contains("rose") || lower.contains("valentine") || lower.contains("አበባ") || lower.contains("🌹") -> {
                "To express deep romantic devotion, our **Royal Crimson Rose & Truffle Box** (6,800 ETB) is the absolute standard of romance.\n\n" +
                "Lined with black velvet, the 24 preserved crimson roses paired with 24K gold-dusted Belgian truffles communicate luxury and eternal affection effortlessly."
            }
            else -> {
                "To celebrate this beautiful occasion, I recommend our signature **Royal Crimson Rose & Truffle Box** (6,800 ETB) or **Signature Handcrafted Gift Chest** (12,500 ETB).\n\n" +
                "Each is handcrafted with extreme care, combining luxury presentation with premium quality to elevate your gifting into an unforgettable work of art."
            }
        }
    }
}
