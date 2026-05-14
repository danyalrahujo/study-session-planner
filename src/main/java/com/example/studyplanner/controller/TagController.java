package com.example.studyplanner.controller;

import com.example.studyplanner.model.Tag;
import com.example.studyplanner.repository.TagRepository;
import java.util.List;

public class TagController {

	private TagRepository tagRepository;

	public TagController(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	public void addTag(Tag tag) {
		tagRepository.save(tag);
	}

	public List<Tag> getAllTags() {
		return tagRepository.findAll();
	}

	public void deleteTag(String id) {
		tagRepository.delete(id);
	}

	public void updateTag(Tag tag) {
		tagRepository.update(tag);
	}
}