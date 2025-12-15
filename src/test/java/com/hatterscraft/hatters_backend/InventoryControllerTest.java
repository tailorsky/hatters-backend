package com.hatterscraft.hatters_backend;

import com.hatterscraft.hatters_backend.controller.InventoryController;
import com.hatterscraft.hatters_backend.model.Item;
import com.hatterscraft.hatters_backend.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController controller;

    @Mock
    private Authentication authentication;

    @Test
    void getInventory_shouldReturnUserInventory() {
        when(authentication.getPrincipal()).thenReturn("1");
        Item item = new Item();
        when(inventoryService.getInventory(1L)).thenReturn(List.of(item));

        var result = controller.getInventory(authentication);
        assertEquals(1, result.size());
    }

    @Test
    void addItem_shouldReturnAddedItem() {
        when(authentication.getPrincipal()).thenReturn("1");
        Item item = new Item();
        when(inventoryService.addItem(1L, item)).thenReturn(item);

        Item result = controller.addItem(item, authentication);
        assertEquals(item, result);
    }

    @Test
    void toggleEquip_shouldCallService() {
        when(authentication.getPrincipal()).thenReturn("1");
        Item item = new Item();
        when(inventoryService.toggleEquip(2L, true, 1L)).thenReturn(item);

        Item result = controller.toggleEquip(2L, true, authentication);
        assertEquals(item, result);
    }

    @Test
    void getInventoryByUser_shouldReturnItems() {
        Item item = new Item();
        when(inventoryService.getInventory(2L)).thenReturn(List.of(item));

        var result = controller.getInventoryByUser(2L);
        assertEquals(1, result.size());
    }

    @Test
    void addItemToUser_shouldReturnItem() {
        Item item = new Item();
        when(inventoryService.addItem(2L, item)).thenReturn(item);

        Item result = controller.addItemToUser(2L, item);
        assertEquals(item, result);
    }

    @Test
    void deleteItemFromUser_shouldCallService() {
        doNothing().when(inventoryService).deleteItem(2L, 3L);
        controller.deleteItemFromUser(2L, 3L);
        verify(inventoryService, times(1)).deleteItem(2L, 3L);
    }
}
