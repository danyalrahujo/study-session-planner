package com.example.studyplanner.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.repository.StudySessionRepository;
import com.example.studyplanner.repository.mongo.MongoStudySessionRepository;
import com.example.studyplanner.view.StudyPlannerView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class StudySessionControllerIT {
	@Mock

	private StudyPlannerView view;
	private StudySessionRepository repository;
	private StudySessionController controller;
	private AutoCloseable closeable;

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		repository = new MongoStudySessionRepository(new MongoClient(new ServerAddress("localhost", 27017)));
		for (StudySession studySession : repository.findAll()) {
			repository.delete(studySession.getId());
		}
		controller = new StudySessionController(repository, view);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllStudySessions() {
		StudySession studySession = new StudySession("1", "Study TDD", false, null, null);
		repository.save(studySession);
		controller.getAllStudySessions();
		verify(view).displayStudySessions(asList(studySession));
	}

	@Test
	public void testNewStudySession() {
		StudySession studySession = new StudySession("1", "Study TDD", false, null, null);
		controller.addStudySession(studySession);
		verify(view).addStudySession(studySession);
	}

	@Test
	public void testDeleteStudySession() {
		StudySession studySession = new StudySession("1", "Study TDD", false, null, null);
		repository.save(studySession);
		controller.deleteStudySession(studySession);
		verify(view).deleteStudySession(studySession);
	}

	@Test
	public void testUpdateStudySession() {
		StudySession original = new StudySession("1", "Study TDD", false, null, null);
		repository.save(original);
		StudySession updated = new StudySession("1", "Study Mockito", false, null, null);
		controller.updateStudySession(updated);
		verify(view).updateStudySession(updated);
	}

}
