package com.example.studyplanner.controller;

import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.repository.StudySessionRepository;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

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
}