package com.example.studyplanner.repository.mongo;

import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.model.Tag;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.bson.Document;
import java.util.List;
import java.util.ArrayList;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class MongoStudySessionRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;
	private MongoStudySessionRepository mongoStudySessionRepository;

	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(serverAddress));

		mongoStudySessionRepository = new MongoStudySessionRepository(client);

		client.getDatabase(MongoStudySessionRepository.STUDY_PLANNER_DB_NAME).drop();
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAllWhenDatabaseIsEmpty() {

		assertThat(mongoStudySessionRepository.findAll()).isEmpty();
	}

	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		mongoStudySessionRepository.save(studySession);

		assertThat(mongoStudySessionRepository.findAll()).hasSize(1);
	}

	@Test
	public void testFindById() {

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		mongoStudySessionRepository.save(studySession);

		StudySession result = mongoStudySessionRepository.findById("1");

		assertThat(result.getDescription()).isEqualTo("Study TDD");
	}

	@Test
	public void testFindByIdReturnsNullWhenStudySessionDoesNotExist() {

		assertThat(mongoStudySessionRepository.findById("missing")).isNull();
	}

	@Test
	public void testUpdate() {

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		mongoStudySessionRepository.save(studySession);

		StudySession updatedStudySession = new StudySession("1", "Study Mockito", false, "2025-05-10", null);

		mongoStudySessionRepository.update(updatedStudySession);

		StudySession result = mongoStudySessionRepository.findById("1");

		assertThat(result.getDescription()).isEqualTo("Study Mockito");
	}

	@Test
	public void testDelete() {

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		mongoStudySessionRepository.save(studySession);

		mongoStudySessionRepository.delete("1");

		assertThat(mongoStudySessionRepository.findAll()).isEmpty();
	}

	@Test
	public void testFindByTag() {

		List<Tag> tags = new ArrayList<>();

		tags.add(new Tag("1", "java"));

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", tags);

		mongoStudySessionRepository.save(studySession);

		List<StudySession> result = mongoStudySessionRepository.findByTag("java");

		assertThat(result).hasSize(1);
	}

	@Test
	public void testFindByTagReturnsEmptyWhenTagNotFound() {

		List<Tag> tags = new ArrayList<>();
		tags.add(new Tag("1", "java"));

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", tags);

		mongoStudySessionRepository.save(studySession);

		List<StudySession> result = mongoStudySessionRepository.findByTag("spring");

		assertThat(result).isEmpty();
	}

	@Test
	public void testFindByTagWhenStudySessionHasNoTags() {

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		mongoStudySessionRepository.save(studySession);

		List<StudySession> result = mongoStudySessionRepository.findByTag("java");

		assertThat(result).isEmpty();
	}

	@Test
	public void testFindByTagWhenStoredDocumentHasNoTagsField() {

		client.getDatabase(MongoStudySessionRepository.STUDY_PLANNER_DB_NAME)
				.getCollection(MongoStudySessionRepository.STUDY_SESSION_COLLECTION_NAME)
				.insertOne(new Document("id", "1").append("description", "Study TDD"));

		List<StudySession> result = mongoStudySessionRepository.findByTag("java");

		assertThat(result).isEmpty();
	}
}
