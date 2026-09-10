package com.sunuhelp.user.mapper;

import com.sunuhelp.user.dto.response.SearchHistoryResponse;
import com.sunuhelp.user.entity.SearchHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SearchHistoryMapper {
    SearchHistoryResponse toResponse(SearchHistory history);
}
