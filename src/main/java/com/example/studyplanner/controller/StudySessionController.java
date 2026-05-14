package com.example.studyplanner.controller;

import com.example.studyplanner.model.StudySession;
import java.util.List;
import com.example.studyplanner.repository.StudySessionRepository;

public class StudySessionController {

	private StudySessionRepository studySessionRepository;

	public StudySessionController(StudySessionRepository studySessionRepository) {
		this.studySessionRepository = studySessionRepository;
	}

	public void addStudySession(StudySession studySession) {
		studySessionRepository.save(studySession);
	}

	public List<StudySession> getAllStudySessions() {
		return studySessionRepository.findAll();
	}

	public void deleteStudySession(String id) {
		studySessionRepository.delete(id);
	}

	public void updateStudySession(StudySession studySession) {
		studySessionRepository.update(studySession);
	}
}