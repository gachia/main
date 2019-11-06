package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalVisits;

public class DeleteVisitCommandTest {

    private static final int VALID_REPORT_INDEX = 3;
    private static final int INVALID_REPORT_INDEX = 0;
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_deleteVisitCommand_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson)
                .withVisitList(TypicalVisits.getLongTypicalVisitList(firstPerson.getName().toString())).build();

        DeleteVisitCommand deleteVisitCommand = new DeleteVisitCommand(INDEX_SECOND_PERSON,
                VALID_REPORT_INDEX);

        String expectedMessage = String.format(DeleteVisitCommand.MESSAGE_DELETE_VISIT_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        CommandResult expectedCommandResult =
                new CommandResult(expectedMessage, editedPerson.getVisitList().getObservableRecords());

        try {
            CommandResult result = deleteVisitCommand.execute(model);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, model);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    @Test
    public void execute_invalidReportIndex_failure() {

        DeleteVisitCommand deleteVisitCommand = new DeleteVisitCommand(INDEX_SECOND_PERSON,
                INVALID_REPORT_INDEX);

        DeleteVisitCommand deleteVisitCommand1 = new DeleteVisitCommand(INDEX_SECOND_PERSON,
                model.getFilteredPersonList()
                        .get(INDEX_SECOND_PERSON.getZeroBased()).getVisitList().getRecords().size() + 1);

        assertCommandFailure(deleteVisitCommand, model, Messages.MESSAGE_INVALID_REPORT_INDEX);
        assertCommandFailure(deleteVisitCommand1, model, Messages.MESSAGE_INVALID_REPORT_INDEX);
    }

    @Test
    public void equals() {
        DeleteVisitCommand deleteFirstCommand = new DeleteVisitCommand(INDEX_FIRST_PERSON, 1);
        DeleteVisitCommand deleteSecondCommand = new DeleteVisitCommand(INDEX_SECOND_PERSON, 1);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteVisitCommand deleteFirstCommandCopy = new DeleteVisitCommand(INDEX_FIRST_PERSON, 1);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        DeleteCommand deleteFirstCommandNoIndex = new DeleteCommand(INDEX_FIRST_PERSON);
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandNoIndex));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }
}
