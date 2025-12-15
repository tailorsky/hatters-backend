package com.hatterscraft.hatters_backend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.hatterscraft.hatters_backend.model.Item;
import com.hatterscraft.hatters_backend.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // ====================== ДЛЯ ИГРОКОВ ======================

    @GetMapping
    public List<Item> getInventory(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return inventoryService.getInventory(Long.valueOf(userId));
    }

    @PostMapping("/add")
    public Item addItem(@RequestBody Item item, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return inventoryService.addItem(Long.valueOf(userId), item);
    }

    @PostMapping("/{itemId}/equip")
    public Item toggleEquip(
            @PathVariable Long itemId,
            @RequestParam boolean equipped,
            Authentication authentication
    ) {
        String userId = (String) authentication.getPrincipal();
        return inventoryService.toggleEquip(itemId, equipped, Long.valueOf(userId));
    }

    // ====================== ДЛЯ АДМИНОВ ======================

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Item> getInventoryByUser(@PathVariable Long userId) {
        return inventoryService.getInventory(userId);
    }

    @PostMapping("/user/{userId}/add")
    @PreAuthorize("hasRole('ADMIN')")
    public Item addItemToUser(@PathVariable Long userId, @RequestBody Item item) {
        return inventoryService.addItem(userId, item);
    }

    @DeleteMapping("/user/{userId}/items/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteItemFromUser(@PathVariable Long userId, @PathVariable Long itemId) {
        inventoryService.deleteItem(userId, itemId);
    }
}
