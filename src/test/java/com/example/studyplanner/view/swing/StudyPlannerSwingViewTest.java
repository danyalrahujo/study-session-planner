package com.example.studyplanner.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import com.example.studyplanner.model.StudySession;

public class StudyPlannerSwingViewTest extends AssertJSwingJUnitTestCase {

	private StudyPlannerSwingView studyPlannerSwingView;

	private FrameFixture window;

	@Override
	protected void onSetUp() {

		studyPlannerSwingView = GuiActionRunner.execute(() -> new StudyPlannerSwingView());

		window = new FrameFixture(robot(), studyPlannerSwingView);

		window.show();
	}

	@Test
	@GUITest
	public void testControlsInitialStates() {

		window.label(JLabelMatcher.withText("id"));

		window.textBox("idTextBox").requireEnabled();

		window.label(JLabelMatcher.withText("description"));

		window.textBox("descriptionTextBox").requireEnabled();

		window.button(JButtonMatcher.withText("Add Session")).requireDisabled();

		window.list("sessionList");

		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();

		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testWhenIdAndDescriptionAreNonEmptyThenAddButtonShouldBeEnabled() {

		window.textBox("idTextBox").enterText("1");
		window.textBox("descriptionTextBox").enterText("Math");

		window.button(JButtonMatcher.withText("Add Session")).requireEnabled();
	}

	@Test
	public void testWhenEitherIdOrDescriptionAreBlankThenAddButtonShouldBeDisabled() {

		window.textBox("idTextBox").enterText("1");
		window.textBox("descriptionTextBox").enterText(" ");

		window.button(JButtonMatcher.withText("Add Session")).requireDisabled();

		window.textBox("idTextBox").setText("");
		window.textBox("descriptionTextBox").setText("");

		window.textBox("idTextBox").enterText(" ");
		window.textBox("descriptionTextBox").enterText("Math");

		window.button(JButtonMatcher.withText("Add Session")).requireDisabled();
	}

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAStudySessionIsSelected() {

		StudySession session = new StudySession("1", "Math", false, "", null);

		GuiActionRunner.execute(() -> studyPlannerSwingView.getListSessionsModel().addElement(session));

		window.list("sessionList").selectItem(0);

		window.button(JButtonMatcher.withText("Delete Selected")).requireEnabled();

		window.list("sessionList").clearSelection();

		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
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
}
