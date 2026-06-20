package com.aptech.finalproject.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.finalproject.enums.RegistrationResult;
import com.aptech.finalproject.model.Event;
import com.aptech.finalproject.repository.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepo;


    public EventService() {
    }


    public RegistrationResult register(Long id) {
        Optional<Event> eventOpt = eventRepo.findById(id);

        if (eventOpt.isEmpty()){
            return RegistrationResult.NOT_FOUND;
        }

        Event event = eventOpt.get();

        if (event.getAvailableTickets() > 0) {
            event.setAvailableTickets(event.getAvailableTickets() - 1);
            eventRepo.save(event);
            return RegistrationResult.SUCCESS;
        } 

        return RegistrationResult.SOLD_OUT;
    }

}
