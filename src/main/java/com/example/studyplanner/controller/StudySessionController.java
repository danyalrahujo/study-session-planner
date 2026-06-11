package com.example.studyplanner.controller;

import java.util.List;

import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.model.Tag;
import com.example.studyplanner.repository.StudySessionRepository;
import com.example.studyplanner.view.StudyPlannerView;

public class StudySessionController {

	private StudySessionRepository studySessionRepository;
	private StudyPlannerView studyPlannerView;

	public StudySessionController(StudySessionRepository studySessionRepository, StudyPlannerView studyPlannerView) {
		this.studySessionRepository = studySessionRepository;
		this.studyPlannerView = studyPlannerView;
	}

	public void addStudySession(StudySession studySession) {
		StudySession existingStudySession = studySessionRepository.findById(studySession.getId());

		if (existingStudySession != null) {
			studyPlannerView.showStudySessionError("Study session already exists with id " + studySession.getId(),
					studySession);
			return;
		}

		studySessionRepository.save(studySession);
		studyPlannerView.addStudySession(studySession);
	}

	public void getAllStudySessions() {
		studyPlannerView.displayStudySessions(studySessionRepository.findAll());
	}

	public void deleteStudySession(StudySession studySession) {
		if (studySession == null) {
			return;
		}

		StudySession existingStudySession = studySessionRepository.findById(studySession.getId());

		if (existingStudySession == null) {
			studyPlannerView.showStudySessionError("No study session exists with id " + studySession.getId(),
					studySession);
			return;
		}

		studySessionRepository.delete(studySession.getId());
		studyPlannerView.deleteStudySession(studySession);
	}

	public void updateStudySession(StudySession studySession) {
		if (studySession == null) {
			return;
		}

		studySessionRepository.update(studySession);
		studyPlannerView.updateStudySession(studySession);
	}

	public StudySession findStudySessionById(String id) {
		return studySessionRepository.findById(id);
	}

	public List<StudySession> findStudySessionsByTag(String tagId) {
		return studySessionRepository.findByTag(tagId);
	}

	public void assignTagToStudySession(StudySession studySession, Tag tag) {
		studySession.getTags().add(tag);
	}

	public void removeTagFromStudySession(StudySession studySession, Tag tag) {
		studySession.getTags().remove(tag);
	}
}