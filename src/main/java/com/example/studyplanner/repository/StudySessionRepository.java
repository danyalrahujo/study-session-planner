package com.example.studyplanner.repository;

import com.example.studyplanner.model.StudySession;
import java.util.List;

public interface StudySessionRepository {

	void save(StudySession studySession);

	List<StudySession> findAll();

	StudySession findById(String id);

	void update(StudySession studySession);

	void delete(String id);

	List<StudySession> findByTag(String tagId);
}