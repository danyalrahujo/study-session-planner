package com.example.studyplanner.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.studyplanner.model.Tag;
import com.example.studyplanner.repository.TagRepository;
import com.example.studyplanner.repository.mongo.MongoTagRepository;
import com.example.studyplanner.view.StudyPlannerView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class TagControllerIT {

	@Mock
	private StudyPlannerView view;

	private TagRepository repository;

	private TagController controller;

	private AutoCloseable closeable;

	@Before
	public void setUp() {

		closeable = MockitoAnnotations.openMocks(this);

		repository = new MongoTagRepository(new MongoClient(new ServerAddress("localhost", 27017)));

		for (Tag tag : repository.findAll()) {

			repository.delete(tag.getId());
		}

		controller = new TagController(repository, view);
	}

	@After
	public void releaseMocks() throws Exception {

		closeable.close();
	}

	@Test
	public void testAllTags() {

		Tag tag1 = new Tag("1", "Java");

		repository.save(tag1);

		controller.getAllTags();

		verify(view).displayTags(asList(tag1));
	}

	@Test
	public void testNewTag() {

		Tag tag = new Tag("1", "Java");

		controller.addTag(tag);

		verify(view).addTag(tag);
	}

	@Test
	public void testDeleteTag() {

		Tag tag = new Tag("1", "Java");

		repository.save(tag);

		controller.deleteTag(tag);

		verify(view).removeTag(tag);
	}

	@Test
	public void testUpdateTag() {

		Tag original = new Tag("1", "Java");

		repository.save(original);

		Tag updated = new Tag("1", "MongoDB");

		controller.updateTag(updated);

		verify(view).updateTag(updated);
	}
}