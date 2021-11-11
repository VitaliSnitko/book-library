package com.itechart.library.converter;

import com.itechart.library.model.dto.RecordDto;
import com.itechart.library.model.entity.RecordEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RecordConverter extends Converter<RecordDto, RecordEntity> {

    List<RecordDto> toDtosFromReq(HttpServletRequest req);
}
