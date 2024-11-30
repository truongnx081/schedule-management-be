package com.fpoly.backend.until;

import com.fpoly.backend.entities.*;
import com.fpoly.backend.repository.*;
import com.fpoly.backend.services.IdentifyUserAccessService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
    private final StudyDayRepository studyDayRepository;
    private final WeekDayRepository weekDayRepository;
    private final SpecializationRepository specializationRepository;
    private final AreaRepository areaRepository;


//    static String[] HEADERs = { "ID", "Student Name", "Email", "Mobile No." };
//    static String SHEET = "student";

    // Kiểm tra có phải là file excel không
    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    // Import excel data clazz
    public void  excelToClazzList(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet("DSLH");
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
                        case 1:
                            clazz.setCode(currentCell.getStringCellValue());
                            break;
                        case 2:
                            clazz.setOnlineLink(currentCell.getStringCellValue());
                            break;
                        case 3:
                            clazz.setQuantity((int)currentCell.getNumericCellValue());
                            break;
                        case 4:
                            clazz.setBlock(blockRepository.findById((int)currentCell.getNumericCellValue()).orElseThrow(() ->
                                    new RuntimeException("Block not found")));
                            break;
                        case 5:
                            clazz.setSemester(semesterRepository.findById(currentCell.getStringCellValue()).orElseThrow(() ->
                                    new RuntimeException("Semester not found")));
                            break;
                        case 6:
                            clazz.setYear(yearRepository.findById((int)currentCell.getNumericCellValue()).orElseThrow(() ->
                                    new RuntimeException("Year not found")));
                            break;
                        case 7:
                            clazz.setSubject(subjectRepository.findByCode(currentCell.getStringCellValue()).orElseThrow(() ->
                                    new RuntimeException("Subject not found")));
                            break;
                        case 8:
                            clazz.setInstructor(instructorRepository.findByCode(currentCell.getStringCellValue()).orElseThrow(() ->
                                    new RuntimeException("Instructor not found")));
                            break;
                        case 9:
                            clazz.setShift(shiftRepository.findById((int)currentCell.getNumericCellValue()).orElseThrow(() ->
                                    new RuntimeException("Shift not found")));
                            break;
                        case 10:
                            if(!currentCell.getStringCellValue().isEmpty()){
                                clazz.setRoom(roomRepository.findByName(currentCell.getStringCellValue()).orElseThrow(() ->
                                        new RuntimeException("Room not found")));
                            }
                            else
                                clazz.setRoom(null);
                            break;
                        case 11:
                            // Lấy chuỗi ngày học trong tuần từ Excel
                            String weekdayString = currentCell.getStringCellValue();

                            // Chuyển đổi chuỗi ngày học thành danh sách WeekDay
                            List<WeekDay> weekDays = parseWeekDays(weekdayString);

                            // Lưu clazz và ngày học vào database
                            saveStudyDays(clazz, weekDays);
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
//                    clazz.setAdmin(identifyUserAccessService.getAdmin());
//                clazzList.add(clazz);
            }
            workbook.close();
