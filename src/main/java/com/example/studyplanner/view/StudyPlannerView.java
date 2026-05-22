package com.example.studyplanner.view;

import java.util.List;

import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.model.Tag;

public interface StudyPlannerView {
	void displayStudySessions(List<StudySession> studySessions);

	void addStudySession(StudySession studySession);

	void removeStudySession(StudySession studySession);

	void updateStudySession(StudySession studySession);

	void displayTags(List<Tag> tags);

	void addTag(Tag tag);

	void removeTag(Tag tag);

	void updateTag(Tag tag);

}
