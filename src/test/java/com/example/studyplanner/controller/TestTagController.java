package com.example.studyplanner.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.example.studyplanner.model.Tag;
import com.example.studyplanner.repository.TagRepository;
import com.example.studyplanner.view.StudyPlannerView;

public class TestTagController {

	private TagController tagController;

	private TagRepository tagRepository;

	private StudyPlannerView studyPlannerView;

	@Before
	public void setUp() {

		tagRepository = mock(TagRepository.class);

		studyPlannerView = mock(StudyPlannerView.class);

		tagController = new TagController(tagRepository, studyPlannerView);
	}

	@Test
	public void testAddTag() {

		Tag tag = new Tag("1", "Programming");

		tagController.addTag(tag);

		verify(tagRepository).save(tag);

		verify(studyPlannerView).addTag(tag);
	}

	@Test
	public void testGetAllTags() {

		Tag tag1 = new Tag("1", "Programming");

		Tag tag2 = new Tag("2", "Java");

		when(tagRepository.findAll()).thenReturn(asList(tag1, tag2));

		tagController.getAllTags();

		verify(studyPlannerView).displayTags(asList(tag1, tag2));
	}

	@Test
	public void testDeleteTag() {

		Tag tag = new Tag("1", "Programming");

		when(tagRepository.findById("1")).thenReturn(tag);

		tagController.deleteTag(tag);

		verify(tagRepository).delete("1");

		verify(studyPlannerView).deleteTag(tag);
	}

	@Test
	public void testUpdateTag() {

		Tag tag = new Tag("1", "Updated Tag");

		tagController.updateTag(tag);

		verify(tagRepository).update(tag);

		verify(studyPlannerView).updateTag(tag);
	}

	@Test
	public void testFindTagById() {

		Tag tag = new Tag("1", "Programming");

		when(tagRepository.findById("1")).thenReturn(tag);

		Tag result = tagController.findTagById("1");

		org.junit.Assert.assertNotNull(result);

		org.junit.Assert.assertEquals("Programming", result.getName());
	}

	@Test
	public void testAddExistingTag() {

		Tag tag = new Tag("1", "Programming");

		when(tagRepository.findById("1")).thenReturn(tag);

		tagController.addTag(tag);

		verify(studyPlannerView).showTagError("Tag already exists with id 1", tag);

		verify(tagRepository, never()).save(tag);
	}

	@Test
	public void testDeleteNonExistingTag() {

		Tag tag = new Tag("1", "Programming");

		when(tagRepository.findById("1")).thenReturn(null);

		tagController.deleteTag(tag);

		verify(studyPlannerView).showTagError("No tag exists with id 1", tag);

		verify(tagRepository, never()).delete("1");
	}
}