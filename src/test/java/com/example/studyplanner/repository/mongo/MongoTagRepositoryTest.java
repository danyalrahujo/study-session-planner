package com.example.studyplanner.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.studyplanner.model.Tag;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class MongoTagRepositoryTest {

	private static MongoServer server;

	private static InetSocketAddress serverAddress;

	private MongoClient client;

	private MongoTagRepository mongoTagRepository;

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

		client.getDatabase(MongoTagRepository.STUDY_PLANNER_DB_NAME).drop();

		mongoTagRepository = new MongoTagRepository(client);
	}

	@After
	public void tearDown() {

		client.close();
	}

	@Test
	public void testFindAllWhenDatabaseIsEmpty() {

		assertThat(mongoTagRepository.findAll()).isEmpty();
	}

	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {

		Tag tag = new Tag("1", "java");

		mongoTagRepository.save(tag);

		assertThat(mongoTagRepository.findAll()).hasSize(1);
	}

	@Test
	public void testFindById() {

		Tag tag = new Tag("1", "java");

		mongoTagRepository.save(tag);

		Tag result = mongoTagRepository.findById("1");

		assertThat(result.getName()).isEqualTo("java");
	}

	@Test
	public void testFindByIdReturnsNullWhenTagDoesNotExist() {

		assertThat(mongoTagRepository.findById("missing")).isNull();
	}

	@Test
	public void testUpdate() {

		Tag tag = new Tag("1", "java");

		mongoTagRepository.save(tag);

		Tag updatedTag = new Tag("1", "mongodb");

		mongoTagRepository.update(updatedTag);

		Tag result = mongoTagRepository.findById("1");

		assertThat(result.getName()).isEqualTo("mongodb");
	}

	@Test
	public void testDelete() {

		Tag tag = new Tag("1", "java");

		mongoTagRepository.save(tag);

		mongoTagRepository.delete("1");

		assertThat(mongoTagRepository.findAll()).isEmpty();
	}
}
