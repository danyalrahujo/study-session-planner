package com.example.studyplanner.view.swing;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.model.Tag;
import com.example.studyplanner.view.StudyPlannerView;
import com.example.studyplanner.controller.StudySessionController;

public class StudyPlannerSwingView extends JFrame implements StudyPlannerView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField idTextBox;
	private JTextField descriptionTextBox;
	private JList<StudySession> listSessions;
	private DefaultListModel<StudySession> listSessionsModel;
	private JButton deleteSessionButton;
	private JLabel errorMessageLabel;
	private StudySessionController studySessionController;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudyPlannerSwingView frame = new StudyPlannerSwingView();
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
	public StudyPlannerSwingView() {
		setTitle("Study Planner View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);
		JLabel lblNewLabel = new JLabel("id");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		idTextBox = new JTextField();
		idTextBox.setName("idTextBox");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		contentPane.add(idTextBox, gbc_textField);
		idTextBox.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("description");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

		descriptionTextBox = new JTextField();
		descriptionTextBox.setName("descriptionTextBox");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		contentPane.add(descriptionTextBox, gbc_textField_1);
		descriptionTextBox.setColumns(10);

		JButton addSessionButton = new JButton("Add Session");
		addSessionButton.setName("addSessionButton");
		addSessionButton.setEnabled(false);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridwidth = 2;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 2;
		contentPane.add(addSessionButton, gbc_btnNewButton);

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
		scrollPane.setName("");
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		contentPane.add(scrollPane, gbc_scrollPane);

		listSessionsModel = new DefaultListModel<>();

		listSessions = new JList<>(listSessionsModel);
		listSessions.setName("sessionList");
		listSessions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane.setViewportView(listSessions);
		listSessions
				.addListSelectionListener(e -> deleteSessionButton.setEnabled(listSessions.getSelectedIndex() != -1));

		JButton updateSessionButton = new JButton("Update Session");
		updateSessionButton.setName("updateSessionButton");
		updateSessionButton.setEnabled(false);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 4;
		contentPane.add(updateSessionButton, gbc_btnNewButton_1);

		deleteSessionButton = new JButton("Delete Selected");
		deleteSessionButton.setName("deleteSessionButton");
		deleteSessionButton.setEnabled(false);
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_2.gridx = 1;
		gbc_btnNewButton_2.gridy = 4;
		contentPane.add(deleteSessionButton, gbc_btnNewButton_2);

		errorMessageLabel = new JLabel(" ");
		errorMessageLabel.setName("errorMessageLabel");

		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.gridwidth = 2;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 5;

		contentPane.add(errorMessageLabel, gbc_lblNewLabel_2);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void displayStudySessions(List<StudySession> studySessions) {
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
		// TODO Auto-generated method stub

	}

	@Override
	public void displayTags(List<Tag> tags) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTag(Tag tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTag(Tag tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTag(Tag tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteStudySession(StudySession studySession) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteTag(Tag tag) {
		// TODO Auto-generated method stub

	}

}