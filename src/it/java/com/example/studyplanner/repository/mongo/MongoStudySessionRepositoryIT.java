package com.example.studyplanner.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

public class MongoStudySessionRepositoryIT {

	@ClassRule
	public static GenericContainer<?> mongo = new GenericContainer<>("mongo:4.4.3").withExposedPorts(27017);

	@Test
	public void testContainerIsRunning() {

		assertThat(mongo.isRunning()).isTrue();
	}
}