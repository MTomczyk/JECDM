package y2025.ERS.e3_samplers;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import print.PrintUtils;

import java.io.File;
import java.io.IOException;

/**
 * Performs a simple analysis on when ERS should stop.
 *
 * @author MTomczyk
 */
public class IterationsLimitAnalyzer
{
    /**
     * Runs the experiment.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        String pref = "D:" + File.separator + "experiments" + File.separator + "ERS" + File.separator + "e3_samplers";
        double[] alphas = new double[]{1.0d, 5.0d, Double.POSITIVE_INFINITY}; // DM's alpha settings
        int[] M = new int[]{2, 3, 4, 5};
        int[] N = new int[]{50, 100, 150, 200};
        int[] PCS = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};


        double devTh = 0.1d;
        int itLimit = 10000;// x 100
        boolean[] optDir = new boolean[]{true, false};

        double[][] R = new double[alphas.length * M.length * N.length][PCS.length];
        for (int i = 0; i < alphas.length; i++)
        {
            for (int j = 0; j < M.length; j++)
            {
                for (int k = 0; k < N.length; k++)
                {
                    int currentRow = i * (N.length * M.length) + j * N.length + k;
                    for (int pc = 0; pc < PCS.length; pc++)
                    {
                        R[currentRow][pc] = itLimit;

                        System.out.println("Processing for alpha = " + alphas[i] + " M = " + M[j] + " N = " + N[k] + " PCS = " + PCS[pc]);
                        String name = "SAMPLER_ERS_2_2_DM_" + alphas[i] + "_M_" + M[j] + "_N_" + N[k] + "_PCS_" + PCS[pc];
                        String path = pref + File.separator + name + File.separator + name + ".xlsx";
                        System.out.println(path);

                        System.out.println("Path = " + path);
                        File file = new File(path);

                        try (XSSFWorkbook workbook = new XSSFWorkbook(file))
                        {
                            XSSFSheet minSh = workbook.getSheetAt(3);
                            XSSFSheet stdSh = workbook.getSheetAt(4);
                            XSSFSheet compSh = workbook.getSheetAt(5);

                            int startGen;
                            for (startGen = 0; startGen < itLimit; startGen++)
                            {
                                // check compatibility
                                {
                                    Row row = compSh.getRow(1 + startGen);
                                    Cell cell = row.getCell(1);
                                    double value = cell.getNumericCellValue();
                                    if (Double.compare(value, 0.0d) >= 0) break;
                                }
                            }
                            System.out.println("Starting generation = " + startGen);

                            if (startGen == itLimit)
                            {
                                R[currentRow][pc] = startGen;
                                continue;
                            }

                            XSSFSheet[] sheets = new XSSFSheet[]{minSh, stdSh};
                            double worstGen = -1;

                            for (int s = 0; s < 2; s++) // only MIN_CN
                            {
                                double minV = Double.POSITIVE_INFINITY;
                                double maxV = -1.0d;
                                for (int g = 0; g < itLimit; g++)
                                {
                                    Row row = sheets[s].getRow(1 + g);
                                    if (!row.getCell(2).getCellType().equals(CellType.NUMERIC)) continue;
                                    double val = row.getCell(2).getNumericCellValue();
                                    if (Double.compare(val, 0.0d) < 0) continue;
                                    if (Double.compare(val, 1.0E3) > 0) continue;
                                    if (Double.compare(val, minV) < 0) minV = val;
                                    if (Double.compare(val, maxV) > 0) maxV = val;
                                }

                                double delta = maxV - minV;
                                System.out.println("min and max = " + minV + " " + maxV);

                                for (int gen = startGen; gen < itLimit; gen++)
                                {
                                    Row cR = sheets[s].getRow(1 + gen);
                                    if (!cR.getCell(2).getCellType().equals(CellType.NUMERIC)) continue;
                                    double cV = cR.getCell(2).getNumericCellValue();
                                    double stat = (maxV - cV) / delta;
                                    if (!optDir[s]) stat = 1.0d - stat;
                                    if (Double.compare(stat, devTh) < 0)
                                    {
                                        if (Double.compare(gen, worstGen) > 0) worstGen = gen;
                                        break;
                                    }
                                }
                            }

                            R[currentRow][pc] = worstGen;

                            System.out.println("Statistics = " + R[currentRow][pc]);

                        } catch (IOException | InvalidFormatException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

        System.out.println("Printing results...");
        PrintUtils.print2dDoubles(R, 4);
    }

}
