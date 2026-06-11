package com.example.studyplanner.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.model.Tag;
import com.example.studyplanner.repository.StudySessionRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class MongoStudySessionRepository implements StudySessionRepository {
	public static final String STUDY_SESSION_COLLECTION_NAME = "studySession";

	public static final String STUDY_PLANNER_DB_NAME = "studyPlanner";
	private static final String ID = "id";
	private static final String DESCRIPTION = "description";
	private static final String TAGS = "tags";
	private MongoCollection<Document> studySessionCollection;

	public MongoStudySessionRepository(MongoClient client) {
		this(client, STUDY_PLANNER_DB_NAME);
	}

	public MongoStudySessionRepository(MongoClient client, String databaseName) {

		studySessionCollection = client.getDatabase(databaseName).getCollection(STUDY_SESSION_COLLECTION_NAME);
	}

	@Override
	public void save(StudySession studySession) {

		Document document = new Document();

		document.append(ID, studySession.getId());

		document.append(DESCRIPTION, studySession.getDescription());
		List<String> tagNames = new ArrayList<>();

		if (studySession.getTags() != null) {

			for (Tag tag : studySession.getTags()) {
				tagNames.add(tag.getName());
			}
		}

		document.append(TAGS, tagNames);
		studySessionCollection.insertOne(document);
	}

	@Override
	public List<StudySession> findAll() {
		List<StudySession> studySessions = new ArrayList<>();

		for (Document document : studySessionCollection.find()) {

			StudySession studySession = new StudySession(

					document.getString("id"),

					document.getString(DESCRIPTION),

					false, null, null);

			studySessions.add(studySession);
		}

		return studySessions;
	}

	@Override
	public StudySession findById(String id) {
		Document document =

				studySessionCollection.find(new Document(ID, id)).first();
		if (document == null) {
			return null;
		}

		return new StudySession(

				document.getString(ID),

				document.getString(DESCRIPTION),

				false, null, null);
	}

	@Override
	public void update(StudySession studySession) {

		Document document = new Document();

		document.append(ID, studySession.getId());
		document.append(DESCRIPTION, studySession.getDescription());

		studySessionCollection.replaceOne(Filters.eq(ID, studySession.getId()), document);
	}

	@Override
	public void delete(String id) {

		studySessionCollection.deleteOne(Filters.eq(ID, id));
	}

	@Override
	public List<StudySession> findByTag(String tagId) {

		List<StudySession> studySessions = new ArrayList<>();

		for (Document document : studySessionCollection.find()) {

			List<String> tags = (List<String>) document.get(TAGS);
			if (tags != null && tags.contains(tagId)) {

				StudySession studySession =

						new StudySession(

								document.getString(ID),

								document.getString(DESCRIPTION),

								false, null, null);

				studySessions.add(studySession);
			}
		}

		return studySessions;
	}
}