//            return clazzList;
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

    private Clazz ensureClazzExists(Clazz clazz) {
        // Kiểm tra clazz dựa trên một tiêu chí xác định (ví dụ: id hoặc mã lớp)
//        Optional<Clazz> existingClazz = clazzRepository.findById(clazz.getId());
//        if (existingClazz.isPresent()) {
//            return existingClazz.get(); // Nếu đã tồn tại, trả về đối tượng từ DB
//        }
        // Nếu chưa tồn tại, lưu clazz vào DB và trả về đối tượng đã lưu
        clazz.setAdmin(identifyUserAccessService.getAdmin());
        return clazzRepository.save(clazz);
    }

    private void saveStudyDays(Clazz clazz, List<WeekDay> weekDays) {
        // Đảm bảo clazz đã tồn tại trong database
        Clazz persistedClazz = ensureClazzExists(clazz);

        // Tạo danh sách StudyDay và liên kết với clazz đã lưu
        List<StudyDay> studyDays = new ArrayList<>();
        for (WeekDay weekDay : weekDays) {
            StudyDay studyDay = new StudyDay();
            studyDay.setClazz(persistedClazz);
            studyDay.setWeekDay(weekDay);
            studyDays.add(studyDay);
        }

        // Lưu danh sách StudyDay vào database
        studyDayRepository.saveAll(studyDays);
    }

    private List<WeekDay> parseWeekDays(String weekdayString) {
        if (weekdayString == null || weekdayString.isEmpty()) {
            throw new IllegalArgumentException("Weekday string is null or empty");
        }

        // Chuyển đổi chuỗi nếu cần
        if (weekdayString.equals("2, 4, 6")) {
            weekdayString = "1,3,5";
        } else if (weekdayString.equals("3, 5, 7")) {
            weekdayString = "2,4,6";
        }

        // Tách chuỗi và tạo danh sách WeekDay
        String[] weekdays = weekdayString.split(",");
        List<WeekDay> weekDayList = new ArrayList<>();
        for (String wd : weekdays) {
            WeekDay weekDay = weekDayRepository.findById(Integer.parseInt(wd.trim())).orElseThrow(()
            -> new RuntimeException("Ngày học này không tồn tại"));
//            weekDay.setId(Integer.parseInt(wd.trim())); // Trim để loại bỏ khoảng trắng
            weekDayList.add(weekDay);
        }
        return weekDayList;
    }

    // Import excel data instructor
    public List<Instructor> excelToInstructorList(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet("DSGV");
            Iterator<Row> rows = sheet.iterator();
            List<Instructor> instructorsList = new ArrayList<Instructor>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                Instructor instructor = new Instructor();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 1:
                            if(instructorRepository.existsByCode(currentCell.getStringCellValue())){
                                throw new RuntimeException(String.format("Mã giảng viên %s đã tồn tại", currentCell.getStringCellValue()));
                            }
                            else
                                instructor.setCode(currentCell.getStringCellValue());
                            break;
                        case 2:
                            instructor.setLastName(currentCell.getStringCellValue());
                            break;
                        case 3:
                            instructor.setFirstName(currentCell.getStringCellValue());
                            break;
                        case 4:
                            instructor.setGender(currentCell.getStringCellValue().equals("Nam"));
                            break;
                        case 5:
                            if(currentCell.getCellType() == CellType.NUMERIC) // là kiểu date
                                instructor.setBirthday(currentCell.getLocalDateTimeCellValue().toLocalDate());
                            else if (currentCell.getCellType() == CellType.STRING) { // là kiểu chuỗi
                                String dateString = currentCell.getStringCellValue();
                                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                LocalDate birthDate = LocalDate.parse(dateString, dateTimeFormatter);
                                instructor.setBirthday(birthDate);
                            }
                            break;
                        case 6:
                            instructor.setPhone(currentCell.getStringCellValue());
                            break;
                        case 7:
                            instructor.setSchoolEmail(currentCell.getStringCellValue());
                            break;
                        case 8:
                            instructor.setPrivateEmail(currentCell.getStringCellValue());
                            break;
                        case 9:
                            instructor.setAddress(currentCell.getStringCellValue());
                            break;
                        case 10:
                            Specialization specialization =specializationRepository.findByName(currentCell.getStringCellValue())
                                            .orElseThrow(()-> new RuntimeException(String.format("Bộ môn %s không tồn tại", currentCell.getStringCellValue().toUpperCase())));
                            instructor.setSpecialization(specialization);
                            break;
                        case 11:
                            instructor.setDescription(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                instructor.setCreatedBy(identifyUserAccessService.getAdmin().getCode());
                instructorsList.add(instructor);
            }
            workbook.close();
            return instructorsList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    // Import excel data event
    public List<Event> excelToEventList(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet("DSSK");
            Iterator<Row> rows = sheet.iterator();
            List<Event> eventList = new ArrayList<Event>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                Event event = new Event();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 1:
                            event.setName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            if(currentCell.getCellType() == CellType.NUMERIC) // là kiểu date
                                event.setDate(currentCell.getLocalDateTimeCellValue().toLocalDate());
                            else if (currentCell.getCellType() == CellType.STRING) { // là kiểu chuỗi
                                String dateString = currentCell.getStringCellValue();
                                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                LocalDate date = LocalDate.parse(dateString, dateTimeFormatter);
                                event.setDate(date);
                            }
                            break;
                        case 3:
                            event.setPlace(currentCell.getStringCellValue());
                            break;
                        case 4:
                            event.setContent(currentCell.getStringCellValue());
                            break;

                        case 5:
                            Area area =areaRepository.findByName(currentCell.getStringCellValue())
                                    .orElseThrow(()-> new RuntimeException(String.format("Khu vực %s không tồn tại", currentCell.getStringCellValue().toUpperCase())));
                            event.setArea(area);
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                event.setCreatedBy(identifyUserAccessService.getAdmin().getCode());
                event.setAdmin(identifyUserAccessService.getAdmin());
                eventList.add(event);
            }
            workbook.close();
            return eventList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    // Import excel data subject
    public List<Subject> excelToSubjectList(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet("DSMH");
            Iterator<Row> rows = sheet.iterator();
            List<Subject> subjectsList = new ArrayList<Subject>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                Subject subject = new Subject();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 1:
                            if(subjectRepository.existsByCode(currentCell.getStringCellValue())){
                                throw new RuntimeException(String.format("Môn học %s đã tồn tại", currentCell.getStringCellValue()));
                            }
                            else
                                subject.setCode(currentCell.getStringCellValue());
                            break;
                        case 2:
                            subject.setName(currentCell.getStringCellValue());
                            break;
                        case 3:
                            subject.setCredits((int)currentCell.getNumericCellValue());
                            break;
                        case 4:
                            subject.setTotal_hours((int)currentCell.getNumericCellValue());
                            break;
                        case 5:
                            Specialization specialization =specializationRepository.findByName(currentCell.getStringCellValue())
                                    .orElseThrow(()-> new RuntimeException(String.format("Bộ môn %s không tồn tại", currentCell.getStringCellValue().toUpperCase())));
                            subject.setSpecialization(specialization);
                            break;
                        case 6:
                            if(currentCell.getStringCellValue().isEmpty()){
                                subject.setRequired(null);
                            }
                            else {
                                Subject subjectRequired =subjectRepository.findByCode(currentCell.getStringCellValue())
                                        .orElseThrow(()-> new RuntimeException(String.format("Môn học %s không tồn tại", currentCell.getStringCellValue().toUpperCase())));
                                subject.setRequired(subjectRequired);
                            }
                            break;
                        case 7:
                            subject.setMission(currentCell.getStringCellValue());
                            break;
                        case 8:
                            subject.setNote(currentCell.getStringCellValue());
                            break;
                        case 9:
                            subject.setDescription(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                subject.setCreatedBy(identifyUserAccessService.getAdmin().getCode());
                subjectsList.add(subject);
            }
            workbook.close();
            return subjectsList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
