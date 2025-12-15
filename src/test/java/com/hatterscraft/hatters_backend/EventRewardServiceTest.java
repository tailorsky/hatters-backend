package com.hatterscraft.hatters_backend;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hatterscraft.hatters_backend.model.Event;
import com.hatterscraft.hatters_backend.model.EventCondition;
import com.hatterscraft.hatters_backend.model.Item;
import com.hatterscraft.hatters_backend.model.User;
import com.hatterscraft.hatters_backend.model.UserEventReward;
import com.hatterscraft.hatters_backend.repository.EventRepository;
import com.hatterscraft.hatters_backend.repository.UserEventRewardRepository;
import com.hatterscraft.hatters_backend.repository.UserRepository;
import com.hatterscraft.hatters_backend.service.EventRewardService;
import com.hatterscraft.hatters_backend.service.InventoryService;


@ExtendWith(MockitoExtension.class)
class EventRewardServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserEventRewardRepository userEventRewardRepository;

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private EventRewardService eventRewardService;

    @Test
    void checkEvents_shouldGiveRewardIfConditionsMet() {
        User user = new User();
        user.setId(1L);
        user.setPlayTimeSeconds(1000L);
        user.setQuestsCompleted(10);
        user.setLevel(5);

        EventCondition condition = new EventCondition();
        condition.setConditionType("level");
        condition.setValue("3");

        Event event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setConditions(List.of(condition));
        event.setRewards(List.of("Sword"));

        when(eventRepository.findCurrentEvent(any()))
                .thenReturn(Optional.of(event));
        when(userRepository.findAll())
                .thenReturn(List.of(user));
        when(userEventRewardRepository.existsByUserIdAndEventId(1L, 1L))
                .thenReturn(false);

        eventRewardService.checkEvents();

        verify(inventoryService)
                .addItem(eq(1L), any(Item.class));
        verify(userEventRewardRepository)
                .save(any(UserEventReward.class));
    }

    @Test
    void checkEvents_shouldNotGiveRewardIfAlreadyRewarded() {
        User user = new User();
        user.setId(1L);

        Event event = new Event();
        event.setId(1L);
        event.setConditions(List.of());

        when(eventRepository.findCurrentEvent(any()))
                .thenReturn(Optional.of(event));
        when(userRepository.findAll())
                .thenReturn(List.of(user));
        when(userEventRewardRepository.existsByUserIdAndEventId(1L, 1L))
                .thenReturn(true);

        eventRewardService.checkEvents();

        verifyNoInteractions(inventoryService);
        verify(userEventRewardRepository, never()).save(any());
    }
}
