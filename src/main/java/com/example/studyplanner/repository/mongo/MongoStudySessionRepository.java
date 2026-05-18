package com.example.studyplanner.repository.mongo;

import java.util.List;
import java.util.ArrayList;

import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.repository.StudySessionRepository;
import com.mongodb.MongoClient;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

public class MongoStudySessionRepository implements StudySessionRepository {
	public static final String STUDY_SESSION_COLLECTION_NAME = "studySession";

	public static final String STUDY_PLANNER_DB_NAME = "studyPlanner";

	private MongoCollection<Document> studySessionCollection;

	public MongoStudySessionRepository(MongoClient client) {

		studySessionCollection = client.getDatabase(STUDY_PLANNER_DB_NAME).getCollection(STUDY_SESSION_COLLECTION_NAME);
	}

	@Override
	public void save(StudySession studySession) {

		Document document = new Document();

		document.append("id", studySession.getId());
		document.append("description", studySession.getDescription());

		studySessionCollection.insertOne(document);
	}

	@Override
	public List<StudySession> findAll() {
		List<StudySession> studySessions = new ArrayList<>();

		for (Document document : studySessionCollection.find()) {

			StudySession studySession = new StudySession(document.getString("id"), document.getString("description"),
					false, null, null);

			studySessions.add(studySession);
		}

		return studySessions;
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