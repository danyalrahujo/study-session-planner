package com.example.studyplanner.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import org.junit.Before;
import org.junit.Test;

import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.model.Tag;
import com.example.studyplanner.repository.StudySessionRepository;
import com.example.studyplanner.view.StudyPlannerView;

public class TestStudySessionController {

	private StudySessionController studySessionController;

	private StudySessionRepository studySessionRepository;

	private StudyPlannerView studyPlannerView;

	@Before
	public void setUp() {

		studySessionRepository = mock(StudySessionRepository.class);

		studyPlannerView = mock(StudyPlannerView.class);

		studySessionController = new StudySessionController(studySessionRepository, studyPlannerView);
	}

	@Test
	public void testAddStudySession() {

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		studySessionController.addStudySession(studySession);

		verify(studySessionRepository).save(studySession);

		verify(studyPlannerView).addStudySession(studySession);
	}

	@Test
	public void testGetAllStudySessions() {

		StudySession studySession1 = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		StudySession studySession2 = new StudySession("2", "Study Mockito", false, "2025-05-11", null);

		when(studySessionRepository.findAll()).thenReturn(asList(studySession1, studySession2));

		studySessionController.getAllStudySessions();

		verify(studyPlannerView).displayStudySessions(asList(studySession1, studySession2));
	}

	@Test
	public void testDeleteStudySession() {

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		when(studySessionRepository.findById("1")).thenReturn(studySession);

		studySessionController.deleteStudySession(studySession);

		verify(studySessionRepository).delete("1");

		verify(studyPlannerView).deleteStudySession(studySession);
	}

	@Test
	public void testUpdateStudySession() {

		StudySession studySession = new StudySession("1", "Updated Study Session", false, "2025-05-10", null);

		studySessionController.updateStudySession(studySession);

		verify(studySessionRepository).update(studySession);

		verify(studyPlannerView).updateStudySession(studySession);
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

		StudySession studySession1 = new StudySession("1", "Study Java", false, "2025-05-10", null);

		StudySession studySession2 = new StudySession("2", "Study Mockito", false, "2025-05-11", null);

		when(studySessionRepository.findByTag("1")).thenReturn(asList(studySession1, studySession2));

		java.util.List<StudySession> result = studySessionController.findStudySessionsByTag("1");

		org.junit.Assert.assertEquals(2, result.size());
	}

	@Test
	public void testAssignTagToStudySession() {

		Tag tag = new Tag("1", "Programming");

		StudySession studySession = new StudySession("1", "Study Java", false, "2025-05-10",
				new java.util.ArrayList<>());

		studySessionController.assignTagToStudySession(studySession, tag);

		org.junit.Assert.assertEquals(1, studySession.getTags().size());
	}

	@Test
	public void testRemoveTagFromStudySession() {

		Tag tag = new Tag("1", "Programming");

		java.util.List<Tag> tags = new java.util.ArrayList<>();

		tags.add(tag);

		StudySession studySession = new StudySession("1", "Study Java", false, "2025-05-10", tags);

		studySessionController.removeTagFromStudySession(studySession, tag);

		org.junit.Assert.assertEquals(0, studySession.getTags().size());
	}

	@Test
	public void testAddExistingStudySession() {

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		when(studySessionRepository.findById("1")).thenReturn(studySession);

		studySessionController.addStudySession(studySession);

		verify(studyPlannerView).showStudySessionError("Study session already exists with id 1", studySession);

		verify(studySessionRepository, never()).save(studySession);
	}

	@Test
	public void testDeleteNonExistingStudySession() {

		StudySession studySession = new StudySession("1", "Study TDD", false, "2025-05-10", null);

		when(studySessionRepository.findById("1")).thenReturn(null);

		studySessionController.deleteStudySession(studySession);

		verify(studyPlannerView).showStudySessionError("No study session exists with id 1", studySession);

		verify(studySessionRepository, never()).delete("1");
	}
}