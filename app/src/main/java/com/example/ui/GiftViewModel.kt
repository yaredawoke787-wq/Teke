package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GiftViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = GiftRepository(database.giftDao())
    private val assistantManager = GeminiAssistantManager()

    // --- Dynamic Language State ---
    var currentLanguageFlow = MutableStateFlow<Language>(Language.English)
        private set

    // --- Visual Theme State ---
    var isDarkModeFlow = MutableStateFlow<Boolean>(true) // default to luxury dark mode
        private set

    // --- UI Navigation State ---
    var currentTabFlow = MutableStateFlow<String>("Home")
        private set

    // --- Search Query State ---
    var searchQueryFlow = MutableStateFlow<String>("")
        private set

    // --- Category Filter State ---
    var selectedCategoryFlow = MutableStateFlow<String?>("All")
        private set

    // --- Selected Product for Detail View ---
    var selectedProductFlow = MutableStateFlow<Product?>(null)
        private set

    // --- Buy Now Modal / Confirmation Dialog State ---
    var confirmPurchaseProductFlow = MutableStateFlow<Product?>(null)
        private set

    // --- AI Assistant Flow State ---
    var assistantPromptFlow = MutableStateFlow<String>("")
        private set
    var assistantResponseFlow = MutableStateFlow<String?>(null)
        private set
    var isAssistantLoadingFlow = MutableStateFlow<Boolean>(false)
        private set
    var showAssistantSheetFlow = MutableStateFlow<Boolean>(false)
        private set

    // --- Voice Search Listening Overlay State ---
    var isVoiceListeningFlow = MutableStateFlow<Boolean>(false)
        private set

    // --- Flow-based State Observables (Room Data) ---
    val favoriteProducts: StateFlow<List<Product>> = repository.favoriteProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartProducts: StateFlow<List<CartProduct>> = repository.cartProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Dynamic Products Flow based on Search and Categories ---
    val searchedProducts: StateFlow<List<Product>> = searchQueryFlow
        .debounce(300)
        .map { query ->
            repository.searchProducts(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.catalog)

    // --- Dynamic Total Cart Value Flow ---
    val cartTotalValue: StateFlow<Double> = cartProducts
        .map { items ->
            items.sumOf { it.totalPrice }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    init {
        // Load initial state if any
    }

    // --- Action Methods ---

    fun changeTab(tab: String) {
        currentTabFlow.value = tab
    }

    fun updateSearchQuery(query: String) {
        searchQueryFlow.value = query
    }

    fun selectCategory(category: String?) {
        selectedCategoryFlow.value = category
    }

    fun viewProductDetails(product: Product?) {
        selectedProductFlow.value = product
    }

    fun initiatePurchase(product: Product?) {
        confirmPurchaseProductFlow.value = product
    }

    fun toggleLanguage() {
        LocalizationManager.toggleLanguage()
        currentLanguageFlow.value = LocalizationManager.currentLanguage
    }

    fun setDarkMode(dark: Boolean) {
        isDarkModeFlow.value = dark
    }

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            repository.toggleFavorite(productId)
        }
    }

    fun addToCart(productId: Int, quantity: Int = 1) {
        viewModelScope.launch {
            repository.addToCart(productId, quantity)
        }
    }

    fun updateCartQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(productId, quantity)
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            repository.removeFromCart(productId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun startVoiceSearch() {
        isVoiceListeningFlow.value = true
        // Simulate premium sound-listening, and then autofill a sophisticated search keyword after 2 seconds
        viewModelScope.launch {
            kotlinx.coroutines.delay(2200)
            isVoiceListeningFlow.value = false
            // Autocomplete a luxurious query based on random premium items
            val samples = listOf("Red Roses Box", "Gold Chronograph watch", "Signature Gift Chest", "Premium chocolate")
            searchQueryFlow.value = samples.random()
        }
    }

    fun toggleAssistantSheet(show: Boolean) {
        showAssistantSheetFlow.value = show
        if (show && assistantResponseFlow.value == null) {
            // Set initial greeting
            assistantResponseFlow.value = LocalizationManager.string("assistant_greeting")
        }
    }

    fun sendAssistantMessage(prompt: String) {
        if (prompt.isBlank()) return
        assistantPromptFlow.value = prompt
        isAssistantLoadingFlow.value = true
        assistantResponseFlow.value = null
        
        viewModelScope.launch {
            val response = assistantManager.queryAssistant(prompt)
            assistantResponseFlow.value = response
            isAssistantLoadingFlow.value = false
        }
    }
}
