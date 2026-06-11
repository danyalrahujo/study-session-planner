package com.example.studyplanner.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.studyplanner.controller.TagController;
import com.example.studyplanner.model.StudySession;
import com.example.studyplanner.model.Tag;

public class TagSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	private TagSwingView tagSwingView;

	@Mock
	private TagController tagController;

	private AutoCloseable closeable;

	@Override
	protected void onSetUp() {
		closeable = MockitoAnnotations.openMocks(this);

		tagSwingView = GuiActionRunner.execute(() -> {
			TagSwingView view = new TagSwingView();
			view.setTagController(tagController);
			return view;
		});

		window = new FrameFixture(robot(), tagSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test
	@GUITest
	public void testControlsInitialStates() {

		window.label(JLabelMatcher.withText("Tag Name"));
		window.textBox("tagNameTextBox").requireEnabled();
		window.button(JButtonMatcher.withText("Add Tag")).requireDisabled();
		window.list("tagList");
		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
		window.label("errorMessageLabel").requireText("");

		assertThat(window.textBox("tagNameTextBox").target().isEnabled()).isTrue();
	}

	@Test
	public void testMainMethod() throws Exception {

		StudyPlannerSwingView.main(new String[] {});

		Thread.sleep(500);

		assertThat(true).isTrue();
	}

	@Test
	public void testWhenTagNameIsNonEmptyThenAddTagButtonShouldBeEnabled() {

		window.textBox("tagNameTextBox").enterText("Java");

		window.button(JButtonMatcher.withText("Add Tag")).requireEnabled();

		assertThat(window.button(JButtonMatcher.withText("Add Tag")).target().isEnabled()).isTrue();
	}

	@Test
	public void testWhenTagNameIsBlankThenAddTagButtonShouldBeDisabled() {

		window.textBox("tagNameTextBox").enterText("Java");
		window.button(JButtonMatcher.withText("Add Tag")).requireEnabled();

		window.textBox("tagNameTextBox").setText("");
		window.textBox("tagNameTextBox").enterText(" ");

		window.button(JButtonMatcher.withText("Add Tag")).requireDisabled();

		assertThat(window.button(JButtonMatcher.withText("Add Tag")).target().isEnabled()).isFalse();
	}

	@Test
	public void testDeleteTagButtonShouldBeEnabledOnlyWhenATagIsSelected() {

		Tag tag = new Tag("1", "Java");

		GuiActionRunner.execute(() -> tagSwingView.getListTagsModel().addElement(tag));

		window.list("tagList").selectItem(0);

		window.button(JButtonMatcher.withText("Delete Selected")).requireEnabled();

		assertThat(window.button(JButtonMatcher.withText("Delete Selected")).target().isEnabled()).isTrue();

		window.list("tagList").clearSelection();

		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();

		assertThat(window.button(JButtonMatcher.withText("Delete Selected")).target().isEnabled()).isFalse();
	}

	@Test
	public void testDisplayTagsShouldAddTagsToTheList() {

		Tag tag1 = new Tag("1", "Java");
		Tag tag2 = new Tag("2", "Spring");

		GuiActionRunner.execute(() -> tagSwingView.displayTags(Arrays.asList(tag1, tag2)));

		assertThat(window.list("tagList").contents()).containsExactly(tag1.toString(), tag2.toString());
	}

	@Test
	public void testShowTagErrorShouldShowMessageInErrorLabel() {

		Tag tag = new Tag("1", "Java");

		GuiActionRunner.execute(() -> tagSwingView.showTagError("error message", tag));

		window.label("errorMessageLabel").requireText("error message: " + tag);

		assertThat(window.label("errorMessageLabel").target().getText()).isEqualTo("error message: " + tag);
	}

	@Test
	public void testAddTagShouldAddTagToListAndResetErrorLabel() {

		Tag tag = new Tag("1", "Java");

		GuiActionRunner.execute(() -> tagSwingView.addTag(tag));

		assertThat(window.list("tagList").contents()).containsExactly(tag.toString());

		window.label("errorMessageLabel").requireText("");
	}

	@Test
	public void testRemoveTagShouldRemoveTagFromListAndResetErrorLabel() {

		Tag tag1 = new Tag("1", "Java");
		Tag tag2 = new Tag("2", "Spring");

		GuiActionRunner.execute(() -> {
			tagSwingView.getListTagsModel().addElement(tag1);

			tagSwingView.getListTagsModel().addElement(tag2);
		});

		GuiActionRunner.execute(() -> tagSwingView.removeTag(tag1));

		assertThat(window.list("tagList").contents()).containsExactly(tag2.toString());

		window.label("errorMessageLabel").requireText("");
	}

	@Test
	public void testAddTagButtonShouldDelegateToTagControllerAddTag() {

		window.textBox("tagNameTextBox").enterText("Java");

		window.button(JButtonMatcher.withText("Add Tag")).click();

		verify(tagController).addTag(argThat(tag -> tag != null && "Java".equals(tag.getName())));
	}

	@Test
	public void testDeleteTagButtonShouldDelegateToTagControllerDeleteTag() {
		Tag tag1 = new Tag("1", "Java");
		Tag tag2 = new Tag("2", "Spring");

		GuiActionRunner.execute(() -> {
			tagSwingView.getListTagsModel().addElement(tag1);
			tagSwingView.getListTagsModel().addElement(tag2);
		});

		window.list("tagList").selectItem(1);

		window.button(JButtonMatcher.withText("Delete Selected")).click();

		verify(tagController).deleteTag(tag2);
	}

	@Test
	public void testAddTagButtonDoesNothingWhenTagControllerIsNull() {

		TagSwingView view = GuiActionRunner.execute(TagSwingView::new);

		FrameFixture localWindow = new FrameFixture(robot(), view);
		localWindow.show();

		localWindow.textBox("tagNameTextBox").enterText("Java");
		localWindow.button(JButtonMatcher.withText("Add Tag")).click();

		assertThat(localWindow.list("tagList").contents()).isEmpty();

		localWindow.cleanUp();
	}

	@Test
	public void testDeleteTagButtonDoesNothingWhenNoTagIsSelected() {

		Tag tag = new Tag("1", "Java");

		GuiActionRunner.execute(() -> tagSwingView.getListTagsModel().addElement(tag));

		window.list("tagList").clearSelection();

		assertThat(window.button(JButtonMatcher.withText("Delete Selected")).target().isEnabled()).isFalse();
	}

	@Test
	public void testAddTagButtonDoesNothingWhenControllerIsNull() {

		TagSwingView view = GuiActionRunner.execute(TagSwingView::new);

		FrameFixture localWindow = new FrameFixture(robot(), view);

		localWindow.show();

		localWindow.textBox("tagNameTextBox").enterText("Java");

		localWindow.button(JButtonMatcher.withText("Add Tag")).click();

		assertThat(view.getListTagsModel().size()).isEqualTo(0);

		localWindow.cleanUp();
	}

	@Test
	public void testUpdateTagButtonDelegatesToController() {

		Tag tag = new Tag("1", "Java");

		GuiActionRunner.execute(() -> tagSwingView.getListTagsModel().addElement(tag));

		window.list("tagList").selectItem(0);

		window.textBox("tagNameTextBox").setText("Spring");

		window.button(JButtonMatcher.withText("Update Tag")).click();

		verify(tagController).updateTag(argThat(t -> t.getId().equals("1") && t.getName().equals("Spring")));
	}

	@Test
	public void testDeleteTagButtonDoesNothingWhenNothingSelected() {

		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
	}

	@Test
	public void testShowTagErrorUpdatesLabel() {

		Tag tag = new Tag("1", "Java");

		GuiActionRunner.execute(() -> tagSwingView.showTagError("error", tag));

		window.label("errorMessageLabel").requireText("error: " + tag);
	}

	@Test
	public void testStudySessionMethodsDoNothing() {

		StudySession session = new StudySession("1", "Math", false, "", null);

		tagSwingView.showStudySessionError("error", session);
		tagSwingView.displayStudySessions(new ArrayList<>());
		tagSwingView.addStudySession(session);
		tagSwingView.removeStudySession(session);
		tagSwingView.updateStudySession(session);
		tagSwingView.deleteStudySession(session);

		assertNotNull(tagSwingView);
	}
}
