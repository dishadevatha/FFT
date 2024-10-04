package com.project.fft

data class CartItem(
    val menuItem: MenuItem,
    var quantity: Int = 1
)

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    // Add an item to the cart or increase its quantity
    fun addItemToCart(menuItem: MenuItem) {
        val existingItem = cartItems.find { it.menuItem.itemName == menuItem.itemName }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            cartItems.add(CartItem(menuItem))
        }
    }

    // Set cart items (used when receiving items from another activity)
    fun setCartItems(menuItems: List<MenuItem>) {
        cartItems.clear()  // Clear existing items
        for (menuItem in menuItems) {
            cartItems.add(CartItem(menuItem))  // Add each menu item as a CartItem
        }
    }

    // Get all items in the cart
    fun getCartItems(): List<CartItem> {
        return cartItems
    }

    // Get the total price of the cart
    fun getTotalPrice(): Double {
        return cartItems.sumByDouble { it.menuItem.price * it.quantity }
    }

    // Update quantity of an item in the cart
    fun updateItemQuantity(cartItem: CartItem, quantity: Int) {
        cartItem.quantity = quantity
        if (cartItem.quantity <= 0) {
            cartItems.remove(cartItem)
        }
    }

    // Clear the cart
    fun clearCart() {
        cartItems.clear()
    }
}