package com.example.studyplanner.view.swing;

import java.awt.EventQueue;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.UUID;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.example.studyplanner.controller.TagController;
import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.model.Tag;
import com.example.studyplanner.view.StudyPlannerView;

public class TagSwingView extends JFrame implements StudyPlannerView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tagNameTextBox;
	private JList<Tag> listTags;
	private DefaultListModel<Tag> listTagsModel;
	private JButton deleteTagButton;
	private JLabel errorMessageLabel;
	private transient TagController tagController;
	private JButton updateTagButton;

	public void setTagController(TagController tagController) {
		this.tagController = tagController;
	}

	DefaultListModel<Tag> getListTagsModel() {
		return listTagsModel;
	}

	public void showTagError(String message, Tag tag) {
		errorMessageLabel.setText(message + ": " + tag);
	}

	public void displayTags(List<Tag> tags) {
		listTagsModel.clear();
		tags.forEach(listTagsModel::addElement);
	}

	public void addTag(Tag tag) {
		listTagsModel.addElement(tag);
		resetErrorLabel();
		tagNameTextBox.setText("");
		listTags.clearSelection();
	}

	@Override

	public void removeTag(Tag tag) {

		listTagsModel.removeElement(tag);

		resetErrorLabel();

		tagNameTextBox.setText("");

		listTags.clearSelection();

	}

	private void resetErrorLabel() {
		errorMessageLabel.setText("");
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			TagSwingView frame = new TagSwingView();
			frame.setVisible(true);
		});
	}

	public TagSwingView() {
		setTitle("Tag View");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout contentPaneLayout = new GridBagLayout();
		contentPaneLayout.columnWidths = new int[] { 0, 0, 0 };
		contentPaneLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		contentPaneLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		contentPaneLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(contentPaneLayout);

		JLabel tagNameLabel = new JLabel("Tag Name");
		GridBagConstraints tagNameLabelConstraints = new GridBagConstraints();
		tagNameLabelConstraints.insets = new Insets(0, 0, 5, 5);
		tagNameLabelConstraints.anchor = GridBagConstraints.EAST;
		tagNameLabelConstraints.gridx = 0;
		tagNameLabelConstraints.gridy = 0;
		contentPane.add(tagNameLabel, tagNameLabelConstraints);

		tagNameTextBox = new JTextField();
		tagNameTextBox.setName("tagNameTextBox");
		GridBagConstraints tagNameTextFieldConstraints = new GridBagConstraints();
		tagNameTextFieldConstraints.insets = new Insets(0, 0, 5, 0);
		tagNameTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
		tagNameTextFieldConstraints.gridx = 1;
		tagNameTextFieldConstraints.gridy = 0;
		contentPane.add(tagNameTextBox, tagNameTextFieldConstraints);
		tagNameTextBox.setColumns(10);

		JButton addTagButton = new JButton("Add Tag");
		addTagButton.setName("addTagButton");
		addTagButton.setEnabled(false);

		GridBagConstraints addTagButtonConstraints = new GridBagConstraints();
		addTagButtonConstraints.insets = new Insets(0, 0, 5, 0);
		addTagButtonConstraints.gridwidth = 2;
		addTagButtonConstraints.gridx = 0;
		addTagButtonConstraints.gridy = 1;
		contentPane.add(addTagButton, addTagButtonConstraints);

		addTagButton.addActionListener(e -> {
			String name = tagNameTextBox.getText().trim();

			if (!name.isEmpty() && tagController != null) {
				Tag tag = new Tag(UUID.randomUUID().toString(), name);
				tagController.addTag(tag);
			}
		});

		KeyAdapter addButtonEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				addTagButton.setEnabled(!tagNameTextBox.getText().trim().isEmpty());
			}
		};

		tagNameTextBox.addKeyListener(addButtonEnabler);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
		scrollPaneConstraints.insets = new Insets(0, 0, 5, 0);
		scrollPaneConstraints.fill = GridBagConstraints.BOTH;
		scrollPaneConstraints.gridwidth = 2;
		scrollPaneConstraints.gridx = 0;
		scrollPaneConstraints.gridy = 2;
		contentPane.add(scrollPane, scrollPaneConstraints);

		listTagsModel = new DefaultListModel<>();
		listTags = new JList<>(listTagsModel);
		scrollPane.setViewportView(listTags);
		listTags.addListSelectionListener(e -> {
			Tag selected = listTags.getSelectedValue();
			boolean selectedItem = selected != null;

			deleteTagButton.setEnabled(selectedItem);
			updateTagButton.setEnabled(selectedItem);

			if (selectedItem) {
				tagNameTextBox.setText(selected.getName());
			}
		});
		listTags.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listTags.setName("tagList");

		updateTagButton = new JButton("Update Tag");
		updateTagButton.setName("updateTagButton");
		updateTagButton.setEnabled(false);

		GridBagConstraints updateTagButtonConstraints = new GridBagConstraints();
		updateTagButtonConstraints.insets = new Insets(0, 0, 5, 5);
		updateTagButtonConstraints.gridx = 0;
		updateTagButtonConstraints.gridy = 3;
		contentPane.add(updateTagButton, updateTagButtonConstraints);

		updateTagButton.addActionListener(e -> {
			Tag selected = listTags.getSelectedValue();

			if (selected != null && tagController != null) {
				Tag updatedTag = new Tag(selected.getId(), tagNameTextBox.getText().trim());
				tagController.updateTag(updatedTag);
			}
		});

		deleteTagButton = new JButton("Delete Selected");
		deleteTagButton.setName("deleteTagButton");
		deleteTagButton.setEnabled(false);
		deleteTagButton.addActionListener(e -> {
			Tag selectedTag = listTags.getSelectedValue();
			if (selectedTag != null && tagController != null) {
				tagController.deleteTag(selectedTag);
			}
		});

		GridBagConstraints deleteTagButtonConstraints = new GridBagConstraints();
		deleteTagButtonConstraints.insets = new Insets(0, 0, 5, 0);
		deleteTagButtonConstraints.gridx = 1;
		deleteTagButtonConstraints.gridy = 3;
		contentPane.add(deleteTagButton, deleteTagButtonConstraints);

		errorMessageLabel = new JLabel("");
		errorMessageLabel.setName("errorMessageLabel");
		GridBagConstraints errorMessageLabelConstraints = new GridBagConstraints();
		errorMessageLabelConstraints.gridwidth = 2;
		errorMessageLabelConstraints.insets = new Insets(0, 0, 0, 5);
		errorMessageLabelConstraints.gridx = 0;
		errorMessageLabelConstraints.gridy = 4;
		contentPane.add(errorMessageLabel, errorMessageLabelConstraints);
	}

	@Override
	public void showStudySessionError(String message, StudySession studySession) {
		// Intentionally left blank: study sessions are managed in
		// StudyPlannerSwingView.
	}

	@Override
	public void displayStudySessions(List<StudySession> studySessions) {
		// Intentionally left blank: study sessions are managed in
		// StudyPlannerSwingView.
	}

	@Override
	public void addStudySession(StudySession studySession) {
		// Intentionally left blank: study sessions are managed in
		// StudyPlannerSwingView.
	}

	@Override
	public void removeStudySession(StudySession studySession) {
		// Intentionally left blank: study sessions are managed in
		// StudyPlannerSwingView.
	}

	@Override
	public void updateStudySession(StudySession studySession) {
		// Intentionally left blank: study sessions are managed in
		// StudyPlannerSwingView.
	}

	@Override
	public void updateTag(Tag tag) {
		int index = listTags.getSelectedIndex();

		if (index >= 0) {
			listTagsModel.set(index, tag);
		}

		resetErrorLabel();
		tagNameTextBox.setText("");
		listTags.clearSelection();
	}

	@Override
	public void deleteStudySession(StudySession studySession) {
		// Intentionally left blank: study sessions are managed in
		// StudyPlannerSwingView.
	}

	@Override
	public void deleteTag(Tag tag) {
		removeTag(tag);
	}
}
