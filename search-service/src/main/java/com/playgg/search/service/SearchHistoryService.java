package com.playgg.search.service;

import com.playgg.search.dto.*;
import com.playgg.search.exception.ResourceNotFoundException;
import com.playgg.search.model.*;
import com.playgg.search.repository.SearchHistoryRepository;
import com.playgg.search.util.DateUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

/** Service con logica de negocio. Flujo CSR: Controller -> Service -> Repository. */
@Service
@RequiredArgsConstructor
public class SearchHistoryService {
  private static final Logger logger = LoggerFactory.getLogger(SearchHistoryService.class);
  private final SearchHistoryRepository repository;

  public SearchHistoryResponseDTO create(CreateSearchHistoryDTO dto) {
    SearchHistory e = new SearchHistory();
    e.setUserId(dto.getUserId());
    e.setQuery(dto.getQuery());
    e.setSearchedAt(DateUtil.now());
    logger.info("Creando SearchHistory");
    return toResponse(repository.save(e));
  }

  public List<SearchHistoryResponseDTO> findAll() {
    logger.info("Listando SearchHistory");
    return repository.findAll().stream().map(this::toResponse).toList();
  }

  public SearchHistoryResponseDTO findById(Long id) {
    return toResponse(get(id));
  }

  public SearchHistoryResponseDTO update(Long id, UpdateSearchHistoryDTO dto) {
    SearchHistory e = get(id);
    e.setUserId(dto.getUserId());
    e.setQuery(dto.getQuery());
    logger.info("Actualizando SearchHistory {}", id);
    return toResponse(repository.save(e));
  }

  public void delete(Long id) {
    repository.delete(get(id));
    logger.info("Eliminando SearchHistory {}", id);
  }

  private SearchHistory get(Long id) {
    return repository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("SearchHistory no encontrado con id: " + id));
  }

  private SearchHistoryResponseDTO toResponse(SearchHistory e) {
    return SearchHistoryResponseDTO.builder()
        .searchId(e.getSearchId())
        .userId(e.getUserId())
        .query(e.getQuery())
        .searchedAt(e.getSearchedAt())
        .build();
  }
}
