package net.engineeringdigest.journalApp.cache;

import net.engineeringdigest.journalApp.Entity.ConfigJournalApp;
import net.engineeringdigest.journalApp.Repository.ConfigJournalAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    public Map<String,String> APP_CACHE;

    @PostConstruct
    public void init(){
        List<ConfigJournalApp> all = configJournalAppRepository.findAll();
        APP_CACHE = null;
        for(ConfigJournalApp configJournalApp : all){
            APP_CACHE.put(configJournalApp.getKey() , configJournalApp.getValue());
        }
    }
}
