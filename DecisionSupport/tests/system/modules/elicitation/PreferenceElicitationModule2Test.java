package system.modules.elicitation;

import alternative.Alternative;
import alternative.Alternatives;
import alternative.IAlternativeWrapper;
import criterion.Criteria;
import dmcontext.DMContext;
import exeption.DecisionMakerSystemException;
import exeption.HistoryException;
import exeption.ModelSystemException;
import exeption.ModuleException;
import inconsistency.RemoveOldest;
import interaction.Status;
import interaction.feedbackprovider.FeedbackProvider;
import interaction.feedbackprovider.dm.artificial.value.ArtificialValueDM;
import interaction.reference.ReferenceSetsConstructor;
import interaction.reference.constructor.IReferenceSetConstructor;
import interaction.refine.Refiner;
import interaction.trigger.InteractionTrigger;
import interaction.trigger.rules.IterationInterval;
import model.constructor.random.LNormGenerator;
import model.constructor.value.rs.frs.FRS;
import model.definitions.LNorm;
import org.junit.jupiter.api.Test;
import preference.IPreferenceInformation;
import preference.indirect.PairwiseComparison;
import relation.Relations;
import space.Range;
import space.os.ObjectiveSpace;
import system.dm.DM;
import system.dm.DecisionMakerSystem;
import system.model.ModelSystem;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Provides various (more complex) tests for {@link PreferenceElicitationModule}.
 *
 * @author MTomczyk
 */
