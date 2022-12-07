package de.fhws.ai.geneticalgorithm.logger.graphplotter;

import com.aspose.cells.*;
import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;
import de.fhws.ai.geneticalgorithm.logger.Logger;
import de.fhws.ai.geneticalgorithm.logger.graphplotter.lines.LineGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GraphPlotLogger implements Logger {

    private static final int WIDTH = 15;
    private static final int HEIGHT = 25;

    private static final String PLOT_HOME_DIR = "plots";
    private static final String PLOT_FILE_ENDING = ".xls";

    private static final String DEFAULT_CHART_TITLE_PREFIX = "Plot for Population size: ";
    private static final String X_AXIS_NAME = "Generation";
    private static final String Y_AXIS_NAME = "Fitness Value";

    private static final AtomicBoolean firstTime = new AtomicBoolean(true);
    private int plottingInterval = 1;
    private final String filename;
    private String chartTitle;
    private final List<LineGenerator> lineGenerators;
    private int counter = 0;


    public GraphPlotLogger(int plottingInterval, String filename, String chartTitle, LineGenerator... lineGenerators) {
        this.plottingInterval = plottingInterval;
        this.filename = filename;
        this.chartTitle = chartTitle;
        this.lineGenerators = Arrays.asList(lineGenerators);
    }

    /**
     * This Plotter can be used to create a .xls file including a graph of the evolution process.
     * @param plottingInterval is the interval in which the file gets created. It will always trigger at the end of the evolution process.
     *                         So a value below 0 results in exactly one triggering
     * @param filename this is the name of the resulting file without file-ending
     * @param lineGenerators generate the plots in the chart
     */
    public GraphPlotLogger(int plottingInterval, String filename, LineGenerator... lineGenerators){
        this.plottingInterval = plottingInterval;
        this.filename = filename;
        this.lineGenerators = Arrays.asList(lineGenerators);
    }

    @Override
    public void log(int maxGen, Population<? extends Individual<?>> population) {
        configureValuesOnFirstLog(population);

        savePopulationValues(population);
        doPlottingIfNeeded(maxGen, population);
    }

    private void configureValuesOnFirstLog(Population<? extends Individual<?>> population) {
        if(firstTime.getAndSet(false))
            configureChartDefaultTitle(population);

    }

    private void configureChartDefaultTitle(Population<? extends Individual<?>> population) {
        if(chartTitle == null)
            chartTitle = DEFAULT_CHART_TITLE_PREFIX+ population.getSize();
    }


    private void savePopulationValues(Population<? extends Individual<?>> population) {
        lineGenerators.forEach(lg -> lg.log(population));
    }

    private void doPlottingIfNeeded(int maxGen, Population<? extends Individual<?>> population) {
        if(!isLastGeneration(maxGen, population) && plottingInterval <= 0)
            return;

        if (counter++ % plottingInterval == 0 || isLastGeneration(maxGen, population))
            plot(maxGen);


    }

    private boolean isLastGeneration(int maxGen, Population<? extends Individual<?>> population) {
        return population.getGeneration() == maxGen;
    }


    public void plot(int plottingAmountOfData) {
        Workbook workbook = new Workbook();
        Worksheet worksheet = workbook.getWorksheets().get(0);

        putDataIntoWorksheet(worksheet, plottingAmountOfData);

        createGraph(worksheet, plottingAmountOfData);

        saveWorkbook(workbook);
    }

    private void createGraph(Worksheet worksheet, int plottingAmountOfData) {
        Chart chart = createChart(worksheet);
        chart.getTitle().setText(chartTitle);
        setAxisNames(chart);
        chart.setChartDataRange(getChartDataArea(worksheet, plottingAmountOfData), true);
    }

    private void setAxisNames(Chart chart) {
        chart.getCategoryAxis().getTitle().setText(X_AXIS_NAME);
        chart.getValueAxis().getTitle().setText(Y_AXIS_NAME);
    }

    private String getChartDataArea(Worksheet worksheet, int plottingAmountOfData) {
        return getChartDataFromLocation(worksheet) + ":" + getChartDataToLocation(worksheet, plottingAmountOfData);
    }

    private String getChartDataFromLocation(Worksheet worksheet) {
        return worksheet.getCells().get(0, 0).getName();
    }

    private String getChartDataToLocation(Worksheet worksheet, int plottingAmountOfData) {
        return worksheet.getCells().get(plottingAmountOfData, lineGenerators.size()-1).getName();
    }

    private Chart createChart(Worksheet worksheet) {
        int chartIndex = worksheet.getCharts().add(ChartType.LINE, 0, 0, HEIGHT, WIDTH);
        return worksheet.getCharts().get(chartIndex);
    }

    private void saveWorkbook(Workbook workbook) {
        try {
            workbook.save(PLOT_HOME_DIR + "/" + filename + PLOT_FILE_ENDING, SaveFormat.AUTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void putDataIntoWorksheet(Worksheet worksheet, int plottingAmountOfData) {
        for (int col = 0; col < lineGenerators.size(); col++) {
            LineGenerator lg = lineGenerators.get(col);
            writeLineGeneratorNameIntoWorksheet(worksheet, col, lg);
            putValuesIntoWorksheet(worksheet, plottingAmountOfData, col, lg);
        }
    }

    private void writeLineGeneratorNameIntoWorksheet(Worksheet worksheet, int col, LineGenerator lg) {
        worksheet.getCells().get(0, col).putValue(lg.getName());
    }

    private void putValuesIntoWorksheet(Worksheet worksheet, int plottingAmountOfData, int col, LineGenerator lg) {
        for (int row = 0; row < plottingAmountOfData; row++)
            putValueInWorksheet(worksheet, row + 1, col, getValueFromLineGenerator(lg, row));
    }

    private Double getValueFromLineGenerator(LineGenerator lg, int index) {
        return lg.getValues().size() > index ? lg.getValue(index) : 0;
    }

    private void putValueInWorksheet(Worksheet worksheet, int row, int col, double value) {
        worksheet.getCells().get(row, col).putValue(value);
    }


}
