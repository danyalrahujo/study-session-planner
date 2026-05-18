package com.example.studyplanner.repository.mongo;

import com.example.studyplanner.model.StudySession;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
}