package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class CartProduct(
    val product: Product,
    val quantity: Int
) {
    val totalPrice: Double
        get() = product.price * quantity
}

class GiftRepository(private val giftDao: GiftDao) {

    // 1. Core Catalog API
    val catalog: List<Product> = ProductCatalog.items

    fun getProductById(id: Int): Product? {
        return catalog.find { it.id == id }
    }

    fun getProductsByCategory(category: String): List<Product> {
        return catalog.filter { it.category.equals(category, ignoreCase = true) }
    }

    fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) return catalog
        return catalog.filter {
            it.titleEn.contains(query, ignoreCase = true) ||
            it.titleAm.contains(query, ignoreCase = true) ||
            it.descEn.contains(query, ignoreCase = true) ||
            it.descAm.contains(query, ignoreCase = true) ||
            it.category.contains(query, ignoreCase = true)
        }
    }

    // 2. Favorites Repository Flow
    val favoriteProducts: Flow<List<Product>> = giftDao.getAllFavorites().map { entities ->
        entities.mapNotNull { entity ->
            getProductById(entity.productId)
        }
    }

    suspend fun toggleFavorite(productId: Int) {
        val isFav = giftDao.isFavorite(productId) > 0
        if (isFav) {
            giftDao.deleteFavoriteById(productId)
        } else {
            giftDao.insertFavorite(FavoriteEntity(productId))
        }
    }

    suspend fun isProductFavorite(productId: Int): Boolean {
        return giftDao.isFavorite(productId) > 0
    }

    // 3. Cart Repository Flow
    val cartProducts: Flow<List<CartProduct>> = giftDao.getAllCartItems().map { entities ->
        entities.mapNotNull { entity ->
            val product = getProductById(entity.productId)
            if (product != null) {
                CartProduct(product, entity.quantity)
            } else {
                null
            }
        }
    }

    suspend fun addToCart(productId: Int, quantity: Int = 1) {
        giftDao.insertCartItem(CartEntity(productId, quantity))
    }

    suspend fun updateCartQuantity(productId: Int, quantity: Int) {
        if (quantity <= 0) {
            giftDao.deleteCartItem(productId)
        } else {
            giftDao.updateCartQuantity(productId, quantity)
        }
    }

    suspend fun removeFromCart(productId: Int) {
        giftDao.deleteCartItem(productId)
    }

    suspend fun clearCart() {
        giftDao.clearCart()
    }
}