class PreferenceElicitationModule2Test
{
    /**
     * Test 1.
     */
    @Test
    void test1()
    {
        PreferenceElicitationModule.Params pP = new PreferenceElicitationModule.Params();
        pP._interactionTrigger = new InteractionTrigger(new IterationInterval(1, 2));
        pP._refiner = Refiner.getDefault();

        DM [] dms = new DM[2];
        dms[0] = new DM(0, "DM1");
        dms[1] = new DM(1, "DM2");

        ReferenceSetsConstructor.Params pRSC = new ReferenceSetsConstructor.Params();
        pRSC._commonConstructors = new LinkedList<>();
        pRSC._commonConstructors.add(new DummyConstructor(0, 1));
        pRSC._dmConstructors = new HashMap<>();
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(2, 3));
            pRSC._dmConstructors.put(dms[0].getName(), dmRSC);
        }
        {
            LinkedList<IReferenceSetConstructor> dmRSC = new LinkedList<>();
            dmRSC.add(new DummyConstructor(4, 5));
            pRSC._dmConstructors.put(dms[1].getName(), dmRSC);
        }

        pP._referenceSetsConstructor = new ReferenceSetsConstructor(pRSC);
        pP._feedbackProvider = FeedbackProvider.getForTwoDMs(
                dms[0].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.3d, 0.7d}, Double.POSITIVE_INFINITY))),
                dms[1].getName(), new ArtificialValueDM<>(new LNorm(new model.internals.value.scalarizing.LNorm(new double[]{0.7d, 0.3d}, Double.POSITIVE_INFINITY)))
        );

        String msg = null;

        pP._DMs = dms;
        pP._DMSs = new DecisionMakerSystem[2];
        {
            DecisionMakerSystem.Params pDMS = new DecisionMakerSystem.Params();
            pDMS._DM = dms[0];
            pDMS._modelSystems = new ModelSystem<?>[1];
            ModelSystem.Params<model.internals.value.scalarizing.LNorm> pMS = new ModelSystem.Params<>();
            pMS._preferenceModel = new LNorm();
            pMS._inconsistencyHandler = new RemoveOldest<>();
            pMS._modelConstructor = new FRS<>(new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY)));
            try
            {
                ModelSystem<model.internals.value.scalarizing.LNorm> modelSystem = new ModelSystem<>(pMS);
                pDMS._modelSystems[0] = modelSystem;
                pP._DMSs[0] = new DecisionMakerSystem(pDMS);
            } catch (ModelSystemException | DecisionMakerSystemException e)
            {
                msg = e.getMessage();
            }

            assertNull(msg);
        }
        {
            DecisionMakerSystem.Params pDMS = new DecisionMakerSystem.Params();
            pDMS._DM = dms[1];
            pDMS._modelSystems = new ModelSystem<?>[1];
            ModelSystem.Params<model.internals.value.scalarizing.LNorm> pMS = new ModelSystem.Params<>();
            pMS._preferenceModel = new LNorm();
            pMS._inconsistencyHandler = new RemoveOldest<>();
            pMS._modelConstructor = new FRS<>(new FRS.Params<>(new LNormGenerator(2, Double.POSITIVE_INFINITY)));
            try
            {
                ModelSystem<model.internals.value.scalarizing.LNorm> modelSystem = new ModelSystem<>(pMS);
                pDMS._modelSystems[0] = modelSystem;
                pP._DMSs[1] = new DecisionMakerSystem(pDMS);
            } catch (ModelSystemException | DecisionMakerSystemException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
        }

        PreferenceElicitationModule module = new PreferenceElicitationModule(pP);
        try
        {
            module.validate();
        } catch (ModuleException e)
        {
            msg = e.getMessage();
        }
        assertNull(msg);

        //////////////////////////////////////////////////////
        Criteria criteria = Criteria.constructCriteria("C", 2, false);
        ObjectiveSpace os = new ObjectiveSpace(Range.getDefaultRanges(2, 1.0d), new boolean[2]);

        {
            DMContext dmContext = new DMContext(criteria, LocalDateTime.now(), new Alternatives(new ArrayList<>()),
                    os, false, 0);

            Report report = null;
            try
            {
                report = module.executeProcess(dmContext);
            } catch (ModuleException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(report);
            assertEquals(Status.INTERACTION_WAS_NOT_TRIGGERED, report._interactionStatus);
            assertTrue(report._processingTime >= 0);
            assertNotNull(report._interactionTriggerResult);
            assertFalse(report._interactionTriggerResult._shouldInteract);
            assertTrue(report._interactionTriggerResult._processingTime >= 0);
            assertNull(report._interactionTriggerResult._callers);
            assertEquals(0, report._interactionTriggerResult._iteration);
            assertNull(report._referenceSetsConstructionResult);
            assertNull(report._feedbackProviderResult);

            System.out.println("=======================================================================================");
            report.printStringRepresentation();
        }

        {
            ArrayList<Alternative> alternatives = new ArrayList<>(13);
            alternatives.add(new Alternative("A0", new double[]{0.0d, 1.0d}));
            alternatives.add(new Alternative("DOMINATED", new double[]{1.0d, 1.0d}));
            alternatives.add(new Alternative("A1", new double[]{0.1d, 0.9d}));
            alternatives.add(new Alternative("DUPLICATED", new double[]{0.1d, 0.9d}));
            alternatives.add(new Alternative("A2", new double[]{0.2d, 0.8d}));
            alternatives.add(new Alternative("A3", new double[]{0.3d, 0.7d}));
            alternatives.add(new Alternative("A4", new double[]{0.4d, 0.6d}));
            alternatives.add(new Alternative("A5", new double[]{0.5d, 0.5d}));
            alternatives.add(new Alternative("A6", new double[]{0.6d, 0.4d}));
            alternatives.add(new Alternative("A7", new double[]{0.7d, 0.3d}));
            alternatives.add(new Alternative("A8", new double[]{0.8d, 0.2d}));
            alternatives.add(new Alternative("A9", new double[]{0.9d, 0.1d}));
            alternatives.add(new Alternative("A10", new double[]{1.0d, 0.0d}));

            DMContext dmContext = new DMContext(criteria, LocalDateTime.now(), new Alternatives(alternatives),
                    os, false, 1);

            Report report = null;
            try
            {
                report = module.executeProcess(dmContext);
            } catch (ModuleException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            assertNotNull(report);
            assertEquals(Status.PROCESS_ENDED_SUCCESSFULLY, report._interactionStatus);
            assertTrue(report._processingTime >= 0);
            {
                assertNotNull(report._interactionTriggerResult);
                assertTrue(report._interactionTriggerResult._shouldInteract);
                assertTrue(report._interactionTriggerResult._processingTime >= 0);
                assertNotNull(report._interactionTriggerResult._callers);
                assertEquals(1, report._interactionTriggerResult._callers.size());
                assertEquals(IterationInterval._NAME + "_1_2", report._interactionTriggerResult._callers.getFirst());
                assertEquals(1, report._interactionTriggerResult._iteration);
            }
            {
                assertNotNull(report._referenceSetsConstructionResult);
                assertEquals(1, report._referenceSetsConstructionResult._iteration);
                assertEquals(Status.PROCESS_ENDED_SUCCESSFULLY, report._referenceSetsConstructionResult._status);
                assertTrue(report._referenceSetsConstructionResult._processingTime >= 0);
                assertFalse(report._referenceSetsConstructionResult._terminatedDueToNotEnoughAlternatives);
                assertNull(report._referenceSetsConstructionResult._terminatedDueToNotEnoughAlternativesMessage);

                {
                    assertNotNull(report._referenceSetsConstructionResult._refiningResults);
                    assertEquals(Status.PROCESS_ENDED_SUCCESSFULLY, report._referenceSetsConstructionResult._refiningResults._status);
                    assertFalse(report._referenceSetsConstructionResult._refiningResults._terminatedDueToTerminationFilter);
                    assertNull(report._referenceSetsConstructionResult._refiningResults._terminatedDueToTerminationFilterMessage);
                    assertTrue(report._referenceSetsConstructionResult._refiningResults._terminationFiltersProcessingTime >= 0);
                    assertEquals(2, report._referenceSetsConstructionResult._refiningResults._reductionSize);
                    assertTrue(report._referenceSetsConstructionResult._refiningResults._reductionFiltersProcessingTime >= 0);
                    assertNotNull(report._referenceSetsConstructionResult._refiningResults._refinedAlternatives);
                    assertEquals(11, report._referenceSetsConstructionResult._refiningResults._refinedAlternatives.size());
                    Set<String> eNames = new HashSet<>();
                    for (int i = 0; i < 11; i++) eNames.add("A" + i);
                    for (IAlternativeWrapper alternative : report._referenceSetsConstructionResult._refiningResults._refinedAlternatives)
                        assertTrue(eNames.contains(alternative.getName()));
                }

                {
                    assertNotNull(report._referenceSetsConstructionResult._referenceSetsContainer);
                    assertEquals(3, report._referenceSetsConstructionResult._referenceSetsContainer.getNoSets());

                    assertEquals(1, report._referenceSetsConstructionResult._referenceSetsContainer.getCommonReferenceSets().getNoSets());
                    assertEquals(1, report._referenceSetsConstructionResult._referenceSetsContainer.getCommonReferenceSets().getUniqueSizes().length);
                    assertEquals(2, report._referenceSetsConstructionResult._referenceSetsContainer.getCommonReferenceSets().getUniqueSizes()[0]);
                    assertEquals(1, report._referenceSetsConstructionResult._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().size());
                    assertEquals(1, report._referenceSetsConstructionResult._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().get(2).size());
                    assertEquals("Alternatives = A0, A1", report._referenceSetsConstructionResult._referenceSetsContainer.getCommonReferenceSets().getReferenceSets().get(2).getFirst().getStringRepresentation());

                    assertEquals(2, report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().size());

                    assertTrue(report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().containsKey(dms[0]));
                    assertEquals(1, report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().get(dms[0]).getNoSets());
                    assertEquals(1, report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().get(dms[0]).getUniqueSizes().length);
                    assertEquals(2, report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().get(dms[0]).getUniqueSizes()[0]);
                    assertEquals(1, report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().get(dms[0]).getReferenceSets().size());
                    assertEquals(1, report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().get(dms[0]).getReferenceSets().get(2).size());
                    assertEquals("Alternatives = A2, A3", report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().get(dms[0]).getReferenceSets().get(2).getFirst().getStringRepresentation());

                    assertTrue(report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().containsKey(dms[1]));
                    assertEquals(1, report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().get(dms[1]).getNoSets());
                    assertEquals(1, report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().get(dms[1]).getUniqueSizes().length);
                    assertEquals(2, report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().get(dms[1]).getUniqueSizes()[0]);
                    assertEquals(1, report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().get(dms[1]).getReferenceSets().size());
                    assertEquals(1, report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().get(dms[1]).getReferenceSets().get(2).size());
                    assertEquals("Alternatives = A4, A5", report._referenceSetsConstructionResult._referenceSetsContainer.getDMReferenceSets().get(dms[1]).getReferenceSets().get(2).getFirst().getStringRepresentation());
                }
            }
            {
                assertNotNull(report._feedbackProviderResult);
                assertEquals(1, report._feedbackProviderResult._iteration);
                assertTrue(report._feedbackProviderResult._processingTime >= 0);
                assertEquals(2, report._feedbackProviderResult._feedback.size());

                assertTrue(report._feedbackProviderResult._feedback.containsKey(dms[0]));
                assertTrue(report._feedbackProviderResult._feedback.get(dms[0])._processingTime >= 0);
                assertEquals(1, report._feedbackProviderResult._feedback.get(dms[0])._iteration);
                assertEquals(2, report._feedbackProviderResult._feedback.get(dms[0])._feedback.size());
                assertInstanceOf(PairwiseComparison.class, report._feedbackProviderResult._feedback.get(dms[0])._feedback.getFirst());
                assertEquals(Relations.PREFERENCE, ((PairwiseComparison) report._feedbackProviderResult._feedback.get(dms[0])._feedback.getFirst()).getRelation());
                assertEquals("A1", ((PairwiseComparison) report._feedbackProviderResult._feedback.get(dms[0])._feedback.getFirst()).getPreferredAlternative().getName());
                assertEquals("A0", ((PairwiseComparison) report._feedbackProviderResult._feedback.get(dms[0])._feedback.getFirst()).getNotPreferredAlternative().getName());
                assertInstanceOf(PairwiseComparison.class, report._feedbackProviderResult._feedback.get(dms[0])._feedback.getLast());
                assertEquals(Relations.PREFERENCE, ((PairwiseComparison) report._feedbackProviderResult._feedback.get(dms[0])._feedback.getLast()).getRelation());
                assertEquals("A3", ((PairwiseComparison) report._feedbackProviderResult._feedback.get(dms[0])._feedback.getLast()).getPreferredAlternative().getName());
                assertEquals("A2", ((PairwiseComparison) report._feedbackProviderResult._feedback.get(dms[0])._feedback.getLast()).getNotPreferredAlternative().getName());


                assertTrue(report._feedbackProviderResult._feedback.containsKey(dms[1]));
                assertTrue(report._feedbackProviderResult._feedback.get(dms[1])._processingTime >= 0);
                assertEquals(1, report._feedbackProviderResult._feedback.get(dms[1])._iteration);
                assertEquals(2, report._feedbackProviderResult._feedback.get(dms[1])._feedback.size());
                assertInstanceOf(PairwiseComparison.class, report._feedbackProviderResult._feedback.get(dms[1])._feedback.getFirst());
                assertEquals(Relations.PREFERENCE, ((PairwiseComparison) report._feedbackProviderResult._feedback.get(dms[1])._feedback.getFirst()).getRelation());
                assertEquals("A1", ((PairwiseComparison) report._feedbackProviderResult._feedback.get(dms[1])._feedback.getFirst()).getPreferredAlternative().getName());
                assertEquals("A0", ((PairwiseComparison) report._feedbackProviderResult._feedback.get(dms[1])._feedback.getFirst()).getNotPreferredAlternative().getName());
                assertInstanceOf(PairwiseComparison.class, report._feedbackProviderResult._feedback.get(dms[1])._feedback.getLast());
                assertEquals(Relations.PREFERENCE, ((PairwiseComparison) report._feedbackProviderResult._feedback.get(dms[1])._feedback.getLast()).getRelation());
                assertEquals("A4", ((PairwiseComparison) report._feedbackProviderResult._feedback.get(dms[1])._feedback.getLast()).getPreferredAlternative().getName());
                assertEquals("A5", ((PairwiseComparison) report._feedbackProviderResult._feedback.get(dms[1])._feedback.getLast()).getNotPreferredAlternative().getName());
            }

            assertEquals(2, pP._DMSs[0].getHistory().getNoPreferenceExamples());
            try
            {
                IPreferenceInformation pi = pP._DMSs[0].getHistory().getPreferenceInformationCopy().getFirst()._preferenceInformation;
                assertInstanceOf(PairwiseComparison.class, pi);
                assertEquals(Relations.PREFERENCE, ((PairwiseComparison) pi).getRelation());
                assertEquals("A1", ((PairwiseComparison) pi).getPreferredAlternative().getName());
                assertEquals("A0", ((PairwiseComparison) pi).getNotPreferredAlternative().getName());

                pi = pP._DMSs[0].getHistory().getPreferenceInformationCopy().getLast()._preferenceInformation;
                assertInstanceOf(PairwiseComparison.class, pi);
                assertEquals(Relations.PREFERENCE, ((PairwiseComparison) pi).getRelation());
                assertEquals("A3", ((PairwiseComparison) pi).getPreferredAlternative().getName());
                assertEquals("A2", ((PairwiseComparison) pi).getNotPreferredAlternative().getName());
            } catch (HistoryException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);

            assertEquals(2, pP._DMSs[1].getHistory().getNoPreferenceExamples());
            try
            {
                IPreferenceInformation pi = pP._DMSs[1].getHistory().getPreferenceInformationCopy().getFirst()._preferenceInformation;
                assertInstanceOf(PairwiseComparison.class, pi);
                assertEquals(Relations.PREFERENCE, ((PairwiseComparison) pi).getRelation());
                assertEquals("A1", ((PairwiseComparison) pi).getPreferredAlternative().getName());
                assertEquals("A0", ((PairwiseComparison) pi).getNotPreferredAlternative().getName());

                pi = pP._DMSs[1].getHistory().getPreferenceInformationCopy().getLast()._preferenceInformation;
                assertInstanceOf(PairwiseComparison.class, pi);
                assertEquals(Relations.PREFERENCE, ((PairwiseComparison) pi).getRelation());
                assertEquals("A4", ((PairwiseComparison) pi).getPreferredAlternative().getName());
                assertEquals("A5", ((PairwiseComparison) pi).getNotPreferredAlternative().getName());
            } catch (HistoryException e)
            {
                msg = e.getMessage();
            }
            assertNull(msg);
            System.out.println("=======================================================================================");
            report.printStringRepresentation();
        }
    }


}