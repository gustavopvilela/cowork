package backend.coworking.resource;

import backend.coworking.dto.EspacoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/espaco")
public class EspacoResource {
    public ResponseEntity<Page<EspacoDTO>> findAll(Pageable pageable) {return null;}

    public ResponseEntity<EspacoDTO> findById(Long id) {return null;}

    public ResponseEntity<EspacoDTO> insert (EspacoDTO dto) {return null;}

    public ResponseEntity<EspacoDTO> update (Long id, EspacoDTO dto) {return null;}

    public ResponseEntity<Void> delete(Long id) {return null;}
}
