package com.example.studyplanner.repository;

import java.util.List;
import java.util.ArrayList;
import com.example.studyplanner.model.Tag;

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

		for (StudySession studySession : studySessions) {

			if (studySession.getId().equals(id)) {
				return studySession;
			}
		}

		return null;
	}

	@Override
	public void update(StudySession updatedStudySession) {

		for (int i = 0; i < studySessions.size(); i++) {

			StudySession currentStudySession = studySessions.get(i);

			if (currentStudySession.getId().equals(updatedStudySession.getId())) {

				studySessions.set(i, updatedStudySession);
			}
		}
	}

	@Override
	public void delete(String id) {

		studySessions.removeIf(studySession -> studySession.getId().equals(id));
	}

	@Override
	public List<StudySession> findByTag(String tagId) {

		List<StudySession> filteredStudySessions = new ArrayList<>();

		for (StudySession studySession : studySessions) {

			List<Tag> tags = studySession.getTags();

			if (tags != null) {

				for (Tag tag : tags) {

					if (tag.getId().equals(tagId)) {

						filteredStudySessions.add(studySession);

						break;
					}
				}
			}
		}

		return filteredStudySessions;
	}

}