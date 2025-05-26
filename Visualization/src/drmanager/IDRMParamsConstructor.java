package drmanager;

/**
 * Auxiliary interface for classes responsible for constructing instances of {@link DisplayRangesManager.Params}.
 *
 * @author MTomczyk
 */
public interface IDRMParamsConstructor
{
    /**
     * The main method.
     *
     * @return params container to be used to instantiate display ranges manager
     */
    DisplayRangesManager.Params getDRMParams();
}
