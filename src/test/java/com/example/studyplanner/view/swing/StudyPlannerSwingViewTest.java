package com.example.studyplanner.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.studyplanner.controller.StudySessionController;
import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.model.Tag;

import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

public class StudyPlannerSwingViewTest extends AssertJSwingJUnitTestCase {

	private StudyPlannerSwingView studyPlannerSwingView;

	private FrameFixture window;

	@Mock
	private StudySessionController studySessionController;

	private AutoCloseable closeable;

	@Override
	protected void onSetUp() {

		closeable = MockitoAnnotations.openMocks(this);

		studyPlannerSwingView = GuiActionRunner.execute(() -> {
			StudyPlannerSwingView view = new StudyPlannerSwingView();

			view.setStudySessionController(studySessionController);

			return view;
		});

		window = new FrameFixture(robot(), studyPlannerSwingView);

		window.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test

	public void testMainMethod() {

		StudyPlannerSwingView.main(new String[] {});

		await()

				.atMost(2, TimeUnit.SECONDS)

				.untilAsserted(() ->

				assertThat(java.awt.Frame.getFrames().length).isGreaterThan(0)

				);

	}

	@Test
	public void testControlsInitialStates() {

		window.label(JLabelMatcher.withText("id"));
		window.textBox("idTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("description"));
		window.textBox("descriptionTextBox").requireEnabled();
		window.button(JButtonMatcher.withText("Add Session")).requireDisabled();
		window.list("sessionList");
		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
		window.label("errorMessageLabel").requireText(" ");

		assertThat(window.textBox("idTextBox").target().isEnabled()).isTrue();
	}

	@Test
	public void testWhenIdAndDescriptionAreNonEmptyThenAddButtonShouldBeEnabled() {

		window.textBox("idTextBox").enterText("1");
		window.textBox("descriptionTextBox").enterText("Math");

		window.button(JButtonMatcher.withText("Add Session")).requireEnabled();

		assertThat(window.button(JButtonMatcher.withText("Add Session")).target().isEnabled()).isTrue();
	}

	@Test
	public void testWhenEitherIdOrDescriptionAreBlankThenAddButtonShouldBeDisabled() {

		window.textBox("idTextBox").enterText("1");
		window.textBox("descriptionTextBox").enterText(" ");
		window.button(JButtonMatcher.withText("Add Session")).requireDisabled();

		assertThat(window.button(JButtonMatcher.withText("Add Session")).target().isEnabled()).isFalse();

		window.textBox("idTextBox").setText("");
		window.textBox("descriptionTextBox").setText("");

		window.textBox("idTextBox").enterText(" ");
		window.textBox("descriptionTextBox").enterText("Math");

		window.button(JButtonMatcher.withText("Add Session")).requireDisabled();

		assertThat(window.button(JButtonMatcher.withText("Add Session")).target().isEnabled()).isFalse();
	}

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAStudySessionIsSelected() {

		StudySession session = new StudySession("1", "Math", false, "", null);

		GuiActionRunner.execute(() -> studyPlannerSwingView.getListSessionsModel().addElement(session));

		window.list("sessionList").selectItem(0);

		window.button(JButtonMatcher.withText("Delete Selected")).requireEnabled();

		assertThat(window.button(JButtonMatcher.withText("Delete Selected")).target().isEnabled()).isTrue();

		window.list("sessionList").clearSelection();

		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();

		assertThat(window.button(JButtonMatcher.withText("Delete Selected")).target().isEnabled()).isFalse();
	}

	@Test
	public void testDisplayStudySessionsShouldAddSessionsToTheList() {

		StudySession session1 = new StudySession("1", "Math", false, "", null);
		StudySession session2 = new StudySession("2", "Physics", false, "", null);

		GuiActionRunner.execute(() -> studyPlannerSwingView.displayStudySessions(Arrays.asList(session1, session2)));

		assertThat(window.list("sessionList").contents()).containsExactly(session1.toString(), session2.toString());
	}

	@Test
	public void testShowStudySessionErrorShouldShowMessageInErrorLabel() {

		StudySession session = new StudySession("1", "Math", false, "", null);

		GuiActionRunner.execute(() -> studyPlannerSwingView.showStudySessionError("error message", session));

		window.label("errorMessageLabel").requireText("error message: " + session);

		assertThat(window.label("errorMessageLabel").target().getText()).isEqualTo("error message: " + session);
	}

	@Test
	public void testAddStudySessionShouldAddSessionToListAndResetErrorLabel() {

		StudySession session = new StudySession("1", "Math", false, "", null);

		GuiActionRunner.execute(() -> studyPlannerSwingView.addStudySession(session));

		assertThat(window.list("sessionList").contents()).containsExactly(session.toString());

		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testRemoveStudySessionShouldRemoveSessionFromListAndResetErrorLabel() {

		StudySession session1 = new StudySession("1", "Math", false, "", null);
		StudySession session2 = new StudySession("2", "Physics", false, "", null);

		GuiActionRunner.execute(() -> {
			studyPlannerSwingView.getListSessionsModel().addElement(session1);

			studyPlannerSwingView.getListSessionsModel().addElement(session2);
		});

		GuiActionRunner.execute(() -> studyPlannerSwingView.removeStudySession(session1));

		assertThat(window.list("sessionList").contents()).containsExactly(session2.toString());

		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testAddButtonShouldDelegateToStudySessionControllerAddStudySession() {

		window.textBox("idTextBox").enterText("1");

		window.textBox("descriptionTextBox").enterText("Math");

		window.button(JButtonMatcher.withText("Add Session")).click();

		verify(studySessionController).addStudySession(new StudySession("1", "Math", false, "", null));
	}

	@Test
	public void testDeleteButtonShouldDelegateToStudySessionControllerDeleteStudySession() {

		StudySession session1 = new StudySession("1", "Math", false, "", null);

		StudySession session2 = new StudySession("2", "Physics", false, "", null);

		GuiActionRunner.execute(() -> {
			studyPlannerSwingView.getListSessionsModel().addElement(session1);

			studyPlannerSwingView.getListSessionsModel().addElement(session2);
		});

		window.list("sessionList").selectItem(1);

		window.button(JButtonMatcher.withText("Delete Selected")).click();

		verify(studySessionController).deleteStudySession(session2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testShowTagErrorThrowsException() {

		studyPlannerSwingView.showTagError("error", new Tag("1", "Java"));
	}

	@Test
	public void testTagMethodsDoNothing() {

		int initialSize = studyPlannerSwingView.getListSessionsModel().size();

		studyPlannerSwingView.displayTags(new ArrayList<Tag>());
		studyPlannerSwingView.addTag(new Tag("1", "Java"));
		studyPlannerSwingView.removeTag(new Tag("1", "Java"));
		studyPlannerSwingView.updateTag(new Tag("1", "Java"));
		studyPlannerSwingView.deleteTag(new Tag("1", "Java"));

		assertThat(studyPlannerSwingView.getListSessionsModel().size()).isEqualTo(initialSize);
	}

	@Test
	public void testUpdateButtonShouldDelegateToController() {

		StudySession session = new StudySession("1", "Math", false, "", null);

		GuiActionRunner.execute(() -> studyPlannerSwingView.getListSessionsModel().addElement(session));

		window.list("sessionList").selectItem(0);

		window.textBox("descriptionTextBox").setText("Physics");

		window.button(JButtonMatcher.withText("Update Session")).click();

		verify(studySessionController).updateStudySession(new StudySession("1", "Physics", false, "", null));
	}

	@Test
	public void testUpdateSessionButtonShouldDelegateToController() {

		StudySession session = new StudySession("1", "Math", false, "", null);

		GuiActionRunner.execute(() -> studyPlannerSwingView.getListSessionsModel().addElement(session));

		window.list("sessionList").selectItem(0);

		window.textBox("descriptionTextBox").setText("Physics");

		window.button(JButtonMatcher.withText("Update Session")).click();

		verify(studySessionController)
				.updateStudySession(argThat(s -> s.getId().equals("1") && s.getDescription().equals("Physics")));
	}

}
