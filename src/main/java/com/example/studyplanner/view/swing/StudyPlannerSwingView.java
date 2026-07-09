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

import com.example.studyplanner.controller.StudySessionController;
import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.model.Tag;
import com.example.studyplanner.view.StudyPlannerView;
import javax.swing.WindowConstants;

public class StudyPlannerSwingView extends JFrame implements StudyPlannerView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField idTextBox;
	private JTextField descriptionTextBox;
	private JList<StudySession> listSessions;
	private DefaultListModel<StudySession> listSessionsModel;
	private JButton deleteSessionButton;
	private JLabel errorMessageLabel;
	private transient StudySessionController studySessionController;
	private JButton updateSessionButton;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			StudyPlannerSwingView frame = new StudyPlannerSwingView();
			frame.setVisible(true);
		});
	}

	public StudyPlannerSwingView() {
		setTitle("Study Planner View");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gridBagLayout);

		JLabel idLabel = new JLabel("id");
		GridBagConstraints idLabelConstraints = new GridBagConstraints();
		idLabelConstraints.insets = new Insets(0, 0, 5, 5);
		idLabelConstraints.anchor = GridBagConstraints.EAST;
		idLabelConstraints.gridx = 0;
		idLabelConstraints.gridy = 0;
		contentPane.add(idLabel, idLabelConstraints);

		idTextBox = new JTextField();
		idTextBox.setName("idTextBox");
		GridBagConstraints idTextFieldConstraints = new GridBagConstraints();
		idTextFieldConstraints.insets = new Insets(0, 0, 5, 0);
		idTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
		idTextFieldConstraints.gridx = 1;
		idTextFieldConstraints.gridy = 0;
		contentPane.add(idTextBox, idTextFieldConstraints);
		idTextBox.setColumns(10);

		JLabel descriptionLabel = new JLabel("description");
		GridBagConstraints descriptionLabelConstraints = new GridBagConstraints();
		descriptionLabelConstraints.anchor = GridBagConstraints.EAST;
		descriptionLabelConstraints.insets = new Insets(0, 0, 5, 5);
		descriptionLabelConstraints.gridx = 0;
		descriptionLabelConstraints.gridy = 1;
		contentPane.add(descriptionLabel, descriptionLabelConstraints);

		descriptionTextBox = new JTextField();
		descriptionTextBox.setName("descriptionTextBox");
		GridBagConstraints descriptionTextFieldConstraints = new GridBagConstraints();
		descriptionTextFieldConstraints.insets = new Insets(0, 0, 5, 0);
		descriptionTextFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
		descriptionTextFieldConstraints.gridx = 1;
		descriptionTextFieldConstraints.gridy = 1;
		contentPane.add(descriptionTextBox, descriptionTextFieldConstraints);
		descriptionTextBox.setColumns(10);

		JButton addSessionButton = new JButton("Add Session");
		addSessionButton.setName("addSessionButton");
		addSessionButton.setEnabled(false);
		GridBagConstraints addSessionButtonConstraints = new GridBagConstraints();
		addSessionButtonConstraints.insets = new Insets(0, 0, 5, 0);
		addSessionButtonConstraints.gridwidth = 2;
		addSessionButtonConstraints.gridx = 0;
		addSessionButtonConstraints.gridy = 2;
		contentPane.add(addSessionButton, addSessionButtonConstraints);

		addSessionButton.addActionListener(e -> studySessionController.addStudySession(
				new StudySession(idTextBox.getText().trim(), descriptionTextBox.getText().trim(), false, "", null)));

		KeyAdapter addButtonEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				addSessionButton.setEnabled(
						!idTextBox.getText().trim().isEmpty() && !descriptionTextBox.getText().trim().isEmpty());
			}
		};

		idTextBox.addKeyListener(addButtonEnabler);
		descriptionTextBox.addKeyListener(addButtonEnabler);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
		scrollPaneConstraints.insets = new Insets(0, 0, 5, 0);
		scrollPaneConstraints.fill = GridBagConstraints.BOTH;
		scrollPaneConstraints.gridwidth = 2;
		scrollPaneConstraints.gridx = 0;
		scrollPaneConstraints.gridy = 3;
		contentPane.add(scrollPane, scrollPaneConstraints);

		listSessionsModel = new DefaultListModel<>();
		listSessions = new JList<>(listSessionsModel);
		listSessions.setName("sessionList");
		listSessions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane.setViewportView(listSessions);
		listSessions.addListSelectionListener(e -> {
			StudySession selected = listSessions.getSelectedValue();
			boolean selectedItem = selected != null;

			deleteSessionButton.setEnabled(selectedItem);
			updateSessionButton.setEnabled(selectedItem);

			if (selectedItem) {
				idTextBox.setText(selected.getId());
				descriptionTextBox.setText(selected.getDescription());
			}
		});

		updateSessionButton = new JButton("Update Session");
		updateSessionButton.setName("updateSessionButton");
		updateSessionButton.setEnabled(false);
		GridBagConstraints updateSessionButtonConstraints = new GridBagConstraints();
		updateSessionButtonConstraints.insets = new Insets(0, 0, 5, 5);
		updateSessionButtonConstraints.gridx = 0;
		updateSessionButtonConstraints.gridy = 4;
		contentPane.add(updateSessionButton, updateSessionButtonConstraints);

		updateSessionButton.addActionListener(e -> {
			StudySession selected = listSessions.getSelectedValue();

			if (selected != null) {
				StudySession updated = new StudySession(selected.getId(), descriptionTextBox.getText().trim(),
						selected.isCompleted(), selected.getCreatedAt(), selected.getTags());

				studySessionController.updateStudySession(updated);
			}
		});

		deleteSessionButton = new JButton("Delete Selected");
		deleteSessionButton.setName("deleteSessionButton");
		deleteSessionButton.setEnabled(false);
		deleteSessionButton
				.addActionListener(e -> studySessionController.deleteStudySession(listSessions.getSelectedValue()));

		GridBagConstraints deleteSessionButtonConstraints = new GridBagConstraints();
		deleteSessionButtonConstraints.insets = new Insets(0, 0, 5, 0);
		deleteSessionButtonConstraints.gridx = 1;
		deleteSessionButtonConstraints.gridy = 4;
		contentPane.add(deleteSessionButton, deleteSessionButtonConstraints);

		errorMessageLabel = new JLabel(" ");
		errorMessageLabel.setName("errorMessageLabel");

		GridBagConstraints errorMessageLabelConstraints = new GridBagConstraints();
		errorMessageLabelConstraints.gridwidth = 2;
		errorMessageLabelConstraints.insets = new Insets(0, 0, 0, 5);
		errorMessageLabelConstraints.gridx = 0;
		errorMessageLabelConstraints.gridy = 5;
		contentPane.add(errorMessageLabel, errorMessageLabelConstraints);
	}

	DefaultListModel<StudySession> getListSessionsModel() {
		return listSessionsModel;
	}

	public void setStudySessionController(StudySessionController studySessionController) {
		this.studySessionController = studySessionController;
	}

	private void resetErrorLabel() {
		errorMessageLabel.setText(" ");
	}

	@Override
	public void showStudySessionError(String message, StudySession studySession) {
		errorMessageLabel.setText(message + ": " + studySession);
	}

	@Override
	public void showTagError(String message, Tag tag) {
		throw new UnsupportedOperationException("showTagError is not supported by this view");
	}

	@Override
	public void displayStudySessions(List<StudySession> studySessions) {
		listSessionsModel.clear();
		studySessions.forEach(listSessionsModel::addElement);
	}

	@Override
	public void addStudySession(StudySession studySession) {
		listSessionsModel.addElement(studySession);
		resetErrorLabel();
	}

	@Override

	public void removeStudySession(StudySession studySession) {

		listSessionsModel.removeElement(studySession);

		resetErrorLabel();

	}

	@Override
	public void updateStudySession(StudySession studySession) {
		int index = listSessions.getSelectedIndex();

		if (index >= 0) {
			listSessionsModel.set(index, studySession);
		}

		resetErrorLabel();
	}

	@Override
	public void displayTags(List<Tag> tags) {
		// Intentionally left blank: tags are managed in TagSwingView.
	}

	@Override
	public void addTag(Tag tag) {
		// Intentionally left blank: tags are managed in TagSwingView.
	}

	@Override
	public void removeTag(Tag tag) {
		// Intentionally left blank: tags are managed in TagSwingView.
	}

	@Override
	public void updateTag(Tag tag) {
		// Intentionally left blank: tags are managed in TagSwingView.
	}

	@Override
	public void deleteTag(Tag tag) {
		// Intentionally left blank: tags are managed in TagSwingView.
	}

	@Override

	public void deleteStudySession(StudySession studySession) {

		removeStudySession(studySession);

	}
}
