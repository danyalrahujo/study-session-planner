package com.example.studyplanner.repository.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.example.studyplanner.model.Tag;
import com.example.studyplanner.repository.TagRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class MongoTagRepository implements TagRepository {

	public static final String TAG_COLLECTION_NAME = "tag";

	public static final String STUDY_PLANNER_DB_NAME = "studyPlanner";

	private MongoCollection<Document> tagCollection;

	public MongoTagRepository(MongoClient client) {

		tagCollection = client.getDatabase(STUDY_PLANNER_DB_NAME).getCollection(TAG_COLLECTION_NAME);
	}

	@Override
	public void save(Tag tag) {

		Document document = new Document();

		document.append("id", tag.getId());
		document.append("name", tag.getName());

		tagCollection.insertOne(document);
	}

	@Override
	public List<Tag> findAll() {

		List<Tag> tags = new ArrayList<>();

		for (Document document : tagCollection.find()) {

			Tag tag = new Tag(document.getString("id"), document.getString("name"));

			tags.add(tag);
		}

		return tags;
	}

	@Override
	public Tag findById(String id) {

		Document document = tagCollection.find(Filters.eq("id", id)).first();

		if (document != null) {

			return new Tag(document.getString("id"), document.getString("name"));
		}

		return null;
	}

	@Override
	public void update(Tag tag) {

		Document document = new Document();

		document.append("id", tag.getId());
		document.append("name", tag.getName());

		tagCollection.replaceOne(Filters.eq("id", tag.getId()), document);
	}

	@Override
	public void delete(String id) {

		tagCollection.deleteOne(Filters.eq("id", id));
	}
}