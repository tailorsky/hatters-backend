package com.hatterscraft.hatters_backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hatterscraft.hatters_backend.model.UserEventReward;
import com.hatterscraft.hatters_backend.repository.EventRepository;
import com.hatterscraft.hatters_backend.repository.UserEventRewardRepository;
import com.hatterscraft.hatters_backend.repository.UserRepository;
import com.hatterscraft.hatters_backend.model.User;
import com.hatterscraft.hatters_backend.model.Event;
import com.hatterscraft.hatters_backend.model.EventCondition;
import com.hatterscraft.hatters_backend.model.Item;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventRewardService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final UserEventRewardRepository userEventRewardRepository;
    private final InventoryService inventoryService;

    @Scheduled(fixedRate = 60000)
    public void checkEvents(){
        LocalDateTime now = LocalDateTime.now();
        Optional<Event> currentEventOpt = eventRepository.findCurrentEvent(LocalDateTime.now());

        currentEventOpt.ifPresent(event -> {
            for (User user : userRepository.findAll()) {
                boolean alreadyRewarded = userEventRewardRepository.existsByUserIdAndEventId(user.getId(), event.getId());
                if (alreadyRewarded) continue;

                if (conditionsMet(user, event)) {
                    giveRewards(user, event);
                }
            }
        });
    }

    private boolean conditionsMet(User user, Event event) {
        for(EventCondition condition : event.getConditions()) {
            switch(condition.getConditionType()) {
                case "play_time":
                    long secondsPlayed = user.getPlayTimeSeconds();
                    if(secondsPlayed < Long.parseLong(condition.getValue())) return false;
                    break;
                case "quests_completed":
                    if(user.getQuestsCompleted() < Integer.parseInt(condition.getValue())) return false;
                    break;
                case "level":
                    if(user.getLevel() < Integer.parseInt(condition.getValue())) return false;
                    break;
                case "custom":
                    if(!checkCustomCondition(user, condition.getValue())) return false;
                    break;
            }
        }
        return true;
    }

    private void giveRewards(User user, Event event) {
        for(String reward : event.getRewards()) {
            // добавляем предмет в инвентарь сайта
            Item item = new Item();
            item.setName(reward);
            item.setDescription("Reward from event " + event.getTitle());
            inventoryService.addItem(user.getId(), item);
        }

        // помечаем, что игрок получил награду
        UserEventReward uer = new UserEventReward();
        uer.setUserId(user.getId());
        uer.setEventId(event.getId());
        uer.setRewardedAt(LocalDateTime.now());
        userEventRewardRepository.save(uer);
    }

    private boolean checkCustomCondition(User user, String value) {
        // например, value = "collected_all_items"
        // можно написать любую логику проверки
        return true;
    }
}
