package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username){
        /*
        user ko find kr liya
        date set kr di
        journal entry save kr li aur uski id variable me store kra di
        in memory user ke journal entries me id add kr di
        aur collections me store kra di
         */
        try {
            User user = userService.findByUsername(username);

            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            if (user.getJournalEntries() == null) {
                user.setJournalEntries(new ArrayList<>());
            }

            user.getJournalEntries().add(saved);

            userService.saveUser(user);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public void saveEntry(JournalEntry journalEntry){
        /*
        user ko find kr liya
        date set kr di
        journal entry save kr li aur uski id variable me store kra di
        in memory user ke journal entries me id add kr di
        aur collections me store kra di
         */
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String username){
        boolean removed = false;
        try{
            User user = userService.findByUsername(username);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(removed){
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        }catch (Exception e){
            log.info("Error",e);
            throw new RuntimeException("An error occurred while deleting the journal entry.");
        }
        return removed;
    }
}
