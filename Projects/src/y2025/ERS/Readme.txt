This project is considered a supplement to the article ``Efficient Preference Learning Algorithm for Interactive
Evolutionary Multi-Objective Optimization'' (https://doi.org/10.1016/j.swevo.2025.102254).

The content:
1) e1_auxiliary package provides auxiliary scripts:
- AnimationERS executes the example animation portraying the performance of ERS
- AnimationFRS executes the example animation portraying the performance of FRS
- GenerateConceptPlots generates concept plots for the main paper
- GeneratePCsData generates the data on artificial DMs and their pairwise comparisons used in the study
- OffspringDistribution demonstrated the performance of the reproduction operators introduced in the main paper
- PCsPlot2D illustrates example simulated pairwise comparisons (2D case)
- PCsPlot3D illustrates example simulated pairwise comparisons (3D case)

2) e2_ers_calibration package provides scripts related to calibrating ERS (sensitivity analysis):
- ExecuteExperiment runs the experiment (see ContainersGetter for the experiment definition)
- results.GenerateTables_e2_appendix_1_RO generates LaTeX tables for the eAppendix
- results.GenerateTables_e2_appendix_1_RO_Summary generates LaTeX tables for the eAppendix

3) e3_samples package provides scripts related to performing a direct comparison of ERS and FRS when treated as standalone procedures:
- ExecuteExperiment runs the experiment (see ContainersGetter for the experiment definition)
- IterationsLimitAnalyzed is an auxiliary post-analyzer that identifies the ERS's sweet spot regarding setting its iterations limit
- results.GenerateTables... are various scripts that generate LaTeX tables for the main paper and the eAppendix
- IllustrateConvergence generates convergence plots
- IllustrateModels generates scatter plots illustrating the models constructed by the samples in an example scenario

4) e4_interactive package provides scripts related to executing an experiment that treats FRS and ERS as subsystems of the IEMO/D algorithm:
- ExecuteExperiment runs the experiment (see ContainersGetter for the experiment definition)
- results.GenerateTables... are various scripts that generate LaTeX tables for the main paper and the eAppendix
- IllustrateConvergence generates convergence plots
- IllustratePopulationsDTLZ2 generates scatter plots illustrating the solutions constructed by IEMO/D in an example scenario

5) e5_use_case package provides scripts related to testing IEMO/D with FRS and ERS when applied to a real-world case study (crash-worthiness):
- UseCaseIEMO/D: generates scatter plots illustrating the solutions constructed by IEMO/D (FRS/ERS)
- UseCaseMOEA/D: generates scatter plots illustrating the solutions constructed by MOEA/D