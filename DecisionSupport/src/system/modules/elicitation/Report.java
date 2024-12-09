package system.modules.elicitation;

import dmcontext.DMContext;
import interaction.Status;
import system.AbstractReport;
import utils.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Report-like class associated with {@link PreferenceElicitationModule} processing.
 *
 * @author MTomczyk
 */


public class Report extends AbstractReport
{
    /**
     * Interaction status.
     */
    public Status _interactionStatus = null;

    /**
     * The most recent result of the 'trigger interactions' process.
     */
    public interaction.trigger.Result _interactionTriggerResult = null;

    /**
     * The most recent result of the 'construct reference sets' process.
     */
    public interaction.reference.Result _referenceSetsConstructionResult = null;

    /**
     * The most recent result of the 'feedback-providing' process.
     */
    public interaction.feedbackprovider.Result _feedbackProviderResult;

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

        lines.add(ind + "REPORT ON THE PREFERENCE ELICITATION CALL");
        applyBasicLines(lines, ind);
        lines.add(ind + "Interaction status = " + _interactionStatus);
        lines.add(ind + "INTERACTION TRIGGER RESULT:");
        if (_interactionTriggerResult == null) lines.add(ind + "  None");
        else
        {
            String[] S = _interactionTriggerResult.getStringRepresentation(indent + 2);
            lines.addAll(Arrays.asList(S));
        }
        lines.add(ind + "REFERENCE SETS CONSTRUCTION RESULT:");
        if (_referenceSetsConstructionResult == null) lines.add(ind + "  None");
        else
        {
            String[] S = _referenceSetsConstructionResult.getStringRepresentation(indent + 2);
            lines.addAll(Arrays.asList(S));
        }
        lines.add(ind + "FEEDBACK PROVIDER RESULTS:");
        if (_feedbackProviderResult == null) lines.add(ind + "  None");
        else
        {
            String[] S = _feedbackProviderResult.getStringRepresentation(indent + 2);
            lines.addAll(Arrays.asList(S));
        }
        return StringUtils.getArrayFromList(lines);
    }


}
