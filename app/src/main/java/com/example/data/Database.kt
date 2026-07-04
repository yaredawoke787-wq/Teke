package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val productId: Int,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey val productId: Int,
    val quantity: Int,
    val addedAt: Long = System.currentTimeMillis()
)

@Dao
interface GiftDao {
    // Favorites
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE productId = :productId")
    suspend fun deleteFavoriteById(productId: Int)

    @Query("SELECT COUNT(*) FROM favorites WHERE productId = :productId")
    suspend fun isFavorite(productId: Int): Int

    // Cart
    @Query("SELECT * FROM cart ORDER BY addedAt DESC")
    fun getAllCartItems(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cart: CartEntity)

    @Query("UPDATE cart SET quantity = :quantity WHERE productId = :productId")
    suspend fun updateCartQuantity(productId: Int, quantity: Int)

    @Query("DELETE FROM cart WHERE productId = :productId")
    suspend fun deleteCartItem(productId: Int)

    @Query("DELETE FROM cart")
    suspend fun clearCart()
}

@Database(entities = [FavoriteEntity::class, CartEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun giftDao(): GiftDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "teke_promotion_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
