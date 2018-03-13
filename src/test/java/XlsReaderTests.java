import com.ce2tech.averager.model.dataacces.XlsReader;
import com.ce2tech.averager.model.dataacces.XlsWriter;
import com.ce2tech.averager.model.dataobjects.Measurand;
import com.ce2tech.averager.model.dataobjects.Measurement;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(DataProviderRunner.class)
public class XlsReaderTests {

    private List< List<Measurand> > testMeasurementAsList;
    private Measurement testMeasurement;
    private XlsReader fileReader = new XlsReader();
    private XlsWriter fileWriter = new XlsWriter();

    @Before
    public void addTestDataToTestMeasurement() {
        List<Measurand> testSample = new ArrayList<>();
        testSample.add(new Measurand("Data", LocalDate.now()));
        testSample.add(new Measurand("Czas", LocalTime.now()));
        testSample.add(new Measurand("NO[ppm]", "-" ));
        testSample.add(new Measurand("SO2[ppm]", 2.0 ));

        List<Measurand> testSample2 = new ArrayList<>(testSample);
        List<Measurand> testSample3 = new ArrayList<>(testSample);

        testMeasurementAsList = new ArrayList<>();
        testMeasurementAsList.add(testSample);
        testMeasurementAsList.add(testSample2);
        testMeasurementAsList.add(testSample3);

        testMeasurement = new Measurement();
        testMeasurement.setMeasurement(testMeasurementAsList);
    }

    @DataProvider
    public static Object[][] fileSizeProvider() {
        return new Object[][]{
                //{file_path, file_samples_length, file_measurement_length_without_header}
                {"testFile_tenSeconds_nox_temp.xls", 9, 738},
                {"testFile_tenSeconds_nox.xls", 8, 446},
                {"testFile_tenSeconds.xls", 7, 363},
                {"testFile_oneMinute.xls", 7, 61},
                {"testFile_messedUp.xls", 5, 2435},
                //Wrong files
                {"testFile_randomWorkbook.xls", 0, 0},
                {"wrong-file-name.jpg", 0, 0},
                {"", 0, 0}
        };
    }

    @Test
    @UseDataProvider("fileSizeProvider")
    public void shouldReturnListWithDataFromFile(String testFilePath, int testFileSamplesSize, int testFileMeasurementSize) {
        //Given
        Measurement measurement;

        //When
        measurement = fileReader.readMeasurementFromFile(testFilePath);

        //Then
        assertThat(measurement.size()).isEqualTo(testFileMeasurementSize);
        for (List<Measurand> sample : measurement.getMeasurement())
            assertThat(sample.size()).isEqualTo(testFileSamplesSize);
    }

    @Test
    public void shouldCreateHeaderInWorkbook() {
        //Given
        fileWriter.prepareEmptyWorkbook();
        Workbook testWorkbook = fileWriter.workbook;
        Sheet testSheet = testWorkbook.getSheetAt(0);

        //When
        fileWriter.createMeasurementHeaderInWorkbook(testMeasurement);

        //Then
        assertThat(testSheet.getPhysicalNumberOfRows()).isEqualTo(1);
        for (Row row : testSheet)
            assertThat(row.getPhysicalNumberOfCells()).isEqualTo(4);
    }

    @Test
    public void shouldNotCreateHeaderInWorkbookWithoutSheets() {
        //Given
        fileWriter.prepareEmptyWorkbook();

        //When
        fileWriter.createMeasurementHeaderInWorkbook(testMeasurement);

        //Then
        assertThat(fileWriter.workbook.getNumberOfSheets()).isEqualTo(0);
    }

    @Test
    public void shouldCreateMultipleHeadersInWorkbook() {
        //Given
        fileWriter.prepareEmptyWorkbook();
        Workbook testWorkbook = fileWriter.workbook;
        Sheet testSheet = testWorkbook.getSheetAt(0);

        //When
        fileWriter.createMeasurementHeaderInWorkbook(testMeasurement);
        fileWriter.createMeasurementHeaderInWorkbook(testMeasurement);

        //Then
        assertThat(testSheet.getPhysicalNumberOfRows()).isEqualTo(2);
        for (Row row : testSheet)
            assertThat(row.getPhysicalNumberOfCells()).isEqualTo(4);
    }

    @Test
    public void shouldCreateMeasurementInWorkbook() {
        //Given
        fileWriter.prepareEmptyWorkbook();
        Workbook testWorkbook = fileWriter.workbook;
        Sheet testSheet = testWorkbook.getSheetAt(0);

        //When
        fileWriter.writeMeasurementToWorkbook(testMeasurementAsList);

        //Then
        assertThat(testSheet.getPhysicalNumberOfRows()).isEqualTo(3);
        for (Row row : testSheet)
            assertThat(row.getPhysicalNumberOfCells()).isEqualTo(4);
    }

    @Test
    public void shouldNotCreateMeasurementInWorkbookWithoutSheets() {
        //Given
        fileWriter.prepareEmptyWorkbook();

        //When
        fileWriter.writeMeasurementToWorkbook(testMeasurementAsList);

        //Then
        assertThat(fileWriter.workbook.getNumberOfSheets()).isEqualTo(0);
    }

    @Test
    public void shouldCreateMultipleMeasurementsInWorkbook() {
        //Given
        fileWriter.prepareEmptyWorkbook();
        Workbook testWorkbook = fileWriter.workbook;
        Sheet testSheet = testWorkbook.getSheetAt(0);

        //When
        fileWriter.writeMeasurementToWorkbook(testMeasurementAsList);
        fileWriter.writeMeasurementToWorkbook(testMeasurementAsList);

        //Then
        assertThat(testSheet.getPhysicalNumberOfRows()).isEqualTo(6);
        for (Row row : testSheet)
            assertThat(row.getPhysicalNumberOfCells()).isEqualTo(4);
    }

}