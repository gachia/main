package seedu.address.logic.commands;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.VisitReport;

/**
 * Saves new record to Visit List.
 */
public class SaveVisitCommand extends Command {
    public static final String MESSAGE_SAVE_VISIT_SUCCESS = "Saved visit to Person: %1$s";
    private static final int INVALID_REPORT_INDEX = -1;

    private final Index index;
    private final VisitReport visitReport;

    private int reportIdx;
    private String medication;
    private String diagnosis;
    private String remarks;

    public SaveVisitCommand(int index, int reportIdx, String date, String meds, String dg, String rmk) {
        requireAllNonNull(index, reportIdx, date);
        this.index = Index.fromOneBased(index);
        this.reportIdx= reportIdx;
        this.visitReport = new VisitReport(date);
        this.medication = meds;
        this.diagnosis = dg;
        this.remarks = rmk;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        visitReport.setName(personToEdit.getName());
        visitReport.setDetails(medication, diagnosis, remarks);
        Person editedPerson = null;
        if (reportIdx == INVALID_REPORT_INDEX) {
            editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                    personToEdit.getAddress(), personToEdit.getVisitList().addRecord(visitReport), personToEdit.getTags());
        } else {
            editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                    personToEdit.getAddress(), personToEdit.getVisitList().editRecord(reportIdx, visitReport), personToEdit.getTags());

        }

        assert(!editedPerson.equals(null));
        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SAVE_VISIT_SUCCESS, personToEdit));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SaveVisitCommand)) {
            return false;
        }

        // state check
        SaveVisitCommand e = (SaveVisitCommand) other;
        return index.equals(e.index)
                && medication.equals(e.medication)
                && diagnosis.equals(e.diagnosis)
                && remarks.equals(e.remarks)
                && visitReport.date.equals(e.visitReport.date);

    }

}
