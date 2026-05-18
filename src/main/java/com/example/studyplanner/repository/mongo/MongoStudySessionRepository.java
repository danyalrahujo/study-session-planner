package com.example.studyplanner.repository.mongo;

import java.util.List;
import java.util.ArrayList;

import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.repository.StudySessionRepository;
import com.mongodb.MongoClient;

public class MongoStudySessionRepository implements StudySessionRepository {

	public MongoStudySessionRepository(MongoClient client) {

	}

	@Override
	public void save(StudySession studySession) {

	}

	@Override
	public List<StudySession> findAll() {
		return new ArrayList<>();
	}

	@Override
	public StudySession findById(String id) {
		return null;
	}

	@Override
	public void update(StudySession studySession) {

	}

	@Override
	public void delete(String id) {

	}

	@Override
	public List<StudySession> findByTag(String tagId) {
		return null;
	}
}