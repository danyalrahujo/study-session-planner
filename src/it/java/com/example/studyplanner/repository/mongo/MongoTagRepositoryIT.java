package com.example.studyplanner.repository.mongo;

import static com.example.studyplanner.repository.mongo.MongoTagRepository.STUDY_PLANNER_DB_NAME;
import static com.example.studyplanner.repository.mongo.MongoTagRepository.TAG_COLLECTION_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.studyplanner.model.Tag;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoTagRepositoryIT {
	private MongoClient client;

	private MongoTagRepository tagRepository;
	private MongoCollection<Document> tagCollection;

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress("localhost", 27017));
		tagRepository = new MongoTagRepository(client);
		MongoDatabase database = client.getDatabase(STUDY_PLANNER_DB_NAME);
		database.drop();
		tagCollection = database.getCollection(TAG_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAll() {
		addTestTagToDatabase("1", "Java");
		addTestTagToDatabase("2", "MongoDB");
		assertThat(tagRepository.findAll()).containsExactly(new Tag("1", "Java"), new Tag("2", "MongoDB"));
	}

	@Test
	public void testFindById() {
		addTestTagToDatabase("1", "Java");
		addTestTagToDatabase("2", "MongoDB");
		assertThat(tagRepository.findById("2")).isEqualTo(new Tag("2", "MongoDB"));
	}

	@Test
	public void testSave() {
		Tag tag = new Tag("1", "Java");
		tagRepository.save(tag);
		assertThat(readAllTagsFromDatabase()).containsExactly(tag);
	}

	@Test
	public void testDelete() {
		addTestTagToDatabase("1", "Java");
		tagRepository.delete("1");
		assertThat(readAllTagsFromDatabase()).isEmpty();
	}

	@Test
	public void testUpdate() {
		Tag tag = new Tag("1", "Java");
		tagRepository.save(tag);
		assertThat(readAllTagsFromDatabase()).containsExactly(tag);
		Tag updatedTag = new Tag("1", "MongoDB");
		tagRepository.update(updatedTag);
		assertThat(readAllTagsFromDatabase()).containsExactly(updatedTag);
	}

	private void addTestTagToDatabase(String id, String name) {
		tagCollection.insertOne(new Document().append("id", id).append("name", name));
	}

	private List<Tag> readAllTagsFromDatabase() {
		List<Tag> tags = new ArrayList<>();
		for (Document document : tagCollection.find()) {
			Tag tag = new Tag(document.getString("id"), document.getString("name"));
			tags.add(tag);
		}
		return tags;
	}

}
