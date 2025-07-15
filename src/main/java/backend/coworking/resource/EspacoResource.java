package backend.coworking.resource;

import backend.coworking.constant.EspacoType;
import backend.coworking.dto.EspacoDTO;
import backend.coworking.service.EspacoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/espaco")
@Tag(name = "Espaço", description = "Controller para espaços de coworking")
public class EspacoResource {

    @Autowired
    private EspacoService espacoService;

    @GetMapping
    @Operation(
            description = "Retorna todos os espaços de forma paginada",
            summary = "Retorna todos os espaços",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401")
            }
    )
    public ResponseEntity<Page<EspacoDTO>> findAll(Pageable pageable) {
        Page<EspacoDTO> page = espacoService.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping(value = "/{id}")
    @Operation(
            description = "Retorna um único espaço baseado em seu ID",
            summary = "Retorna um espaço por ID",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFISSIONAL')")
    public ResponseEntity<EspacoDTO> findById(@PathVariable Long id) {
        EspacoDTO dto = espacoService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    @Operation(
            description = "Cria um novo espaço no banco de dados",
            summary = "Cria um novo espaço",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<EspacoDTO> insert(@Valid @RequestBody EspacoDTO dto) {
        dto = espacoService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    @Operation(
            description = "Atualiza os dados de um espaço com base em seu ID",
            summary = "Atualiza um espaço",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<EspacoDTO> update(@PathVariable Long id, @Valid @RequestBody EspacoDTO dto) {
        dto = espacoService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(
            description = "Deleta um espaço do banco de dados baseado em seu ID",
            summary = "Deleta um espaço",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        espacoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disponibilidade")
    @Operation(
        summary = "Busca espaços disponíveis",
        description = "Retorna uma lista de espaços que estão livres em um determinado período de tempo",
        responses = {
            @ApiResponse(description = "OK", responseCode = "200"),
            @ApiResponse(description = "Bad Request", responseCode = "400"),
            @ApiResponse(description = "Unauthorized", responseCode = "401")
        },
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFISSIONAL')")
    public ResponseEntity<List<EspacoDTO>> findDisponibilidade (
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fim,
            @RequestParam(required = false) EspacoType tipo,
            @RequestParam(required = false) Integer capacidade
    ) {
        List<EspacoDTO> list = espacoService.findEspacosDisponiveis(inicio, fim, tipo, capacidade);
        return ResponseEntity.ok().body(list);
    }
}