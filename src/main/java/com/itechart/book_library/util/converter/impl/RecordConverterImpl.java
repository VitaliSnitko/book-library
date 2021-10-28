package com.itechart.book_library.util.converter.impl;

import com.itechart.book_library.model.dto.ReaderDto;
import com.itechart.book_library.model.dto.RecordDto;
import com.itechart.book_library.model.entity.RecordEntity;
import com.itechart.book_library.model.entity.Status;
import com.itechart.book_library.util.converter.api.ReaderConverter;
import com.itechart.book_library.util.converter.api.RecordConverter;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecordConverterImpl implements RecordConverter {

    private final ReaderConverter readerConverter = new ReaderConverterImpl();

    @Override
    public RecordEntity toEntity(RecordDto recordDto) {
        LocalDate borrowDate = recordDto.getBorrowDate();
        LocalDate dueDate = recordDto.getDueDate();
        LocalDate returnDate = recordDto.getReturnDate();
        ReaderDto readerDto = recordDto.getReader();
        return RecordEntity.builder()
                .id(recordDto.getId())
                .borrowDate(borrowDate != null ? Date.valueOf(borrowDate) : null)
                .dueDate(borrowDate != null ? Date.valueOf(dueDate) : null)
                .returnDate(returnDate != null ? Date.valueOf(returnDate) : null)
                .bookId(recordDto.getBookId())
                .reader(readerDto != null ? readerConverter.toEntity(readerDto) : null)
                .status(recordDto.getStatus())
                .build();
    }

    @Override
    public RecordDto toDto(RecordEntity recordEntity) {
        Date returnDate = recordEntity.getReturnDate();
        return RecordDto.builder()
                .id(recordEntity.getId())
                .borrowDate(recordEntity.getBorrowDate().toLocalDate())
                .dueDate(recordEntity.getDueDate().toLocalDate())
                .returnDate(returnDate != null ? returnDate.toLocalDate() : null)
                .bookId(recordEntity.getBookId())
                .reader(readerConverter.toDto(recordEntity.getReader()))
                .status(recordEntity.getStatus())
                .build();
    }

    @Override
    public List<RecordDto> toDtosFromReq(HttpServletRequest req) {
        List<RecordDto> recordDtos = new ArrayList<>();
        String[] recordIds = req.getParameterValues("recordId");
        String[] statuses = req.getParameterValues("status");
        for (int i = 0; i < recordIds.length; i++) {
            if (statuses[i].equals("borrowed")) {
                continue;
            }
            recordDtos.add(RecordDto.builder()
                    .id(Integer.parseInt(recordIds[i]))
                    .bookId(Integer.parseInt(req.getParameter("id")))
                    .status(Enum.valueOf(Status.class, String.join("_", statuses[i].toUpperCase().split(" "))))
                    .build());
        }
        return recordDtos;
    }
}
