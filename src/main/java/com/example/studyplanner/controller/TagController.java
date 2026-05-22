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

		tagRepository.save(tag);

		studyPlannerView.addTag(tag);
	}

	public void getAllTags() {

		studyPlannerView.displayTags(tagRepository.findAll());
	}

	public void deleteTag(Tag tag) {

		tagRepository.delete(tag.getId());

		studyPlannerView.removeTag(tag);
	}

	public void updateTag(Tag tag) {

		tagRepository.update(tag);

		studyPlannerView.updateTag(tag);
	}

	public Tag findTagById(String id) {

		return tagRepository.findById(id);
	}
}