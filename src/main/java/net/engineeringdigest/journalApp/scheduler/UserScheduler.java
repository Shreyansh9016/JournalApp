package net.engineeringdigest.journalApp.scheduler;

import net.engineeringdigest.journalApp.Entity.JournalEntry;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private AppCache appCache;

//    @Autowired
//    private SentimentAnalysisService sentimentAnalysisService;

//    @Scheduled(cron = "0 * * ? * *")
//    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAnsSendSAMails() {
        try {
            List<User> users = userRepository.getUserForSA()    ;
            System.out.println(users);
            for (User user : users) {
                List<JournalEntry> journalEntries = user.getJournalEntries();
                List<Sentiment> sentiments = journalEntries.stream().filter(x ->
                                x.getDate().
                                        isAfter(LocalDateTime.now().minusDays(15))).
                        map(x -> x.getSentiment()).collect(Collectors.toList());
                Map<Sentiment,Integer> sentimentCount = new HashMap<>();
                for(Sentiment sentiment : sentiments){
                    if(sentiment != null){
                        sentimentCount.put(sentiment,sentimentCount.getOrDefault(sentiment,0)+1);
                    }
                }
                Sentiment mostFrequentSentiment = null;
                int max = 0;
                for(Map.Entry<Sentiment,Integer> entry : sentimentCount.entrySet()){
                    if(entry.getValue() > max){
                        max = entry.getValue();
                        mostFrequentSentiment = entry.getKey();
                    }
                }
                if(mostFrequentSentiment != null){
                    emailService.sendEmail(user.getEmail(), "Sentiment for last 15 days", mostFrequentSentiment.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0/10 0 ? * * ")
    public void clearAppCache(){
        appCache.init();
    }
}
