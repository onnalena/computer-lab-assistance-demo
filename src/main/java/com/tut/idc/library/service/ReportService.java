package com.tut.idc.library.service;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tut.idc.library.model.*;
import com.tut.idc.library.model.enums.ReportType;
import com.tut.idc.library.model.report.*;
import com.tut.idc.library.persistence.*;
import com.tut.idc.library.persistence.entity.*;
import com.tut.idc.library.util.ComputerLabMapper;
import com.tut.idc.library.util.annotation.ColumnName;
import com.tut.idc.library.web.exception.ReportException;
import org.mapstruct.factory.Mappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ReportService {

    private ComputerRepository computerRepository;
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private UserContactRepository userContactRepository;
    private ComputerLabRepository computerLabRepository;
    private  UserService userService;
    private ComputerLabMapper mapper;
    private ComputerLabService computerLabService;

    @Autowired
    public ReportService(ComputerRepository computerRepository, BookingRepository bookingRepository, UserRepository userRepository, UserContactRepository userContactRepository, ComputerLabRepository computerLabRepository, ComputerLabService computerLabService, UserService userService) {
        this.computerRepository = computerRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.userContactRepository = userContactRepository;
        this.computerLabRepository = computerLabRepository;
        this.userService = userService;
        this.mapper = Mappers.getMapper(ComputerLabMapper.class);
	  this.computerLabService =  computerLabService;
    }

    public ByteArrayResource downloadFile(final ReportCriteria criteria) throws IOException, DocumentException {
        switch (criteria.getDownloadFileType()) {
            case "excel":
            case "EXCEL":
                final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                final Workbook workbook = new XSSFWorkbook();
                buildWorkbookColumns(workbook, criteria.getReportType());
                writeExcelContent(criteria, workbook);
                workbook.write(stream);
                workbook.close();
                return new ByteArrayResource(stream.toByteArray());
            case "pdf":
            case "PDF":
                ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
                Document document = new Document();
                PdfPTable table = buildPdfColumns(criteria.getReportType());
                writePdfContent(criteria, table);
                PdfWriter.getInstance(document, pdfStream);
                document.open();
                document.add(table);
                document.close();
                return new ByteArrayResource(pdfStream.toByteArray());
            default:
        }
        return null;
    }

    //Add Headers
    private PdfPTable buildPdfColumns(final ReportType reportType) {
        final Field[] reportTypeFields = getReportTypeFields(reportType);
        PdfPTable table = new PdfPTable(reportTypeFields.length);
        table.setWidthPercentage(100);

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        for (int i = 0; i < reportTypeFields.length; i++) {
            PdfPCell headerCell;
            headerCell = new PdfPCell(new Phrase(reportTypeFields[i].getAnnotation(ColumnName.class).name(), headFont));
            headerCell.setBackgroundColor(BaseColor.GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }
        return table;
    }
    private void buildWorkbookColumns(final Workbook workbook, final ReportType reportType) {
        final Sheet sheet = workbook.createSheet(reportType.ofType());
        final Row header = sheet.createRow(0);

        final CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        final Field[] reportTypeFields = getReportTypeFields(reportType);

        for (int i = 0; i < reportTypeFields.length; i++) {
            final Cell headerCell = header.createCell(i);
            headerCell.setCellValue(reportTypeFields[i].getAnnotation(ColumnName.class).name());
            headerCell.setCellStyle(headerStyle);
        }
    }

    //Add Content
    private void writePdfContent(final ReportCriteria criteria, final PdfPTable table) {
        Field[] reportTypeFields = getReportTypeFields(criteria.getReportType());

        if (ReportType.USER.equals(criteria.getReportType())) {
            List<UserReport> userReportEntities = setUserContent((List<UserEntity>) userRepository.findAll());
            userReportEntities.forEach(record -> {
                populatePdfCell(table, reportTypeFields, record);
            });
        } else if (ReportType.COMPUTER_LAB.equals(criteria.getReportType())) {
            List<ComputerLabReport> labs = setComputerLabContent(computerLabService.retrieveAllComputerLabsReport());
            labs.forEach(record -> {
                populatePdfCell(table, reportTypeFields, record);
            });
        } else if (ReportType.COMPUTER.equals(criteria.getReportType())) {
            List<ComputerReport> computers = setComputerContent(mapper.convertComputerEntityToDTO((List<ComputerEntity>) computerRepository.findAll()));
            computers.forEach(record -> {
                populatePdfCell(table, reportTypeFields, record);
            });
        } else if (ReportType.BOOKING.equals(criteria.getReportType())) {
            List<BookingReport> bookings = setBookingContent(findBookingByDate((List<BookingEntity>) bookingRepository.findAll(), criteria.getReportDate()));
            bookings.forEach(record -> {
                populatePdfCell(table, reportTypeFields, record);
            });
        }else if (ReportType.FEEDBACK.equals(criteria.getReportType())) {
            List<FeedbackReport> feedback = setFeedbackContent(userService.getFeedback());
            feedback.forEach(record -> {
                populatePdfCell(table, reportTypeFields, record);
            });
        }
    }
    private void writeExcelContent(final ReportCriteria criteria, final Workbook workbook) {
        Field[] reportTypeFields = getReportTypeFields(criteria.getReportType());
        AtomicInteger rowCount = new AtomicInteger(1);
        final Sheet sheet = workbook.getSheet(criteria.getReportType().ofType());

        if (ReportType.USER.equals(criteria.getReportType())) {
            List<UserReport> userReportEntities = setUserContent((List<UserEntity>) userRepository.findAll());
            userReportEntities.forEach(record -> {
                populateCell(reportTypeFields, rowCount, sheet, record);
                rowCount.getAndIncrement();
            });
        } else if (ReportType.COMPUTER_LAB.equals(criteria.getReportType())) {
            List<ComputerLabReport> labs = setComputerLabContent(computerLabService.retrieveAllComputerLabsReport());
            labs.forEach(record -> {
                populateCell(reportTypeFields, rowCount, sheet, record);
                rowCount.getAndIncrement();
            });
        } else if (ReportType.COMPUTER.equals(criteria.getReportType())) {
            List<ComputerReport> computers = setComputerContent(mapper.convertComputerEntityToDTO((List<ComputerEntity>) computerRepository.findAll()));
            computers.forEach(record -> {
                populateCell(reportTypeFields, rowCount, sheet, record);
                rowCount.getAndIncrement();
            });
        } else if (ReportType.BOOKING.equals(criteria.getReportType())) {
            List<BookingReport> bookings = setBookingContent(findBookingByDate((List<BookingEntity>) bookingRepository.findAll(), criteria.getReportDate()));
            bookings.forEach(record -> {
                populateCell(reportTypeFields, rowCount, sheet, record);
                rowCount.getAndIncrement();
            });
        }else if (ReportType.FEEDBACK.equals(criteria.getReportType())) {
            List<FeedbackReport> feedback = setFeedbackContent(userService.getFeedback());
            feedback.forEach(record -> {
                populateCell(reportTypeFields, rowCount, sheet, record);
            });
        }
    }

    //Populate Cells
    private static <T> void populatePdfCell(PdfPTable table, Field[] reportTypeFields, T record) {
        for (int i = 0; i < reportTypeFields.length; i++) {
            PdfPCell cell;
            try {
                Field declaredField = reportTypeFields[i];
                declaredField.setAccessible(true);
                cell = new PdfPCell(new Phrase(String.valueOf(declaredField.get(record))));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                declaredField.setAccessible(false);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static <T> void populateCell(Field[] reportTypeFields, AtomicInteger rowCount, Sheet sheet, T record) {
        final Row row = sheet.createRow(rowCount.get());
        for(int i = 0; i < reportTypeFields.length; i++){
            Cell cell = row.createCell(i);
            try {
                Field declaredField = reportTypeFields[i];
                declaredField.setAccessible(true);
                cell.setCellValue(String.valueOf(declaredField.get(record)));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Get fields for specific report type
    private Field[] getReportTypeFields(final ReportType reportType) {
        final Map<ReportType, Field[]> result = new HashMap<>();

        if(ReportType.USER.equals(reportType)){
            result.put(ReportType.USER, UserReport.class.getDeclaredFields());
            return result.get(reportType);
        } else if(ReportType.COMPUTER_LAB.equals(reportType)){
            result.put(ReportType.COMPUTER_LAB, ComputerLabReport.class.getDeclaredFields());
            return result.get(reportType);
        } else if(ReportType.COMPUTER.equals(reportType)){
            result.put(ReportType.COMPUTER, ComputerReport.class.getDeclaredFields());
            return result.get(reportType);
        } else if(ReportType.BOOKING.equals(reportType)){
            result.put(ReportType.BOOKING, BookingReport.class.getDeclaredFields());
            return result.get(reportType);
        } else if(ReportType.FEEDBACK.equals(reportType)){
            result.put(ReportType.FEEDBACK, FeedbackReport.class.getDeclaredFields());
            return result.get(reportType);
        }
        return null;
    }

    //Set Report Content
    private List<FeedbackReport> setFeedbackContent(List<FeedbackDTO> feedback){
        List<FeedbackReport> feedbackReport = new ArrayList<>();
        feedback.forEach(feed -> {
            feedbackReport.add(FeedbackReport.builder()
                    .IDNumber(feed.getIDNumber())
                    .stars(feed.getStars())
                    .comment(feed.getComment())
                    .date(feed.getDate())
                    .build());
        });
        return feedbackReport;
    }
    private  List<BookingReport> setBookingContent(final List<BookingDTO> bookingDTOS){
        List<BookingReport> bookingsReport = new ArrayList<>();
        bookingDTOS.forEach(booking -> {
            BookingReport bookingReport = BookingReport.builder()
                    .IDNumber(booking.getIDNumber())
                    .computerName(booking.getComputerName())
                    .computerLab(booking.getComputerLabName())
                    .Date(booking.getDateTime())
                    .contactPreference(booking.getContactPreference())
                    .status(booking.getStatus())
                    .build();
            bookingsReport.add(bookingReport);
        });
        return bookingsReport;
    }
    private List<BookingDTO> findBookingByDate(final List<BookingEntity> allBookings, String reportDate){
        List<BookingDTO> bookings = new ArrayList<>();

        if (reportDate.isEmpty()){
            return mapper.convertBookingEntityToDTO(allBookings);
        }
		

        for (BookingEntity entry : allBookings) {
            if(entry.getDateTime().toLocalDate().equals(LocalDate.parse(reportDate))){
                bookings.add(mapper.convertBookingEntityToDTO(entry));
            }
        }

        if(bookings.isEmpty()){
            throw new ReportException("No data to create a report.");
        }
        return bookings;
    }
    private  List<ComputerReport> setComputerContent(final List<ComputerDTO> computerDTOS){
        List<ComputerReport> computersReport = new ArrayList<>();
        computerDTOS.forEach(computerDTO -> {
            ComputerReport computer = ComputerReport.builder()
                    .computerName(computerDTO.getComputerName())
                    .computerLab(computerDTO.getComputerLab().getComputerLabName())
                    .serialNumber(computerDTO.getSerialNumber())
                    .brandName(computerDTO.getBrandName())
                    .build();
            computersReport.add(computer);
        });

        return computersReport;
    }
    private  List<ComputerLabReport> setComputerLabContent(final List<ComputerLabDTO> computerLabDTOS){
       List<ComputerLabReport> computerLabsReport = new ArrayList<>();
       computerLabDTOS.forEach(lab -> {
           ComputerLabReport labReport = ComputerLabReport.builder()
                   .computerLabName(lab.getComputerLabName())
                   .buildingName(lab.getBuildingName())
                   .description(lab.getDescription())
                   .openingTime(lab.getOpeningTime())
                   .closingTime(lab.getClosingTime())
                   .numberOfComputersAvailable(lab.getNumberOfComputersAvailable())
                   .numberOfComputersBooked(lab.getNumberOfComputersBooked())
                   .numberOfComputers(lab.getNumberOfComputers())
                   .build();
           computerLabsReport.add(labReport);
       });

       return computerLabsReport;
    }
    private List<UserReport> setUserContent(final List<UserEntity> userEntity) {
	List<UserReport> userReports = new ArrayList<>();
	userEntity.forEach(user -> {
		UserReport userReportDTO = new UserReport();
		userReportDTO.setIDNumber(user.getIDNumber());
      	userReportDTO.setLastname(user.getLastname());
		userReportDTO.setFirstname(user.getFirstname());
		userReportDTO.setStatus(user.getStatus());
		userReportDTO.setUserType(user.getUserType());
		List<UserContactEntity> contacts = user.getContact();
		userReportDTO.setEmail(contacts.stream().filter(x -> x.getContactPreference().name().equals("EMAIL")).findAny().get().getContact());
		userReportDTO.setCellPhoneNumber(contacts.stream().filter(x -> x.getContactPreference().name().equals("SMS")).findAny().get().getContact());
		userReports.add(userReportDTO);
	});
	return userReports;
 }
}
