package com.example.studyplanner.repository;

import java.util.List;
import java.util.ArrayList;

import com.example.studyplanner.model.StudySession;

public class InMemoryStudySessionRepository implements StudySessionRepository {
	private List<StudySession> studySessions = new ArrayList<>();

	@Override
	public void save(StudySession studySession) {
		studySessions.add(studySession);

	}

	@Override
	public List<StudySession> findAll() {
		return studySessions;
	}

	@Override
	public StudySession findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(StudySession studySession) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<StudySession> findByTag(String tagId) {
		// TODO Auto-generated method stub
		return null;
	}

}