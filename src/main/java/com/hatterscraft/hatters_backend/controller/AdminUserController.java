package com.hatterscraft.hatters_backend.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.hatterscraft.hatters_backend.model.Item;
import com.hatterscraft.hatters_backend.model.Role;
import com.hatterscraft.hatters_backend.model.User;
import com.hatterscraft.hatters_backend.repository.RoleRepository;
import com.hatterscraft.hatters_backend.repository.UserRepository;
import com.hatterscraft.hatters_backend.service.InventoryService;


@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final InventoryService inventoryService;

    public AdminUserController(UserRepository userRepository,
                               RoleRepository roleRepository,
                               InventoryService inventoryService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.inventoryService = inventoryService;
    }

    @GetMapping("/admin/users")
    public List<Map<String, Object>> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    Set<Role> roles = user.getRoles() != null ? user.getRoles() : new HashSet<>();
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", user.getId());
                    map.put("username", user.getUsername());
                    map.put("roles", roles);
                    return map;
                })
                .toList();
    }

    @PostMapping("/{id}/addRole")
    @PreAuthorize("hasRole('ADMIN')")
    public User addRole(@PathVariable Long id, @RequestParam String roleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @PostMapping("/{id}/removeRoleFromUser")
    @PreAuthorize("hasRole('ADMIN')")
    public User removeRoleFromUser(@PathVariable Long userId, @PathVariable String roleName) {
        User user = userRepository.findById(userId).orElseThrow();
        Role role = roleRepository.findByName(roleName).orElseThrow();

        Set<Role> roles = user.getRoles() != null ? new HashSet<>(user.getRoles()) : new HashSet<>();
        roles.remove(role);
        user.setRoles(roles);
        return userRepository.save(user);
    }
    

    @GetMapping("/{id}/inventory")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Item> getInventory(@PathVariable Long id) {
        return inventoryService.getInventory(id);
    }

    @DeleteMapping("/{id}/inventory/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteItem(@PathVariable Long id, @PathVariable Long itemId) {
        inventoryService.deleteItem(id, itemId);
    }

    @PostMapping("/{id}/inventory")
    @PreAuthorize("hasRole('ADMIN')")
    public Item addItem(@PathVariable Long id, @RequestBody Item item) {
        return inventoryService.addItem(id, item);
    }
}
