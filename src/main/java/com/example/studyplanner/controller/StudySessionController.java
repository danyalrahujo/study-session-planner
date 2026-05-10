package com.example.studyplanner.controller;

import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.repository.StudySessionRepository;

public class StudySessionController {

	private StudySessionRepository studySessionRepository;

	public StudySessionController(StudySessionRepository studySessionRepository) {
		this.studySessionRepository = studySessionRepository;
	}

	public void addStudySession(StudySession studySession) {
		studySessionRepository.save(studySession);
	}
}