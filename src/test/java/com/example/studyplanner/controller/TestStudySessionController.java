package com.example.studyplanner.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.model.Tag;
import com.example.studyplanner.repository.StudySessionRepository;

public class TestStudySessionController {

	private StudySessionController studySessionController;
	private StudySessionRepository studySessionRepository;

	@Before
	public void setUp() {
		studySessionRepository = mock(StudySessionRepository.class);
		studySessionController = new StudySessionController(studySessionRepository);
	}

	@Test
	public void testAddStudySession() {
		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		studySessionController.addStudySession(studySession);

		verify(studySessionRepository).save(studySession);
	}

	@Test
	public void testGetAllStudySessions() {

		when(studySessionRepository.findAll())
				.thenReturn(java.util.Arrays.asList(new StudySession("1", "Study TDD", false, "2025-05-10", null),
						new StudySession("2", "Study Mockito", false, "2025-05-11", null)));

		java.util.List<StudySession> studySessions = studySessionController.getAllStudySessions();

		org.junit.Assert.assertEquals(2, studySessions.size());
	}

	@Test
	public void testDeleteStudySession() {

		studySessionController.deleteStudySession("1");

		verify(studySessionRepository).delete("1");
	}

	@Test
	public void testUpdateStudySession() {

		StudySession studySession = new StudySession("1", "Updated Study Session", false, "2025-05-10", null);

		studySessionController.updateStudySession(studySession);

		verify(studySessionRepository).update(studySession);
	}

	@Test
	public void testFindStudySessionById() {

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		when(studySessionRepository.findById("1")).thenReturn(studySession);

		StudySession result = studySessionController.findStudySessionById("1");

		org.junit.Assert.assertNotNull(result);

		org.junit.Assert.assertEquals("Study TDD", result.getDescription());
	}

	@Test
	public void testFindStudySessionsByTag() {

		Tag tag = new Tag("1", "Programming");

		StudySession studySession1 = new StudySession("1", "Study Java", false, "2025-05-10", null);

		StudySession studySession2 = new StudySession("2", "Study Mockito", false, "2025-05-11", null);

		when(studySessionRepository.findByTag("1")).thenReturn(java.util.Arrays.asList(studySession1, studySession2));

		java.util.List<StudySession> result = studySessionController.findStudySessionsByTag("1");

		org.junit.Assert.assertEquals(2, result.size());
	}
}