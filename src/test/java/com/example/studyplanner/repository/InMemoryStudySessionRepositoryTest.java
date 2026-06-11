package com.example.studyplanner.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.model.Tag;

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

	@Test
	public void testDelete() {

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		inMemoryStudySessionRepository.save(studySession);

		inMemoryStudySessionRepository.delete("1");

		List<StudySession> result = inMemoryStudySessionRepository.findAll();

		assertEquals(0, result.size());
	}

	@Test
	public void testUpdate() {

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		inMemoryStudySessionRepository.save(studySession);

		StudySession updatedStudySession = new StudySession("1", "Study Mockito", false, "2025-05-10", null);

		inMemoryStudySessionRepository.update(updatedStudySession);

		StudySession result = inMemoryStudySessionRepository.findById("1");

		assertEquals("Study Mockito", result.getDescription());
	}

	@Test
	public void testFindByTag() {

		Tag tag = new Tag("1", "Programming");

		List<Tag> tags = new java.util.ArrayList<>();

		tags.add(tag);

		StudySession studySession = new StudySession("1", "Study Java", false, "2025-05-10", tags);

		inMemoryStudySessionRepository.save(studySession);

		List<StudySession> result = inMemoryStudySessionRepository.findByTag("1");

		assertEquals(1, result.size());
	}

	@Test
	public void testFindByIdReturnsNullWhenSessionDoesNotExist() {
		assertNull(inMemoryStudySessionRepository.findById("999"));
	}

	@Test
	public void testUpdateDoesNothingWhenSessionDoesNotExist() {
		StudySession session = new StudySession("1", "Math", false, "", null);

		inMemoryStudySessionRepository.save(session);

		StudySession updated = new StudySession("999", "Physics", false, "", null);

		inMemoryStudySessionRepository.update(updated);

		assertEquals("Math", inMemoryStudySessionRepository.findById("1").getDescription());
	}

	@Test
	public void testFindByTagReturnsEmptyWhenSessionHasNullTags() {
		StudySession session = new StudySession("1", "Math", false, "", null);

		inMemoryStudySessionRepository.save(session);

		assertTrue(inMemoryStudySessionRepository.findByTag("tag1").isEmpty());
	}

	@Test
	public void testFindByTagReturnsMatchingSession() {
		Tag tag = new Tag("tag1", "Java");

		List<Tag> tags = new ArrayList<>();
		tags.add(tag);

		StudySession session = new StudySession("1", "Math", false, "", tags);

		inMemoryStudySessionRepository.save(session);

		List<StudySession> result = inMemoryStudySessionRepository.findByTag("tag1");

		assertEquals(1, result.size());
		assertEquals(session, result.get(0));
	}

	@Test
	public void testFindByTagReturnsEmptyWhenTagDoesNotExist() {
		Tag tag = new Tag("tag1", "Java");

		List<Tag> tags = new ArrayList<>();
		tags.add(tag);

		StudySession session = new StudySession("1", "Math", false, "", tags);

		inMemoryStudySessionRepository.save(session);

		assertTrue(inMemoryStudySessionRepository.findByTag("tag999").isEmpty());
	}
}