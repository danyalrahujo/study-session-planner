package com.example.studyplanner.model;

import java.util.List;

public class StudySession {
	private String id;
	private String description;
	private boolean completed;
	private String createdAt;
	private List<Tag> tags;

	public StudySession() {
	}

	public StudySession(String id, String description, boolean completed, String createdAt, List<Tag> tags) {
		this.id = id;
		this.description = description;
		this.completed = completed;
		this.createdAt = createdAt;
		this.tags = tags;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		StudySession that = (StudySession) o;

		return id != null ? id.equals(that.id) : that.id == null;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
    public String toString() {
        return "StudySession{" +
						"id='" + id + "'" +
						", description='" + description + "'" +
                ", completed=" + completed +
						", createdAt='" + createdAt + "'" +
                ", tags=" + tags +
                '}';
    }
}