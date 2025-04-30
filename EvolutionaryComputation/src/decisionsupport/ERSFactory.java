package decisionsupport;

import compatibility.CompatibilityAnalyzer;
import model.constructor.value.rs.ers.comparators.MostSimilarWithTieResolving;
import model.constructor.value.rs.ers.evolutionary.EvolutionaryModelConstructor;
import model.constructor.value.rs.ers.evolutionary.IOffspringConstructor;
import model.constructor.value.rs.ers.iterationslimit.IImprovementAttemptsLimit;
import model.similarity.lnorm.Euclidean;
import model.constructor.random.IRandomModel;
import model.constructor.random.LNormGenerator;
import model.internals.value.scalarizing.LNorm;
import space.normalization.INormalization;

/**
 * Provides useful means for generating suitably parameterized instances of {@link model.constructor.value.rs.ers.ERS}.
 *
 * @author MTomczyk
 */
public class ERSFactory
{
    /**
     * Returns a default instance of {@link model.constructor.value.rs.ers.ERS} set to construct L-norms
     * {@link LNorm} (see the code to see how the parameters are set).
     *
     * @param feasibleSamplesToGenerate required no. of feasible samples to generate
     * @param improvementAttemptsLimit  limit for the improvement attempts (can be conditional, see {@link IImprovementAttemptsLimit})
     * @param criteria                  the number of criteria
     * @param alpha                     pre-fixed compensation level for generated L-norms
     * @param normalizations            normalization objects used to suitably rescale the evaluated points (can be null; not used)
     * @param offspringConstructor      offspring constructor
     * @param ts                        tournament size used in the tournament selection (see {@link EvolutionaryModelConstructor})
     * @param initialModels             initial models
     * @return parameterized evolutionary rejection sampling object
     */
    public static model.constructor.value.rs.ers.ERS<LNorm> getDefaultForLNorms(int feasibleSamplesToGenerate,
                                                                                IImprovementAttemptsLimit improvementAttemptsLimit,
                                                                                int criteria,
                                                                                double alpha,
                                                                                INormalization[] normalizations,
                                                                                IOffspringConstructor<LNorm> offspringConstructor,
                                                                                int ts,
                                                                                LNorm[] initialModels)

    {
        IRandomModel<LNorm> RM = new LNormGenerator(criteria, alpha, normalizations);
        model.constructor.value.rs.ers.ERS.Params<LNorm> pERS = new model.constructor.value.rs.ers.ERS.Params<>(RM, criteria);
        pERS._improvementAttemptsLimit = improvementAttemptsLimit;
        pERS._inconsistencyThreshold = feasibleSamplesToGenerate - 1;
        pERS._feasibleSamplesToGenerate = feasibleSamplesToGenerate;
        pERS._initialModels = initialModels;
        pERS._kMostSimilarNeighbors = 3;
        pERS._comparator = new MostSimilarWithTieResolving<>();
        pERS._similarity = new Euclidean();
        pERS._compatibilityAnalyzer = new CompatibilityAnalyzer();
        pERS._validateAlreadyExistingSamplesFirst = true;
        if (offspringConstructor != null) pERS._EMC = new EvolutionaryModelConstructor<>(offspringConstructor, ts);
        return new model.constructor.value.rs.ers.ERS<>(pERS);
    }
}
