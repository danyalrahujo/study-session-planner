package com.example.studyplanner.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.studyplanner.controller.StudySessionController;
import com.example.studyplanner.controller.TagController;
import com.example.studyplanner.repository.mongo.MongoStudySessionRepository;
import com.example.studyplanner.repository.mongo.MongoTagRepository;
import com.example.studyplanner.view.swing.StudyPlannerSwingView;
import com.example.studyplanner.view.swing.TagSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class StudyPlannerAppSwing implements Callable<Void> {

	@Option(names = { "--mongoHost", "--host" }, description = "MongoDB host")
	private static String mongoHost = "localhost";

	@Option(names = { "--mongoPort", "--port" }, description = "MongoDB port")
	private static int mongoPort = 27017;

	public static void main(String[] args) {
		new CommandLine(new StudyPlannerAppSwing()).execute(args);
	}

	@Override
	public Void call() throws Exception {

		EventQueue.invokeLater(() -> {

			try {

				MongoClient client = new MongoClient(new ServerAddress(mongoHost, mongoPort));

				MongoStudySessionRepository studySessionRepository = new MongoStudySessionRepository(client);

				MongoTagRepository tagRepository = new MongoTagRepository(client);

				StudyPlannerSwingView studyPlannerView = new StudyPlannerSwingView();

				TagSwingView tagView = new TagSwingView();

				StudySessionController studySessionController = new StudySessionController(studySessionRepository,
						studyPlannerView);

				TagController tagController = new TagController(tagRepository, tagView);

				studyPlannerView.setStudySessionController(studySessionController);

				tagView.setTagController(tagController);

				studyPlannerView.displayStudySessions(studySessionRepository.findAll());

				tagView.displayTags(tagRepository.findAll());

				studyPlannerView.setVisible(true);
				tagView.setVisible(true);

			} catch (Exception e) {

				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception", e);
			}
		});

		return null;
	}
}