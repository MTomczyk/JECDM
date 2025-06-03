package decisionsupport.operators;

import model.constructor.value.rs.ers.ModelsQueue;
import model.constructor.value.rs.ers.SortedModel;
import model.constructor.value.rs.ers.evolutionary.IOffspringConstructor;
import reproduction.operators.crossover.OnSimplexCombination;
import reproduction.operators.mutation.OnSimplexSimplexMutation;
import dmcontext.DMContext;
import model.internals.value.scalarizing.LNorm;
import space.normalization.INormalization;

import java.util.ArrayList;

/**
 * This class implements {@link IOffspringConstructor} and is dedicated to reproducing L-norms
 * ({@link LNorm}). Specifically, it produces an offspring by crossing-over the parent
 * models' weight vectors and mutating the output. The following operators are used:
 * {@link OnSimplexCombination} (crossover) and {@link OnSimplexSimplexMutation} (mutation).
 * It is also assumed that the compensation level alpha for the resulting L-norm is pre-fixed.
 *
 * @author MTomczyk
 */
public class LNormOnSimplex implements IOffspringConstructor<LNorm>
{
    /**
     * Pre-fixed compensation level.
     */
    private final double _alpha;


    /**
     * Optional fixed normalizations used when instantiating models.
     */
    private final INormalization[] _normalizations;

    /**
     * Weights reproducer object.
     */
    private final OnSimplexWeightsReproducer _wr;

    /**
     * Parameterized constructor.
     *
     * @param alpha  pre-fixed compensation level
     * @param cStd   standard deviation for the Gaussian operator employed by default in {@link OnSimplexCombination}
     * @param mScale scaling factor utilized by {@link OnSimplexSimplexMutation}
     */
    public LNormOnSimplex(double alpha, double cStd, double mScale)
    {
        this(alpha, cStd, mScale, null);
    }

    /**
     * Parameterized constructor.
     *
     * @param alpha          pre-fixed compensation level
     * @param cStd           standard deviation for the Gaussian operator employed by default in {@link OnSimplexCombination}
     * @param mScale         scaling factor utilized by {@link OnSimplexSimplexMutation}
     * @param normalizations optional fixed normalizations used when instantiating models
     *                       (if not provided, the normalizations are derived from the decision-making context)
     */
    public LNormOnSimplex(double alpha, double cStd, double mScale, INormalization[] normalizations)
    {
        _alpha = alpha;
        _normalizations = normalizations;
        _wr = new OnSimplexWeightsReproducer(cStd, mScale);
    }

    /**
     * Signature for the main method responsible for producing a new (offspring) model given two selected ones (parents).
     * The parents are wrapped via {@link SortedModel}
     * (derived from {@link ModelsQueue}).
     *
     * @param dmContext current decision-making context
     * @param parents   selected parents
     * @return new model (offspring)
     */
    @Override
    public LNorm getModel(DMContext dmContext, ArrayList<SortedModel<LNorm>> parents)
    {
        double[] o =_wr.getWeights(parents.get(0)._model, parents.get(1)._model, dmContext.getR());
        if (_normalizations != null) return new LNorm(o, _alpha, _normalizations);
        return new LNorm(o, _alpha, dmContext.getNormalizationsCurrentOS());
    }
}
