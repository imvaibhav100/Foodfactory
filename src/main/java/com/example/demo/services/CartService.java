package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.CartItem;
import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.repositories.CartItemRepository;

@Component
public class CartService {
	@Autowired
	private CartItemRepository cartItemRepository;

	public List<CartItem> getCartItems(User user) {
		return cartItemRepository.findByUser(user);
	}

	public void addToCart(User user, Product product, int quantity) {
		CartItem existingItem = cartItemRepository.findByUserAndProduct_Pid(user, product.getPid());
		
		if (existingItem != null) {
			existingItem.setQuantity(existingItem.getQuantity() + quantity);
			existingItem.setTotalPrice(existingItem.getQuantity() * product.getPprice());
			cartItemRepository.save(existingItem);
		} else {
			CartItem newItem = new CartItem();
			newItem.setUser(user);
			newItem.setProduct(product);
			newItem.setQuantity(quantity);
			newItem.setTotalPrice(quantity * product.getPprice());
			cartItemRepository.save(newItem);
		}
	}

	public void updateCartItemQuantity(int cartItemId, int quantity) {
		CartItem item = cartItemRepository.findById(cartItemId).orElse(null);
		if (item != null) {
			item.setQuantity(quantity);
			item.setTotalPrice(quantity * item.getProduct().getPprice());
			cartItemRepository.save(item);
		}
	}

	public void removeFromCart(int cartItemId) {
		cartItemRepository.deleteById(cartItemId);
	}

	@Transactional
	public void clearCart(User user) {
		cartItemRepository.deleteByUser(user);
	}

	public double getCartTotal(User user) {
		List<CartItem> items = getCartItems(user);
		return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
	}
}
