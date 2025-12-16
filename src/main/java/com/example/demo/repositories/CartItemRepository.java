package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.CartItem;
import com.example.demo.entities.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
	List<CartItem> findByUser(User user);
	CartItem findByUserAndProduct_Pid(User user, int productId);
	void deleteByUser(User user);
}
