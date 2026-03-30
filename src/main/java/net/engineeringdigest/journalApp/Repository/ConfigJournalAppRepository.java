package net.engineeringdigest.journalApp.Repository;

import net.engineeringdigest.journalApp.Entity.ConfigJournalApp;
import net.engineeringdigest.journalApp.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalApp, ObjectId> {
}
