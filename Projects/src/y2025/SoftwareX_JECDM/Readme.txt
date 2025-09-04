This project provides executables used when preparing the article on
"JECDM: a Java Framework for Evolutionary Computation and Decision-Making".
Specifically:
- ContainsGetter, ExecuteCrossSummarizer, and ExecuteExperiment are responsible for preparing, running, and
  post-processing an experiment concerning a time analysis of three implementations of the NSGA-II method (JECDM,
  jMetal, and pymoo). The JMETAL and PYMOO class serves as wrappers for external implementations.
- GenerateTable: Reads the time results from TimeResults.xlsx file and parses them into a LaTeX code.
- Figure2: Generates article's Figure 2.
- Figure3: Generates article's Figure 3.
- Figure4: Generates article's Figure 4.