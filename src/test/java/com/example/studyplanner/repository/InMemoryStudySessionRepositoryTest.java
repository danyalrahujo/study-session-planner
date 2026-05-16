package com.example.studyplanner.repository;

import com.example.studyplanner.model.StudySession;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InMemoryStudySessionRepositoryTest {

	private InMemoryStudySessionRepository inMemoryStudySessionRepository;

	@Before
	public void setUp() {

		inMemoryStudySessionRepository = new InMemoryStudySessionRepository();
	}

	@Test
	public void testSave() {

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		inMemoryStudySessionRepository.save(studySession);

		List<StudySession> result = inMemoryStudySessionRepository.findAll();

		assertEquals(1, result.size());
	}

	@Test
	public void testFindById() {

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		inMemoryStudySessionRepository.save(studySession);

		StudySession result = inMemoryStudySessionRepository.findById("1");

		assertEquals("Study TDD", result.getDescription());
	}
}