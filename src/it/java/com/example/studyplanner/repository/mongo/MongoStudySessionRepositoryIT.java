package com.example.studyplanner.repository.mongo;

import static com.example.studyplanner.repository.mongo.MongoStudySessionRepository.STUDY_PLANNER_DB_NAME;
import static com.example.studyplanner.repository.mongo.MongoStudySessionRepository.STUDY_SESSION_COLLECTION_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.studyplanner.model.StudySession;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoStudySessionRepositoryIT {

	private MongoClient client;

	private MongoStudySessionRepository studySessionRepository;

	private MongoCollection<Document> studySessionCollection;

	@Before
	public void setup() {

		client = new MongoClient(new ServerAddress("localhost", 27017));
		studySessionRepository = new MongoStudySessionRepository(client);

		MongoDatabase database = client.getDatabase(STUDY_PLANNER_DB_NAME);

		database.drop();

		studySessionCollection = database.getCollection(STUDY_SESSION_COLLECTION_NAME);
	}

	@After
	public void tearDown() {

		client.close();
	}

	@Test
	public void testFindAll() {

		addTestStudySessionToDatabase("1", "Study TDD");

		addTestStudySessionToDatabase("2", "Study Mockito");

		assertThat(studySessionRepository.findAll()).containsExactly(
				new StudySession("1", "Study TDD", false, null, null),
				new StudySession("2", "Study Mockito", false, null, null));
	}

	@Test
	public void testFindById() {

		addTestStudySessionToDatabase("1", "Study TDD");

		addTestStudySessionToDatabase("2", "Study Mockito");

		assertThat(studySessionRepository.findById("2"))
				.isEqualTo(new StudySession("2", "Study Mockito", false, null, null));
	}

	@Test
	public void testSave() {

		StudySession studySession = new StudySession("1", "Study TDD", false, null, null);

		studySessionRepository.save(studySession);

		assertThat(readAllStudySessionsFromDatabase()).containsExactly(studySession);
	}

	@Test
	public void testDelete() {

		addTestStudySessionToDatabase("1", "Study TDD");

		studySessionRepository.delete("1");

		assertThat(readAllStudySessionsFromDatabase()).isEmpty();
	}

	@Test
	public void testUpdate() {

		StudySession studySession = new StudySession("1", "Study TDD", false, null, null);

		studySessionRepository.save(studySession);

		assertThat(readAllStudySessionsFromDatabase()).containsExactly(studySession);

		StudySession updatedStudySession = new StudySession("1", "Study Mockito", false, null, null);

		studySessionRepository.update(updatedStudySession);

		assertThat(readAllStudySessionsFromDatabase()).containsExactly(updatedStudySession);
	}

	private void addTestStudySessionToDatabase(String id, String description) {

		studySessionCollection.insertOne(new Document().append("id", id).append("description", description));
	}

	private List<StudySession> readAllStudySessionsFromDatabase() {

		List<StudySession> studySessions = new ArrayList<>();

		for (Document document : studySessionCollection.find()) {

			StudySession studySession = new StudySession(document.getString("id"), document.getString("description"),
					false, null, null);

			studySessions.add(studySession);
		}

		return studySessions;
	}

}