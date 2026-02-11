package br.com.interno.orders_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta paginada genérica")
public class PageResponse<T> {

    @Schema(description = "Lista de elementos da página atual")
    private List<T> content;

    @Schema(description = "Número da página atual (inicia em 0)", example = "0")
    private int page;

    @Schema(description = "Quantidade de elementos por página", example = "10")
    private int size;

    @Schema(description = "Total de páginas disponíveis", example = "5")
    private int totalPages;

    public static <T> PageResponse<T> from(org.springframework.data.domain.Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages()
        );
    }
}
