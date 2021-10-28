package com.itechart.book_library.util.converter.api;

import com.itechart.book_library.model.dto.RecordDto;
import com.itechart.book_library.model.entity.RecordEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RecordConverter extends Converter<RecordDto, RecordEntity> {
    List<RecordDto> toDtosFromReq(HttpServletRequest req);
}
