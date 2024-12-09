package t1_10.t4_decision_support_module.t2_preference_elicitation_module.t3_reference_sets_constructor;

import alternative.Alternative;
import alternative.Alternatives;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.ReferenceSetsConstructorException;
import exeption.RefinerException;
import interaction.reference.ReferenceSetsConstructor;
import interaction.reference.constructor.AllAlternatives;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.reference.constructor.RandomPairs;
import interaction.reference.validator.RequiredSpread;
import interaction.refine.Refiner;
import interaction.refine.Result;
import random.IRandom;
import random.MersenneTwister64;
import space.Range;
import space.os.ObjectiveSpace;
import system.dm.DM;
import t1_10.t4_decision_support_module.t2_preference_elicitation_module.Common;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This tutorial focuses on the {@link interaction.reference.ReferenceSetsConstructor} class.
 *
 * @author MTomczyk
 */
public class Tutorial3c
{
    /**
     * Runs the tutorial.
     *
     * @param args not used
     */
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args)
    {
        // Create RNG:
        IRandom R = new MersenneTwister64(0);

        LocalDateTime startingTimestamp = LocalDateTime.now();
        Criteria criteria = Criteria.constructCriteria("C", 2, false);
        // OS spanned over [0, 2]^2 bounds
        ObjectiveSpace os = new ObjectiveSpace(Range.getDefaultRanges(2, 2.0d), new boolean[2]);

        // Create the params container:
        ReferenceSetsConstructor.Params pRSC = new ReferenceSetsConstructor.Params();
        pRSC._commonConstructors = new LinkedList<>();
        // Use GlobalSet for all DMs:
        pRSC._commonConstructors.add(new AllAlternatives());

        // Create DMs identifiers (strings):
        String[] DMs = new String[]{"DM1", "DM2"};

        // Create reference sets constructors for all DMs (RandomParis, each DM has a dedicated constructor):
        pRSC._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> constructors = new LinkedList<>();
            constructors.add(new RandomPairs(new RequiredSpread(0.1d)));
            pRSC._dmConstructors.put(DMs[0], constructors);
        }
        {
            LinkedList<IReferenceSetConstructor> constructors = new LinkedList<>();
            constructors.add(new RandomPairs(new RequiredSpread(0.1d)));
            pRSC._dmConstructors.put(DMs[1], constructors);
        }

        // Create the reference sets constructor:
        ReferenceSetsConstructor RSC = new ReferenceSetsConstructor(pRSC);

        // Not pointless, the final values were decomposed into the normalized part and the multiplier
        @SuppressWarnings("PointlessArithmeticExpression")
        ArrayList<Alternative> alternatives = Alternative.getAlternativeArray("A",
                new double[][]
                        {
                                {1.0d * 2.0d, 0.1d * 2.0d}, // A0
                                {0.25d * 2.0d, 0.15d * 2.0d}, // A1
                                {0.2d * 2.0d, 0.2d * 2.0d}, // A2
                                {0.1d * 2.0d, 1.0d * 2.0d}, // A3
                        });

        // Create the context:
        DMContext context = Common.getContext(0, new Alternatives(alternatives), startingTimestamp, criteria, os, R);

        // Create an empty refiner (does nothing):
        Refiner.Params pR = new Refiner.Params();
        // Do not use the filters here:
        pR._reductionFilters = null;
        pR._terminationFilters = null;
        Refiner refiner = new Refiner(pR);

        try
        {
            // Create refiner results (we need it to manually use the reference sets constructor)
            Result refinerResult = refiner.refine(context);

            // Normally, the DM ids are maintained by the system. But wee need them here to manually construct the reference sets:
            DM[] DMsIDs = new DM[DMs.length];
            DMsIDs[0] = new DM(0, DMs[0]);
            DMsIDs[1] = new DM(1, DMs[1]);

            // Create reference sets:
            interaction.reference.Result result = RSC.constructReferenceSets(context, DMsIDs, refinerResult);

            // Print string representation:
            result.printStringRepresentation();

        } catch (RefinerException | ReferenceSetsConstructorException e)
        {
            throw new RuntimeException(e);
        }
    }
}
