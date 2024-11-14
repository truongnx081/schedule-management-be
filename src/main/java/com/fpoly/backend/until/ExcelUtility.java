package com.fpoly.backend.until;

import com.fpoly.backend.entities.Clazz;
import com.fpoly.backend.entities.ExamSchedule;
import com.fpoly.backend.entities.Schedule;
import com.fpoly.backend.repository.*;
import com.fpoly.backend.services.IdentifyUserAccessService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Component
public class ExcelUtility {

     BlockRepository blockRepository;
     SemesterRepository semesterRepository;
     YearRepository yearRepository;
     SubjectRepository subjectRepository;
     InstructorRepository instructorRepository;
     IdentifyUserAccessService identifyUserAccessService;
     ShiftRepository shiftRepository;
     RoomRepository roomRepository;
     ClazzRepository clazzRepository;

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


//    static String[] HEADERs = { "ID", "Student Name", "Email", "Mobile No." };
//    static String SHEET = "student";

    // Kiểm tra có phải là file excel không
    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    // Import excel data clazz
    public List<Clazz> excelToClazzList(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet("clazz");
            Iterator<Row> rows = sheet.iterator();
            List<Clazz> clazzList = new ArrayList<Clazz>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                Clazz clazz = new Clazz();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            clazz.setCode(currentCell.getStringCellValue());
                            break;
                        case 1:
                            clazz.setOnlineLink(currentCell.getStringCellValue());
                            break;
                        case 2:
                            clazz.setQuantity((int)currentCell.getNumericCellValue());
                            break;
                        case 3:
                            clazz.setBlock(blockRepository.findById((int)currentCell.getNumericCellValue()).orElseThrow(() ->
                                    new RuntimeException("Block not found")));
                            break;
                        case 4:
                            clazz.setSemester(semesterRepository.findById(currentCell.getStringCellValue()).orElseThrow(() ->
                                    new RuntimeException("Semester not found")));
                            break;
                        case 5:
                            clazz.setYear(yearRepository.findById((int)currentCell.getNumericCellValue()).orElseThrow(() ->
                                    new RuntimeException("Year not found")));
                            break;
                        case 6:
                            clazz.setSubject(subjectRepository.findById((int)currentCell.getNumericCellValue()).orElseThrow(() ->
                                    new RuntimeException("Subject not found")));
                            break;
                        case 7:
                            clazz.setInstructor(instructorRepository.findById((int)currentCell.getNumericCellValue()).orElseThrow(() ->
                                    new RuntimeException("Instructor not found")));
                            break;
                        case 8:
                            clazz.setAdmin(identifyUserAccessService.getAdmin());
                            break;
                        case 9:
                            clazz.setShift(shiftRepository.findById((int)currentCell.getNumericCellValue()).orElseThrow(() ->
                                    new RuntimeException("Shift not found")));
                            break;
                        case 10:
                            clazz.setRoom(roomRepository.findById((int)currentCell.getNumericCellValue()).orElseThrow(() ->
                                    new RuntimeException("Room not found")));
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                clazzList.add(clazz);
            }
            workbook.close();
            return clazzList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    // Import excel data exam schedule
    public List<ExamSchedule> excelToExamScheduleList(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet("examSchedule");
            Iterator<Row> rows = sheet.iterator();
            List<ExamSchedule> examSchedulesList = new ArrayList<ExamSchedule>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                ExamSchedule examSchedule = new ExamSchedule();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            examSchedule.setDate(currentCell.getLocalDateTimeCellValue().toLocalDate());
                            break;
                        case 1:
                            examSchedule.setClazz(clazzRepository.findById((int)currentCell.getNumericCellValue()).orElseThrow(() ->
                                    new RuntimeException("Clazz not found")));
                            break;
                        case 2:
                            examSchedule.setBatch((int)currentCell.getNumericCellValue());
                            break;
                        case 3:
                            examSchedule.setRoom(roomRepository.findById((int)currentCell.getNumericCellValue()).orElseThrow(() ->
                                    new RuntimeException("Room not found")));
                            break;
                        case 4:
                            examSchedule.setShift(shiftRepository.findById((int)currentCell.getNumericCellValue()).orElseThrow(() ->
                                    new RuntimeException("Shift not found")));
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                examSchedulesList.add(examSchedule);
            }
            workbook.close();
            return examSchedulesList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    // Import excel data study schedule
    public List<Schedule> excelToStudyScheduleList(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet("studySchedule");
            Iterator<Row> rows = sheet.iterator();
            List<Schedule> schedulesList = new ArrayList<Schedule>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                Schedule schedule = new Schedule();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            schedule.setDate(currentCell.getLocalDateTimeCellValue().toLocalDate());
                            break;
                        case 1:
                            schedule.setClazz(clazzRepository.findById((int)currentCell.getNumericCellValue()).orElseThrow(() ->
                                    new RuntimeException("Clazz not found")));
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                schedulesList.add(schedule);
            }
            workbook.close();
            return schedulesList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
