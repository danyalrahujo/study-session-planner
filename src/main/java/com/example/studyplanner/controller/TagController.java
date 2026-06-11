package com.example.studyplanner.controller;

import com.example.studyplanner.model.Tag;
import com.example.studyplanner.repository.TagRepository;
import com.example.studyplanner.view.StudyPlannerView;

public class TagController {

	private TagRepository tagRepository;
	private StudyPlannerView studyPlannerView;

	public TagController(TagRepository tagRepository, StudyPlannerView studyPlannerView) {
		this.tagRepository = tagRepository;
		this.studyPlannerView = studyPlannerView;
	}

	public void addTag(Tag tag) {
		Tag existingTag = tagRepository.findById(tag.getId());

		if (existingTag != null) {
			studyPlannerView.showTagError("Tag already exists with id " + tag.getId(), tag);
			return;
		}

		tagRepository.save(tag);
		studyPlannerView.addTag(tag);
	}

	public void getAllTags() {
		studyPlannerView.displayTags(tagRepository.findAll());
	}

	public void deleteTag(Tag tag) {
		if (tag == null) {
			return;
		}

		Tag existingTag = tagRepository.findById(tag.getId());

		if (existingTag == null) {
			studyPlannerView.showTagError("No tag exists with id " + tag.getId(), tag);
			return;
		}

		tagRepository.delete(tag.getId());
		studyPlannerView.deleteTag(tag);
	}

	public void updateTag(Tag tag) {
		if (tag == null) {
			return;
		}

		tagRepository.update(tag);
		studyPlannerView.updateTag(tag);
	}

	public Tag findTagById(String id) {
		return tagRepository.findById(id);
	}
}