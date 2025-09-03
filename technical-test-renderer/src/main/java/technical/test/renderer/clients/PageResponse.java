package technical.test.renderer.clients;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Data
public class PageResponse<T> {

    private List<T> content;
    private int number; // Numéro de page dans la réponse Spring Data
    private int size; // Taille de la page dans la réponse Spring Data
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean empty;
    private int numberOfElements;

    // Classe interne pour mapper l'objet pageable
    @Data
    public static class Pageable {
        private int pageNumber;
        private int pageSize;
        private long offset;
        private boolean paged;
        private boolean unpaged;
        private Sort sort;
    }

    @Data
    public static class Sort {
        private boolean empty;
        private boolean sorted;
        private boolean unsorted;
    }

    private Pageable pageable;
    private Sort sort;

    public Page<T> toPage() {
        return new PageImpl<>(
            this.content,
            PageRequest.of(this.number, this.size),
            this.totalElements
        );
    }
}