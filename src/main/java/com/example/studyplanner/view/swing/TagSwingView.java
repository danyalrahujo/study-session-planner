package com.example.studyplanner.view.swing;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.example.studyplanner.controller.TagController;
import com.example.studyplanner.model.Tag;

public class TagSwingView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tagNameTextBox;
	private JList<Tag> listTags;
	private DefaultListModel<Tag> listTagsModel;
	private JButton deleteTagButton;
	private JLabel errorMessageLabel;
	private TagController tagController;

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

		tags.forEach(listTagsModel::addElement);
	}

	public void addTag(Tag tag) {

		listTagsModel.addElement(tag);

		resetErrorLabel();
	}

	public void removeTag(Tag tag) {

		listTagsModel.removeElement(tag);

		resetErrorLabel();
	}

	private void resetErrorLabel() {
		errorMessageLabel.setText("");
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TagSwingView frame = new TagSwingView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	public TagSwingView() {
		setTitle("Tag View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel tagNameLabel = new JLabel("Tag Name");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(tagNameLabel, gbc_lblNewLabel);

		tagNameTextBox = new JTextField();
		tagNameTextBox.setName("tagNameTextBox");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		contentPane.add(tagNameTextBox, gbc_textField);
		tagNameTextBox.setColumns(10);

		JButton addTagButton = new JButton("Add Tag");
		addTagButton.setName("addTagButton");
		addTagButton.setEnabled(false);

		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridwidth = 2;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 1;

		contentPane.add(addTagButton, gbc_btnNewButton);

		addTagButton.addActionListener(e -> {
			String name = tagNameTextBox.getText().trim();
			if (!name.isEmpty() && tagController != null) {
				Tag tag = new Tag("1", name);
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
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		contentPane.add(scrollPane, gbc_scrollPane);

		listTagsModel = new DefaultListModel<>();
		listTags = new JList<>(listTagsModel);
		scrollPane.setViewportView(listTags);
		listTags.addListSelectionListener(e -> deleteTagButton.setEnabled(listTags.getSelectedIndex() != -1));
		listTags.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listTags.setName("tagList");

		JButton btnNewButton_1 = new JButton("Update Tag");
		btnNewButton_1.setName("updateTagButton");
		btnNewButton_1.setEnabled(false);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 3;
		contentPane.add(btnNewButton_1, gbc_btnNewButton_1);

		deleteTagButton = new JButton("Delete Selected");
		deleteTagButton.setName("deleteTagButton");
		deleteTagButton.setEnabled(false);
		deleteTagButton.addActionListener(e -> {
			Tag selectedTag = listTags.getSelectedValue();
			if (selectedTag != null && tagController != null) {
				tagController.deleteTag(selectedTag);
			}
		});
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_2.gridx = 1;
		gbc_btnNewButton_2.gridy = 3;
		contentPane.add(deleteTagButton, gbc_btnNewButton_2);

		errorMessageLabel = new JLabel("");
		errorMessageLabel.setName("errorMessageLabel");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.gridwidth = 2;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 4;
		contentPane.add(errorMessageLabel, gbc_lblNewLabel_1);

	}

}
