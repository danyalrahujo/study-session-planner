package com.example.studyplanner.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import com.example.studyplanner.model.Tag;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;

import com.example.studyplanner.controller.TagController;

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
	}

	@Test
	public void testWhenTagNameIsNonEmptyThenAddTagButtonShouldBeEnabled() {

		window.textBox("tagNameTextBox").enterText("Java");

		window.button(JButtonMatcher.withText("Add Tag")).requireEnabled();
	}

	@Test
	public void testWhenTagNameIsBlankThenAddTagButtonShouldBeDisabled() {

		window.textBox("tagNameTextBox").enterText("Java");

		window.button(JButtonMatcher.withText("Add Tag")).requireEnabled();

		window.textBox("tagNameTextBox").setText("");

		window.textBox("tagNameTextBox").enterText(" ");

		window.button(JButtonMatcher.withText("Add Tag")).requireDisabled();
	}

	@Test
	public void testDeleteTagButtonShouldBeEnabledOnlyWhenATagIsSelected() {

		Tag tag = new Tag("1", "Java");

		GuiActionRunner.execute(() -> tagSwingView.getListTagsModel().addElement(tag));

		window.list("tagList").selectItem(0);

		window.button(JButtonMatcher.withText("Delete Selected")).requireEnabled();

		window.list("tagList").clearSelection();

		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
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

		verify(tagController).addTag(new Tag("1", "Java"));
	}
}
