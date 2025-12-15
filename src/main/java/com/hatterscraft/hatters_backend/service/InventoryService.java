package com.hatterscraft.hatters_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hatterscraft.hatters_backend.model.Item;
import com.hatterscraft.hatters_backend.model.User;
import com.hatterscraft.hatters_backend.repository.ItemRepository;
import com.hatterscraft.hatters_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public List<Item> getInventory(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getInventory();
    }

    public Item addItem(Long userId, Item item) {
        User user = userRepository.findById(userId).orElseThrow();

        Item savedItem = itemRepository.save(item);

        user.getInventory().add(savedItem);
        userRepository.save(user);

        return savedItem;
    }

    public Item toggleEquip(Long itemId, boolean equipped, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow();

        // Проверяем владельца предмета
        User user = userRepository.findById(userId).orElseThrow();
        if (!user.getInventory().contains(item)) {
            throw new RuntimeException("Этот предмет не принадлежит пользователю");
        }

        item.setEquipped(equipped);
        return itemRepository.save(item);
    }

    public void deleteItem(Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow();
        Item item = itemRepository.findById(itemId).orElseThrow();

        if (!user.getInventory().contains(item)) {
            throw new RuntimeException("Этот предмет не принадлежит пользователю");
        }

        user.getInventory().remove(item);
        userRepository.save(user);

        itemRepository.delete(item);
    }
}
