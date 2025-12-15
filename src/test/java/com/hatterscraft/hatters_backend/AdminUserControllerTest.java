package com.hatterscraft.hatters_backend;

import com.hatterscraft.hatters_backend.controller.AdminUserController;
import com.hatterscraft.hatters_backend.model.Item;
import com.hatterscraft.hatters_backend.model.Role;
import com.hatterscraft.hatters_backend.model.User;
import com.hatterscraft.hatters_backend.repository.RoleRepository;
import com.hatterscraft.hatters_backend.repository.UserRepository;
import com.hatterscraft.hatters_backend.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private AdminUserController controller;

    @Test
    void getInventory_shouldReturnItems() {
        Item item = new Item();
        when(inventoryService.getInventory(1L)).thenReturn(List.of(item));

        var items = controller.getInventory(1L);

        assertEquals(1, items.size());
        assertEquals(item, items.get(0));
    }

    @Test
    void addItem_shouldReturnItem() {
        Item item = new Item();
        when(inventoryService.addItem(1L, item)).thenReturn(item);

        Item result = controller.addItem(1L, item);

        assertEquals(item, result);
    }

    @Test
    void deleteItem_shouldCallService() {
        doNothing().when(inventoryService).deleteItem(1L, 2L);

        controller.deleteItem(1L, 2L);

        verify(inventoryService, times(1)).deleteItem(1L, 2L);
    }
}
