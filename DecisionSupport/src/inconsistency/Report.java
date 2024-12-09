package inconsistency;

import dmcontext.DMContext;
import model.internals.AbstractInternalModel;
import system.AbstractReport;
import utils.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Auxiliary container-like class providing a report on inconsistency handling.
 *
 * @author MTomczyk
 */
public class Report<T extends AbstractInternalModel> extends AbstractReport
{

    /**
     * Stores all states (all) created during the consistency reintroduction process. Note that the inconsistency handler
     * ({@link IInconsistencyHandler}) may be parameterized not to store them. This list additionally, stores the initial
     * bundle (input) that caused the inconsistency (should be added as the first element).
     */
    public LinkedList<State<T>> _states;

    /**
     * Represents the final, i.e., consistent state (stores the consistent model bundle and linked preference information).
     */
    public State<T> _consistentState;

    /**
     * Can be used to store the number of attempts taken before the consistency was introduced.
     */
    public int _attempts = 0;

    /**
     * Parameterized constructor.
     *
     * @param dmContext current decision-making context
     */
    protected Report(DMContext dmContext)
    {
        super(dmContext);
    }


    /**
     * Creates and returns the string representation of the report. Each line ends with a new line symbol and
     * is stored in a different output array element.
     *
     * @param indent auxiliary indent used when constructing the lines
     * @return string representation
     */
    @Override
    public String[] getStringRepresentation(int indent)
    {
        LinkedList<String> lines = new LinkedList<>();
        String ind = StringUtils.getIndent(indent);

        lines.add(ind + "The number of attempts = " + _attempts);
        lines.add(ind + "All states: ");
        if ((_states == null) || (_states.isEmpty())) lines.add(ind + "  None");
        else
        {
            for (State<?> st : _states)
            {
                String[] S = st.getStringRepresentation(indent + 2);
                lines.addAll(Arrays.asList(S));
            }
        }
        lines.add(ind + "Consistent state: ");
        if (_consistentState == null) lines.add(ind + "  None");
        else
        {
            String[] S = _consistentState.getStringRepresentation(indent + 2);
            lines.addAll(Arrays.asList(S));
        }

        return StringUtils.getArrayFromList(lines);
    }


}
