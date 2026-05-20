package com.example.studyplanner.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import org.bson.Document;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import org.testcontainers.containers.GenericContainer;

import com.example.studyplanner.model.StudySession;

import static com.example.studyplanner.repository.mongo.MongoStudySessionRepository.STUDY_PLANNER_DB_NAME;
import static com.example.studyplanner.repository.mongo.MongoStudySessionRepository.STUDY_SESSION_COLLECTION_NAME;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoStudySessionRepositoryIT {

	@SuppressWarnings("rawtypes")

	@ClassRule
	public static final GenericContainer mongo = new GenericContainer("mongo:4.4.2").withExposedPorts(27017);

	private MongoClient client;

	private MongoStudySessionRepository studySessionRepository;

	private MongoCollection<Document> studySessionCollection;

	@Before
	public void setup() {

		client = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));

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

		assertThat(studySessionRepository.findAll()).isEmpty();
	}
}