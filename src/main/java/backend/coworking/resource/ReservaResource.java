package backend.coworking.resource;

import backend.coworking.dto.CancelamentoReservaDTO;
import backend.coworking.dto.ReservaDTO;
import backend.coworking.dto.insert.ReservaInsertDTO;
import backend.coworking.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/reserva")
@Tag(name = "Reserva", description = "Controller para reservas")
public class ReservaResource {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    @Operation(
            description = "Retorna todas as reservas de forma paginada",
            summary = "Retorna todas as reservas",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<ReservaDTO>> findAll(Pageable pageable) {
        Page<ReservaDTO> page = reservaService.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping(value = "/{id}")
    @Operation(
            description = "Retorna uma única reserva baseado em seu ID",
            summary = "Retorna uma reserva por ID",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFISSIONAL')")
    public ResponseEntity<ReservaDTO> findById(@PathVariable Long id) {
        ReservaDTO dto = reservaService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    @Operation(
            description = "Cria uma nova reserva no banco de dados",
            summary = "Cria uma nova reserva",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFISSIONAL')")
    public ResponseEntity<ReservaDTO> insert(@Valid @RequestBody ReservaInsertDTO dto) {
        ReservaDTO newDto = reservaService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping(value = "/{id}")
    @Operation(
            description = "Atualiza os dados de uma reserva com base em seu ID",
            summary = "Atualiza uma reserva",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFISSIONAL')")
    public ResponseEntity<ReservaDTO> update(@PathVariable Long id, @Valid @RequestBody ReservaInsertDTO dto) {
        ReservaDTO newDto = reservaService.update(id, dto);
        return ResponseEntity.ok().body(newDto);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(
            description = "Deleta uma reserva do banco de dados baseado em seu ID",
            summary = "Deleta uma reserva",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFISSIONAL')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @Operation(
            summary = "Retorna as reservas do usuário logado",
            description = "Tendo os dados do usuário logado, retorna todas as suas reservas",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not Found", responseCode = "404")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFISSIONAL')")
    public ResponseEntity<Page<ReservaDTO>> encontrarMinhasReservas (Pageable pageable) {
        Page<ReservaDTO> page = reservaService.findByUsuarioLogado(pageable);
        return ResponseEntity.ok().body(page);
    }

    @PostMapping("/cancelar-por-periodo")
    @Operation(
            summary = "Cancela reservas em lote",
            description = "Cancela todas as reservas dentro de um período específico",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> cancelarPorPeriodo (@RequestBody CancelamentoReservaDTO dto) {
        int totalCancelado = reservaService.cancelarReservasPorPeriodo(dto);
        return ResponseEntity.ok().body(Map.of(
                "total_reservas_canceladas", totalCancelado,
                "motivo", dto.getMotivo()
        ));
    }
}