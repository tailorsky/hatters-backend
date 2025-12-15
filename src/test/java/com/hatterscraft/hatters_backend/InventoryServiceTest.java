package com.hatterscraft.hatters_backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hatterscraft.hatters_backend.model.Item;
import com.hatterscraft.hatters_backend.model.User;
import com.hatterscraft.hatters_backend.repository.ItemRepository;
import com.hatterscraft.hatters_backend.repository.UserRepository;
import com.hatterscraft.hatters_backend.service.InventoryService;


@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void addItem_shouldAddItemToUserInventory() {
        User user = new User();
        user.setId(1L);
        user.setInventory(new ArrayList<>());

        Item item = new Item();
        item.setName("Sword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item saved = inventoryService.addItem(1L, item);

        assertThat(user.getInventory()).contains(saved);
        verify(userRepository).save(user);
    }

    @Test
    void toggleEquip_shouldThrowIfNotOwner() {
        User user = new User();
        user.setInventory(new ArrayList<>());

        Item item = new Item();
        item.setId(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        assertThatThrownBy(() ->
                inventoryService.toggleEquip(1L, true, 2L)
        ).isInstanceOf(RuntimeException.class);
    }
}
