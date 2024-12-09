package updater;

import color.gradient.Color;
import dataset.DataSet;
import dataset.IDataSet;
import dataset.painter.style.MarkerStyle;
import dataset.painter.style.enums.Marker;
import org.junit.jupiter.api.Test;
import plot.Plot2D;
import plotswrapper.AbstractPlotsWrapper;
import plotswrapper.PlotsWrapper;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Various tests for {@link DataUpdater}.
 *
 * @author MTomczyk
 */
class DataUpdaterTest
{
    /**
     * Tests for validation.
     */
    @Test
    void test1()
    {
        {
            // with one plot
            Plot2D plot = new Plot2D(new Plot2D.Params());
            AbstractPlotsWrapper pw = new PlotsWrapper(plot);

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
                pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0)};
                pDU._processorToPlots = new ProcessorToPlots[]{new ProcessorToPlots(0,  DataSet.getFor2D("DS1", new MarkerStyle(1.0f, Color.RED, Marker.SQUARE)))};
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                }
                assertTrue(passed);
            }

            {
                MarkerStyle ms = new MarkerStyle(1.0f, Color.RED, Marker.SQUARE);
                boolean passed = true;
                try
                {
                    DataUpdater.getSimpleDataUpdater(pw, new DummySource(), DataSet.getFor2D("DS1", ms));
                } catch (Exception e)
                {
                    passed = false;
                }
                assertTrue(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = null;
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Data sources not provided", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[0];
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Data sources not provided", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[1];
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Data source no. 0 is null", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Data processors not provided", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[0];
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Data processors not provided", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[1];
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Data processor no. 0 is null", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Sources->processors: mapping not provided", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
                pDU._sourcesToProcessors = new SourceToProcessors[0];
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Sources->processors: mapping not provided", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
                pDU._sourcesToProcessors = new SourceToProcessors[1];
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Sources->processors: mapping no. 0 is null", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }



            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
                pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(null)};
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Sources->processors: mapping no. 0 has no mapping", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
                pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0)};
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Processors->plots: mapping not provided", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
                pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0)};
                pDU._processorToPlots = new ProcessorToPlots[0];
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Processors->plots: mapping not provided", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
                pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0)};
                pDU._processorToPlots = new ProcessorToPlots[1];
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Processors->plots: mapping no. 0 is null", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
                pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0)};
                pDU._processorToPlots = new ProcessorToPlots[]{new ProcessorToPlots(
                        null,
                        new IDataSet[]{DataSet.getFor2D("DS1", new MarkerStyle(1.0f, Color.RED, Marker.SQUARE))})};

                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Processors->plots: mapping no. 0 has no mapping", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
                pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0)};
                pDU._processorToPlots = new ProcessorToPlots[]{new ProcessorToPlots(0, null)};
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Processors->plots: mapping no. 0: one of the reference data sets is null", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor(), new DataProcessor()};
                pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0)};
                pDU._processorToPlots = new ProcessorToPlots[]{new ProcessorToPlots(0,
                        DataSet.getFor2D("DS1", new MarkerStyle(1.0f, Color.RED, Marker.SQUARE)))};
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Provided processors length != processors -> plots length", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource(), new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
                pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0)};
                pDU._processorToPlots = new ProcessorToPlots[]{new ProcessorToPlots(0,
                        DataSet.getFor2D("DS1", new MarkerStyle(1.0f, Color.RED, Marker.SQUARE)))};
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Provided sources length != sources -> processors length", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(null);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
                pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0)};
                pDU._processorToPlots = new ProcessorToPlots[]{new ProcessorToPlots(0,
                        DataSet.getFor2D("DS1", new MarkerStyle(1.0f, Color.RED, Marker.SQUARE)))};
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Plots wrapper not provided", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
                pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0)};
                pDU._processorToPlots = new ProcessorToPlots[]{new ProcessorToPlots(1,
                        DataSet.getFor2D("DS1", new MarkerStyle(1.0f, Color.RED, Marker.SQUARE)))};
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("There is no plot of id = 1", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }

            {
                DataUpdater.Params pDU = new DataUpdater.Params(pw);
                pDU._dataSources = new IDataSource[]{new DummySource(), new DummySource()};
                pDU._dataProcessors = new IDataProcessor[]{new DataProcessor()};
                pDU._sourcesToProcessors = new SourceToProcessors[]{new SourceToProcessors(0), new SourceToProcessors(0)};
                pDU._processorToPlots = new ProcessorToPlots[]{new ProcessorToPlots(0,
                        DataSet.getFor2D("DS1", new MarkerStyle(1.0f, Color.RED, Marker.SQUARE)))};
                boolean passed = true;
                try
                {
                    new DataUpdater(pDU);
                } catch (Exception e)
                {
                    passed = false;
                    assertEquals("Processor no. 0 is assigned multiple sources", e.getMessage());
                    System.out.println(e.getMessage());
                }
                assertFalse(passed);
            }
        }
    }
}